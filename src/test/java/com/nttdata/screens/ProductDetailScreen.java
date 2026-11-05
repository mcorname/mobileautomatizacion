package com.nttdata.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.nttdata.screens.AppConfigScreen.getDriver;

public class ProductDetailScreen {

    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    // Localizadores del detalle del producto
    private final By tituloProducto = By.id("com.saucelabs.mydemoapp.android:id/productTV");
    private final By btnPlus = By.id("com.saucelabs.mydemoapp.android:id/plusIV");
    private final By btnMinus = By.id("com.saucelabs.mydemoapp.android:id/minusIV");
    private final By cantidadTexto = By.id("com.saucelabs.mydemoapp.android:id/noTV");
    private final By btnAddToCart = By.id("com.saucelabs.mydemoapp.android:id/cartBt");

    public boolean validarPantallaDetalle(String nombreProducto) {
        System.out.println("[ProductDetailScreen] Validando pantalla de detalle del producto...");
        try {
            AndroidDriver driver = getDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // Confirmar que estamos en la pantalla de detalle verificando el boton de agregar
            WebElement boton = wait.until(ExpectedConditions.visibilityOfElementLocated(btnAddToCart));
            return boton != null && boton.isDisplayed();
        } catch (Exception e) {
            System.out.println("[ProductDetailScreen] No se confirmo pantalla de detalle: " + e.getMessage());
            return false;
        }
    }

    public void seleccionarUnidades(int unidadesDeseadas) {
        System.out.println("[ProductDetailScreen] Configurando " + unidadesDeseadas + " unidades...");
        if (unidadesDeseadas <= 1) {
            System.out.println("[ProductDetailScreen] La cantidad por defecto ya es 1.");
            return;
        }

        AndroidDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        // Desplazar hacia abajo si el boton de incrementar no esta en pantalla
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(" +
                            "new UiSelector().resourceId(\"com.saucelabs.mydemoapp.android:id/plusIV\"))"
            ));
        } catch (Exception ignored) {
        }

        WebElement botonPlus = wait.until(ExpectedConditions.elementToBeClickable(btnPlus));
        int clicks = unidadesDeseadas - 1;
        for (int i = 0; i < clicks; i++) {
            System.out.println("[ProductDetailScreen] Clic en boton '+' (" + (i + 1) + " de " + clicks + ")");
            botonPlus.click();
            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {
            }
        }

        System.out.println("[ProductDetailScreen] Unidades seleccionadas: " + obtenerCantidadActual());
    }

    public int obtenerCantidadActual() {
        try {
            AndroidDriver driver = getDriver();
            WebElement elementoCantidad = driver.findElement(cantidadTexto);
            return Integer.parseInt(elementoCantidad.getText().trim());
        } catch (Exception e) {
            return 1;
        }
    }

    public void agregarAlCarrito() {
        System.out.println("[ProductDetailScreen] Clic en boton 'Add to cart'...");
        AndroidDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        // Desplazar para asegurar visibilidad del boton
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(" +
                            "new UiSelector().resourceId(\"com.saucelabs.mydemoapp.android:id/cartBt\"))"
            ));
        } catch (Exception ignored) {
        }

        try {
            WebElement botonAgregar = wait.until(ExpectedConditions.elementToBeClickable(btnAddToCart));
            botonAgregar.click();
            System.out.println("[ProductDetailScreen] Producto agregado con exito.");
        } catch (Exception e) {
            WebElement botonAlt = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.Button[@text='Add to cart' or contains(@content-desc, 'Add to cart')]")
            ));
            botonAlt.click();
            System.out.println("[ProductDetailScreen] Producto agregado mediante boton alternativo.");
        }
    }
}
