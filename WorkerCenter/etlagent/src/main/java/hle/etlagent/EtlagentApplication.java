package hle.etlagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EtlagentApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtlagentApplication.class, args);
    }

}
