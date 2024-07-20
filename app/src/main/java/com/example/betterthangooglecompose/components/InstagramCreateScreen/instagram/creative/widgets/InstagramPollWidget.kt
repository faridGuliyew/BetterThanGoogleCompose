package com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class InstagramPollWidgetState (
    override val mainTextHint: String
) : TextEditableWidgetState {
    override val mainText: MutableState<String> = mutableStateOf("")

    override val textStyle: MutableState<TextStyle> = mutableStateOf(
        TextStyle(
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )
    )

    var options by mutableStateOf(Pair("", ""))
}

@Composable
fun InstagramPollWidget(
    modifier: Modifier,
    screenWidth : Dp,
    state: InstagramPollWidgetState
) {
    Box (modifier = modifier.requiredWidth(screenWidth * 0.75f), contentAlignment = Alignment.Center) {
        Card (
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = White,
            )
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp)
            ) {
                InstagramTextWidget(
                    modifier = Modifier.fillMaxWidth(),
                    state = state
                    )
            }

            Column (
                modifier = Modifier.padding(24.dp)
            ) {
                state.options.toList().forEach {
                    PollOption(it)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

}

@Composable
fun InstagramPollWidgetEditor(
    state : InstagramPollWidgetState
) {
    Box (modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Card (
            modifier = Modifier
                .fillMaxWidth(.75f),
            colors = CardDefaults.cardColors(
                containerColor = White,
            )
        ) {
            Box(modifier = Modifier
                .background(Color.Black)
                .padding(16.dp)
            ) {
                InstagramAnyWidgetTextEditor(
                    modifier = Modifier,
                    state = state
                )
            }

            Column (
                modifier = Modifier.padding(24.dp)
            ) {
                PollOptionEditor("Yes", state.options.first) {
                    state.options = state.options.copy(first = it)
                }
                Spacer(modifier = Modifier.height(8.dp))
                PollOptionEditor("No", state.options.second) {
                    state.options = state.options.copy(second = it)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun InstagramPollWidgetEditorScene(
    onDone: (InstagramPollWidgetState) -> Unit
) {
    val state = remember {
        InstagramPollWidgetState("ASK A QUESTION...")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.clickable {
                        onDone(state)
                    },
                    text = "done",
                    style = state.textStyle.value.copy(fontSize = 18.sp)
                )
            }
        }
        InstagramPollWidgetEditor(
            state = state
        )
        Text(text = "Made by Farid G.", color = White)
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PollOptionEditor(hint : String, value : String, onValueChange : (String) -> Unit) {
    Box (
        Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(0.05f), shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        BasicTextField2(
            value = value,
            onValueChange = onValueChange
        )
        if (value.isEmpty())
        Text(text = hint, color = Color.LightGray)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PollOption(value : String) {
    Box (
        Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(0.05f), shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        Text(text = value, color = Color.Black)
    }
}


@Preview (showBackground = true)
@Composable
private fun InstagramPollWidgetEditorPrev() {
//    InstagramPollWidgetEditor() {_,_->
//
//    }
    InstagramPollWidget(
        modifier = Modifier,
        screenWidth = 400.dp,
        state = InstagramPollWidgetState("LOL ?")
    )
}