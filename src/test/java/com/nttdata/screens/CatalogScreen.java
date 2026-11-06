package com.nttdata.screens;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.nttdata.screens.AppConfigScreen.getDriver;

public class CatalogScreen {

    // Identificadores de la pantalla de catalogo
    private final By tituloProductos = By.id("com.saucelabs.mydemoapp.android:id/productTV");
    private final By botonCarrito = By.id("com.saucelabs.mydemoapp.android:id/cartRL");

    // Valida que el titulo 'Products' este visible
    public boolean validarCargaProductos() {
        try {
            AndroidDriver driver = getDriver();
            WebElement titulo = driver.findElement(tituloProductos);
            return titulo.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Selecciona la imagen del producto segun su nombre
    public void seleccionarProducto(String nombreProducto) {
        AndroidDriver driver = getDriver();
        // Se limpia el guion para coincidir con el nombre en la aplicacion
        String nombreLimpio = nombreProducto.replace(" - ", " ");
        By locatorImagen = By.xpath("//android.widget.ImageView[contains(@content-desc, '" + nombreLimpio + "')]");
        driver.findElement(locatorImagen).click();
    }

    // Abre el carrito de compras
    public void abrirCarrito() {
        AndroidDriver driver = getDriver();
        driver.findElement(botonCarrito).click();
    }
}
