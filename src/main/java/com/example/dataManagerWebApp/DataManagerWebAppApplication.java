package com.example.dataManagerWebApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataManagerWebAppApplication {

	public static void main(String[] args) {
		PersonStorage.createSampleStorage();
		SpringApplication.run(DataManagerWebAppApplication.class, args);
	}

}
