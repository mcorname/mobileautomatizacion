package com.nttdata.screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

public class AppConfigScreen {

    private static AndroidDriver driver;

    // Metodo para leer el archivo de configuracion e iniciar Appium
    public void iniciarAplicacion(String archivoConfig) {
        try {
            Properties prop = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("config/" + archivoConfig);
            prop.load(input);

            UiAutomator2Options options = new UiAutomator2Options();
            options.setPlatformName(prop.getProperty("platformName"));
            options.setDeviceName(prop.getProperty("deviceName"));
            options.setUdid(prop.getProperty("udid"));
            options.setPlatformVersion(prop.getProperty("platformVersion"));
            options.setApp(prop.getProperty("app"));
            options.setAppPackage(prop.getProperty("appPackage"));
            options.setAppActivity(prop.getProperty("appActivity"));
            options.setAppWaitActivity(prop.getProperty("appWaitActivity"));
            options.setNoReset(false);
            options.setAutoGrantPermissions(true);

            driver = new AndroidDriver(new URL(prop.getProperty("appium.url")), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        } catch (Exception e) {
            throw new RuntimeException("Error al iniciar la aplicacion: " + e.getMessage(), e);
        }
    }

    public static AndroidDriver getDriver() {
        return driver;
    }

    public byte[] tomarScreenshot() {
        if (driver != null) {
            return ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
        }
        return new byte[0];
    }

    public void cerrarAplicacion(String appPackage) {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
