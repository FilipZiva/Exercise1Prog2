package at.ac.fhcampuswien.fhmdb.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyFactoryTest {

    private MyFactory factory;

    @BeforeEach
    void setUp() {
        factory = new MyFactory();
    }

    @Test
    void testSingleInstanceCreation() {
        HomeController instance1 = (HomeController) factory.call(HomeController.class);
        HomeController instance2 = (HomeController) factory.call(HomeController.class);

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
    }
}