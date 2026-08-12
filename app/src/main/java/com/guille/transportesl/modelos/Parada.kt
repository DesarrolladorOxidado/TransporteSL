package com.guille.transportesl.modelos

// data class porque Parada representa principalmente datos del dominio;
// nos interesa su contenido y la comparación por valores.
data class Parada(val callePrincipal : String, val interseccion : String, val coordenada : Coordenada)
