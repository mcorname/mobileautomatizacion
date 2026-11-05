# language: es
Característica: Validacion del carrito de compras en SauceLabs

  Como usuario de la aplicacion SauceLabs
  Quiero agregar productos al carrito de compras
  Para validar que las unidades y el carrito se actualicen correctamente

  @CarritoCompras @Smoke
  Esquema del escenario: Validar la actualizacion del carrito de compras al agregar productos
    Dado estoy en la aplicación de SauceLabs
    Y valido que carguen correctamente los productos en la galeria
    Cuando agrego <UNIDADES> del siguiente producto "<PRODUCTO>"
    Entonces valido el carrito de compra actualice correctamente

    Ejemplos:
      | PRODUCTO                  | UNIDADES |
      | Sauce Labs Backpack       | 1        |
      | Sauce Labs Bolt - T-Shirt | 1        |
      | Sauce Labs Bike Light     | 2        |
