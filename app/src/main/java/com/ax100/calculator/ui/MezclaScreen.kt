package com.ax100.calculator.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ax100.calculator.*
import com.ax100.calculator.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.math.RoundingMode

// Only presentation is rounded; the calculation retains its original precision.
internal fun mostrarCantidad(valor: Double): String {
    if (valor > 0 && valor < 0.01) return "< 0,01"
    return DecimalFormat("#,##0.##", DecimalFormatSymbols(Locale.forLanguageTag("es-AR"))).apply {
        roundingMode = RoundingMode.HALF_UP
    }.format(valor)
}

@Composable
fun MezclaScreen(
    texto: String,
    onTexto: (String) -> Unit,
    porcentaje: Int,
    onPorcentaje: (Int) -> Unit
) {
    val litros = interpretarLitros(texto)
    val error = litros == null && (texto.isNotEmpty() || porcentaje != 0)
    val foco = LocalFocusManager.current
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(painterResource(R.drawable.suzuki), "Suzuki", Modifier.fillMaxWidth().height(64.dp))
        Text("AX100 · Mezcla", style = MaterialTheme.typography.headlineMedium)
        Text("Calculá el aceite para la nafta que cargaste.")
        OutlinedTextField(
            value = texto,
            onValueChange = onTexto,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Litros de nafta") },
            placeholder = { Text("Ej.: 7,5") },
            singleLine = true,
            isError = error,
            supportingText = {
                Text(if (error) "Ingresá un número válido, igual o mayor que cero." else "Podés usar coma o punto decimal.")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { foco.clearFocus() })
        )
        Text("Proporción de aceite", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth().selectableGroup(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf(2, 3, 4).forEach { valor ->
                val modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                    .semantics { selected = porcentaje == valor }
                val seleccionar = { onPorcentaje(valor); foco.clearFocus() }
                if (porcentaje == valor) {
                    Button(onClick = seleccionar, modifier = modifier) { Text("$valor %") }
                } else {
                    OutlinedButton(onClick = seleccionar, modifier = modifier) { Text("$valor %") }
                }
            }
        }
        if (litros != null && porcentaje != 0) {
            val aceite = calcularAceite(litros, porcentaje)
            Card(
                Modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Polite },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Aceite necesario", style = MaterialTheme.typography.titleMedium)
                    Text("${mostrarCantidad(aceite)} ml", style = MaterialTheme.typography.headlineLarge)
                    Text("${mostrarCantidad(calcularTapitas(aceite))} tapitas de 50 ml",
                        style = MaterialTheme.typography.titleLarge)
                    if (litros == 0.0) Text("Sin nafta, no hace falta agregar aceite.")
                }
            }
            Text("Equivalencia calculada con tapitas de 50 ml. Resultados mostrados con hasta 2 decimales.",
                style = MaterialTheme.typography.bodySmall)
        } else if (!error) {
            Text("Ingresá los litros y elegí 2 %, 3 % o 4 % para ver el resultado.")
        }
    }
}
