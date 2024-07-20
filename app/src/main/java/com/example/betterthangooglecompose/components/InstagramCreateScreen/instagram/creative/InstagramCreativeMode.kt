package com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.betterthangooglecompose.R
import com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.states.DraggableItemState
import com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.widgets.InstagramPollWidget
import com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.widgets.InstagramPollWidgetEditor
import com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.widgets.InstagramPollWidgetEditorScene
import com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.widgets.InstagramPollWidgetState
import com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.widgets.InstagramSliderWidget
import com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.widgets.InstagramSliderWidgetEditor
import com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.widgets.InstagramSliderWidgetEditorScene
import com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.widgets.InstagramSliderWidgetState
import com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.widgets.InstagramTextWidget
import com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.widgets.InstagramTextWidgetEditorScene
import com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.widgets.InstagramTextWidgetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.collections.forEach

class ScreenState(
    val screenSize: Size
) {
    val backgroundColors = listOf(
        Brush.linearGradient(
            colors = listOf(
                Color.Cyan,
                Color.Green,
                Color.Yellow
            )
        ),
        Brush.linearGradient(
            colors = listOf(
                Color.Red,
                Color.Yellow,
                Color.Green,
                Color.Blue
            )
        ),
        Brush.linearGradient(
            colors = listOf(Color.Yellow, Color.Magenta, Color.Cyan)
        ),
        Brush.linearGradient(
            colors = listOf(Color.Black, Color.White)
        )
    )

    var backgroundColor by mutableStateOf(backgroundColors[0])
    var backgroundColorIndex by mutableIntStateOf(0)

    val xSnapCoordinates =
        buildList { listOf(10, 50, 90).forEach { add(screenSize.width * it / 100) } }

    val ySnapCoordinates =
        buildList { listOf(10, 50, 90).forEach { add(screenSize.height * it / 100) } }

    var visibleXSnapCoordinate by mutableFloatStateOf(0f)

    var visibleYSnapCoordinate by mutableFloatStateOf(0f)

    fun changeColor() {
        backgroundColorIndex = (backgroundColorIndex + 1) % backgroundColors.size
        backgroundColor = backgroundColors[backgroundColorIndex]
        Brush.linearGradient()
    }

    var isDeleteActive by mutableStateOf(false)

}

sealed interface InstagramWidget {
    data class Text(val state: InstagramTextWidgetState) : InstagramWidget
    data class Slider(val state: InstagramSliderWidgetState) : InstagramWidget
    data class Poll(val state: InstagramPollWidgetState) : InstagramWidget
}

@Composable
fun InstagramCreativeMode() {
    val config = LocalConfiguration.current
    val width = config.screenWidthDp.dp.toPx()
    val height = config.screenHeightDp.dp.toPx()
    val scope = rememberCoroutineScope()

    val screenState = remember {
        ScreenState(Size(width, height))
    }

    val instagramWidgets = remember {
        mutableStateListOf<InstagramWidget>()
    }

    var isSelectionDialogVisible by remember {
        mutableStateOf(false)
    }

    if (isSelectionDialogVisible) {
        SelectItemsDialog(
            onTextWidgetAdded = {
                isSelectionDialogVisible = false
                instagramWidgets.add(InstagramWidget.Text(it))
            },
            onSliderAdded = {
                isSelectionDialogVisible = false
                instagramWidgets.add(InstagramWidget.Slider(it))
            },
            onPollAdded = {
                isSelectionDialogVisible = false
                instagramWidgets.add(InstagramWidget.Poll(it))
            }
        ) {
            isSelectionDialogVisible = false
        }
    }

    //background and delete button
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = screenState.backgroundColor),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (screenState.isDeleteActive)
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Drag here to remove", color = Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color.Black,
                            style = Stroke(width = 1.dp.toPx()),
                            radius = size.center.x * 2
                        )
                    },
                painter = painterResource(id = R.drawable.trash), contentDescription = "del")
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    //Layer for selections
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = {
                isSelectionDialogVisible = true
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add_item",
                    tint = Color.White
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = {
                screenState.changeColor()
            }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "change_color",
                    tint = Color.White
                )
            }
        }
    }

    //Layer for dragging added things around
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        instagramWidgets.forEach {
            when (it) {
                is InstagramWidget.Text -> {
                    InstagramTextWidget(
                        modifier = Modifier
                            .makeWidget(
                                screenState = screenState,
                                scope = scope
                            ),
                        state = it.state
                    )
                }

                is InstagramWidget.Slider -> {
                    InstagramSliderWidget(
                        screenWidth = config.screenWidthDp.dp,
                        modifier = Modifier
                            .makeWidget(
                                screenState = screenState,
                                scope = scope
                            ),
                        state = it.state
                    )
                }

                is InstagramWidget.Poll -> {
                    InstagramPollWidget(
                        modifier = Modifier.makeWidget(
                            screenState = screenState,
                            scope = scope
                        ),
                        screenWidth = config.screenWidthDp.dp,
                        state = it.state
                    )
                }
            }
        }
    }

    //layer for lines
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        VerticalLine(
            xCoordinate = screenState.visibleXSnapCoordinate
        )
        HorizontalLine(
            yCoordinate = screenState.visibleYSnapCoordinate
        )
    }

}

@Composable
fun VerticalLine(
    xCoordinate: Float = 100f
) {
    Canvas(modifier = Modifier.fillMaxHeight()) {
        drawLine(
            color = Color.Blue,
            start = Offset(xCoordinate, 0f),
            end = Offset(xCoordinate, size.height),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(20f, 10f),
            ), strokeWidth = 1.dp.toPx()
        )
    }
}

@Composable
fun HorizontalLine(
    yCoordinate: Float = 100f
) {
    Canvas(modifier = Modifier.fillMaxWidth()) {
        drawLine(
            color = Color.Blue,
            start = Offset(0f, yCoordinate),
            end = Offset(size.width, yCoordinate),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(20f, 10f),
            ), strokeWidth = 1.dp.toPx()
        )
    }
}

enum class ContentType {
    NONE,
    POLL,
    TEXT,
    SLIDER
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectItemsDialog(
    onTextWidgetAdded: (InstagramTextWidgetState) -> Unit,
    onSliderAdded: (InstagramSliderWidgetState) -> Unit,
    onPollAdded: (InstagramPollWidgetState) -> Unit,
    onDismiss: () -> Unit
) {
    var contentType by remember {
        mutableStateOf(ContentType.NONE)
    }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AnimatedContent(
            targetState = contentType
        ) {
            when (it) {
                ContentType.NONE -> {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 32.dp), contentAlignment = Alignment.BottomCenter) {
                        FlowRow (

                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                modifier = Modifier.rotate(2f),
                                onClick = {
                                contentType = ContentType.TEXT
                            }, shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = White)) {
                                Row (
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier.size(16.dp),
                                        painter = painterResource(id = R.drawable.normal), contentDescription = "poll")
                                    Spacer(modifier = Modifier.width(24.dp))
                                    Text("Text", color = Black)
                                }
                            }
                            Spacer(modifier = Modifier.width(64.dp))
                            Button(
                                modifier = Modifier.rotate(-1f),
                                onClick = {
                                contentType = ContentType.POLL
                            }, shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = White)) {
                                Row (
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier.size(16.dp),
                                        painter = painterResource(id = R.drawable.align_left), contentDescription = "poll")
                                    Spacer(modifier = Modifier.width(24.dp))
                                    Text("Poll", color = Black)
                                }
                            }

                            Spacer(modifier = Modifier.width(32.dp))
                            Button(onClick = {
                                contentType = ContentType.SLIDER
                            }, shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = White)) {
                                Row (
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier.size(16.dp),
                                        painter = painterResource(id = R.drawable.slider), contentDescription = "poll")
                                    Spacer(modifier = Modifier.width(24.dp))
                                    Text("Slider", color = Black)
                                }
                            }
                        }

                    }
                }

                ContentType.TEXT -> {
                    InstagramTextWidgetEditorScene {
                        onTextWidgetAdded(it)
                    }
                }

                ContentType.POLL -> {
                    InstagramPollWidgetEditorScene {
                        onPollAdded(it)
                    }
                }

                ContentType.SLIDER -> {
                    InstagramSliderWidgetEditorScene(
                        onDone = onSliderAdded
                    )
                }
            }
        }
    }
}

fun Modifier.makeWidget(
    screenState: ScreenState,
    scope: CoroutineScope
) = composed {
    val itemState = remember {
        DraggableItemState(screenState.screenSize)
    }
    val animatedScale by animateFloatAsState(targetValue = itemState.scale, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    this
        .onSizeChanged {
            itemState.size = it
            itemState.onPlaced(it)
        }
        .graphicsLayer {
            this.translationX = itemState.translationX
            this.translationY = itemState.translationY
            this.scaleX = animatedScale
            this.scaleY = animatedScale
        }
        .pointerInput(Unit) {
            detectDragGestures(
                onDragEnd = {
                    screenState.visibleYSnapCoordinate = 0f
                    screenState.visibleXSnapCoordinate = 0f
                    screenState.isDeleteActive = false
                }, onDragCancel = {
                    screenState.visibleYSnapCoordinate = 0f
                    screenState.visibleXSnapCoordinate = 0f
                    screenState.isDeleteActive = false
                },
                onDrag = { _, dragAmount ->
                    scope.launch {
                        itemState.handleDrag(
                            dragAmount = dragAmount,
                            screenState = screenState
                        )

                    }
                }
            )
        }
}

@Composable
fun Dp.toPx(): Float {
    val density = LocalDensity.current
    return with(density) { this@toPx.toPx() }
}

@Preview(showBackground = true)
@Composable
private fun InstagramSnapToGridPrev() {
    InstagramCreativeMode()
}