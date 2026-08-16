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

    private val paradaCordobaRawson = Parada(
        callePrincipal = "Córdoba",
        interseccion = "Rawson",
        coordenada = Coordenada(longitud = -57.553401,latitud = -38.007610 )
    )

    private val paradaCordobaColon = Parada(
        callePrincipal = "Córdoba",
        interseccion = "Av. Colón",
        coordenada = Coordenada(longitud = -57.550382,latitud = -38.003860 )
    )

    private val paradaLamadridAvPeraltaRamos = Parada(
        callePrincipal = "Lamadrid",
        interseccion = "Av Patricio Peralta Ramos",
        coordenada = Coordenada(longitud = -57.541717,latitud = -38.007102 )
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
        paradas = listOf(paradaTalcahuanoJuanBJusto, paradaCordobaAzcuenaga, paradaCordobaRawson,
            paradaCordobaColon, paradaLamadridAvPeraltaRamos),
        coordenadas = listOf(
            Coordenada( longitud = -57.566071, latitud = -38.023167),
            Coordenada(longitud = -57.565948,latitud = -38.023061),
            Coordenada(longitud = -57.565813,latitud = -38.022898),
            Coordenada(longitud = -57.550151, latitud = -38.003503),
            Coordenada(longitud = -57.542129, latitud = -38.007536),
            Coordenada(longitud = -57.541717,latitud = -38.007102 )
        )

    )


    private val recorridoPuerto553 = Recorrido(
        sentido = "Hacia el Puerto",
        paradas = listOf(paradaSanLuisJuanBJusto, paradaDellepianeSolis),
        coordenadas = listOf(Coordenada(-57.566718,-38.022157),
            Coordenada(-57.567823,-38.023078))
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