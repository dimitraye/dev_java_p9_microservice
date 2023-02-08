package com.example.manageNote.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConfDockerService {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public boolean isDocker() {
        log.debug("activeprofile in confdockerservice : " + activeProfile);
        return activeProfile != null && activeProfile.contains("docker");
    }

}
