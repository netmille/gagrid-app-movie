package com.netmillennium.gagrid.app.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import com.netmillennium.gagrid.model.Chromosome;
import com.netmillennium.gagrid.parameter.GAGrid;


@SpringBootApplication
public class MovieApplication implements CommandLineRunner {
	   
	@Autowired
	private GAGrid gaGrid;
	
	public static void main(String[] args) {
		
	   SpringApplication.run(MovieApplication.class, args);
	  }
	
	@Override
	 public void run(String... args) {  
		
		Chromosome solution =  gaGrid.evolve();
	 }
}
