package com.guille.transportesl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guille.transportesl.ui.theme.TransporteSLTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.guille.transportesl.datos.DatosPrueba
import com.guille.transportesl.modelos.Linea
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.LineLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.LineString
import org.maplibre.spatialk.geojson.MultiPoint
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position

enum class Pantalla{
    INICIAL,
    SELECCION_LINEA,
    RECORRIDO
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Para que la barra de estado se ponga transparente
        enableEdgeToEdge()
        setContent {
            TransporteSLTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TransporteSLApp( modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TransporteSLApp(modifier : Modifier = Modifier){

    var pantallaActual by remember {
        mutableStateOf(Pantalla.INICIAL)
    }

    var lineaSeleccionada by remember {
        mutableStateOf<Linea?>(null)
    }

    BackHandler( enabled = pantallaActual != Pantalla.INICIAL) {
        when(pantallaActual){
            Pantalla.SELECCION_LINEA ->{
                pantallaActual = Pantalla.INICIAL
            }

            Pantalla.RECORRIDO -> {
                pantallaActual = Pantalla.SELECCION_LINEA
            }

            Pantalla.INICIAL -> Unit
        }
    }


    when (pantallaActual){
        Pantalla.INICIAL -> {
            PantallaInicial(modifier = modifier, onContinuar = {
                pantallaActual = Pantalla.SELECCION_LINEA
            })
        }

        Pantalla.SELECCION_LINEA -> {
            PantallaSeleccion(modifier = modifier, onSeleccionLinea = {linea ->
                lineaSeleccionada = linea
                pantallaActual = Pantalla.RECORRIDO
            }, onVolver = {
               pantallaActual = Pantalla.INICIAL
            }, lineas = DatosPrueba.lineas)
        }

        Pantalla.RECORRIDO -> {

            val linea = lineaSeleccionada

            if ( linea != null ){
                PantallaRecorrido(modifier = modifier, lineaSeleccionada = linea, onVolver = {
                    pantallaActual = Pantalla.SELECCION_LINEA
                })
            }
        }
    }
}

@Composable
fun PantallaInicial(modifier : Modifier = Modifier, onContinuar : ()-> Unit){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "TransporteSL",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Información del transporte público de San Luis")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onContinuar)
        {
            Text(
                text = "Continuar",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun PantallaSeleccion( modifier : Modifier = Modifier, onSeleccionLinea : (Linea) -> Unit, onVolver : () -> Unit, lineas : List<Linea>){

    Column( modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Box(modifier = Modifier.fillMaxWidth()) {

            Button(modifier = Modifier.align(Alignment.CenterStart),
                onClick = onVolver) {
                Text(text = "<-")
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Seleccioná una línea",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(Modifier.height(15.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
            items(lineas) { linea -> ItemLinea( linea = linea, onClick = {
                onSeleccionLinea(linea)
            })
                HorizontalDivider()
            }
        }

    }
}
@Composable
fun PantallaRecorrido( modifier : Modifier = Modifier, lineaSeleccionada : Linea, onVolver : () -> Unit){


    val cameraState = rememberCameraState( firstPosition = CameraPosition(target = Position(
        -57.55,
        -38.00
    ), zoom = 12.0
    ))

    val paradas = lineaSeleccionada.recorridoIda.paradas

    val posiciones  = paradas.map {
        parada -> Position(
        parada.coordenada.longitud,
        parada.coordenada.latitud)
    }

    val multiPoint = MultiPoint(posiciones)

    val lineaRecorrido = LineString(posiciones)


    Column(modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Box(modifier = Modifier.fillMaxWidth()) {

            Button(modifier = Modifier.align(Alignment.CenterStart),
                onClick = onVolver) {
                Text(text = "<-")
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Recorrido",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(Modifier.height(15.dp))


        Text(text = lineaSeleccionada.identificador)

        MaplibreMap(baseStyle = BaseStyle.Uri(
            "https://tiles.openfreemap.org/styles/liberty"
        ),cameraState = cameraState){


            val puntoSource = rememberGeoJsonSource(
                data = GeoJsonData.Features(
                    geoJson = multiPoint
                )
            )

            val recorridoSource = rememberGeoJsonSource(
                data = GeoJsonData.Features(
                    lineaRecorrido
                )
            )

            CircleLayer(
                id = "punto-prueba",
                source = puntoSource,
                radius = const(10.dp)
            )

            LineLayer(
                id = "recorrido",
                source = recorridoSource,
            )


        }
    }
}
@Composable
fun ItemLinea( linea : Linea, onClick : () -> Unit, modifier: Modifier = Modifier){

    Surface( modifier = modifier.fillMaxWidth(), onClick = onClick) {

        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = linea.identificador,
                style = MaterialTheme.typography.titleLarge)
        }
    }
}
