package com.pepotec.cooperative_taxi_managment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CooperativeTaxiManagmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(CooperativeTaxiManagmentApplication.class, args);
	}

}
