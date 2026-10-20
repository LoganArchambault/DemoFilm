package cstjean.mobile2.demofilms.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cstjean.mobile2.demofilms.R
import cstjean.mobile2.demofilms.ui.theme.DemoFilmsTheme

@Composable
fun BtnDetails(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick
    ) {
        Text(stringResource(R.string.txt_btndetails));
    }
}

@Preview(name = "Bouton détails")
@Composable
fun BtnDetailsPreview() {
    DemoFilmsTheme {
        BtnDetails(
            onClick = { System.out.println("Bouton Détails cliqué") }
        )
    }
}