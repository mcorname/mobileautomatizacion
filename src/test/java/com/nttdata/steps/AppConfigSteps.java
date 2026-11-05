package com.nttdata.steps;

import com.nttdata.screens.AppConfigScreen;

public class AppConfigSteps {

    private final AppConfigScreen appConfigScreen = new AppConfigScreen();

    public void iniciarAplicacion(String archivoConfiguracion) {
        System.out.println("[AppConfigSteps] Iniciar aplicacion con configuracion: " + archivoConfiguracion);
        appConfigScreen.iniciarAplicacion(archivoConfiguracion);
    }

    public void cerrarAplicacion(String appPackage) {
        System.out.println("[AppConfigSteps] Cerrar aplicacion: " + appPackage);
        appConfigScreen.cerrarAplicacion(appPackage);
    }

    public byte[] tomarScreenshot() {
        return appConfigScreen.tomarScreenshot();
    }
}
