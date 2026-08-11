package com.guille.transportesl.datos

import com.guille.transportesl.modelos.Linea
import com.guille.transportesl.modelos.Recorrido

object DatosPrueba {
    val lineas : List<Linea> = listOf(

        Linea(identificador = "551",
            recorridoIda = Recorrido(paradas = emptyList(), coordenadas = emptyList()),
            recorridoVuelta = Recorrido( paradas = emptyList(), coordenadas = emptyList())
        ),
        Linea(identificador = "552",
            recorridoIda = Recorrido(paradas = emptyList(), coordenadas = emptyList()),
            recorridoVuelta = Recorrido( paradas = emptyList(), coordenadas = emptyList())
        ),
        Linea(identificador = "553",
              recorridoIda = Recorrido(paradas = emptyList(), coordenadas = emptyList()),
              recorridoVuelta = Recorrido( paradas = emptyList(), coordenadas = emptyList())
             ),
        Linea(identificador = "555",
            recorridoIda = Recorrido(paradas = emptyList(), coordenadas = emptyList()),
            recorridoVuelta = Recorrido( paradas = emptyList(), coordenadas = emptyList())
        )
    )
}