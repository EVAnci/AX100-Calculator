package com.ax100.calculator

import org.junit.Assert.*
import org.junit.Test

class CalculadoraMezclaTest {
    @Test fun ejemplosOriginales() {
        for ((litros, porcentaje, aceite) in listOf(
            Triple(5.0, 2, 100.0), Triple(10.0, 3, 300.0),
            Triple(12.0, 3, 360.0), Triple(7.5, 4, 300.0)
        )) {
            assertEquals(aceite, calcularAceite(litros, porcentaje), 0.000001)
            assertEquals(aceite / 50, calcularTapitas(calcularAceite(litros, porcentaje)), 0.000001)
        }
        assertEquals(7.2, calcularTapitas(calcularAceite(12.0, 3)), 0.000001)
        assertEquals(0.0, calcularAceite(0.0, 2), 0.0)
    }

    @Test fun admiteSeparadoresDecimales() {
        for (texto in listOf("7,5", "7.5", " 7,5 ")) assertEquals(7.5, interpretarLitros(texto)!!, 0.0)
        assertEquals(0.5, interpretarLitros(",5")!!, 0.0)
        assertEquals(0.0, interpretarLitros("0")!!, 0.0)
    }

    @Test fun rechazaEntradasInvalidas() {
        for (texto in listOf("", " ", "-1", "abc", "NaN", "Infinity", "1,2.3", "1e3", "9".repeat(310))) {
            assertNull(texto, interpretarLitros(texto))
        }
    }

    @Test fun noRedondeaCalculos() {
        assertEquals(0.2468, calcularAceite(0.01234, 2), 0.0000001)
        assertEquals(0.004936, calcularTapitas(calcularAceite(0.01234, 2)), 0.0000001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun rechazaPorcentajeNoAdmitido() { calcularAceite(1.0, 5) }

    @Test(expected = IllegalArgumentException::class)
    fun rechazaLitrosNegativos() { calcularAceite(-1.0, 2) }
}
