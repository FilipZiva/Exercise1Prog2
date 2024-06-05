package at.ac.fhcampuswien.fhmdb.controller;

import javafx.util.Callback;

public class MyFactory implements Callback<Class<?>, Object> {
    private HomeController instance;

    @Override
    public Object call(Class<?> aClass) {
        if (aClass == HomeController.class) {
            if (instance == null) {
                try {
                    instance = (HomeController) aClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return instance;
        }
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
