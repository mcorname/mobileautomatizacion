package com.nttdata.support;

import com.nttdata.steps.AppConfigSteps;
import io.cucumber.java.Scenario;

public final class ScreenshotAttacher {

    private static final AppConfigSteps appConfigSteps = new AppConfigSteps();

    private ScreenshotAttacher() {
    }

    public static void attach(Scenario scenario, String nombre) {
        if (scenario == null) {
            return;
        }

        byte[] screenshot = appConfigSteps.tomarScreenshot();
        if (screenshot != null && screenshot.length > 0) {
            System.out.println("[ScreenshotAttacher] Adjuntando evidencia: " + nombre);
            scenario.attach(screenshot, "image/png", nombre);
        } else {
            System.out.println("[ScreenshotAttacher] No se pudo obtener evidencia para: " + nombre);
        }
    }
}
