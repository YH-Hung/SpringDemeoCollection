package org.hle.maildeliver;

import org.hle.maildeliver.dto.MailDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@Testcontainers
public class MailEndpointTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MailpitContainer mailpitContainer;

    @Test
    public void testSendMail() {
        System.out.println("[DEBUG_LOG] Starting testSendMail test");
        System.out.println("[DEBUG_LOG] Mailpit container running: " + mailpitContainer.isRunning());
        System.out.println("[DEBUG_LOG] Mailpit container host: " + mailpitContainer.getHost());
        System.out.println("[DEBUG_LOG] Mailpit container SMTP port: " + mailpitContainer.getSmtpPort());
        System.out.println("[DEBUG_LOG] Mailpit container HTTP port: " + mailpitContainer.getHttpApiPort());
        // Create test email
        MailDto mailDto = new MailDto();
        mailDto.setFrom("sender@example.com");
        mailDto.setTo("recipient@example.com");
        mailDto.setCc("cc@example.com");
        mailDto.setSubject("Test Subject");
        mailDto.setText("Test Message");

        // Send email through the API
        webTestClient.post()
                .uri("/api/mail")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mailDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Email sent successfully");

        // Create WebClient for Mailpit API
        WebClient webClient = WebClient.create();
        String mailpitApiUrl = mailpitContainer.getMessagesApiUrl();

        // Use Awaitility to wait for the email to be processed and verify it
        AtomicReference<String> responseRef = new AtomicReference<>();

        await().atMost(Duration.ofSeconds(10))
               .pollInterval(Duration.ofMillis(200))
               .until(() -> {
                   try {
                       // Fetch messages from Mailpit
                       String result = webClient.get()
                               .uri(mailpitApiUrl)
                               .retrieve()
                               .bodyToMono(String.class)
                               .toFuture()
                               .get();

                       // Store the result for later assertions
                       responseRef.set(result);

                       // Check if our email is in the results
                       return result.contains("recipient@example.com") && 
                              result.contains("Test Subject");
                   } catch (Exception e) {
                       System.out.println("[DEBUG_LOG] Error polling Mailpit: " + e.getMessage());
                       return false;
                   }
               });

        // Additional verification if needed
        String response = responseRef.get();
        assertTrue(response.contains("recipient@example.com"), "Email recipient not found in Mailpit");
        assertTrue(response.contains("Test Subject"), "Email subject not found in Mailpit");
    }

    @Test
    public void testSendMailWithoutCc() {
        System.out.println("[DEBUG_LOG] Starting testSendMailWithoutCc test");
        System.out.println("[DEBUG_LOG] Mailpit container running: " + mailpitContainer.isRunning());
        System.out.println("[DEBUG_LOG] Mailpit container host: " + mailpitContainer.getHost());
        System.out.println("[DEBUG_LOG] Mailpit container SMTP port: " + mailpitContainer.getSmtpPort());
        System.out.println("[DEBUG_LOG] Mailpit container HTTP port: " + mailpitContainer.getHttpApiPort());
        // Create test email without cc
        MailDto mailDto = new MailDto();
        mailDto.setFrom("sender@example.com");
        mailDto.setTo("recipient@example.com");
        // Not setting cc field to test optional behavior
        mailDto.setSubject("Test Subject Without CC");
        mailDto.setText("Test Message Without CC");

        // Send email through the API
        webTestClient.post()
                .uri("/api/mail")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mailDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Email sent successfully");

        // Create WebClient for Mailpit API
        WebClient webClient = WebClient.create();
        String mailpitApiUrl = mailpitContainer.getMessagesApiUrl();

        // Use Awaitility to wait for the email to be processed and verify it
        AtomicReference<String> responseRef = new AtomicReference<>();

        await().atMost(Duration.ofSeconds(10))
               .pollInterval(Duration.ofMillis(200))
               .until(() -> {
                   try {
                       // Fetch messages from Mailpit
                       String result = webClient.get()
                               .uri(mailpitApiUrl)
                               .retrieve()
                               .bodyToMono(String.class)
                               .toFuture()
                               .get();

                       // Store the result for later assertions
                       responseRef.set(result);

                       // Check if our email is in the results
                       return result.contains("recipient@example.com") && 
                              result.contains("Test Subject Without CC");
                   } catch (Exception e) {
                       System.out.println("[DEBUG_LOG] Error polling Mailpit: " + e.getMessage());
                       return false;
                   }
               });

        // Additional verification if needed
        String response = responseRef.get();
        assertTrue(response.contains("recipient@example.com"), "Email recipient not found in Mailpit");
        assertTrue(response.contains("Test Subject Without CC"), "Email subject not found in Mailpit");
    }
}
