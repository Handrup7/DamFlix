package com.example.damflix_2.ui.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.damflix_2.R

// Pantalla que muestra información sobre la aplicación DAMFlix.
@Composable
fun PantallaInformacion(
    modifier: Modifier = Modifier
) {

    // Colocamos todos los elementos en una columna centrada.
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Imagen que representa el perfil de la aplicación.
        Image(
            painter = painterResource(id = R.drawable.avatar_handrup),
            contentDescription = stringResource(R.string.info_profile_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(150.dp)
        )

        // Espacio entre la imagen y el nombre de la aplicación.
        Spacer(modifier = Modifier.height(16.dp))

        // Nombre de la aplicación.
        Text(
            text = stringResource(R.string.info_app_name),
            style = MaterialTheme.typography.headlineMedium
        )

        // Espacio entre el nombre y la descripción.
        Spacer(modifier = Modifier.height(8.dp))

        // Descripción breve de la aplicación.
        Text(
            text = stringResource(R.string.info_description),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        // Espacio antes de mostrar las tecnologías utilizadas.
        Spacer(modifier = Modifier.height(16.dp))

        // Información adicional sobre el desarrollo de la aplicación.
        Text(
            text = stringResource(R.string.info_technologies),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}
