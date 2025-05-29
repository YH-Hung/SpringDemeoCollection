package hle.jpacookbook.s3;

import hle.jpacookbook.model.report.InspectionReport;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class S3ClientTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15.4-alpine"
    ).withInitScript("postgres_init.sql");

    @Container
    static MinIOContainer minio = new MinIOContainer("minio/minio");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) throws IOException {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("minio.endpoint", minio::getS3URL);
        registry.add("minio.accessKey", minio::getUserName);
        registry.add("minio.secretKey", minio::getPassword);
    }

    static final String BUCKET_NAME = "product";

    // Create a bean for S3Client to be used in tests
    private static S3Client createS3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(minio.getS3URL()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(minio.getUserName(), minio.getPassword())
                ))
                .region(Region.US_EAST_1) // Default region, MinIO doesn't use regions
                .forcePathStyle(true) // Required for MinIO
                .build();
    }

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        // Create S3Client with MinIO configuration
        S3Client s3Client = createS3Client();

        // Check if bucket exists
        boolean isBucketExist = false;
        try {
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(BUCKET_NAME)
                    .build();
            s3Client.headBucket(headBucketRequest);
            isBucketExist = true;
        } catch (NoSuchBucketException e) {
            // Bucket doesn't exist
        }

        // Create bucket if it doesn't exist
        if (!isBucketExist) {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(BUCKET_NAME)
                    .build();
            s3Client.createBucket(createBucketRequest);
        }

        // Enable versioning on the bucket
        PutBucketVersioningRequest versioningRequest = PutBucketVersioningRequest.builder()
                .bucket(BUCKET_NAME)
                .versioningConfiguration(software.amazon.awssdk.services.s3.model.VersioningConfiguration.builder()
                        .status(BucketVersioningStatus.ENABLED)
                        .build())
                .build();
        s3Client.putBucketVersioning(versioningRequest);

        // Upload test file
        ClassLoader loader = S3ClientTest.class.getClassLoader();
        File file = new File(loader.getResource("InspReport.txt").getFile());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key("judge/InspReport.txt")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
    }

    @Autowired
    S3Client s3Client;

    @SneakyThrows
    @Test
    void parseFromMinIO() {
        // Get object from S3
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key("judge/InspReport.txt")
                .build();

        // Use try-with-resources to ensure the input stream is closed
        try (InputStream is = s3Client.getObject(getObjectRequest)) {
            // Read lines from the input stream
            List<String> data = IOUtils.readLines(is, StandardCharsets.UTF_8);

            // Parse the data using InspectionReport.parseTmp
            var result = InspectionReport.parseTmp(data);

            // Assert the result matches the expected value
            assertThat(result).isEqualTo("12.986343,78.38263");
        }
    }
}
