package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;

@SpringBootApplication
public class FastFood {

	public static void main(String[] args) {
		// Kiểm tra nếu .env tồn tại thì mới load
		File envFile = new File(".env");
		if (envFile.exists()) {
			Dotenv dotenv = Dotenv.load();
			dotenv.entries().forEach(entry ->
					System.setProperty(entry.getKey(), entry.getValue())
			);
		}
		SpringApplication.run(FastFood.class, args);
	}

}
