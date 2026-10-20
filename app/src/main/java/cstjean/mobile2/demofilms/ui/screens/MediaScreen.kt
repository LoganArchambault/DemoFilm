package cstjean.mobile2.demofilms.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cstjean.mobile2.demofilms.data.Film
import cstjean.mobile2.demofilms.repository.FakeFilmData
import cstjean.mobile2.demofilms.ui.theme.DemoFilmsTheme
import cstjean.mobile2.demofilms.viewmodel.MediaUiState
import cstjean.mobile2.demofilms.viewmodel.MediaViewModel
import cstjean.mobile2.demofilms.ui.components.MediaCard

@Composable
fun MediaScreen(
    modifier: Modifier = Modifier,
    viewModel: MediaViewModel = viewModel(),
    onDetailsClick: (Film) -> Unit = { },
    onCreateClick: () -> Unit = { }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            MediaTopAppBar(
                onSortByTitle = { viewModel.trierParTitre() },
                onSortByRating = { viewModel.trierParNote() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onCreateClick()
                    // Action lorsqu'on clique sur le bouton
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ajouter un film"
                )
            }
        }
    ) { innerPadding ->
        MediaContent(
            uiState = uiState,
            modifier = modifier.padding(innerPadding),
            // Deux nouveaux événements du ViewModel
            onQueryChange = { viewModel.onRechercheOmdbChange(it) },
            onImporterClick = { viewModel.importerFilm() },
            onDetailsClick = { film ->
                viewModel.selectionnerFilm(film)
                onDetailsClick(film)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaTopAppBar(
    onSortByTitle: () -> Unit,
    onSortByRating: () -> Unit
) {
    // Force donc une recomposition si la valeur change.
    var menuOuvert by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Films") },
        actions = {
            IconButton(onClick = { menuOuvert = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = menuOuvert,
                onDismissRequest = { menuOuvert = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Trier par titre") },
                    onClick = {
                        menuOuvert = false
                        onSortByTitle()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Trier par note") },
                    onClick = {
                        menuOuvert = false
                        onSortByRating()
                    }
                )
            }
        }
    )
}

@Composable
private fun MediaContent(
    uiState: MediaUiState,
    modifier: Modifier = Modifier,
    // 2 nouveaux événements ici.
    onQueryChange: (String) -> Unit,
    onImporterClick: () -> Unit,
    onDetailsClick: (Film) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {

        // La barre d'importation reste toujours accessible en haut
        BarreImportationApi(
            query = uiState.rechercheOmdb,
            isSearching = uiState.isSearching,
            onQueryChange = onQueryChange,
            onImporterClick = onImporterClick,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Gestion explicite des états de contenu / chargement / erreur
        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.erreur != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Erreur : ${uiState.erreur}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            else -> {
                MediaList(uiState.films, Modifier, onDetailsClick)
            }
        }
    }
}

@Composable
private fun MediaList(
    films: List<Film>,
    //Ajout du modifier
    modifier: Modifier = Modifier,
    onDetailsClick: (Film) -> Unit
) {
    Surface(
        //On réutilise le modifier reçu en paramètre
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            //Ce padding ne sera plus nécessaire avec le menu
            //contentPadding = PaddingValues(top = 45.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            items(films) { film ->
                MediaCard(
                    film.titre,
                    film.annee,
                    film.note,
                    onDetailsClick = { onDetailsClick(film) })
            }
        }
    }
}
// Composable dédié à la barre d'importation OMDb
@Composable
private fun BarreImportationApi(
    query: String,
    isSearching: Boolean,
    onQueryChange: (String) -> Unit,
    onImporterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Importer un film (ex: Inception)") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (isSearching) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 4.dp),
                    strokeWidth = 2.dp
                )
            } else if (query.isNotBlank()) {
                IconButton(onClick = onImporterClick) {
                    Icon(
                        imageVector = Icons.Default.Add, // Ou une icône de téléchargement/import
                        contentDescription = "Importer le film"
                    )
                }
            }
        },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}


@Preview(
    name = "Media List",
    showBackground = true
)
@Preview(
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES
)
@Composable
fun MediaListsPreview() {
    DemoFilmsTheme() {
        MediaList(FakeFilmData.fullList, onDetailsClick = { film ->
            System.out.println("Titre du film : ${film.titre}")
        })
    }
}