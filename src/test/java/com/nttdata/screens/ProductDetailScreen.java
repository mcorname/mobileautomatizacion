package com.nttdata.screens;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.nttdata.screens.AppConfigScreen.getDriver;

public class ProductDetailScreen {

    // Identificadores de la pantalla de detalle
    private final By botonPlus = By.id("com.saucelabs.mydemoapp.android:id/plusIV");
    private final By botonAgregarCarrito = By.id("com.saucelabs.mydemoapp.android:id/cartBt");

    // Valida que cargue la pantalla de detalle verificando el boton de agregar
    public boolean validarPantallaDetalle() {
        try {
            AndroidDriver driver = getDriver();
            WebElement boton = driver.findElement(botonAgregarCarrito);
            return boton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Aumenta las unidades si se piden mas de 1
    public void seleccionarUnidades(int unidades) {
        if (unidades <= 1) {
            return;
        }
        AndroidDriver driver = getDriver();
        WebElement boton = driver.findElement(botonPlus);
        for (int i = 1; i < unidades; i++) {
            boton.click();
        }
    }

    // Presiona el boton de agregar al carrito
    public void agregarAlCarrito() {
        AndroidDriver driver = getDriver();
        driver.findElement(botonAgregarCarrito).click();
    }
}
