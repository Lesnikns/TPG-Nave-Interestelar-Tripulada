
# Asistente de Navegación y Comando — Software embarcado de una nave interestelar

Trabajo Práctico Grupal 2026 · Programación C · Facultad de Ingeniería, UNMdP

**Grupo:** `SENIT_G5` · **Entrega:** 1 · **Etiqueta Git:** `entrega-e1` · **Fecha de entrega:** 11/10/2026

| Integrante | GitHub | Respondabilidad |
|---|---|---|
| Mc Kesey Juan | https://github.com/juanmaki | Motor Warp y Bitcora |
| Iasillo Amelie | https://github.com/Lesnikns | Asistente de comandos y Misiones |
| Elizalde Facundo | https://github.com/FacuElizalde29 | Nave |
| Escobar Peñalva Ramiro Nahuel | https://github.com/ramiropnlv | Tripulacion |

Núcleo orientado a objetos del software embarcado: naves (creadas con **Factory**), Motor Warp (**State**), Asistente de Comando, Bitácora, tripulación con liquidación de haberes (**Decorator**) y las misiones M-01, M-02 y M-03 (**Template Method**). El diseño completo está en [`docs/Documentacion.md`](docs/Documentacion.md).

## 1. Requisitos de ejecución

- **JDK 25.** El proyecto compila con `maven.compiler.release` en 25, por lo que hace falta un JDK (no un JRE) de esa versión o superior.
- **Maven.** Probado con el Maven que incluye IntelliJ IDEA 2026.2.1. También sirve un Maven instalado aparte (`mvn -v` debe responder y mostrar el JDK 25).
- **Conexión a internet la primera vez**, para que Maven descargue sus plugins (`maven-compiler-plugin` y `exec-maven-plugin`) desde Maven Central.
- No se usan bibliotecas adicionales: el `pom.xml` no declara dependencias.

## 2. Comandos Maven

Todos se ejecutan desde la raíz del proyecto (donde está el `pom.xml`).

| Para | Comando |
|---|---|
| Compilar | `mvn compile` |
| Compilar y ejecutar la demostración | `mvn compile exec:java -Dexec.mainClass=Sistema` |

La clase `Sistema` está en el paquete por defecto (`src/main/java/Sistema.java`); por eso el nombre de la clase principal se indica sin paquete.

## 3. Cómo iniciar el sistema

**Con Maven instalado:** ejecutar en una terminal, dentro de la raíz del proyecto:

```
mvn compile exec:java -Dexec.mainClass=Sistema
```

**Con IntelliJ IDEA (sin instalar Maven):** abrir el proyecto, apretar **Ctrl dos veces seguidas** (Run Anything) y ejecutar el mismo comando. También se puede usar el panel **Maven** → **Execute Maven Goal**.

El programa imprime por consola los escenarios de demostración y termina en `BUILD SUCCESS`.

## 4. Cómo ejecutar las verificaciones

El proyecto no usa JUnit: la verificación es la propia clase `Sistema`, que ejecuta seis escenarios y, en cada comprobación, imprime `[OK]` o `[FALLO]` según el estado real de los objetos.

| Escenario | Qué demuestra |
|---|---|
| A — Ejecución correcta | Factory con los tres tipos de nave, tripulación válida, M-01, M-02 y M-03 completas, recursos tras cada misión y Bitácora |
| B — Recursos insuficientes | Rechazo por desgaste crítico y por combustible insuficiente, sin cambios parciales y con el motivo registrado |
| C — Motor Warp | Transiciones válidas e inválidas, estas últimas rechazadas y registradas como `Error` |
| D — Contrato inválido | Cargas que exceden la capacidad: se rechazan completas y el estado anterior se conserva |
| Haberes | 4 cargos × 3 orígenes y liquidación mensual de la tripulación de ejemplo |
| Mantenimiento | Recursos antes y después de una misión y de un mantenimiento |

Para comprobar que no hubo fallas, buscar la cadena `[FALLO]` en la salida: si no aparece, todas las comprobaciones pasaron. La salida de una ejecución completa está guardada en [`docs/evidencias/salida-E1.txt`](docs/evidencias/salida-E1.txt).

## 5. Configuración local necesaria

- **No hay variables de entorno, archivos de configuración ni base de datos** que preparar.
- El archivo `.mvn/jvm.config` (ya incluido en el repositorio) fuerza UTF-8 en la JVM de Maven para que los acentos se vean bien en la consola de Windows. No requiere ninguna acción.
- Si `mvn` no se reconoce en la terminal, usar el Maven de IntelliJ como se indica en la sección 3.


## Organización del repositorio

| Ruta                         | Contenido |
|------------------------------|---|
| `src/main/java/`             | Código fuente: paquetes `Asistente`, `Excepciones`, `Motor`, `Naves`, `Tripulacion` y la clase `Sistema` |
| `pom.xml`, `.mvn/jvm.config` | Proyecto Maven y su configuración de codificación |
| `docs/Documentacion.md`      | Documento de diseño: arquitectura, responsabilidades, patrones, contratos, decisiones y trazabilidad con E1-01 a E1-10 |
| `docs/UML SENIT-G5.png`      | Diagrama de clases |
| `docs/evidencias/`           | Salida de ejecución de los escenarios |
| `docs/uso-ia.md`             | Registro del uso de inteligencia artificial |


