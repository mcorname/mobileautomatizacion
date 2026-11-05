package com.nttdata.hooks;

import com.nttdata.steps.AppConfigSteps;
import com.nttdata.support.ScreenshotAttacher;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    private final AppConfigSteps appConfigSteps = new AppConfigSteps();
    private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("========================================");
        System.out.println("[Hooks] Iniciando escenario: " + scenario.getName());
        System.out.println("========================================");
    }

    @After
    public void afterScenario(Scenario scenario) {
        String estado = scenario.isFailed() ? "FALLIDO" : "EXITOSO";
        System.out.println("[Hooks] Escenario finalizado con estado: " + estado);

        // Toma captura del estado final del escenario
        String nombreCaptura = scenario.isFailed() ? "Estado final con fallo" : "Estado final exitoso";
        ScreenshotAttacher.attach(scenario, nombreCaptura);

        // Cierra la aplicacion para aislar cada prueba
        appConfigSteps.cerrarAplicacion(APP_PACKAGE);
    }
}
