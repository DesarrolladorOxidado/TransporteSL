package com.guille.transportesl

import PantallaRecorrido
import PantallaSeleccion
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guille.transportesl.ui.theme.TransporteSLTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import com.guille.transportesl.datos.DatosPrueba
import com.guille.transportesl.modelos.LimitesRecorrido
import com.guille.transportesl.modelos.Linea
import com.guille.transportesl.modelos.Recorrido
import com.guille.transportesl.pantallas.PantallaInicial
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.expressions.value.SymbolPlacement
import org.maplibre.compose.layers.LineLayer
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.LineString
import org.maplibre.spatialk.geojson.Position
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Point
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import org.maplibre.compose.sources.GeoJsonSource
import org.maplibre.spatialk.geojson.Geometry
import org.maplibre.spatialk.turf.measurement.distance
import org.maplibre.spatialk.units.extensions.inMeters
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log2

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

    // remember conserva el valor entre recomposiciones;
   // mutableStateOf hace que Compose observe ese valor y reaccione cuando cambia.
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
fun ItemLinea( linea : Linea, onClick : () -> Unit, modifier: Modifier = Modifier){

    Surface( modifier = modifier.fillMaxWidth(), onClick = onClick) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = linea.identificador,
                style = MaterialTheme.typography.titleLarge)
        }
    }
}