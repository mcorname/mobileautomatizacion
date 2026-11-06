package com.nttdata.hooks;

import com.nttdata.steps.AppConfigSteps;
import com.nttdata.support.ScreenshotAttacher;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;

public class Hooks {

    private final AppConfigSteps appConfigSteps = new AppConfigSteps();

    @After
    public void afterScenario(Scenario scenario) {
        ScreenshotAttacher.attach(scenario, "Estado final");
        appConfigSteps.cerrarAplicacion("com.saucelabs.mydemoapp.android");
    }
}
