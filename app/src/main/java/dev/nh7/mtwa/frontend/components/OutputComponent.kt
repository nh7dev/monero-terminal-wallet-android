package dev.nh7.mtwa.frontend.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nh7.mtwa.backend.TerminalMessage

@ExperimentalMaterial3Api
@Composable
fun OutputComponent(
    messages: List<TerminalMessage>,
    onClickMessage: (message: String) -> Unit,
    modifier: Modifier = Modifier
) {

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size)
    }

    LazyColumn(modifier = modifier.clip(RoundedCornerShape(8.dp)), state = listState) {
        items(messages) { entry ->

            val cardColors = if (entry.isFromUser)
                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            else
                CardDefaults.cardColors()

            Card(
                onClick = { onClickMessage(entry.message) },
                modifier = Modifier.padding(bottom = 4.dp),
                colors = cardColors,
                shape = RoundedCornerShape(4.dp)
            ) {
                SelectionContainer {
                    Text(
                        text = entry.message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )
                }
            }

        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun OutputComponentPreview() {
    val list = listOf(
        TerminalMessage(false, "this is a test message"),
        TerminalMessage(true, "this is another test message"),
    )
    OutputComponent(list, {})
}