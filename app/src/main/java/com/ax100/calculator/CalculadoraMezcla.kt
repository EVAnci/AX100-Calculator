package com.ax100.calculator

private val decimal = Regex("""(?:[0-9]+(?:[.,][0-9]*)?|[.,][0-9]+)""")

fun interpretarLitros(texto: String): Double? {
    val limpio = texto.trim()
    if (!decimal.matches(limpio)) return null
    return limpio.replace(',', '.').toDoubleOrNull()
        ?.takeIf { it.isFinite() && it >= 0 && (it * 40).isFinite() }
}

fun calcularAceite(litros: Double, porcentaje: Int): Double {
    require(litros.isFinite() && litros >= 0)
    require(porcentaje in listOf(2, 3, 4))
    return (litros * porcentaje * 10.0).also { require(it.isFinite()) }
}

fun calcularTapitas(aceiteMl: Double): Double {
    require(aceiteMl.isFinite() && aceiteMl >= 0)
    return aceiteMl / 50.0
}
