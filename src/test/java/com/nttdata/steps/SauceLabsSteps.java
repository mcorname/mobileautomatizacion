package com.nttdata.steps;

import com.nttdata.screens.CartScreen;
import com.nttdata.screens.CatalogScreen;
import com.nttdata.screens.ProductDetailScreen;

public class SauceLabsSteps {

    private final CatalogScreen catalogScreen = new CatalogScreen();
    private final ProductDetailScreen productDetailScreen = new ProductDetailScreen();
    private final CartScreen cartScreen = new CartScreen();

    public boolean validarGaleriaProductos() {
        System.out.println("[SauceLabsSteps] Validando galeria de productos...");
        return catalogScreen.validarCargaProductos();
    }

    public void agregarProductoConUnidades(String nombreProducto, int unidades) {
        System.out.println("[SauceLabsSteps] Paso 1: Seleccionar producto en el catalogo");
        catalogScreen.seleccionarProducto(nombreProducto);

        System.out.println("[SauceLabsSteps] Paso 2: Validar pantalla de detalle");
        productDetailScreen.validarPantallaDetalle(nombreProducto);

        System.out.println("[SauceLabsSteps] Paso 3: Ajustar cantidad a " + unidades + " unidades");
        productDetailScreen.seleccionarUnidades(unidades);

        System.out.println("[SauceLabsSteps] Paso 4: Agregar producto al carrito");
        productDetailScreen.agregarAlCarrito();
    }

    public boolean validarCarritoActualizado(String nombreProducto, int unidades) {
        System.out.println("[SauceLabsSteps] Paso 5: Abrir y verificar el carrito de compras");
        catalogScreen.abrirCarrito();

        boolean estaEnCarrito = cartScreen.validarPantallaCarrito();
        if (!estaEnCarrito) {
            System.out.println("[SauceLabsSteps] Advertencia: No se detecto titulo de carrito");
            return false;
        }

        boolean productoPresente = cartScreen.validarProductoEnCarrito(nombreProducto, unidades);
        System.out.println("[SauceLabsSteps] Resultado validacion carrito: " + productoPresente);
        return productoPresente;
    }
}
