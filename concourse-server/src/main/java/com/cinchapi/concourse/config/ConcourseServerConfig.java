/*
 * Copyright (c) 2013-2019 Cinchapi Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cinchapi.concourse.config;

import java.io.File;

import javax.annotation.PostConstruct;

import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cinchapi.concourse.server.ConcourseServer;
import com.cinchapi.concourse.server.GlobalState;
import com.cinchapi.concourse.shell.CommandLine;

@SpringBootApplication
public class ConcourseServerConfig {

    @Value("${concourse.host}")
    private String concourseServerHost;

    @Value("${concourse.port}")
    private int concourseServerPort;

    @Value("${server.debug}")
    private String debugFlag;

    private ConcourseServer concourseServer;

    public static String DATABASE_DIRECTORY = System.getProperty("user.home")
            + File.separator + "concourse" + File.separator + "db";

    /**
     * The absolute path to the directory where the Buffer data is stored. For
     * optimal write performance, the Buffer should be placed on a separate disk
     * partition (ideally a separate physical device) from the
     * database_directory.
     */
    public static String BUFFER_DIRECTORY = System.getProperty("user.home")
            + File.separator + "concourse" + File.separator + "buffer";

    public static void main(String[] args) {

        SpringApplication.run(ConcourseServerConfig.class, args);
    }

    @Bean
    public ConcourseServer concourseServer() throws TTransportException {

        System.out.println("Server Debug Flag: " + debugFlag);

        return concourseServer;

    }

    @PostConstruct
    public void init() throws TTransportException {

        this.concourseServer = ConcourseServer.create(concourseServerPort,
                BUFFER_DIRECTORY, DATABASE_DIRECTORY);

        new Thread(() -> {

            try {
                CommandLine.displayWelcomeBanner();
                System.out.println("System ID: " + GlobalState.SYSTEM_ID);
                this.concourseServer.start();
            }
            catch (TTransportException e) {
                e.printStackTrace();
                System.exit(-1);
            }

        }, "main").start();

        System.out.println("System is now up and running");
    }

}
