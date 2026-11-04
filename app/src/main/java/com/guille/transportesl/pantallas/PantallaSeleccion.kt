import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guille.transportesl.ItemLinea
import com.guille.transportesl.modelos.Linea

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