package com.cinchapi.concourse.config;

import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cinchapi.concourse.server.ConcourseServer;

@SpringBootApplication
public class ConcourseServerConfig {

	@Value("${concourse.host}")
	private String concourseServerHost;

	@Value("${concourse.port}")
	private int concourseServerPort;

	public static void main(String[] args) {

		SpringApplication.run(ConcourseServerConfig.class, args);
	}

	@Bean
	public ConcourseServer startConcourseServer() throws TTransportException {
		
		ConcourseServer server = ConcourseServer.create();

		return server;

	}

}
