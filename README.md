# AX100

Aplicación Android sencilla para consultar información útil de una **Suzuki AX100** y calcular cuánto aceite de motor de 2 tiempos se necesita agregar a la nafta, según la cantidad cargada y la proporción elegida.

> **Estado:** primera versión nativa implementada en Kotlin + Jetpack Compose. Incluye calculadora, navegación, datos e imágenes recuperados de `AX_100.aia`. Pendiente de prueba en teléfono y de contrastar los datos mecánicos con el manual.

## Compilar y probar

Requiere Android SDK y el JDK configurado por Android Studio. En este entorno:

```bash
export JAVA_HOME=/opt/android-studio/jbr
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew testDebugUnitTest assembleDebug lintDebug
adb devices
./gradlew installDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`.

Podés copiarlo con otro nombre sin afectar la firma o la instalación:

```bash
cp app/build/outputs/apk/debug/app-debug.apk app/build/outputs/apk/debug/AX100.apk
```

La copia debe actualizarse después de cada compilación. Sigue siendo un APK de depuración.

El ícono utiliza `Logo/logo-ax100-mezcla-2t.png`, con fondo blanco y margen para
las máscaras de los lanzadores. Para regenerar sus tamaños, ejecutar
`python scripts/generar_iconos.py` (requiere Pillow).

## Git y recursos

El PNG original del logo se versiona con **Git LFS**. Al clonar el proyecto,
instalar Git LFS y ejecutar:

```bash
git lfs install
git lfs pull
```

Los recursos Android generados se guardan en Git normal. El original
`AX_100.aia` se conserva solo localmente. Los AIA, APK, AAB, resultados de
compilación y configuración local del IDE están excluidos del repositorio.

- Compatible desde Android 7.0 (API 24).
- Colores del sistema desde Android 12; paleta alternativa en versiones anteriores.
- La selección de porcentaje es explícita y el resultado se actualiza al editar los litros.
- Acepta coma o punto decimal; cero produce cero y entradas inválidas ocultan el resultado.
- Visualización con hasta dos decimales, sin redondear los cálculos. Valores positivos menores a 0,01 se muestran como `< 0,01`.
- Litros y porcentaje se conservan al navegar y al recrear la Activity; no se guardan en una base de datos.

### Comprobación manual en teléfono

1. Verificar `12` litros al `3 %`: `360 ml` y `7,2 tapitas`.
2. Probar `7,5`, `7.5`, cero, campo vacío y pegar texto inválido o negativo.
3. Cambiar entre secciones, volver con Atrás y rotar el teléfono: conservar litros y porcentaje.
4. Revisar las imágenes, desplazamiento, teclado y texto ampliado.
5. Revisar modo claro/oscuro y colores dinámicos en Android 12+; paleta alternativa en Android 7.

Los textos mecánicos se presentan como referencias heredadas sin verificar, no como recomendaciones oficiales.

## Objetivo

Mantener una herramienta personal rápida, fácil de usar y completamente offline. La prioridad es que la calculadora de mezcla resulte clara incluso al utilizarla en una estación de servicio. El proyecto debe ser fácil de mantener y ampliar cuando surja una necesidad real, sin anticipar funciones innecesarias.

## Funcionalidades de la primera versión

### 1. Calculadora de mezcla (pantalla principal)

- Introducir la cantidad de nafta cargada, en litros, incluyendo valores decimales.
- Seleccionar una proporción de aceite de **2 %, 3 % o 4 %**.
- Mostrar el aceite necesario en **mililitros (ml)**.
- Mostrar la equivalencia en **tapitas de 50 ml**, admitiendo fracciones de tapita.
- Evitar resultados confusos cuando el campo esté vacío o contenga un número inválido o negativo.
- Mantener visible la selección del porcentaje y actualizar los resultados con los datos vigentes.

**Fórmulas** (porcentaje calculado respecto del volumen de nafta, siguiendo la convención de la app original):

```text
aceiteMl = litrosNafta * 1000 * (porcentaje / 100)
tapitas50ml = aceiteMl / 50
```

Ejemplo: 12 litros al 3 % equivalen a **360 ml de aceite** o **7,2 tapitas de 50 ml**.

Se conservarán las unidades reales; no se redondearán las tapitas a enteros, porque se perdería precisión. Para mostrar resultados, se podrá limitar la cantidad de decimales sin alterar el valor utilizado en el cálculo.

### 2. Presión de neumáticos

Sección de lectura con los valores **heredados de la aplicación App Inventor**:

| Moto | Delantero | Trasero |
| --- | --- | --- |
| Suzuki AX100 | 25–30 psi (1,7–2,1 bar) | 28–33 psi (1,9–2,3 bar) |
| Rouser / Gilera | 30–36 psi (2,1–2,5 bar) | 32–42 psi (2,2–2,9 bar) |

La aplicación original también indica que lo ideal es ubicarse en el medio del rango.

**Importante:** estos datos no han sido contrastados con manuales oficiales. Antes de usarlos como recomendaciones de seguridad, verificar la variante de moto, neumáticos, carga y condiciones indicadas por el fabricante. La nueva aplicación no debe presentarlos como valores oficiales sin validación.

### 3. Mantenimiento

Sección informativa que conserva el contenido original sobre el cambio de aceite de la caja de la AX100:

- Intervalo mencionado en la app original: **6.000–7.000 km**.
- Dos imágenes de referencia: **entrada de aceite** y **tapón de salida**.

**Importante:** validar el intervalo, el procedimiento y la terminología con el manual correspondiente antes de tratarlos como instrucciones oficiales de mantenimiento.

## Diseño y experiencia de uso

- **Jetpack Compose** para la interfaz.
- **Material Design 3** como sistema visual.
- Calculadora como pantalla de inicio.
- Navegación simple entre **Mezcla**, **Neumáticos** y **Mantenimiento**.
- Compatibilidad con tema claro y oscuro.
- Botones grandes, texto legible y buen contraste.
- Interfaz en español.
- Sin conexión a Internet ni registro de usuario.

## Tecnologías

- **Kotlin**.
- **Android Studio**.
- **Jetpack Compose** y **Material 3**.
- **Gradle Wrapper** y configuración en Kotlin DSL, generados con la plantilla de Android Studio.
- **Git** para versionado.
- SDK mínimo: **API 24 (Android 7.0)**.

No se necesitan inicialmente una base de datos, servicios web, permisos especiales, inyección de dependencias o una arquitectura multicapa.

## Estructura propuesta

La estructura exacta puede ajustarse a la plantilla actual de Android Studio. Como guía:

```text
AX100/
├── app/
│   └── src/main/
│       ├── java/com/ax100/calculator/
│       │   ├── MainActivity.kt
│       │   ├── CalculadoraMezcla.kt
│       │   └── ui/
│       │       ├── MezclaScreen.kt
│       │       ├── NeumaticosScreen.kt
│       │       ├── MantenimientoScreen.kt
│       │       └── theme/
│       └── res/drawable/
├── README.md
└── ... archivos Gradle generados por Android Studio
```

Para mantener el principio KISS, la lógica de cálculo será una función pura en Kotlin, separada de la interfaz. El estado simple de las pantallas podrá manejarse con las primitivas de estado de Compose. No se incorpora un `ViewModel` hasta que exista una necesidad concreta.

## Preparar el entorno en Arch Linux

1. Instalar Android Studio desde el AUR (`yay -S android-studio`) o desde [la web oficial](https://developer.android.com/studio).
2. Instalar Git y ADB: `sudo pacman -S git android-tools`.
3. Abrir Android Studio y completar el asistente de instalación del **Android SDK**.
4. Abrir este proyecto en Android Studio (Kotlin, Compose, paquete `com.ax100.calculator`, mínimo API 24).
5. Abrir el proyecto en Android Studio y dejar que sincronice Gradle.
6. Conectar un teléfono con depuración USB y verificar su presencia con `adb devices`, o instalar un dispositivo virtual desde **Device Manager**.
7. Ejecutar la aplicación desde Android Studio.

Android Studio incluye un runtime Java apropiado y la plantilla genera el Gradle Wrapper; no hace falta instalar Kotlin o Gradle globalmente. Es recomendable administrar el SDK desde Android Studio para no mezclar instalaciones en diferentes directorios.

## Verificaciones básicas de la calculadora

| Litros | Porcentaje | Aceite | Tapitas de 50 ml |
| ---: | ---: | ---: | ---: |
| 5 | 2 % | 100 ml | 2 |
| 10 | 3 % | 300 ml | 6 |
| 12 | 3 % | 360 ml | 7,2 |
| 7,5 | 4 % | 300 ml | 6 |

También deben verificarse los campos vacíos, entradas negativas, cero y separadores decimales (`7,5` y `7.5`). Para cero litros, el comportamiento elegido debe ser consistente y no mostrar valores engañosos.

## Alcance y decisiones KISS

**Incluido en la primera versión:** cálculos, tres secciones, contenido informativo heredado, imágenes de referencia, diseño Material 3 y pruebas manuales de navegación y entrada de datos.

**Fuera de alcance por ahora:** historial de cargas, seguimiento del kilometraje, notificaciones, persistencia de datos, cuentas, sincronización, base de datos, permisos extra y acceso a Internet.

## Migración desde App Inventor

Archivo fuente histórico: `AX_100.aia` (MIT App Inventor). Contiene una pantalla `Screen1` con secciones que se muestran y ocultan mediante botones, lógica de cálculo en bloques y tres imágenes (logo Suzuki y dos referencias mecánicas).

El `.aia` sirve como **referencia funcional** y para recuperar recursos, pero no se convierte directamente en un proyecto Kotlin. Los datos informativos se trasladarán como contenido estático inicial, sujeto a las verificaciones mencionadas.

## Plan de implementación

1. Crear el proyecto Android con Compose y confirmar que ejecuta en el teléfono.
2. Implementar y probar las funciones de cálculo.
3. Construir la pantalla **Mezcla**.
4. Incorporar navegación y las pantallas **Neumáticos** y **Mantenimiento**.
5. Recuperar las imágenes necesarias del `.aia`.
6. Revisar textos, accesibilidad, tema oscuro y comportamiento con decimales.
7. Generar un APK de depuración para uso personal y preparar una versión firmada solo si se necesita distribuir.

## Pautas para colaborar con IA

- Priorizar **simplicidad, legibilidad y código Kotlin idiomático**.
- No añadir librerías, servicios ni patrones arquitectónicos sin justificar una necesidad.
- Mantener los cálculos separados de la UI y acompañarlos con pruebas sencillas.
- Implementar cambios pequeños y verificables.
- Respetar estas especificaciones; consultar antes de alterar reglas o datos de mantenimiento.
- Evitar inventar información técnica de la moto: señalar expresamente cualquier dato que requiera contrastarse con el manual.
- Conservar la aplicación offline y sin permisos innecesarios.

## Licencia

Pendiente de definir si el proyecto se publica. Los recursos gráficos heredados pueden tener derechos de terceros; revisar permisos antes de redistribuirlos.
