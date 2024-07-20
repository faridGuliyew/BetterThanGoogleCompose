package com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.betterthangooglecompose.R

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun InstagramTextWidget(
    modifier: Modifier = Modifier,
    state: TextEditableWidgetState,
    isEditable: Boolean = true
) {
    val focusManager = LocalFocusManager.current

    val customTextSelectionColors = TextSelectionColors(
        handleColor = state.textStyle.value.color,
        backgroundColor = state.textStyle.value.color.copy(0.25f)
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField2(
            modifier = modifier,
            value = state.mainText.value,
            onValueChange = {
                state.mainText.value = it
            },
            textStyle = state.textStyle.value,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            cursorBrush = SolidColor(Color.White),
            enabled = isEditable,
        )
    }
}


class InstagramTextWidgetState(
    val hint: String = "Type something here..."
) : TextEditableWidgetState {
    val possibleTextColors = listOf(
        Color.White,
        Color(0xFFFF5733), // Vibrant Red-Orange
        Color(0xFF33FF57), // Fresh Green
        Color(0xFF3357FF), // Bright Blue
        Color(0xFFFF33A8), // Vivid Pink
        Color(0xFFFFD733), // Sunny Yellow
        Color(0xFF33FFF3), // Aqua Cyan
        Color(0xFF8A33FF)  // Deep Purple
    )

    override val mainText: MutableState<String> = mutableStateOf("")
    override val mainTextHint = "Type something here"

    override val textStyle = mutableStateOf(
        TextStyle(
            color = White,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
    )

    fun setTextColor(color: Color) {
        textStyle.value = textStyle.value.copy(color = color)
    }


    fun setTextStyle(style: WidgetTextStyle) {
        when (style) {
            WidgetTextStyle.NORMAL -> {
                textStyle.value = textStyle.value.copy(
                    fontWeight = FontWeight.Normal,
                    fontStyle = null,
                    textDecoration = null
                )
            }

            WidgetTextStyle.BOLD -> {
                textStyle.value = textStyle.value.copy(fontWeight = FontWeight.Bold)
            }

            WidgetTextStyle.ITALIC -> {
                textStyle.value =
                    textStyle.value.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            }

            WidgetTextStyle.UNDERLINE -> {
                textStyle.value = textStyle.value.copy(textDecoration = TextDecoration.Underline)
            }

            WidgetTextStyle.STRIKE_THROUGH -> {
                textStyle.value = textStyle.value.copy(textDecoration = TextDecoration.LineThrough)
            }

            WidgetTextStyle.INCREASE_FONT -> {
                textStyle.value = textStyle.value.copy(fontSize = (textStyle.value.fontSize.value + 1f).sp)
            }

            WidgetTextStyle.DECREASE_FONT -> {
                textStyle.value = textStyle.value.copy(fontSize = (textStyle.value.fontSize.value - 1f).sp)
            }
        }
    }
}

interface TextEditableWidgetState {
    val mainText: MutableState<String>
    val mainTextHint : String
    val textStyle : MutableState<TextStyle>
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InstagramAnyWidgetTextEditor(
    modifier: Modifier = Modifier,
    state: TextEditableWidgetState
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val customTextSelectionColors = TextSelectionColors(
        handleColor = state.textStyle.value.color,
        backgroundColor = state.textStyle.value.color.copy(0.1f)
    )
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                BasicTextField2(
                    modifier = modifier.focusRequester(focusRequester),
                    value = state.mainText.value,
                    onValueChange = {
                        state.mainText.value = it
                    }, textStyle = state.textStyle.value,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ), keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ), cursorBrush = SolidColor(state.textStyle.value.color)
                )
                if (state.mainText.value.isEmpty())
                    Text(
                        text = state.mainTextHint,
                        style = state.textStyle.value.copy(color = state.textStyle.value.color.copy(0.25f))
                    )
            }
        }
    }
}

@Composable
fun InstagramTextWidgetEditorScene(
    onDone: (InstagramTextWidgetState) -> Unit
) {
    val state = remember {
        InstagramTextWidgetState()
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
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorSelectionRow(colors = state.possibleTextColors) {
                    state.setTextColor(it)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    modifier = Modifier.clickable {
                        onDone(state)
                    },
                    text = "done", style = state.textStyle.value.copy(fontSize = 18.sp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextStyleSelectionRow(color = state.textStyle.value.color) {
                state.setTextStyle(it)
            }
        }
        InstagramAnyWidgetTextEditor(
            modifier = Modifier.weight(1f),
            state = state
        )
        Text(text = "Yo wasup?", color = White)
    }
}

@Composable
fun RowScope.ColorSelectionRow(
    colors: List<Color>,
    onColor: (Color) -> Unit
) {
    colors.forEach {
        Box(modifier = Modifier
            .size(32.dp)
            .background(it, shape = CircleShape)
            .border(3.dp, White, shape = CircleShape)
            .clickable {
                onColor(it)
            }
        )
    }
}

@Composable
fun TextStyleSelectionRow(
    color: Color,
    onTextStyle: (WidgetTextStyle) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onTextStyle(WidgetTextStyle.NORMAL)
                },
            painter = painterResource(id = R.drawable.normal), contentDescription = "",
            colorFilter = ColorFilter.tint(color = color)
        )
        Image(
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onTextStyle(WidgetTextStyle.BOLD)
                },
            painter = painterResource(id = R.drawable.bold), contentDescription = "",
            colorFilter = ColorFilter.tint(color = color)
        )
        Image(
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onTextStyle(WidgetTextStyle.ITALIC)
                },
            painter = painterResource(id = R.drawable.italic), contentDescription = "",
            colorFilter = ColorFilter.tint(color = color)
        )
        Image(
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onTextStyle(WidgetTextStyle.UNDERLINE)
                },
            painter = painterResource(id = R.drawable.underline), contentDescription = "",
            colorFilter = ColorFilter.tint(color = color)
        )
        Image(
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onTextStyle(WidgetTextStyle.STRIKE_THROUGH)
                },
            painter = painterResource(id = R.drawable.strike_through),
            contentDescription = "",
            colorFilter = ColorFilter.tint(color = color)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.normal), contentDescription = "",
                colorFilter = ColorFilter.tint(color = color)
            )
            Column {
                Icon(
                    modifier = Modifier.clickable {
                        onTextStyle(WidgetTextStyle.INCREASE_FONT)
                    },
                    imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "up",
                    tint = color
                )
                Icon(
                    modifier = Modifier.clickable {
                        onTextStyle(WidgetTextStyle.DECREASE_FONT)
                    },
                    imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "up",
                    tint = color
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InstagramTextWidgetPrev() {
    InstagramTextWidget(
        modifier = Modifier.fillMaxSize(),
        state = InstagramTextWidgetState()
    )
}

@Preview(showBackground = true)
@Composable
private fun InstagramTextWidgetEditorPrev() {
    InstagramAnyWidgetTextEditor(state = InstagramTextWidgetState())
}

@Preview(showBackground = true)
@Composable
private fun InstagramTextPrev() {
    InstagramTextWidgetEditorScene {

    }
}