package com.ax100.calculator.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ax100.calculator.R

@Composable
private fun Pagina(titulo: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineMedium)
        content()
    }
}

@Composable
private fun Referencia(texto: String) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Referencia heredada · sin verificar", style = MaterialTheme.typography.titleSmall)
            Text(texto)
        }
    }
}

@Composable
fun NeumaticosScreen() {
    Pagina("Presión de neumáticos") {
        Referencia("Estos valores provienen de la app original, no de un manual oficial verificado. Consultá el manual de tu modelo según neumáticos, carga y condiciones antes de utilizarlos.")
        Presiones("Suzuki AX100", "25–30 psi (1,7–2,1 bar)", "28–33 psi (1,9–2,3 bar)")
        Presiones("Rouser / Gilera", "30–36 psi (2,1–2,5 bar)", "32–42 psi (2,2–2,9 bar)")
        Text("Nota de la app original: «lo ideal es que esté en el medio». Esta indicación también está pendiente de verificar; el original no precisa los modelos de Rouser / Gilera.",
            style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun Presiones(modelo: String, delantero: String, trasero: String) {
    OutlinedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(modelo, style = MaterialTheme.typography.titleLarge)
            Text("Delantero", style = MaterialTheme.typography.titleSmall)
            Text(delantero)
            HorizontalDivider()
            Text("Trasero", style = MaterialTheme.typography.titleSmall)
            Text(trasero)
        }
    }
}

@Composable
fun MantenimientoScreen() {
    Pagina("Mantenimiento") {
        Referencia("El intervalo, la terminología y las imágenes proceden de la app original. Falta contrastarlos con el manual de la AX100 antes de usarlos como instrucciones de mantenimiento.")
        Text("Aceite de la caja · AX100", style = MaterialTheme.typography.titleLarge)
        Text("Intervalo mencionado en el original: 6.000–7.000 km.",
            style = MaterialTheme.typography.titleMedium)
        Text("Texto original: «El cambio del aceite de la caja del Cigüeñal es recomendable realizarlo cada 6000-7000 km».",
            style = MaterialTheme.typography.bodySmall)
        Text("Estas referencias son sobre el aceite de la caja; el cálculo de mezcla corresponde al aceite agregado a la nafta.")
        ImagenReferencia(R.drawable.entrada_aceite, "Entrada de aceite")
        ImagenReferencia(R.drawable.salida_aceite, "Tapón de salida")
    }
}

@Composable
private fun ImagenReferencia(recurso: Int, titulo: String) {
    OutlinedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Image(painterResource(recurso), "$titulo: imagen de referencia de la aplicación original",
                Modifier.fillMaxWidth().heightIn(max = 320.dp))
        }
    }
}
