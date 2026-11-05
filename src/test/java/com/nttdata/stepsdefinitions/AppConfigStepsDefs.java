package com.nttdata.stepsdefinitions;

import com.nttdata.steps.AppConfigSteps;
import com.nttdata.support.ScreenshotAttacher;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class AppConfigStepsDefs {

    private final AppConfigSteps appConfigSteps = new AppConfigSteps();
    private Scenario scenario;

    @Before
    public void setupScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("ingreso al aplicativo {string}")
    public void ingresoAlAplicativo(String appKey) {
        System.out.println("[AppConfigStepsDefs] Iniciando aplicativo: " + appKey);
        appConfigSteps.iniciarAplicacion(appKey + ".properties");
        ScreenshotAttacher.attach(scenario, "Pantalla inicial");
    }

    @Then("cierro el aplicativo {string}")
    public void cierroElAplicativo(String appPackage) {
        System.out.println("[AppConfigStepsDefs] Cerrando aplicativo: " + appPackage);
        appConfigSteps.cerrarAplicacion(appPackage);
    }
}
