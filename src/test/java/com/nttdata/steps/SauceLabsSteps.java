package com.nttdata.steps;

import com.nttdata.screens.CartScreen;
import com.nttdata.screens.CatalogScreen;
import com.nttdata.screens.ProductDetailScreen;
import org.junit.Assert;

public class SauceLabsSteps {

    private final CatalogScreen catalogScreen = new CatalogScreen();
    private final ProductDetailScreen productDetailScreen = new ProductDetailScreen();
    private final CartScreen cartScreen = new CartScreen();

    // 1. Valida que los productos cargaron en la galeria
    public boolean validarGaleriaProductos() {
        return catalogScreen.validarCargaProductos();
    }

    // 2. Agrega el producto con la cantidad solicitada
    public void agregarProductoConUnidades(String nombreProducto, int unidades) {
        // Toca el producto en el catalogo
        catalogScreen.seleccionarProducto(nombreProducto);

        // Valida que abra la pantalla de detalle
        boolean detalleAbierto = productDetailScreen.validarPantallaDetalle();
        Assert.assertTrue("Defecto detectado en el APK: La pantalla de detalle no abrio para " + nombreProducto, detalleAbierto);

        // Configura las unidades y lo agrega al carrito
        productDetailScreen.seleccionarUnidades(unidades);
        productDetailScreen.agregarAlCarrito();
    }

    // 3. Abre el carrito y confirma que el producto este adentro
    public boolean validarCarritoActualizado(String nombreProducto) {
        catalogScreen.abrirCarrito();
        return cartScreen.validarProductoEnCarrito(nombreProducto);
    }
}
