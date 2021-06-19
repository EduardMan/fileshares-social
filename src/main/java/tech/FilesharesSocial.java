package tech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FilesharesSocial {
    public static void main(String[] args) {
        SpringApplication.run(FilesharesSocial.class, args);
    }
}
