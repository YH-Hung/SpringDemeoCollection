package org.hle.springconsole;

import org.springframework.boot.SpringApplication;

public class TestSpringConsoleApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringConsoleApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
