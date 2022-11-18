package dev.nh7.mtwa.frontend.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nh7.mtwa.R
import java.io.File


@ExperimentalMaterial3Api
@Composable
fun WalletListComponent(
    wallets: List<File>,
    onClickWallet: (wallet: File) -> Unit,
    onClickCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {

        if (wallets.isEmpty()) {

            Text(
                text = stringResource(id = R.string.no_wallets_found),
                modifier = Modifier.align(Alignment.Center)
            )

        } else {

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(wallets) { wallet ->

                    Card(
                        onClick = {
                            onClickWallet(wallet)
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Icon(
                                painter = painterResource(id = R.drawable.icon_wallet),
                                contentDescription = stringResource(id = R.string.wallet),
                                modifier = Modifier
                                    .size(72.dp)
                                    .padding(16.dp)
                            )

                            Text(
                                text = wallet.name,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp),
                                style = MaterialTheme.typography.headlineLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                        }
                    }

                }
            }

        }

        FloatingActionButton(
            onClick = onClickCreate,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_create),
                contentDescription = stringResource(id = R.string.create_wallet)
            )
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun WalletListComponentPreview() {
    val list = listOf(File("wallet1"), File("this is a wallet"))
    WalletListComponent(list, {}, {})
}