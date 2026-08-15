package com.guille.transportesl.datos

import com.guille.transportesl.modelos.Coordenada
import com.guille.transportesl.modelos.Linea
import com.guille.transportesl.modelos.Parada
import com.guille.transportesl.modelos.Recorrido

// object crea una única instancia de DatosPrueba (Singleton).
// Se accede directamente sin instanciar la clase
// Se utiliza como fuente temporal de datos mientras no exista una fuente de datos real.
object DatosPrueba {

    private val paradaTalcahuanoJuanBJusto = Parada(
        callePrincipal = "Talcahuano",
        interseccion = "Juan B. Justo",
        coordenada = Coordenada( longitud = -57.566071, latitud = -38.023167)
    )

    private val paradaCordobaAzcuenaga = Parada(
        callePrincipal = "Córdoba",
        interseccion = "Azcuénaga",
        coordenada = Coordenada(longitud = -57.564917 , latitud =-38.021819 )
    )

    private val paradaSanLuisJuanBJusto = Parada(
        callePrincipal = "San Luis",
        interseccion = "Juan B. Justo",
        coordenada = Coordenada(-57.566718,-38.022157)
    )

    private val paradaDellepianeSolis = Parada(
        callePrincipal = "Luis Dellepiane",
        interseccion = "Solis",
        coordenada = Coordenada(-57.567823,-38.023078)
    )

    private val recorridoConstitucion553 = Recorrido(
        sentido = "Hacia Constitución",
        paradas = listOf(paradaTalcahuanoJuanBJusto, paradaCordobaAzcuenaga),
        coordenadas = emptyList()
    )

    private val recorridoPuerto553 = Recorrido(
        sentido = "Hacia el Puerto",
        paradas = listOf(paradaSanLuisJuanBJusto, paradaDellepianeSolis),
        coordenadas = emptyList()
    )

    val lineas : List<Linea> = listOf(

        Linea(identificador = "551",
            recorridoIda = Recorrido(sentido = "Hacia Constitución",paradas = emptyList(), coordenadas = emptyList()),
            recorridoVuelta = Recorrido( sentido = "Hacia Constitución",paradas = emptyList(), coordenadas = emptyList())
        ),
        Linea(identificador = "552",
            recorridoIda = Recorrido(sentido = "Hacia Constitución",paradas = emptyList(), coordenadas = emptyList()),
            recorridoVuelta = Recorrido( sentido = "Hacia Constitución",paradas = emptyList(), coordenadas = emptyList())
        ),
        Linea(identificador = "553",
              recorridoIda = recorridoConstitucion553,
              recorridoVuelta = recorridoPuerto553
             ),
        Linea(identificador = "555",
            recorridoIda = Recorrido(sentido = "Hacia Constitución",paradas = emptyList(), coordenadas = emptyList()),
            recorridoVuelta = Recorrido( sentido = "Hacia Constitución",paradas = emptyList(), coordenadas = emptyList())
        )
    )
}