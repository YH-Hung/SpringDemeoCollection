package org.hle.maildeliver;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * A specialized container for Mailpit email testing service.
 * Mailpit provides both SMTP server and HTTP API for email testing.
 */
public class MailpitContainer extends GenericContainer<MailpitContainer> {

    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("axllent/mailpit:latest");
    private static final int SMTP_PORT = 1025;
    private static final int HTTP_API_PORT = 8025;
    private static final Duration STARTUP_TIMEOUT = Duration.ofSeconds(60);

    /**
     * Creates a new Mailpit container with the default image and exposed ports.
     */
    public MailpitContainer() {
        super(DEFAULT_IMAGE_NAME);
        withExposedPorts(SMTP_PORT, HTTP_API_PORT);
        configureWaitStrategy();
    }

    /**
     * Creates a new Mailpit container with a specific image and exposed ports.
     * 
     * @param dockerImageName the Docker image to use
     */
    public MailpitContainer(String dockerImageName) {
        super(DockerImageName.parse(dockerImageName));
        withExposedPorts(SMTP_PORT, HTTP_API_PORT);
        configureWaitStrategy();
    }

    /**
     * Configures the wait strategy for the container.
     * Uses HTTP endpoint checking to verify the API is available.
     */
    private void configureWaitStrategy() {
        // Use HTTP wait strategy to check if the API is available
        setWaitStrategy(
            new HttpWaitStrategy()
                .forPath("/api/v1/info")
                .forPort(HTTP_API_PORT)
                .forStatusCode(200)
                .withStartupTimeout(STARTUP_TIMEOUT)
        );
    }

    /**
     * Gets the SMTP port for sending emails.
     * 
     * @return the mapped SMTP port
     */
    public Integer getSmtpPort() {
        return getMappedPort(SMTP_PORT);
    }

    /**
     * Gets the HTTP API port for accessing the Mailpit web interface and API.
     * 
     * @return the mapped HTTP API port
     */
    public Integer getHttpApiPort() {
        return getMappedPort(HTTP_API_PORT);
    }

    /**
     * Gets the base URL for the Mailpit HTTP API.
     * 
     * @return the HTTP API URL
     */
    public String getApiUrl() {
        return String.format("http://%s:%d/api/v1", getHost(), getHttpApiPort());
    }

    /**
     * Gets the URL for accessing all messages in the Mailpit API.
     * 
     * @return the messages API URL
     */
    public String getMessagesApiUrl() {
        return getApiUrl() + "/messages";
    }
}
