package cstjean.mobile2.demofilms.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cstjean.mobile2.demofilms.R
import cstjean.mobile2.demofilms.ui.theme.DemoFilmsTheme

@Composable
fun MediaCard(
    titre: String,
    annee: Int,
    note: Double,
    onDetailsClick: () -> Unit
) {
    Card(
        shape = CutCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 15.dp/*, shape = CutCornerShape(15.dp)*/)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_baseline_local_movies_100),
                contentDescription = stringResource(R.string.cd_affiche_du_film)
            )
            Column {
                Text(titre)
                Text(stringResource(R.string.txt_annee, annee))

                Row {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = stringResource(R.string.cd_evaluation_etoile),
                        tint = Color(0xFFBD8328),
                        modifier = Modifier
                            .size(15.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        stringResource(R.string.txt_note, note),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                BtnDetails(
                    onClick = onDetailsClick
                )
            }
        }
    }
}

@Preview
@Composable
fun MediaCardPreview() {
    DemoFilmsTheme() {
        MediaCard(
            "Le Seigneur des Anneaux",
            2001,
            8.8,
            onDetailsClick = { System.out.println("Titre du film : Le Seigneur des Anneaux") })
    }
}