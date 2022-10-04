package com.otus.hooks;

import com.google.inject.Inject;
import com.otus.diconfig.GuiceScoped;
import io.cucumber.java.After;

public class Hooks {

    @Inject
    private final GuiceScoped guiceScoped;
    public Hooks(GuiceScoped guiceScoped) {
        this.guiceScoped = guiceScoped;
    }

    @After
    public void afterScenario() {
        if (guiceScoped.driver != null) {
            guiceScoped.driver.close();
            guiceScoped.driver.quit();
        }
    }
}
