package com.guille.transportesl.modelos

// Se utiliza List en lugar de MutableList porque las paradas del recorrido
// solo se consultan durante la ejecución; no necesitamos agregar ni eliminar elementos.
data class Recorrido( val sentido: String, val paradas : List<Parada>, val coordenadas: List<Coordenada>)
