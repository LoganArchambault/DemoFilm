package cstjean.mobile2.demofilms.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cstjean.mobile2.demofilms.R
import cstjean.mobile2.demofilms.viewmodel.MediaDetailsUiState
import cstjean.mobile2.demofilms.viewmodel.MediaDetailsViewModel

@Composable
fun MediaDetailsScreen(
    modifier: Modifier = Modifier,
    idFilm: Int?,
    onBackClick: () -> Unit,
    viewModel: MediaDetailsViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                MediaDetailsViewModel(idFilm = idFilm ?: 0)
            }
        }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var afficherConfirmation by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            MediaDetailsTopAppBar(
                onBackClick = onBackClick,
                onDeleteClick = {
                    afficherConfirmation = true
                }
            )
        }
    ) { innerPadding ->

        MediaDetailsContent(
            uiState = uiState,
            modifier = modifier.padding(innerPadding)
        )

        if (afficherConfirmation) {
            ConfirmDeleteDialog(
                onConfirm = {
                    // Suppression du film
                    viewModel.supprimerFilm()

                    afficherConfirmation = false

                    // Retour à la liste des films
                    onBackClick()
                },
                onDismiss = {
                    // Fermer le dialogue
                    afficherConfirmation = false
                }
            )
        }
    }
}

@Composable
private fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Supprimer le film?")
        },
        text = {
            Text("Voulez-vous vraiment supprimer ce film?")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Supprimer")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Annuler")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaDetailsTopAppBar(
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text("Détails")
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour"
                )
            }
        },
        actions = {
            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer"
                )
            }
        }
    )
}

@Composable
private fun MediaDetailsContent(
    uiState: MediaDetailsUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }

            uiState.erreur != null -> {
                Text(
                    text = uiState.erreur,
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {
                if (uiState.film == null) {
                    Text(
                        text = "Film introuvable!",
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    MediaDetails(
                        titre = uiState.film.titre,
                        annee = uiState.film.annee,
                        note = uiState.film.note,
                        nomRealisateur = uiState.film.nomRealisateur
                    )
                }
            }
        }
    }
}

@Composable
private fun MediaDetails(
    titre: String,
    annee: Int,
    note: Double,
    nomRealisateur: String? = null
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = titre,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = stringResource(
                R.string.mediadtls_txt_annee,
                annee
            ),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(
                R.string.media_dtls_txt_note,
                note
            ),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Réalisateur : ${nomRealisateur ?: "Inconnu"}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}