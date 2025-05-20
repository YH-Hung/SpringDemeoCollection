package hle.jpacookbook.config;

import hle.jpacookbook.dto.MinIOProperties;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class MinIOConfig {

    final
    MinIOProperties minIOProperties;

    public MinIOConfig(MinIOProperties minIOProperties) {
        this.minIOProperties = minIOProperties;
    }

    @Bean
    public MinioClient minioClient() {
        MinioClient client = MinioClient.builder()
                .endpoint(minIOProperties.getEndpoint())
                .credentials(minIOProperties.getAccessKey(), minIOProperties.getSecretKey())
                .build();

        client.setTimeout(
                TimeUnit.SECONDS.toMillis(minIOProperties.getConnectTimeoutSec()),
                TimeUnit.SECONDS.toMillis(minIOProperties.getWriteTimeoutSec()),
                TimeUnit.SECONDS.toMillis(minIOProperties.getReadTimeoutSec())
        );

        return client;
    }

    @Bean
    public S3Client s3Client() {
        // Configure timeouts
        ClientOverrideConfiguration clientOverrideConfiguration = ClientOverrideConfiguration.builder()
                .apiCallTimeout(Duration.ofSeconds(minIOProperties.getConnectTimeoutSec()))
                .apiCallAttemptTimeout(Duration.ofSeconds(minIOProperties.getReadTimeoutSec()))
                .build();

        // Create S3Client with MinIO configuration
        return S3Client.builder()
                .endpointOverride(URI.create(minIOProperties.getEndpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(minIOProperties.getAccessKey(), minIOProperties.getSecretKey())
                ))
                .region(Region.US_EAST_1) // Default region, MinIO doesn't use regions
                .overrideConfiguration(clientOverrideConfiguration)
                .forcePathStyle(true) // Required for MinIO
                .build();
    }


}
