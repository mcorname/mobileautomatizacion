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
    private final By precioProducto = By.id("com.saucelabs.mydemoapp.android:id/priceTV");
    private final By btnPlus = By.id("com.saucelabs.mydemoapp.android:id/plusIV");
    private final By btnMinus = By.id("com.saucelabs.mydemoapp.android:id/minusIV");
    private final By cantidadTexto = By.id("com.saucelabs.mydemoapp.android:id/noTV");
    private final By btnAddToCart = By.id("com.saucelabs.mydemoapp.android:id/cartBt");

    public boolean validarPantallaDetalle(String nombreProducto) {
        System.out.println("[ProductDetailScreen] Validando pantalla de detalle del producto...");
        try {
            AndroidDriver driver = getDriver();
            WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

            WebElement titulo = wait.until(ExpectedConditions.visibilityOfElementLocated(tituloProducto));
            System.out.println("[ProductDetailScreen] Titulo en detalle: " + titulo.getText());
            return titulo.isDisplayed();
        } catch (Exception e) {
            System.out.println("[ProductDetailScreen] No se encontro titulo de detalle: " + e.getMessage());
            return false;
        }
    }

    public void seleccionarUnidades(int unidadesDeseadas) {
        System.out.println("[ProductDetailScreen] Configurando " + unidadesDeseadas + " unidades...");
        AndroidDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        // Si la pantalla requiere scroll hacia abajo para ver los botones de cantidad y agregar al carrito:
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(" +
                            "new UiSelector().resourceId(\"com.saucelabs.mydemoapp.android:id/cartBt\"))"
            ));
        } catch (Exception ignored) {
        }

        // Obtener la cantidad actual (generalmente inicia en 1)
        int cantidadActual = obtenerCantidadActual();
        System.out.println("[ProductDetailScreen] Cantidad inicial: " + cantidadActual);

        // Incrementar pulsando el boton '+' hasta alcanzar las unidades deseadas
        WebElement botonPlus = wait.until(ExpectedConditions.elementToBeClickable(btnPlus));
        while (cantidadActual < unidadesDeseadas) {
            System.out.println("[ProductDetailScreen] Clic en boton '+' para sumar unidad...");
            botonPlus.click();
            cantidadActual++;
            try {
                Thread.sleep(500); // Breve pausa para animacion de UI
            } catch (InterruptedException ignored) {
            }
        }

        System.out.println("[ProductDetailScreen] Unidades seleccionadas correctamente: " + obtenerCantidadActual());
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

        try {
            WebElement botonAgregar = wait.until(ExpectedConditions.elementToBeClickable(btnAddToCart));
            botonAgregar.click();
            System.out.println("[ProductDetailScreen] Producto agregado satisfactoriamente.");
        } catch (Exception e) {
            // Intento alternativo por texto
            WebElement botonPorTexto = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.Button[@text='Add to cart' or contains(@content-desc, 'Add to cart')]")
            ));
            botonPorTexto.click();
            System.out.println("[ProductDetailScreen] Producto agregado por texto alternativo.");
        }
    }
}
