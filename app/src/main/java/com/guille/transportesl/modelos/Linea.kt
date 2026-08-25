package com.guille.transportesl.modelos

data class Linea( val identificador : String,
                  val servicio: TipoServicio,
                  val recorridos: List<Recorrido>)
