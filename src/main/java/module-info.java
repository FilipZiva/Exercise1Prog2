module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.jfoenix;

    // Füge Spring-Module hinzu
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.web; // Hinzugefügt für RestTemplate

    // Füge Jackson-Module hinzu
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    // Öffnet Pakete für Spring, JavaFX und Jackson
    opens at.ac.fhcampuswien.fhmdb to javafx.fxml, spring.core, spring.beans, spring.context, spring.web, com.fasterxml.jackson.databind;
    opens at.ac.fhcampuswien.fhmdb.service to javafx.fxml, spring.beans, spring.context, spring.core, spring.web, com.fasterxml.jackson.databind;

    exports at.ac.fhcampuswien.fhmdb;
    exports at.ac.fhcampuswien.fhmdb.service;
}
