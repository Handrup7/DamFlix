package com.example.damflix_2.Navigation

import com.example.damflix_2.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.damflix_2.ui.View.FormularioEditarPelicula
import com.example.damflix_2.ui.View.FormularioPelicula
import com.example.damflix_2.ui.View.ListaPeliculas
import com.example.damflix_2.ui.View.PantallaInformacion
import com.example.damflix_2.ViewModel.PeliculasViewModel
import kotlinx.coroutines.launch

// Contenedor principal de la navegación de DAMFlix.
// Recibe el mismo ViewModel que utilizarán las pantallas.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navegacion(viewModel: PeliculasViewModel) {

    // Controlador que gestiona las rutas y la pila de navegación.
    val navController = rememberNavController()

    // Estado que indica si el menú lateral está abierto o cerrado.
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    // Permite ejecutar las animaciones de apertura y cierre del menú.
    val coroutineScope = rememberCoroutineScope()

    // Obtenemos la entrada actual de la pila de navegación.
    // Compose actualiza la interfaz cuando cambia la pantalla.
    val entradaActual by navController.currentBackStackEntryAsState()

    // Guardamos la ruta de la pantalla que se está mostrando.
    val rutaActual = entradaActual?.destination?.route

    // Lista de opciones que aparecerán en el menú lateral.
    // Reutilizamos las mismas rutas que tiene la barra inferior.
    val opcionesMenu = listOf(
        Ruta.Listado,
        Ruta.Formulario,
        Ruta.Informacion
    )

    // Menú lateral que permite acceder a las diferentes pantallas.
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            // Contenido visual del menú lateral.
            ModalDrawerSheet {

                // Cabecera del menú.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = stringResource(R.string.drawer_header),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Línea divisoria entre la cabecera y las opciones.
                HorizontalDivider()

                Spacer(modifier = Modifier.height(12.dp))

                // Creamos una opción del menú por cada ruta.
                opcionesMenu.forEach { ruta ->

                    NavigationDrawerItem(
                        // Icono definido en Ruta.kt.
                        icon = {
                            Icon(
                                imageVector = ruta.icon,
                                contentDescription = stringResource(ruta.label)
                            )
                        },

                        // Nombre de la opción del menú.
                        label = {
                            Text(text = stringResource(ruta.label))
                        },

                        // Resaltamos la opción de la pantalla actual.
                        selected = rutaActual == ruta.path,

                        // Acción que se ejecuta al pulsar una opción.
                        onClick = {

                            // Cerramos el menú lateral mediante una corrutina.
                            coroutineScope.launch {
                                drawerState.close()
                            }

                            // Solo navegamos si el destino es diferente.
                            if (rutaActual != ruta.path) {
                                navController.navigate(ruta.path) {

                                    // Conservamos el estado de las pantallas.
                                    popUpTo(
                                        navController.graph.startDestinationId
                                    ) {
                                        saveState = true
                                    }

                                    // Evitamos duplicar el mismo destino.
                                    launchSingleTop = true

                                    // Recuperamos el estado guardado.
                                    restoreState = true
                                }
                            }
                        },

                        // Aplicamos el espaciado recomendado para cada opción.
                        modifier = Modifier.padding(
                            NavigationDrawerItemDefaults.ItemPadding
                        )
                    )
                }
            }
        }
    ) {

        // Scaffold proporciona la estructura general de la aplicación:
        // barra superior, contenido principal y barra inferior.
        Scaffold(

            // Barra superior común a todas las pantallas.
            topBar = {
                TopAppBar(

                    // Nombre de la aplicación obtenido de strings.xml.
                    title = {
                        Text(stringResource(R.string.app_name))
                    },

                    // Botón de hamburguesa para abrir el menú lateral.
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.drawer_open_menu)
                            )
                        }
                    },

                    // Conservamos los colores originales de tu aplicación.
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor =
                            MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor =
                            MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            },

            // Barra inferior con las tres opciones de navegación.
            bottomBar = {
                NavigationBar {

                    // Cada elemento de Ruta se convierte en una pestaña.
                    opcionesMenu.forEach { ruta ->

                        NavigationBarItem(

                            // Resaltamos la pestaña de la pantalla actual.
                            selected = rutaActual == ruta.path,

                            // Navegamos solo si el destino es diferente.
                            onClick = {
                                if (rutaActual != ruta.path) {

                                    navController.navigate(ruta.path) {

                                        // Conservamos el estado de los destinos.
                                        popUpTo(
                                            navController.graph.startDestinationId
                                        ) {
                                            saveState = true
                                        }

                                        // Evitamos destinos duplicados.
                                        launchSingleTop = true

                                        // Recuperamos el estado guardado.
                                        restoreState = true
                                    }
                                }
                            },

                            // Icono definido en Ruta.kt.
                            icon = {
                                Icon(
                                    imageVector = ruta.icon,
                                    contentDescription =
                                        stringResource(ruta.label)
                                )
                            },

                            // Texto que aparece debajo del icono.
                            label = {
                                Text(
                                    text = stringResource(ruta.label)
                                )
                            }
                        )
                    }
                }
            }

        ) { innerPadding ->

            // NavHost define el contenido de cada ruta.
            // innerPadding evita que las barras se superpongan
            // al contenido de las pantallas.
            NavHost(
                navController = navController,

                // La primera pantalla que aparece es el catálogo.
                startDestination = Ruta.Listado.path,

                modifier = Modifier.padding(innerPadding)
            ) {

                // Ruta del catálogo de películas.
                composable(Ruta.Listado.path) {
                    ListaPeliculas(
                        viewModel = viewModel,

                        // Al pulsar editar guardamos la película en el
                        // ViewModel y navegamos a la pantalla de edición.
                        onEditarPelicula = { pelicula ->
                            viewModel.seleccionarParaEditar(pelicula)
                            navController.navigate(Ruta.Editar.path)
                        }
                    )
                }

                // Ruta del formulario para añadir películas.
                composable(Ruta.Formulario.path) {
                    FormularioPelicula(
                        viewModel = viewModel,

                        // Al guardar, volvemos al catálogo.
                        onPeliculaGuardada = {
                            navController.navigate(Ruta.Listado.path) {

                                // Evitamos acumular pantallas del formulario.
                                popUpTo(Ruta.Listado.path) {
                                    inclusive = false
                                }

                                // Evitamos duplicar el catálogo.
                                launchSingleTop = true
                            }
                        }
                    )
                }

                // Ruta para editar una película existente.
                // La película a editar la trae el ViewModel.
                composable(Ruta.Editar.path) {
                    FormularioEditarPelicula(
                        viewModel = viewModel,

                        // Al guardar, volvemos al catálogo.
                        onGuardada = {
                            navController.navigate(Ruta.Listado.path) {
                                popUpTo(Ruta.Listado.path) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                // Ruta con información general de la aplicación.
                composable(Ruta.Informacion.path) {
                    PantallaInformacion()
                }
            }
        }
    }
}
