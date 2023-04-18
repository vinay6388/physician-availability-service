package com.pms;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.pms.service.impl.PhysicianServiceImpl;



@SpringBootApplication
@EnableDiscoveryClient
public class G2PhysicianAvailabilityServiceApplication implements CommandLineRunner {
	
	@Autowired
    PhysicianServiceImpl physicianImpl;

	public static void main(String[] args) {
		
		SpringApplication.run(G2PhysicianAvailabilityServiceApplication.class, args);		
		
	}
	
	@Override
    public void run(String... args) throws Exception {
        System.out.println("In CommandLineRunnerImpl ");       
        physicianImpl.addPhysicianInfo();

    }

}
