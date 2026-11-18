package com.ax100.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ax100.calculator.ui.*
import com.ax100.calculator.ui.theme.AX100Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { AX100Theme { AX100App() } }
    }
}

@Composable
fun AX100App() {
    var destino by rememberSaveable { mutableIntStateOf(0) }
    var litros by rememberSaveable { mutableStateOf("") }
    var porcentaje by rememberSaveable { mutableIntStateOf(0) }
    val foco = LocalFocusManager.current
    val destinos = listOf("Mezcla", "Neumáticos", "Mantenimiento")
    val iconos = listOf(R.drawable.ic_mezcla, R.drawable.ic_neumaticos, R.drawable.ic_mantenimiento)
    BackHandler(destino != 0) { destino = 0 }
    Scaffold(
        bottomBar = {
            NavigationBar {
                destinos.forEachIndexed { indice, titulo ->
                    NavigationBarItem(
                        selected = destino == indice,
                        onClick = { foco.clearFocus(); destino = indice },
                        icon = { Icon(painterResource(iconos[indice]), contentDescription = null) },
                        label = { Text(titulo) }
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding).imePadding()) {
            when (destino) {
                0 -> MezclaScreen(litros, { litros = it }, porcentaje, { porcentaje = it })
                1 -> NeumaticosScreen()
                2 -> MantenimientoScreen()
            }
        }
    }
}
