package com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class WidgetTextStyle {
    NORMAL, BOLD, ITALIC, UNDERLINE, STRIKE_THROUGH, INCREASE_FONT, DECREASE_FONT
}

class InstagramSliderWidgetState(
    override val mainTextHint: String = "ASK A QUESTION..."
) : TextEditableWidgetState {
    val possibleTextColors = listOf(
        Color.Black,
        Color(0xFFFF5733), // Vibrant Red-Orange
        Color(0xFF33FF57), // Fresh Green
        Color(0xFF3357FF), // Bright Blue
        Color(0xFFFF33A8), // Vivid Pink
        Color(0xFFFFD733), // Sunny Yellow
        Color(0xFF33FFF3), // Aqua Cyan
        Color(0xFF8A33FF)  // Deep Purple
    )

    var text by mutableStateOf("")
    override val mainText: MutableState<String> = mutableStateOf("")

    override val textStyle = mutableStateOf(
        TextStyle(
            color = Black,
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
    }}

@Composable
fun InstagramSliderWidget(
    modifier: Modifier = Modifier,
    screenWidth : Dp,
    state: InstagramSliderWidgetState
) {
    Box (modifier = modifier
        .requiredWidth(screenWidth * 0.75f), contentAlignment = Alignment.Center) {
        Card (
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = White,
            )
        ) {
            Column (
                modifier = Modifier.padding(24.dp)
            ) {
                InstagramTextWidget(
                    modifier = Modifier,
                    state = state
                )
                Spacer(modifier = Modifier.height(16.dp))
                Slider(emoji = "❤️\uFE0F\u200D\uD83D\uDD25", progress = 0.5f)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SliderEditor(
    emoji : String,
    style: TextStyle = TextStyle(fontSize = 32.sp)
) {
    val textMeasurer = rememberTextMeasurer()
    val textSize = textMeasurer.measure(text = emoji, style = style)
    var dragAmountX by remember {
        mutableFloatStateOf(48f)
    }
    var sliderWidth by remember {
        mutableFloatStateOf(0f)
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged {
                sliderWidth = it.width.toFloat()
            }
            .height(36.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    if (
                        dragAmountX + dragAmount < 48f || dragAmountX + dragAmount > sliderWidth - 48f) return@detectHorizontalDragGestures
                    dragAmountX += dragAmount
                }
            }
    ) {
        drawLine(
            color = Color.Red,
            start = Offset(0f, size.center.y),
            end = Offset(size.width * dragAmountX / sliderWidth, size.center.y),
            strokeWidth = 10.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawText(
            textMeasurer = textMeasurer,
            text = emoji,
            topLeft = Offset(size.width * dragAmountX / sliderWidth - textSize.size.width / 2, size.center.y - textSize.size.height / 2),
            style = style
        )
    }
}

@Composable
fun Slider(
    emoji : String,
    progress: Float,
    style: TextStyle = TextStyle(fontSize = 32.sp)
) {
    val textMeasurer = rememberTextMeasurer()
    val textSize = textMeasurer.measure(text = emoji, style = style)
    var sliderWidth by remember {
        mutableFloatStateOf(0f)
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged {
                sliderWidth = it.width.toFloat()
            }
            .height(36.dp)
    ) {
        drawLine(
            color = Color.Red,
            start = Offset(0f, size.center.y),
            end = Offset(size.width * progress, size.center.y),
            strokeWidth = 10.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawText(
            textMeasurer = textMeasurer,
            text = emoji,
            topLeft = Offset(size.width * progress - textSize.size.width / 2, size.center.y - textSize.size.height / 2),
            style = style
        )
    }
}



@Composable
fun InstagramSliderWidgetEditor(
    state: InstagramSliderWidgetState
) {
    Box (modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Card (
            modifier = Modifier
                .fillMaxWidth(.75f),
            colors = CardDefaults.cardColors(
                containerColor = White,
            )
        ) {
            Column (
                modifier = Modifier.padding(24.dp)
            ) {
                InstagramAnyWidgetTextEditor(
                    modifier = Modifier,
                    state = state
                )
                Spacer(modifier = Modifier.height(16.dp))
                SliderEditor(emoji = "❤️\uFE0F\u200D\uD83D\uDD25")
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun InstagramSliderWidgetEditorScene(
    onDone: (InstagramSliderWidgetState) -> Unit
) {
    val state = remember {
        InstagramSliderWidgetState()
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
        InstagramSliderWidgetEditor(
            state = state
        )
        Text(text = "Yo wasup?", color = White)
    }

}

@Preview (showBackground = true)
@Composable
private fun InstagramSliderWidgetEditorPrev() {
    InstagramSliderWidgetEditor(InstagramSliderWidgetState())
}

@Preview (showBackground = true)
@Composable
private fun InstagramSliderWidgetPrev() {
    //InstagramSliderWidget(screenWidth = 1000.dp,"I am real...", 0f)
    InstagramSliderWidgetEditorScene {}
}