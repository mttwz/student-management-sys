package com.radnoti.studentmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class StudentmanagementsystemApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(StudentmanagementsystemApplication.class, args);
		String[] beanz = applicationContext.getBeanDefinitionNames();
		Arrays.sort(beanz);
		for (String s : beanz) {
			//System.out.println(s);
		}




	}

}
