package com.vmwaretest.numgen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author arjun9218
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.vmwaretest.numgen" })
public class NumgenApplication {

	static Logger logger = LoggerFactory.getLogger(NumgenApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(NumgenApplication.class, args);
		logger.info("Application started");
	}

}
