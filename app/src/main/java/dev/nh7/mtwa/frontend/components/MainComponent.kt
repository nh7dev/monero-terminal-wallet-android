package dev.nh7.mtwa.frontend.components


import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.nh7.mtwa.R
import dev.nh7.mtwa.backend.MainViewModel
import dev.nh7.mtwa.frontend.theme.MainTheme
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainComponent() {

    val activity = LocalContext.current as? Activity

    val viewModel: MainViewModel = viewModel()

    val clipboardManager = LocalClipboardManager.current

    val createDialogOpen = remember { mutableStateOf(false) }
    val openDialogOpen = remember { mutableStateOf(null as File?) }

    var lastBackClick = remember { 0L }

    BackHandler {
        if (viewModel.walletStarted.value) {
            viewModel.sendCommand("exit")
            //viewModel.stopWallet()
        } else {
            val now = System.currentTimeMillis()
            if (now - lastBackClick < 2000L) {
                activity?.finish()
            } else {
                lastBackClick = now
                viewModel.showToast(R.string.close_app)
            }
        }
    }

    MainTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {

                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) },
                    actions = {
                        IconButton(onClick = { viewModel.showToast(R.string.coming_soon) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_settings),
                                contentDescription = stringResource(id = R.string.settings)
                            )
                        }
                    }
                )

                Crossfade(
                    targetState = viewModel.walletStarted.value,
                    modifier = Modifier.weight(1f)
                ) { started ->

                    if (started) {

                        Column(modifier = Modifier.fillMaxSize()) {

                            OutputComponent(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp),
                                messages = viewModel.terminalMessages,
                                onClickMessage = { message ->
                                    clipboardManager.setText(AnnotatedString(message))
                                    viewModel.showToast(R.string.copied)
                                }
                            )

                            InputComponent(
                                modifier = Modifier.padding(8.dp),
                                onSendInput = { input ->
                                    viewModel.sendCommand(input)
                                }
                            )

                        }

                    } else {

                        val openDialogWalletFile = openDialogOpen.value
                        if (createDialogOpen.value || openDialogWalletFile != null) {
                            EditWalletDialogComponent(
                                create = openDialogWalletFile == null,
                                onDone = { name, password ->
                                    createDialogOpen.value = false
                                    openDialogOpen.value = null
                                    if (openDialogWalletFile != null) {
                                        viewModel.openWallet(openDialogWalletFile, password)
                                    } else {
                                        viewModel.createWallet(name, password)
                                    }
                                },
                                onDismiss = {
                                    createDialogOpen.value = false
                                    openDialogOpen.value = null
                                }
                            )
                        }

                        WalletListComponent(
                            wallets = viewModel.wallets,
                            onClickWallet = { walletFile ->
                                openDialogOpen.value = walletFile
                            },
                            onClickCreate = {
                                createDialogOpen.value = true
                            },
                            modifier = Modifier.fillMaxSize()
                        )

                    }


                }

            }
        }
    }
}

@Preview
@Composable
fun MainComponentPreview() = MainComponent()