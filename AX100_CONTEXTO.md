# AX100 — Contexto para retomar el desarrollo

> Documento de continuidad preparado el 26 de octubre de 2026. Podés adjuntarlo en una conversación nueva con una IA y pedir que se retome el proyecto desde acá.

## 1. Objetivo

Migrar una aplicación personal y muy sencilla, **AX100**, creada en **MIT App Inventor**, a una aplicación Android nativa desarrollada en **Kotlin**, con **Jetpack Compose** y **Material Design 3**.

El usuario tiene una **Suzuki AX100**, moto de dos tiempos. La aplicación sirve principalmente para calcular cuánto aceite agregar según la cantidad de nafta cargada. Quiere mantener el proyecto **KISS** (simple, mantenible y fácil de ampliar en el futuro), sin complejidad innecesaria.

**Estado actual:** se discutieron los requisitos, las herramientas y cómo crear el proyecto, pero **no consta en la conversación que el proyecto Android ya se haya creado o compilado**. El próximo paso es confirmar el estado local y partir de una plantilla de Android Studio que compile.

## 2. Funcionalidades de la aplicación

### A. Calculadora de mezcla (pantalla principal)

- Campo numérico para introducir **litros de combustible** (admitir decimales).
- Tres botones para seleccionar proporción de aceite: **2 %, 3 % o 4 %**.
- Mostrar volumen de aceite requerido en **mililitros (ml)**.
- Mostrar cantidad equivalente de **tapitas de 50 ml**; permitir valores fraccionarios (p. ej., 7,2 tapitas).
- Interfaz clara para un cálculo frecuente y rápido.

Fórmulas iniciales propuestas:

```kotlin
fun calcularAceite(litros: Double, porcentaje: Int): Double =
    litros * porcentaje * 10.0

fun calcularTapitas(aceiteMl: Double): Double =
    aceiteMl / 50.0
```

Ejemplo: **12 litros al 3 % = 360 ml = 7,2 tapitas**. Durante la conversación se corrigió un ejemplo informal anterior de «4 tapitas».

Validar entradas vacías, no numéricas o negativas; definir cómo redondear **solo la visualización**, sin perder precisión innecesariamente. Conservar el comportamiento de mezcla de la versión original salvo decisión explícita del usuario.

### B. Presión de neumáticos

Sección de **texto informativo**, con valores/rangos para neumático delantero y trasero. La app original también incluye referencias a otros modelos; revisar los textos concretos del `.aia` antes de migrarlos. No inventar presiones: verificar los datos con el manual correspondiente antes de considerarlos recomendaciones mecánicas.

### C. Mantenimiento / cambio de aceite

Sección informativa sobre mantenimiento, especialmente reemplazo del aceite de la caja (kilometraje o intervalo temporal). La versión App Inventor contiene texto e imágenes de referencia; conservarlas inicialmente, revisando su exactitud.

## 3. Proyecto App Inventor existente

Archivo aportado por el usuario: **`AX_100.aia`**. Su contenido fue inspeccionado y se comprobó la presencia de:

```text
assets/Capturadesde2025-03-0114-15-18.png
assets/Capturadesde2025-03-0114-19-22.png
assets/logo-suzuki-4096-624403915.png
src/appinventor/ai_e_anci/AX_100/Screen1.bky
src/appinventor/ai_e_anci/AX_100/Screen1.scm
youngandroidproject/project.properties
```

La app actual usa una única `Screen1` con secciones que se muestran/ocultan mediante botones; la migración puede presentar esas secciones como tres destinos de navegación Compose. Entre sus componentes figuran el campo `litros`, los botones `p2`, `p3`, `p4`, etiquetas de resultado, `tire_pressure_btn` y `oil_btn`.

El `.aia` **no se convierte directamente a Kotlin**: sirve como referencia de lógica, textos, pantallas y recursos. Volver a adjuntarlo en una nueva conversación si se necesita inspeccionar detalles exactos.

## 4. Entorno y herramientas

- Sistema operativo: **Arch Linux**.
- Editor habitual y preferido: **Neovim**.
- Se propone **Android Studio** para inicializar el proyecto, configurar Android SDK, usar previews y depurar cuando convenga; **Neovim** para editar cotidianamente.
- **OpenCode** como asistente/agente de programación opcional.
- **Git** para versionado, **Gradle Wrapper** (`./gradlew`) para compilar, **ADB** para instalar en teléfono físico.
- Se prefiere probar inicialmente en un **teléfono Android real**, sin necesidad de emulador.

Herramientas discutidas para Arch (comprobar paquetes/versiones vigentes antes de instalarlos):

```bash
# Android Studio desde AUR si se utiliza yay
yay -S android-studio

# Herramientas básicas
sudo pacman -S git android-tools
```

Android Studio incluye los asistentes para instalar el SDK y generar el proyecto. No es necesario instalar Kotlin o Gradle globalmente para este caso. El proyecto debe incluir su propio Gradle Wrapper.

## 5. Configuración propuesta del proyecto Android

En Android Studio: **New Project → Empty Activity** (plantilla de Jetpack Compose).

| Campo | Valor sugerido |
|---|---|
| Name | `AX100` |
| Package name | `com.ax100.calculator` |
| Language | Kotlin |
| Minimum SDK | API 26 (Android 8.0), ajustable |
| Build configuration language | Kotlin DSL (`.kts`) |
| UI | Jetpack Compose + Material 3 |

Ruta local sugerida, **no confirmada**: `~/Projects/AX100`.

Tras crear el proyecto y finalizar la sincronización de Gradle:

```bash
cd ~/Projects/AX100  # adaptar a la ruta real
./gradlew assembleDebug
adb devices
./gradlew installDebug
```

Primero asegurarse de obtener `BUILD SUCCESSFUL` con la aplicación generada por Android Studio, **antes** de agregar la lógica propia.

## 6. Arquitectura: KISS de verdad

Al inicio, **una Activity y tres pantallas Compose**, con lógica de cálculo en una función Kotlin independiente. No agregar de entrada Room, Hilt, repositorios, red, base de datos, servicios, sincronización ni patrones innecesarios.

Estructura orientativa (no es obligatorio crear todas las carpetas inmediatamente):

```text
app/src/main/java/com/ax100/calculator/
├── MainActivity.kt
├── CalculadoraMezcla.kt
├── ui/
│   ├── MezclaScreen.kt
│   ├── NeumaticosScreen.kt
│   ├── MantenimientoScreen.kt
│   └── theme/
```

- **Mezcla** debe ser la pantalla principal al abrir la app.
- Las otras dos secciones pueden ser accesibles mediante **navegación inferior Material 3**.
- Preferir estado local de Compose mientras las necesidades sean pequeñas.
- Mantener la app **offline**, sin permisos innecesarios.
- Soporte de modo claro/oscuro si no complica demasiado la primera versión.

## 7. Plan de implementación acordado

1. Crear un proyecto **Empty Activity** en Android Studio.
2. Sincronizar y compilar la plantilla **sin cambios**.
3. Inicializar Git y agregar documentación.
4. Implementar `CalculadoraMezcla.kt` (lógica pura) y comprobar ejemplos.
5. Implementar `MezclaScreen.kt` con campo de litros, botones 2/3/4 %, resultado en ml y tapitas.
6. Recién después implementar navegación y las secciones informativas de neumáticos y mantenimiento.
7. Contrastar textos, imágenes y comportamientos con el archivo original `.aia`.
8. Probar en un teléfono Android mediante ADB.

No sumar funcionalidades nuevas antes de completar una primera versión equivalente a la existente.

## 8. Uso de IA / OpenCode

El usuario quiere poder usar **OpenCode** con agentes, manteniendo Neovim como editor. Se recomendó un único agente inicialmente, con tareas pequeñas y revisables, y un archivo `AGENTS.md` complementario al README.

Reglas aconsejadas para ese archivo:

```markdown
# AX100 — Agent Instructions

- Proyecto Android en Kotlin, Jetpack Compose y Material 3.
- Mantener una arquitectura simple (KISS).
- App offline, sin dependencias ni permisos innecesarios.
- No introducir funcionalidades no solicitadas.
- Hacer cambios pequeños, fáciles de revisar.
- Separar la lógica de mezcla de la interfaz.
- Respetar los textos y datos del proyecto original al migrar.
- No cambiar archivos Gradle salvo necesidad justificada.
- Verificar con `./gradlew assembleDebug` cuando sea posible.
- Explicar brevemente las decisiones importantes.
```

## 9. Archivos útiles para continuar

- `AX_100.aia`: fuente original de MIT App Inventor (aportado en esta conversación).
- `README.md`: documento de proyecto ya preparado durante la conversación.
- `AX100_CONTEXTO.md`: **este archivo**, para reanudar el trabajo.

Si abrís una conversación nueva, adjuntá **este contexto** y, para una migración fiel de datos informativos, también `AX_100.aia` y el README original. No asumir que el nuevo asistente tiene acceso a adjuntos de conversaciones anteriores.

## 10. Prompt sugerido para retomar

> Estoy desarrollando AX100, una app Android muy sencilla que quiero migrar de MIT App Inventor a Kotlin con Jetpack Compose y Material 3. Adjunto `AX100_CONTEXTO.md` con los requisitos y acuerdos previos. Trabajo en Arch Linux, prefiero Neovim y puedo usar Android Studio y OpenCode. Quiero mantener el proyecto KISS. Ayudame a continuar desde el estado real del repositorio, sin agregar arquitectura innecesaria. Primero comprobemos que la plantilla de Android Studio compila, y luego implementemos la calculadora de mezcla.
