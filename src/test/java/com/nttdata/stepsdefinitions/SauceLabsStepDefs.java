package com.nttdata.stepsdefinitions;

import com.nttdata.steps.AppConfigSteps;
import com.nttdata.steps.SauceLabsSteps;
import com.nttdata.support.ScreenshotAttacher;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class SauceLabsStepDefs {

    private final AppConfigSteps appConfigSteps = new AppConfigSteps();
    private final SauceLabsSteps sauceLabsSteps = new SauceLabsSteps();

    private Scenario scenario;
    private String productoActual;

    @Before
    public void setupScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("estoy en la aplicación de SauceLabs")
    public void estoyEnLaAplicacionDeSauceLabs() {
        appConfigSteps.iniciarAplicacion("saucelabs.properties");
        ScreenshotAttacher.attach(scenario, "Inicio de aplicacion");
    }

    @And("valido que carguen correctamente los productos en la galeria")
    public void validoQueCarguenCorrectamenteLosProductosEnLaGaleria() {
        boolean cargaron = sauceLabsSteps.validarGaleriaProductos();
        ScreenshotAttacher.attach(scenario, "Galeria cargada");
        Assert.assertTrue("Error: Los productos no cargaron en la galeria", cargaron);
    }

    @When("agrego {int} del siguiente producto {string}")
    public void agregoUnidadesDelSiguienteProducto(int unidades, String producto) {
        this.productoActual = producto;
        sauceLabsSteps.agregarProductoConUnidades(producto, unidades);
        ScreenshotAttacher.attach(scenario, "Producto agregado");
    }

    @Then("valido el carrito de compra actualice correctamente")
    public void validoElCarritoDeCompraActualiceCorrectamente() {
        boolean actualizado = sauceLabsSteps.validarCarritoActualizado(productoActual);
        ScreenshotAttacher.attach(scenario, "Carrito actualizado");
        Assert.assertTrue("Error: El carrito no se actualizo", actualizado);
    }
}
