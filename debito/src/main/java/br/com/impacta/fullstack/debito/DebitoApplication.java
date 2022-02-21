package br.com.impacta.fullstack.debito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@SpringBootApplication
public class DebitoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebitoApplication.class, args);
	}

}
