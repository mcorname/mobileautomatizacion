package com.nttdata.screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

public class AppConfigScreen {

    private static AndroidDriver driver;

    public void iniciarAplicacion(String archivoConfiguracion) {
        if (driver != null) {
            System.out.println("[AppConfigScreen] Ya existe una sesion activa de Appium, se reutiliza.");
            return;
        }

        System.out.println("[AppConfigScreen] Cargando configuracion: " + archivoConfiguracion);
        Properties properties = cargarPropiedades(archivoConfiguracion);

        try {
            UiAutomator2Options options = new UiAutomator2Options();
            options.setPlatformName(properties.getProperty("platformName", "Android"));
            options.setAutomationName(properties.getProperty("automationName", "UiAutomator2"));
            options.setUdid(obtenerPropiedad(properties, "udid"));
            options.setDeviceName(obtenerPropiedad(properties, "deviceName"));
            options.setPlatformVersion(obtenerPropiedad(properties, "platformVersion"));
            options.setApp(obtenerPropiedad(properties, "app"));
            options.setAppPackage(obtenerPropiedad(properties, "appPackage"));
            options.setAppActivity(obtenerPropiedad(properties, "appActivity"));
            options.setAppWaitActivity(properties.getProperty("appWaitActivity", "*"));
            options.setNoReset(Boolean.parseBoolean(properties.getProperty("noReset", "false")));
            options.setAutoGrantPermissions(Boolean.parseBoolean(properties.getProperty("autoGrantPermissions", "true")));

            long timeoutSeconds = Long.parseLong(properties.getProperty("newCommandTimeoutSeconds", "300"));
            options.setNewCommandTimeout(Duration.ofSeconds(timeoutSeconds));

            String appiumUrl = obtenerPropiedad(properties, "appium.url");
            System.out.println("[AppConfigScreen] Conectando con servidor Appium: " + appiumUrl);
            driver = new AndroidDriver(new URL(appiumUrl), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            // Asegurar que no hayan paneles del sistema o notificaciones bloqueando la pantalla
            try {
                Runtime.getRuntime().exec("adb shell cmd statusbar collapse").waitFor();
            } catch (Exception ignored) {
            }

            System.out.println("[AppConfigScreen] Sesion iniciada con exito. SessionId: " + driver.getSessionId());
        } catch (Exception e) {
            throw new RuntimeException("[AppConfigScreen] Error al conectar con Appium: " + e.getMessage(), e);
        }
    }

    public static AndroidDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("[AppConfigScreen] El driver no ha sido inicializado. Debe iniciar la app primero.");
        }
        return driver;
    }

    public void cerrarAplicacion(String appPackage) {
        if (driver == null) {
            return;
        }

        try {
            System.out.println("[AppConfigScreen] Cerrando aplicacion: " + appPackage);
            try {
                driver.terminateApp(appPackage);
            } catch (Exception ignored) {
            }
        } finally {
            System.out.println("[AppConfigScreen] Finalizando sesion de Appium...");
            try {
                driver.quit();
            } catch (Exception ignored) {
            }
            driver = null;
            System.out.println("[AppConfigScreen] Sesion cerrada.");
        }
    }

    public byte[] tomarScreenshot() {
        if (driver == null) {
            return new byte[0];
        }
        try {
            return driver.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            System.out.println("[AppConfigScreen] No fue posible tomar captura: " + e.getMessage());
            return new byte[0];
        }
    }

    private Properties cargarPropiedades(String archivoConfiguracion) {
        Properties properties = new Properties();
        String resourcePath = "config/" + archivoConfiguracion;
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (input != null) {
                properties.load(input);
                return properties;
            }

            File file = new File("src/test/resources/config/" + archivoConfiguracion);
            if (file.exists()) {
                try (InputStream fis = new FileInputStream(file)) {
                    properties.load(fis);
                    return properties;
                }
            }

            throw new IllegalStateException("No se encontro el archivo de configuracion: " + resourcePath);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer archivo de configuracion: " + resourcePath, e);
        }
    }

    private String obtenerPropiedad(Properties properties, String key) {
        String value = properties.getProperty(key);
        return Objects.requireNonNull(value, "[AppConfigScreen] Falta la propiedad obligatoria: '" + key + "'");
    }
}
