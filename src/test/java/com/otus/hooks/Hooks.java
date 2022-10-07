package com.otus.hooks;

import com.google.inject.Inject;
import diconfig.GuiceScoped;
import io.cucumber.java.After;

public class Hooks {

    @Inject
    private GuiceScoped guiceScoped;

    @After
    public void afterScenario() {
        if (guiceScoped.driver != null) {
            guiceScoped.driver.close();
            guiceScoped.driver.quit();
        }
    }
}
