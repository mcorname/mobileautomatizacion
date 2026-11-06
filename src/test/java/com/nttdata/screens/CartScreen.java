package com.nttdata.screens;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.nttdata.screens.AppConfigScreen.getDriver;

public class CartScreen {

    // Valida que el producto agregado aparezca en la lista del carrito
    public boolean validarProductoEnCarrito(String nombreProducto) {
        try {
            AndroidDriver driver = getDriver();
            String nombreLimpio = nombreProducto.replace(" - ", " ");
            By locatorProducto = By.xpath("//*[@text='" + nombreLimpio + "']");
            WebElement elemento = driver.findElement(locatorProducto);
            return elemento.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
