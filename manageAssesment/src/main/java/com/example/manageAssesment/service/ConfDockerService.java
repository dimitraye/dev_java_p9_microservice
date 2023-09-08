package com.example.manageAssesment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * This class provides a service for detecting whether the application is running in a Docker environment by
 * checking the value of the "spring.profiles.active" property. If the value contains the word "docker," it
 * indicates that the application is running in a Docker container.
 */
@Service
@Slf4j
public class ConfDockerService {

    @Value("${spring.profiles.active}")
    private String activeProfile;


    /**
     * Checks if the application is running in a Docker environment by analyzing the value of the
     * "spring.profiles.active" property.
     *
     * @return true if the application is running in a Docker environment, otherwise false.
     */
    public boolean isDocker() {
        log.debug("activeprofile in confdockerservice : " + activeProfile);
        return activeProfile != null && activeProfile.contains("docker");
    }

}