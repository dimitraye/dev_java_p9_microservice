package com.example.manageAssesment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Cette classe fournit un service permettant de détecter si l'application s'exécute dans un environnement Docker
 * en vérifiant la valeur de la propriété "spring.profiles.active". Si la valeur contient le mot "docker", cela
 * indique que l'application s'exécute dans un conteneur Docker.
 */
@Service
@Slf4j
public class ConfDockerService {

    @Value("${spring.profiles.active}")
    private String activeProfile;


    /**
     * Vérifie si l'application s'exécute dans un environnement Docker en analysant la valeur de la propriété
     * "spring.profiles.active".
     *
     * @return true si l'application s'exécute dans un environnement Docker, sinon false.
     */
    public boolean isDocker() {
        log.debug("activeprofile in confdockerservice : " + activeProfile);
        return activeProfile != null && activeProfile.contains("docker");
    }

}