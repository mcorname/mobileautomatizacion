# Automatización Mobile SauceLabs - Carrito de Compras

Proyecto de pruebas de regresión automatizadas para la aplicación Android de **SauceLabs** (**My Demo App**), implementado con **Appium**, **Java 21**, **Selenium** y **Cucumber** (BDD).

---

## 1. Arquitectura del Proyecto

El proyecto sigue el patrón **Page Object Model (POM)** estructurado en capas:

```
SauceLabsMobileAutomation/
├── apps/
│   └── mda-2.0.2-23.apk                       → APK bajo prueba
├── src/test/java/com/nttdata/
│   ├── screens/                               → Capa de Pantallas (Page Objects)
│   │   ├── AppConfigScreen.java               → Control del ciclo de vida del driver Appium
│   │   ├── CatalogScreen.java                 → Interacción con la galería de productos
│   │   ├── ProductDetailScreen.java           → Selección de unidades y agregar al carrito
│   │   └── CartScreen.java                    → Validación del contenido del carrito
│   ├── steps/                                 → Capa de Lógica de Negocio
│   │   ├── AppConfigSteps.java                → Pasos genéricos de inicio/cierre
│   │   └── SauceLabsSteps.java                → Lógica de compra y validaciones
│   ├── stepsdefinitions/                      → Mapeo de Steps de Cucumber
│   │   ├── AppConfigStepsDefs.java            → Steps genéricos
│   │   └── SauceLabsStepDefs.java             → Steps específicos del escenario
│   ├── hooks/
│   │   └── Hooks.java                         → Setup y Teardown de cada prueba
│   ├── runners/
│   │   └── CucumberTestSuite.java             → Ejecutor principal de pruebas
│   └── support/
│       └── ScreenshotAttacher.java            → Utilidad de capturas de evidencia
├── src/test/resources/
│   ├── config/
│   │   └── saucelabs.properties               → Capabilities y configuración del emulador
│   └── features/
│       └── SauceLabsCarrito.feature           → Escenario en lenguaje Gherkin
├── pom.xml                                    → Dependencias Maven y configuración Java 21
└── README.md
```

---

## 2. Escenario de Prueba (Gherkin)

```gherkin
Given estoy en la aplicación de SauceLabs
And valido que carguen correctamente los productos en la galeria
When agrego <UNIDADES> del siguiente producto "<PRODUCTO>"
Then valido el carrito de compra actualice correctamente

Examples:
| PRODUCTO                  | UNIDADES |
| Sauce Labs Backpack       | 1        |
| Sauce Labs Bolt - T-Shirt | 1        |
| Sauce Labs Bike Light     | 2        |
```

---

## 3. Prerrequisitos

- **Java JDK 21**
- **Apache Maven 3.9+**
- **Node.js** y **Appium 2.x / 3.x** con el driver `uiautomator2`:
  ```bash
  appium driver install uiautomator2
  ```
- **Android SDK** con un emulador configurado (ej: Pixel con Android 10/11+).

---

## 4. Configuración

Editar el archivo `src/test/resources/config/saucelabs.properties` según los datos de tu emulador:

```properties
appium.url=http://127.0.0.1:4723/
platformName=Android
automationName=UiAutomator2
udid=emulator-5554
deviceName=PruebaMobile
platformVersion=11
app=D:/Documentos/SauceLabsMobileAutomation/apps/mda-2.0.2-23.apk
appPackage=com.saucelabs.mydemoapp.android
appActivity=com.saucelabs.mydemoapp.android.view.activities.MainActivity
noReset=false
autoGrantPermissions=true
newCommandTimeoutSeconds=300
```

---

## 5. Ejecución de las Pruebas

1. Iniciar el emulador de Android.
2. Iniciar el servidor de Appium:
   ```bash
   appium
   ```
3. Ejecutar las pruebas desde la raíz del proyecto con Maven:
   ```bash
   mvn clean test
   ```

---

## 6. Reportes Generados

Tras la ejecución, los reportes se generan automáticamente en la carpeta `target/`:
- **Reporte HTML:** `target/cucumber-report.html`
- **Reporte JSON:** `target/cucumber-report.json`
