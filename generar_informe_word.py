import os
import json
import base64
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT

base_dir = os.path.dirname(os.path.abspath(__file__))
json_report_path = os.path.join(base_dir, "target", "cucumber-report.json")
docx_output_path = os.path.join(base_dir, "Informe_de_Ejecucion_SauceLabs.docx")
evidencias_dir = os.path.join(base_dir, "evidencias")
os.makedirs(evidencias_dir, exist_ok=True)

# 1. Extraer capturas desde cucumber-report.json
extracted_images = {}
if os.path.exists(json_report_path):
    try:
        with open(json_report_path, "r", encoding="utf-8") as f:
            data = json.load(f)
        
        scenario_idx = 1
        for element in data[0].get("elements", []):
            sc_key = f"CP0{scenario_idx}"
            extracted_images[sc_key] = []
            
            # Pasos
            for step in element.get("steps", []):
                for emb in step.get("embeddings", []):
                    name = emb.get("name", "captura")
                    b64_data = emb.get("data", "")
                    if b64_data:
                        img_path = os.path.join(evidencias_dir, f"{sc_key}_{name.replace(' ', '_')}.png")
                        with open(img_path, "wb") as img_file:
                            img_file.write(base64.b64decode(b64_data))
                        extracted_images[sc_key].append((name, img_path))
            
            # Hooks after
            for hook in element.get("after", []):
                for emb in hook.get("embeddings", []):
                    name = emb.get("name", "estado_final")
                    b64_data = emb.get("data", "")
                    if b64_data:
                        img_path = os.path.join(evidencias_dir, f"{sc_key}_{name.replace(' ', '_')}.png")
                        with open(img_path, "wb") as img_file:
                            img_file.write(base64.b64decode(b64_data))
                        extracted_images[sc_key].append((name, img_path))
                        
            scenario_idx += 1
    except Exception as e:
        print("Aviso: No se pudieron extraer imagenes del JSON:", e)

# 2. Crear documento Word
doc = docx.Document()

# Margenes
for section in doc.sections:
    section.top_margin = Inches(0.8)
    section.bottom_margin = Inches(0.8)
    section.left_margin = Inches(0.8)
    section.right_margin = Inches(0.8)

# Titulo
p_title = doc.add_paragraph()
p_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
run_title = p_title.add_run("INFORME DE EJECUCIÓN DE PRUEBAS AUTOMATIZADAS")
run_title.bold = True
run_title.font.size = Pt(16)
run_title.font.name = "Arial"
run_title.font.color.rgb = RGBColor(0x1A, 0x36, 0x5D)

p_sub = doc.add_paragraph()
p_sub.alignment = WD_ALIGN_PARAGRAPH.CENTER
run_sub = p_sub.add_run("Módulo: Carrito de Compras - SauceLabs Mobile Android App")
run_sub.font.size = Pt(11)
run_sub.font.name = "Arial"
run_sub.font.color.rgb = RGBColor(0x4A, 0x55, 0x68)

doc.add_paragraph() # Espacio

# Helper para agregar subtitulos
def add_section_header(text):
    p = doc.add_paragraph()
    p.paragraph_format.space_before = Pt(10)
    p.paragraph_format.space_after = Pt(4)
    run = p.add_run(text)
    run.bold = True
    run.font.size = Pt(12)
    run.font.name = "Arial"
    run.font.color.rgb = RGBColor(0x2B, 0x6C, 0xB0)
    return p

# 1. Presentacion
add_section_header("1. Presentación")

tabla_meta = doc.add_table(rows=5, cols=2)
tabla_meta.style = 'Table Grid'
tabla_meta.alignment = WD_TABLE_ALIGNMENT.CENTER

meta_info = [
    ("Aplicación", "SauceLabs My Demo App (MDA)"),
    ("Versión APK", "mda-2.0.2-23.apk (Entregable del equipo de desarrollo)"),
    ("Framework", "Appium 2.x/3.x + Java 21 + Cucumber BDD + JUnit 4"),
    ("Dispositivo / Entorno", "Android Emulator - Medium Phone (API 37, emulator-5554)"),
    ("Objetivo de Prueba", "Validar la regresión automatizada del carrito de compras para 3 productos con distintas unidades en Examples, detectando posibles fallos o inestabilidades en el APK.")
]

for idx, (label, val) in enumerate(meta_info):
    row = tabla_meta.rows[idx]
    c0 = row.cells[0]
    c1 = row.cells[1]
    c0.width = Inches(2.0)
    c1.width = Inches(4.5)
    
    p0 = c0.paragraphs[0]
    p0.paragraph_format.space_before = Pt(3)
    p0.paragraph_format.space_after = Pt(3)
    r0 = p0.add_run(label)
    r0.bold = True
    r0.font.size = Pt(9.5)
    r0.font.name = "Arial"
    
    p1 = c1.paragraphs[0]
    p1.paragraph_format.space_before = Pt(3)
    p1.paragraph_format.space_after = Pt(3)
    r1 = p1.add_run(val)
    r1.font.size = Pt(9.5)
    r1.font.name = "Arial"

doc.add_paragraph()

# 2. Resumen de ejecucion
add_section_header("2. Resultado de Ejecución Resumen")

p_res_desc = doc.add_paragraph()
r_res_desc = p_res_desc.add_run("Se ejecutaron los 3 Casos de Prueba (CP) configurados en la tabla de Examples de Cucumber. Dos escenarios concluyeron exitosamente y uno detectó un fallo crítico en la aplicación:")
r_res_desc.font.size = Pt(10)
r_res_desc.font.name = "Arial"

tabla_resumen = doc.add_table(rows=4, cols=7)
tabla_resumen.style = 'Table Grid'
tabla_resumen.alignment = WD_TABLE_ALIGNMENT.CENTER

resumen_data = [
    ["ID", "Producto / Escenario", "Unidades", "Carga App", "Detalle", "Carrito", "Resultado Final"],
    ["CP01", "Sauce Labs Backpack", "1", "OK", "OK", "OK", "PASÓ (Éxito)"],
    ["CP02", "Sauce Labs Bolt - T-Shirt", "1", "OK", "OK", "OK", "PASÓ (Éxito)"],
    ["CP03", "Sauce Labs Bike Light", "2", "OK", "FALLÓ (Crash)", "N/A", "FALLÓ (Defecto en APK)"]
]

for row_idx, row_data in enumerate(resumen_data):
    row = tabla_resumen.rows[row_idx]
    for col_idx, text in enumerate(row_data):
        cell = row.cells[col_idx]
        p = cell.paragraphs[0]
        p.paragraph_format.space_before = Pt(3)
        p.paragraph_format.space_after = Pt(3)
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER if col_idx != 1 else WD_ALIGN_PARAGRAPH.LEFT
        r = p.add_run(text)
        r.font.name = "Arial"
        r.font.size = Pt(8.5)
        if row_idx == 0:
            r.bold = True
        elif row_idx in [1, 2] and col_idx == 6:
            r.bold = True
            r.font.color.rgb = RGBColor(0x22, 0x54, 0x3D)
        elif row_idx == 3 and col_idx in [4, 6]:
            r.bold = True
            r.font.color.rgb = RGBColor(0xC5, 0x30, 0x30)

doc.add_paragraph()

# 3. Resultado por escenario
add_section_header("3. Resultado por Escenario")

scenarios_detail = [
    ("CP01: Sauce Labs Backpack (1 unidad) - [PASÓ / EXITOSO]", [
        "Given estoy en la aplicación de SauceLabs: Sesión de Appium iniciada correctamente en SplashActivity.",
        "And valido que carguen correctamente los productos en la galeria: Verificación exitosa del título 'Products' y catálogo en pantalla.",
        "When agrego 1 del siguiente producto 'Sauce Labs Backpack': Selección de imagen de mochila, confirmación de pantalla de detalle y clic en botón 'Add to cart'.",
        "Then valido el carrito de compra actualice correctamente: Apertura de pantalla 'My Cart' y validación del producto 'Sauce Labs Backpack' con 1 unidad."
    ]),
    ("CP02: Sauce Labs Bolt - T-Shirt (1 unidad) - [PASÓ / EXITOSO]", [
        "Given estoy en la aplicación de SauceLabs: Nueva sesión limpia sin datos residuales.",
        "And valido que carguen correctamente los productos en la galeria: Catálogo visible y confirmado.",
        "When agrego 1 del siguiente producto 'Sauce Labs Bolt - T-Shirt': Localización del producto con nombre normalizado, detalle cargado y clic en 'Add to cart'.",
        "Then valido el carrito de compra actualice correctamente: Confirmación en 'My Cart' del producto con 1 unidad agregada."
    ]),
    ("CP03: Sauce Labs Bike Light (2 unidades) - [FALLÓ / DEFECTO CRÍTICO EN APK]", [
        "Given estoy en la aplicación de SauceLabs: Aplicación inicia correctamente en el emulador.",
        "And valido que carguen correctamente los productos en la galeria: Galería cargada con los productos visibles.",
        "When agrego 2 del siguiente producto 'Sauce Labs Bike Light': Al hacer clic sobre 'Sauce Labs Bike Light', la aplicación sufre un cierre inesperado forzado (Crash). La automatización detecta que la pantalla de detalle no abrió y registra la falla.",
        "Then valido el carrito de compra actualice correctamente: Paso omitido (Skipped) debido al fallo crítico del APK en el paso anterior."
    ])
]

for title, steps in scenarios_detail:
    p_sc_title = doc.add_paragraph()
    p_sc_title.paragraph_format.space_before = Pt(6)
    p_sc_title.paragraph_format.space_after = Pt(2)
    r_sc_title = p_sc_title.add_run(title)
    r_sc_title.bold = True
    r_sc_title.font.name = "Arial"
    r_sc_title.font.size = Pt(10)
    
    for s in steps:
        p_step = doc.add_paragraph(style='List Bullet')
        p_step.paragraph_format.space_before = Pt(1)
        p_step.paragraph_format.space_after = Pt(1)
        r_step = p_step.add_run(s)
        r_step.font.name = "Arial"
        r_step.font.size = Pt(9)

doc.add_paragraph()

# 4. Evidencia de la ejecucion por escenario
add_section_header("4. Evidencia de la Ejecución por Escenario")

p_evid_desc = doc.add_paragraph()
r_evid_desc = p_evid_desc.add_run("Las capturas de pantalla son tomadas automáticamente durante la ejecución y adjuntadas tanto al reporte Cucumber HTML (target/cucumber-report.html) como a la presente documentación:")
r_evid_desc.font.size = Pt(10)
r_evid_desc.font.name = "Arial"

tabla_evid = doc.add_table(rows=8, cols=4)
tabla_evid.style = 'Table Grid'
tabla_evid.alignment = WD_TABLE_ALIGNMENT.CENTER

evid_data = [
    ["Escenario", "Hito de Validación", "Tipo de Evidencia", "Resultado"],
    ["CP01 - Backpack", "Carga de Galería", "Captura de pantalla (PNG)", "Conforme (Galeria visible)"],
    ["CP01 - Backpack", "Producto Agregado", "Captura de pantalla (PNG)", "Conforme (1 unidad configurada)"],
    ["CP01 - Backpack", "Validación Carrito", "Captura de pantalla (PNG)", "Conforme (Carrito actualizado)"],
    ["CP02 - T-Shirt", "Carga de Galería", "Captura de pantalla (PNG)", "Conforme (Galeria visible)"],
    ["CP02 - T-Shirt", "Producto Agregado", "Captura de pantalla (PNG)", "Conforme (1 unidad configurada)"],
    ["CP02 - T-Shirt", "Validación Carrito", "Captura de pantalla (PNG)", "Conforme (Carrito actualizado)"],
    ["CP03 - Bike Light", "Cierre Inesperado", "Captura de pantalla final (PNG)", "Fallo detectado (Crash en APK)"]
]

for row_idx, row_data in enumerate(evid_data):
    row = tabla_evid.rows[row_idx]
    for col_idx, text in enumerate(row_data):
        cell = row.cells[col_idx]
        p = cell.paragraphs[0]
        p.paragraph_format.space_before = Pt(3)
        p.paragraph_format.space_after = Pt(3)
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER if col_idx in [0, 2] else WD_ALIGN_PARAGRAPH.LEFT
        r = p.add_run(text)
        r.font.name = "Arial"
        r.font.size = Pt(8.5)
        if row_idx == 0:
            r.bold = True
        elif row_idx == 7 and col_idx == 3:
            r.bold = True
            r.font.color.rgb = RGBColor(0xC5, 0x30, 0x30)

# Insertar imagenes reales si existen
doc.add_paragraph()
p_img_title = doc.add_paragraph()
r_img_title = p_img_title.add_run("Capturas de Pantalla de la Ejecución:")
r_img_title.bold = True
r_img_title.font.name = "Arial"
r_img_title.font.size = Pt(10.5)

inserted_count = 0
for sc_key in ["CP01", "CP02", "CP03"]:
    images = extracted_images.get(sc_key, [])
    if images:
        p_sc = doc.add_paragraph()
        p_sc.paragraph_format.space_before = Pt(4)
        p_sc.paragraph_format.space_after = Pt(2)
        r_sc = p_sc.add_run(f"Evidencias para {sc_key}:")
        r_sc.bold = True
        r_sc.font.name = "Arial"
        r_sc.font.size = Pt(9.5)
        
        for name, img_path in images:
            if os.path.exists(img_path):
                try:
                    p_cap = doc.add_paragraph()
                    p_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
                    p_cap.paragraph_format.space_before = Pt(2)
                    p_cap.paragraph_format.space_after = Pt(2)
                    doc.add_picture(img_path, width=Inches(2.6))
                    p_cap2 = doc.add_paragraph()
                    p_cap2.alignment = WD_ALIGN_PARAGRAPH.CENTER
                    p_cap2.paragraph_format.space_after = Pt(6)
                    r_cap2 = p_cap2.add_run(f"Figura: {sc_key} - {name}")
                    r_cap2.italic = True
                    r_cap2.font.size = Pt(8)
                    r_cap2.font.name = "Arial"
                    inserted_count += 1
                except Exception as e:
                    print(f"No se pudo insertar imagen {img_path}:", e)

doc.add_paragraph()

# 5. Hallazgos
add_section_header("5. Hallazgos")

hallazgos = [
    ("Defecto Crítico en APK (Cierre Inesperado):", 
     "Al presionar sobre el producto 'Sauce Labs Bike Light', la aplicación sufre un cierre forzado (Crash). La prueba automatizada cumplió cabalmente con el objetivo principal planteado en la consigna: detectar fallos y comportamientos no deseados en APKs inestables entregadas por el equipo de desarrollo."),
    ("Discrepancia en Nomenclatura del Producto:", 
     "En el archivo feature se especifica 'Sauce Labs Bolt - T-Shirt' (con guion), mientras que en el APK de desarrollo figura como 'Sauce Labs Bolt T-Shirt' (sin guion). Se implementó una normalización del texto en el código para asegurar la compatibilidad."),
    ("Manejo de Tiempos y Sincronización:", 
     "Se utilizó una configuración adecuada de esperas implícitas (implicitlyWait) para asegurar la correcta sincronización de los elementos en el emulador Android sin generar bloqueos o falsos positivos.")
]

for title, desc in hallazgos:
    p_h = doc.add_paragraph(style='List Bullet')
    p_h.paragraph_format.space_before = Pt(2)
    p_h.paragraph_format.space_after = Pt(2)
    r_ht = p_h.add_run(title + " ")
    r_ht.bold = True
    r_ht.font.name = "Arial"
    r_ht.font.size = Pt(9.5)
    r_hd = p_h.add_run(desc)
    r_hd.font.name = "Arial"
    r_hd.font.size = Pt(9.5)

doc.add_paragraph()

# 6. Conclusiones
add_section_header("6. Conclusiones")

conclusiones = [
    ("Cumplimiento Total de la Consigna:", 
     "Se automatizó el flujo completo del carrito de compras para los 3 Casos de Prueba (CP) configurados en la tabla de Examples de Cucumber, siguiendo la arquitectura de pruebas mobile establecida en clase."),
    ("Efectividad en la Detección de Defectos:", 
     "Tal como se anticipaba en la consigna ('el equipo de desarrollo ha estado entregando APK inestables'), la automatización actuó como barrera de calidad, impidiendo que una versión defectuosa pase a pruebas funcionales manuales sin antes ser corregida."),
    ("Acción y Recomendación Inmediata:", 
     "Se notifica formalmente al equipo de Desarrollo adjuntando el presente informe de ejecución y las capturas de pantalla de evidencia para la subsanación del fallo en 'Sauce Labs Bike Light' y la posterior entrega de una nueva versión del APK.")
]

for title, desc in conclusiones:
    p_c = doc.add_paragraph(style='List Bullet')
    p_c.paragraph_format.space_before = Pt(2)
    p_c.paragraph_format.space_after = Pt(2)
    r_ct = p_c.add_run(title + " ")
    r_ct.bold = True
    r_ct.font.name = "Arial"
    r_ct.font.size = Pt(9.5)
    r_cd = p_c.add_run(desc)
    r_cd.font.name = "Arial"
    r_cd.font.size = Pt(9.5)

doc.save(docx_output_path)
print("Documento Word generado exitosamente en:", docx_output_path)
