package com.example.manageNote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class ConfDockerService {

    static public String HTTP = "http://";
    static public String HOST = "localhost";
    static public String HTTP_HOST = HTTP + HOST;
    static public String DOCKER_HOST = "host.docker.internal";

    @Autowired
    Environment environment;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public String MONGODB_HOST;



    ConfDockerService() {
        config();
        System.out.println("----- NOTE APP HOST ---------");
        System.out.println(HTTP_HOST);

        System.out.println("----- MONGODB_HOST NOTE APP HOST ---------");
        System.out.println(MONGODB_HOST);
    }



    public boolean isDocker() {
        return activeProfile != null && activeProfile.contains("docker");
    }

    public void config() {
        System.out.println("in config");
        if (isDocker()) {
            System.out.println("in isdocker");

            HTTP_HOST = HTTP + DOCKER_HOST;
        }
    }
    
}