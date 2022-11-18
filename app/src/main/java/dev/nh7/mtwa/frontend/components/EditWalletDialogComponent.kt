package dev.nh7.mtwa.frontend.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nh7.mtwa.R


@ExperimentalMaterial3Api
@Composable
fun EditWalletDialogComponent(
    create: Boolean,
    onDone: (name: String, password: String) -> Unit,
    onDismiss: () -> Unit
) {

    val nameInput = remember { mutableStateOf("") }
    val passwordInput = remember { mutableStateOf("") }

    fun onDone() = onDone(nameInput.value, passwordInput.value)

    val title = stringResource(id = if (create) R.string.create_wallet else R.string.open_wallet)

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (!create) focusRequester.requestFocus()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            val icon = if (create) R.drawable.icon_create else R.drawable.icon_open
            Icon(painter = painterResource(id = icon), contentDescription = title)
        },
        title = {
            Text(text = title)
        },
        text = {
            Column {

                if (create) {
                    OutlinedTextField(
                        value = nameInput.value,
                        onValueChange = { nameInput.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text(text = stringResource(id = R.string.wallet_name_input_label)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Ascii
                        )
                    )
                }

                OutlinedTextField(
                    value = passwordInput.value,
                    onValueChange = { passwordInput.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .focusRequester(focusRequester),
                    label = { Text(text = stringResource(id = R.string.wallet_password_input_label)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onDone() }
                    )
                )

            }
        },
        confirmButton = {
            Row(modifier = Modifier.fillMaxWidth()) {

                TextButton(
                    onClick = { onDismiss() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(text = "Cancel")
                }

                TextButton(
                    onClick = { onDone() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(text = "Done")
                }

            }
        },
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EditWalletDialogComponentPreview() =
    EditWalletDialogComponent(true, { _, _ -> }, {})