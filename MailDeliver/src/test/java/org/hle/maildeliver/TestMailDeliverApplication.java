package org.hle.maildeliver;

import org.springframework.boot.SpringApplication;

public class TestMailDeliverApplication {

    public static void main(String[] args) {
        SpringApplication.from(MailDeliverApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
