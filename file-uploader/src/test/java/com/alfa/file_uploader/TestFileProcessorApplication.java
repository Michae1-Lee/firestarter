package com.alfa.file_uploader;

import org.springframework.boot.SpringApplication;

public class TestFileProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.from(FileProcessorApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
