package com.nttdata.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.nttdata.screens.AppConfigScreen.getDriver;

public class CatalogScreen {

    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    // Localizadores de la galeria de productos
    private final By tituloProductos = By.id("com.saucelabs.mydemoapp.android:id/productTV");
    private final By listaProductos = By.id("com.saucelabs.mydemoapp.android:id/productRV");
    private final By btnCarrito = By.id("com.saucelabs.mydemoapp.android:id/cartRL");
    private final By iconoCarrito = By.id("com.saucelabs.mydemoapp.android:id/cartIV");
    private final By badgeCarrito = By.id("com.saucelabs.mydemoapp.android:id/cartTV");

    public boolean validarCargaProductos() {
        System.out.println("[CatalogScreen] Validando que carguen los productos en la galeria...");
        try {
            AndroidDriver driver = getDriver();
            WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

            // Espera explicita a que el titulo o la lista de productos esten visibles
            WebElement titulo = wait.until(ExpectedConditions.visibilityOfElementLocated(tituloProductos));
            boolean visible = titulo.isDisplayed();
            System.out.println("[CatalogScreen] Galeria cargada correctamente: " + visible);
            return visible;
        } catch (Exception e) {
            System.out.println("[CatalogScreen] Error esperando carga de galeria: " + e.getMessage());
            return false;
        }
    }

    public void seleccionarProducto(String nombreProducto) {
        System.out.println("[CatalogScreen] Seleccionando producto: " + nombreProducto);
        AndroidDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        // Limpieza del nombre para busquedas flexibles (ej: con o sin guion)
        String textoLimpio = nombreProducto.replace(" - ", " ").trim();
        String palabraClave = textoLimpio.replace("Sauce Labs ", "").trim();

        WebElement elementoProducto = null;

        // 1. Intentar ubicarlo si ya esta visible en pantalla
        try {
            By locator = By.xpath("//android.widget.TextView[@text='" + nombreProducto + "' or @text='" + textoLimpio + "' or contains(@text, '" + palabraClave + "')]");
            elementoProducto = driver.findElement(locator);
        } catch (Exception e) {
            System.out.println("[CatalogScreen] Producto no visible a primera vista, realizando scroll...");
        }

        // 2. Si no esta visible, hacer scroll mediante UiScrollable
        if (elementoProducto == null) {
            try {
                elementoProducto = driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(" +
                                "new UiSelector().textContains(\"" + palabraClave + "\"))"
                ));
            } catch (Exception e) {
                // Alternativa de busqueda por texto completo
                elementoProducto = driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(" +
                                "new UiSelector().textContains(\"" + nombreProducto + "\"))"
                ));
            }
        }

        // 3. Hacer clic en el producto seleccionado
        wait.until(ExpectedConditions.elementToBeClickable(elementoProducto)).click();
        System.out.println("[CatalogScreen] Clic realizado en el producto: " + nombreProducto);
    }

    public void abrirCarrito() {
        System.out.println("[CatalogScreen] Abriendo el carrito de compras...");
        AndroidDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        try {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnCarrito));
            btn.click();
        } catch (Exception e) {
            // Si no responde por cartRL, intentar por cartIV
            WebElement icono = wait.until(ExpectedConditions.elementToBeClickable(iconoCarrito));
            icono.click();
        }
    }

    public int obtenerCantidadBadgeCarrito() {
        try {
            AndroidDriver driver = getDriver();
            WebElement badge = driver.findElement(badgeCarrito);
            String texto = badge.getText().trim();
            System.out.println("[CatalogScreen] Cantidad en badge de carrito: " + texto);
            return Integer.parseInt(texto);
        } catch (Exception e) {
            System.out.println("[CatalogScreen] Badge de carrito no visible o vacio.");
            return 0;
        }
    }
}
