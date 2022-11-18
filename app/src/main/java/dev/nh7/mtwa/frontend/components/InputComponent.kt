package dev.nh7.mtwa.frontend.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nh7.mtwa.R

@Composable
fun InputComponent(onSendInput: (input: String) -> Unit, modifier: Modifier = Modifier) {

    val input = remember { mutableStateOf("") }

    fun onClickSend() {
        onSendInput(input.value)
        input.value = ""
    }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Card(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            BasicTextField(
                value = input.value,
                onValueChange = { input.value = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onClickSend() }),
                maxLines = 3,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface)
            )

            SmallFloatingActionButton(
                onClick = { onClickSend() },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_send),
                    contentDescription = stringResource(id = R.string.send_command)
                )
            }

        }
    }
}

@Preview
@Composable
fun InputComponentPreview() = InputComponent({})