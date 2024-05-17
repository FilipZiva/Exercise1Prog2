module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.jfoenix;

    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.web;

    requires com.h2database;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires static lombok;
    requires ormlite.jdbc;
    requires java.sql;

    opens at.ac.fhcampuswien.fhmdb to javafx.fxml, spring.core, spring.beans, spring.context, spring.web, com.fasterxml.jackson.databind;
    opens at.ac.fhcampuswien.fhmdb.service to javafx.fxml, spring.beans, spring.context, spring.core, spring.web, com.fasterxml.jackson.databind;
    opens at.ac.fhcampuswien.fhmdb.models to ormlite.jdbc;
    opens at.ac.fhcampuswien.fhmdb.controller to com.fasterxml.jackson.databind, javafx.fxml, spring.beans, spring.context, spring.core, spring.web;

    exports at.ac.fhcampuswien.fhmdb;
    exports at.ac.fhcampuswien.fhmdb.service;
    exports at.ac.fhcampuswien.fhmdb.controller;
    exports at.ac.fhcampuswien.fhmdb.ui;
    opens at.ac.fhcampuswien.fhmdb.ui to com.fasterxml.jackson.databind, javafx.fxml, spring.beans, spring.context, spring.core, spring.web;
}