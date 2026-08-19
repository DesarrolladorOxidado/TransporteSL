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
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import com.guille.transportesl.datos.DatosPrueba
import com.guille.transportesl.modelos.Linea
import com.guille.transportesl.modelos.Recorrido
import kotlinx.coroutines.launch
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.expressions.value.SymbolPlacement
import org.maplibre.compose.layers.CircleLayer
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

    Column( modifier = modifier
        .fillMaxSize()
        .padding(24.dp),
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

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
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

    var recorridoSeleccionado by remember {
        mutableStateOf(lineaSeleccionada.recorridoIda)
    }

    var mostrarParadas by remember {
        mutableStateOf( true)
    }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(24.dp),
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

        SelectorRecorrido(recorridoSeleccionado = recorridoSeleccionado, lineaSeleccionada = lineaSeleccionada, onSeleccionRecorrido = {
            recorrido -> recorridoSeleccionado = recorrido
        })

        Box(modifier = Modifier.fillMaxWidth().weight(1f)){

            MapaRecorrido( recorrido = recorridoSeleccionado,
                            mostrarParadas = mostrarParadas,
                            modifier = Modifier.fillMaxSize())

            Button( modifier = Modifier.align(Alignment.TopStart).padding(12.dp),
                onClick = {
                mostrarParadas = !mostrarParadas;
            }) {

                Text( text = if (mostrarParadas){ "Ocultar Paradas"}else{"Mostrar Paradas"})
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
// Se pasa la línea para que el selector conozca los recorridos disponibles
// y recorridoSeleccionado para que sepa cuál está activo actualmente.
// Cuando el usuario elige otro recorrido, onSeleccionRecorrido informa
// la selección a PantallaRecorrido, que actualiza su estado.
@Composable
fun SelectorRecorrido( recorridoSeleccionado : Recorrido, lineaSeleccionada : Linea, onSeleccionRecorrido : (Recorrido)->Unit){

    var menuExpandido by remember {
        mutableStateOf(false)
    }

    Box{
        Surface( onClick = {
            menuExpandido = true
        }) {
            Text( text = recorridoSeleccionado.sentido)
        }

        DropdownMenu(expanded = menuExpandido, onDismissRequest = {
            menuExpandido = false
        }) {

            DropdownMenuItem( text = {
                Text( text = lineaSeleccionada.recorridoIda.sentido) },
                onClick = {
                    onSeleccionRecorrido(lineaSeleccionada.recorridoIda)
                    menuExpandido = false
                })

            DropdownMenuItem(
                text = {
                    Text( text = lineaSeleccionada.recorridoVuelta.sentido)
                },
                onClick = {
                    onSeleccionRecorrido(lineaSeleccionada.recorridoVuelta)
                    menuExpandido = false
                })
        }
    }

}

@Composable
fun MapaRecorrido(recorrido : Recorrido, mostrarParadas : Boolean, modifier: Modifier = Modifier){



    val paradas = recorrido.paradas

    val paradaGeoJson = paradas.map{
        parada -> Feature(
           geometry = Point(
               Position(
                   parada.coordenada.longitud,
                   parada.coordenada.latitud
               )
           ),
            properties = buildJsonObject {
                put("callePrincipal", parada.callePrincipal)
                put("interseccion", parada.interseccion)
            }
        )
    }

    val coleccionParadas = FeatureCollection(
        features =  paradaGeoJson
    )

    val puntosRecorridos = recorrido.coordenadas
    val posicionesRecorrido  = puntosRecorridos.map {
            puntosRecorrido -> Position(
        puntosRecorrido.longitud,
        puntosRecorrido.latitud)
    }

    val lineaRecorrido = LineString(posicionesRecorrido)

    val iconoFlecha = painterResource(R.drawable.ic_flecha_recorrido)

    val primerPunto = puntosRecorridos.first()

    val posicionInicial =  CameraPosition(
        target = Position(primerPunto.longitud, primerPunto.latitud),
        tilt = 25.0,
        zoom = 15.0
    )

    var paradaSeleccionada by remember {
        mutableStateOf<String?>(null)
    }

    val estadoCamara = rememberCameraState(
        firstPosition = posicionInicial
    )

    val scope = rememberCoroutineScope()

    LaunchedEffect(recorrido) {
        estadoCamara.animateTo(
            finalPosition = posicionInicial

        )
    }

    Box(modifier = Modifier.fillMaxSize()){

        MaplibreMap(modifier = modifier,
            baseStyle = BaseStyle.Uri(
                "https://tiles.openfreemap.org/styles/liberty"
            ),cameraState = estadoCamara) {

            // Features es una clase anidada dentro de la interfaz GeoJsonData.
            // GeoJsonData.Features(...) llama al constructor de Features.
            val paradasSource = rememberGeoJsonSource(
                data = GeoJsonData.Features(
                    geoJson = coleccionParadas
                )
            )

            // Features envuelve la geometría (MultiPoint/LineString) como GeoJsonData.
            // rememberGeoJsonSource crea la fuente GeoJSON y la conserva entre recomposiciones.
            val recorridoSource = rememberGeoJsonSource(
                data = GeoJsonData.Features(
                    lineaRecorrido
                )
            )

            if ( mostrarParadas ) {
                //La geometría define QUÉ son los datos;
                // la Layer define CÓMO se renderizan en el mapa.
                CircleLayer(
                    id = "paradas",
                    source = paradasSource,
                    radius = const(10.dp),
                    color = const(Color(0xFF1565C0)),

                )

                //Momentáneo, hasta usar los íconos de paradas
                //Es para mejorar el touch en las paradas para visualizar sus datos
                CircleLayer(
                    id = "toque-paradas",
                    source = paradasSource,
                    radius = const(15.dp),
                    color = const(Color(0x001565C0)),
                    onClick = {
                            features -> val paradaTocada = features.first()

                        val callePrincipal =
                            paradaTocada.properties?.get("callePrincipal")?.jsonPrimitive?.content

                        val interseccion =
                            paradaTocada.properties?.get("interseccion")?.jsonPrimitive?.content

                        paradaSeleccionada = "$callePrincipal y $interseccion"
                        ClickResult.Consume
                    }
                )
            }

            LineLayer(
                id = "recorrido",
                source = recorridoSource,
                color = const(Color(0xFF1565C0)),
                width = const(8.dp)
            )

            SymbolLayer(
                id = "sentido-recorrido",
                source = recorridoSource,
                placement = const(SymbolPlacement.Line),
                spacing = const(5.dp),
                iconImage = image(iconoFlecha)
            )

        }

        if (paradaSeleccionada != null) {

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = paradaSeleccionada!!,
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = {
                            paradaSeleccionada = null
                        }
                    ) {
                        Text("X")
                    }
                }
            }
        }




        Column(modifier = Modifier.align(Alignment.TopEnd).
                            padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)){

            Button(
                onClick = {
                    val posicionActual = estadoCamara.position

                    scope.launch {
                        estadoCamara.animateTo(
                            finalPosition = CameraPosition(
                                target = posicionActual.target,
                                zoom = posicionActual.zoom + 1.0,
                                tilt = posicionActual.tilt,
                                bearing = posicionActual.bearing
                            )
                        )
                    }

                }){
                Text( text = "+")
            }

            Button(
                onClick = {
                    val posicionActual = estadoCamara.position

                    scope.launch {
                        estadoCamara.animateTo(
                            finalPosition = CameraPosition(
                                target = posicionActual.target,
                                zoom = posicionActual.zoom - 1.0,
                                tilt = posicionActual.tilt,
                                bearing = posicionActual.bearing
                            )
                        )
                    }

                }){
                Text( text = "-")
            }

            Button(
                onClick = {
                    scope.launch {
                        estadoCamara.animateTo(
                            finalPosition = CameraPosition(
                                target = posicionInicial.target,
                                zoom = posicionInicial.zoom ,
                                tilt = posicionInicial.tilt,
                                bearing = posicionInicial.bearing
                            )
                        )
                    }

                }){
                Text( text = "R")
            }

        }


    }

}