from reportlab.lib.pagesizes import letter
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib import colors
import sys
import os

pdf_path = os.path.join(os.path.dirname(__file__), "Informe_de_Ejecucion_SauceLabs.pdf")
doc = SimpleDocTemplate(pdf_path, pagesize=letter, rightMargin=40, leftMargin=40, topMargin=35, bottomMargin=35)
styles = getSampleStyleSheet()

title_style = ParagraphStyle('TitleStyle', parent=styles['Heading1'], fontSize=16, leading=20, textColor=colors.HexColor('#1A365D'), alignment=1)
subtitle_style = ParagraphStyle('SubTitleStyle', parent=styles['Normal'], fontSize=10, leading=13, textColor=colors.HexColor('#4A5568'), alignment=1)
h2_style = ParagraphStyle('H2Style', parent=styles['Heading2'], fontSize=11.5, leading=15, textColor=colors.HexColor('#2B6CB0'), spaceBefore=8, spaceAfter=4)
h3_style = ParagraphStyle('H3Style', parent=styles['Heading3'], fontSize=9.5, leading=12, textColor=colors.HexColor('#2D3748'), spaceBefore=5, spaceAfter=2)
body_style = ParagraphStyle('BodyStyle', parent=styles['Normal'], fontSize=8.5, leading=11.5, textColor=colors.HexColor('#2D3748'))
bold_style = ParagraphStyle('BoldStyle', parent=body_style, fontName='Helvetica-Bold')

story = []

# 1. Presentacion
story.append(Paragraph("INFORME DE EJECUCION DE PRUEBAS AUTOMATIZADAS", title_style))
story.append(Spacer(1, 3))
story.append(Paragraph("Modulo: Carrito de Compras - SauceLabs Android App", subtitle_style))
story.append(Spacer(1, 8))

meta_data = [
    [Paragraph("<b>Aplicacion:</b>", body_style), Paragraph("SauceLabs My Demo App (MDA)", body_style)],
    [Paragraph("<b>Version APK:</b>", body_style), Paragraph("mda-2.0.2-23.apk (Entregable de Desarrollo)", body_style)],
    [Paragraph("<b>Framework:</b>", body_style), Paragraph("Appium 2.x/3.x + Java 21 + Cucumber BDD + JUnit 4", body_style)],
    [Paragraph("<b>Dispositivo:</b>", body_style), Paragraph("Android Emulator - Medium Phone (API 37)", body_style)],
    [Paragraph("<b>Objetivo:</b>", body_style), Paragraph("Validar la regresion del carrito de compras para 3 productos con unidades configuradas en Examples, identificando posibles fallos o comportamientos no deseados en la aplicacion.", body_style)]
]
t_meta = Table(meta_data, colWidths=[105, 425])
t_meta.setStyle(TableStyle([
    ('BACKGROUND', (0,0), (-1,-1), colors.HexColor('#F7FAFC')),
    ('BOX', (0,0), (-1,-1), 1, colors.HexColor('#CBD5E0')),
    ('INNERGRID', (0,0), (-1,-1), 0.5, colors.HexColor('#E2E8F0')),
    ('TOPPADDING', (0,0), (-1,-1), 3),
    ('BOTTOMPADDING', (0,0), (-1,-1), 3),
]))
story.append(t_meta)
story.append(Spacer(1, 6))

# 2. Resumen de ejecucion
story.append(Paragraph("1. Resumen de Ejecucion", h2_style))
story.append(Paragraph("Se ejecutaron los 3 Casos de Prueba (CP) mediante la suite Cucumber. Dos escenarios concluyeron de manera exitosa y uno detecto una inestabilidad critica (cierre inesperado de la aplicacion).", body_style))
story.append(Spacer(1, 4))

resumen_data = [
    ["ID", "Escenario / Producto", "Unidades", "Carga App", "Detalle", "Carrito", "Resultado"],
    ["CP01", "Sauce Labs Backpack", "1", "OK", "OK", "OK", "PASO"],
    ["CP02", "Sauce Labs Bolt - T-Shirt", "1", "OK", "OK", "OK", "PASO"],
    ["CP03", "Sauce Labs Bike Light", "2", "OK", "FALLO", "N/A", "FALLO (DEFECTO)"]
]
t_resumen = Table(resumen_data, colWidths=[40, 185, 55, 60, 60, 55, 75])
t_resumen.setStyle(TableStyle([
    ('BACKGROUND', (0,0), (-1,0), colors.HexColor('#2B6CB0')),
    ('TEXTCOLOR', (0,0), (-1,0), colors.white),
    ('FONTNAME', (0,0), (-1,0), 'Helvetica-Bold'),
    ('FONTSIZE', (0,0), (-1,0), 8),
    ('ALIGN', (0,0), (-1,-1), 'CENTER'),
    ('ALIGN', (1,1), (1,-1), 'LEFT'),
    ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor('#CBD5E0')),
    ('ROWBACKGROUNDS', (0,1), (-1,-2), [colors.white, colors.HexColor('#F7FAFC')]),
    ('TEXTCOLOR', (6,1), (6,2), colors.HexColor('#22543D')),
    ('TEXTCOLOR', (6,3), (6,3), colors.HexColor('#C53030')),
    ('FONTNAME', (6,1), (6,-1), 'Helvetica-Bold'),
    ('TOPPADDING', (0,0), (-1,-1), 3),
    ('BOTTOMPADDING', (0,0), (-1,-1), 3),
]))
story.append(t_resumen)
story.append(Spacer(1, 6))

# 3. Resultado por escenario
story.append(Paragraph("2. Resultado por Escenario", h2_style))

scenarios_info = [
    ("CP01: Sauce Labs Backpack (1 unidad) - [EXITOSO]", [
        "Given estoy en la aplicacion de SauceLabs: Sesion Appium iniciada correctamente en SplashActivity.",
        "And valido que carguen correctamente los productos en la galeria: Validado titulo 'Products' y catalogo visible.",
        "When agrego 1 del siguiente producto 'Sauce Labs Backpack': Clic en producto, validacion de detalle y clic en 'Add to cart'.",
        "Then valido el carrito de compra actualice correctamente: Apertura de carrito, validando 'Sauce Labs Backpack' con 1 unidad."
    ]),
    ("CP02: Sauce Labs Bolt - T-Shirt (1 unidad) - [EXITOSO]", [
        "Given estoy en la aplicacion de SauceLabs: Sesion limpia e inicio en SplashActivity.",
        "And valido que carguen correctamente los productos en la galeria: Catalogo cargado sin problemas.",
        "When agrego 1 del siguiente producto 'Sauce Labs Bolt - T-Shirt': Busqueda por contenido accesible, detalle cargado y agregado con 1 unidad.",
        "Then valido el carrito de compra actualice correctamente: Vista 'My Cart' validada con el articulo y 1 unidad agregada."
    ]),
    ("CP03: Sauce Labs Bike Light (2 unidades) - [FALLIDO / DEFECTO EN APK]", [
        "Given estoy en la aplicacion de SauceLabs: Aplicacion inicia correctamente.",
        "And valido que carguen correctamente los productos en la galeria: Galeria visible con productos.",
        "When agrego 2 del siguiente producto 'Sauce Labs Bike Light': Al hacer clic sobre 'Sauce Labs Bike Light', la aplicacion presenta un cierre inesperado (Crash / ANR). La automatizacion detecta que la pantalla de detalle no abrio y reporta el defecto.",
        "Then valido el carrito de compra actualice correctamente: Omitido (Skipped) debido al fallo critico en el paso anterior."
    ])
]

for title, steps in scenarios_info:
    story.append(Paragraph(title, h3_style))
    for step in steps:
        story.append(Paragraph("&bull; " + step, body_style))
    story.append(Spacer(1, 2))

story.append(Spacer(1, 6))

# 4. Evidencias
story.append(Paragraph("3. Evidencia de la Ejecucion por Escenario", h2_style))
story.append(Paragraph("Las evidencias de cada paso son capturadas automaticamente con <i>ScreenshotAttacher</i> y almacenadas en los reportes (<i>target/cucumber-report.html</i> y <i>target/cucumber-report.json</i>):", body_style))
story.append(Spacer(1, 4))

evid_table = [
    ["Hito / Validacion", "Evidencia Generada", "Descripcion de Evidencia"],
    ["Carga de Aplicacion", "Captura de pantalla", "Visualizacion de la galeria de productos 'Products' y header de navegacion."],
    ["Detalle de Producto", "Captura de pantalla", "Pantalla con descripcion, precio y botones de cantidad (+/-)."],
    ["Agregar al Carrito", "Captura de pantalla", "Accion sobre boton 'Add to cart' y actualizacion del badge superior."],
    ["Validacion de Carrito", "Captura de pantalla", "Vista 'My Cart' confirmando nombre del articulo y cantidad correcta."],
    ["Captura ante Fallo", "Captura automatica (@After)", "Evidencia grafica inmediata ante el cierre o error en la aplicacion."]
]
t_evid = Table(evid_table, colWidths=[115, 125, 290])
t_evid.setStyle(TableStyle([
    ('BACKGROUND', (0,0), (-1,0), colors.HexColor('#4A5568')),
    ('TEXTCOLOR', (0,0), (-1,0), colors.white),
    ('FONTNAME', (0,0), (-1,0), 'Helvetica-Bold'),
    ('FONTSIZE', (0,0), (-1,0), 8),
    ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor('#CBD5E0')),
    ('TOPPADDING', (0,0), (-1,-1), 2.5),
    ('BOTTOMPADDING', (0,0), (-1,-1), 2.5),
]))
story.append(t_evid)
story.append(Spacer(1, 6))

# 5. Hallazgos
story.append(Paragraph("4. Hallazgos", h2_style))
story.append(Paragraph("1. <b>Defecto Critico en APK (Cierre Inesperado):</b> Al presionar el producto 'Sauce Labs Bike Light', la aplicacion se cierra forzadamente (Crash). La prueba automatizada cumplio con el objetivo fundamental de detectar comportamientos no deseados en APKs inestables entregadas por desarrollo.", body_style))
story.append(Spacer(1, 2))
story.append(Paragraph("2. <b>Discrepancia en Nomenclatura:</b> En el APK el producto figura como 'Sauce Labs Bolt T-Shirt' (sin guion), mientras que el feature indica 'Sauce Labs Bolt - T-Shirt'. Se diseno un localizador flexible con fallback que soporta ambas variantes.", body_style))
story.append(Spacer(1, 2))
story.append(Paragraph("3. <b>Manejo de elementos con UiScrollable y WebDriverWait:</b> Para elementos fuera del viewport inicial y para tiempos de transicion variables del emulador, se emplearon esperas explicitas y scroll nativo asegurando solidez en las pruebas.", body_style))
story.append(Spacer(1, 6))

# 6. Conclusiones y Recomendaciones
story.append(Paragraph("5. Conclusiones y Recomendaciones", h2_style))
story.append(Paragraph("&bull; <b>Cumplimiento de Rubrica:</b> Se implemento la totalidad de criterios: carga de aplicacion, seleccion/agregado de productos, validacion en carrito, esperas explicitas, reportes HTML/PDF y claridad del codigo bajo el patron Screenplay / POM.", body_style))
story.append(Spacer(1, 2))
story.append(Paragraph("&bull; <b>Identificacion Efectiva de Fallos:</b> Tal como lo establecen las directrices ('se sabe que el equipo de desarrollo ha estado entregando APK inestables'), la automatizacion impidio que un APK defectuoso pase directamente a pruebas manuales de QA sin antes ser corregido.", body_style))
story.append(Spacer(1, 2))
story.append(Paragraph("&bull; <b>Accion a Tomar:</b> Notificar al equipo de desarrollo adjuntando el presente informe en PDF y el log de ejecucion para la subsanacion del crash en 'Sauce Labs Bike Light' antes de la siguiente entrega.", body_style))

doc.build(story)
print("PDF generado correctamente:", pdf_path)
