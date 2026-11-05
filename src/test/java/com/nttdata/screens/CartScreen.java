package com.nttdata.screens;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.nttdata.screens.AppConfigScreen.getDriver;

public class CartScreen {

    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    // Localizadores de la pantalla del carrito
    private final By tituloCarrito = By.id("com.saucelabs.mydemoapp.android:id/productTV");
    private final By textoItemsTotal = By.id("com.saucelabs.mydemoapp.android:id/itemsTV");
    private final By textoPrecioTotal = By.id("com.saucelabs.mydemoapp.android:id/totalPriceTV");
    private final By btnProcederCheckout = By.id("com.saucelabs.mydemoapp.android:id/cartBt");

    public boolean validarPantallaCarrito() {
        System.out.println("[CartScreen] Validando que la pantalla del carrito este visible...");
        try {
            AndroidDriver driver = getDriver();
            WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

            // Validar por titulo "My Cart" o por el resumen de items
            WebElement titulo = wait.until(ExpectedConditions.visibilityOfElementLocated(tituloCarrito));
            boolean esVisible = titulo.isDisplayed();
            System.out.println("[CartScreen] Pantalla de carrito visible: " + esVisible + " (Texto: " + titulo.getText() + ")");
            return esVisible;
        } catch (Exception e) {
            try {
                // Alternativa: validar por el texto de items o boton de checkout
                AndroidDriver driver = getDriver();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                return wait.until(ExpectedConditions.visibilityOfElementLocated(btnProcederCheckout)).isDisplayed();
            } catch (Exception ex) {
                System.out.println("[CartScreen] No se pudo confirmar la pantalla del carrito: " + ex.getMessage());
                return false;
            }
        }
    }

    public boolean validarProductoEnCarrito(String nombreProducto, int unidadesEsperadas) {
        System.out.println("[CartScreen] Validando producto en carrito: " + nombreProducto + " con " + unidadesEsperadas + " unidades");
        try {
            AndroidDriver driver = getDriver();
            WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

            String textoLimpio = nombreProducto.replace(" - ", " ").trim();
            String palabraClave = textoLimpio.replace("Sauce Labs ", "").trim();

            // Verificar que aparezca el nombre del producto
            By locatorProducto = By.xpath("//android.widget.TextView[@text='" + nombreProducto + "' or @text='" + textoLimpio + "' or contains(@text, '" + palabraClave + "')]");
            WebElement elemento = wait.until(ExpectedConditions.visibilityOfElementLocated(locatorProducto));

            System.out.println("[CartScreen] Producto encontrado en carrito: " + elemento.getText());
            return true;
        } catch (Exception e) {
            System.out.println("[CartScreen] No se encontro el producto en el carrito: " + e.getMessage());
            return false;
        }
    }

    public String obtenerTextoTotalItems() {
        try {
            AndroidDriver driver = getDriver();
            WebElement elemento = driver.findElement(textoItemsTotal);
            return elemento.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }
}
