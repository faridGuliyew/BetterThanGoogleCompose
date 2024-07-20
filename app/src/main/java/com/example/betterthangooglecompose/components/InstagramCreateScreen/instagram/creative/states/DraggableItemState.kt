package com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import com.example.betterthangooglecompose.components.InstagramCreateScreen.instagram.creative.ScreenState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.collections.forEach
import kotlin.math.abs
import kotlin.ranges.rangeTo

class DraggableItemState(
    val screenSize : Size,
    val maxDragAmountToShowSnapLines: Float = 5f,
    val snapDistance: Float = 5f,
    val dragAmountToLeaveSnap: Float = 10f
) {
    var size by mutableStateOf(IntSize.Zero)
    var isPlaced = false

    var scale by mutableFloatStateOf(1f)

    fun onPlaced(newSize : IntSize) {
        if (isPlaced) return
        isPlaced = true
        size = newSize
        translationX = (screenSize.width - size.width) / 2
        translationY = (screenSize.height - size.height) / 2
    }

    //X coordinate related
    var isSnappedX by mutableStateOf(false)
    var translationX by mutableFloatStateOf(0f)

    var xStartCoordinate
        get() = translationX
        set(value) {
            translationX = value
        }

    var xMidCoordinate
        get() = translationX + size.width / 2
        set(value) {
            translationX += value - (translationX + size.width / 2)
        }

    var xEndCoordinate
        get() = translationX + size.width
        set(value) {
            translationX += value - (translationX + size.width)
        }

    //Y coordinate related
    var isSnappedY by mutableStateOf(false)
    var translationY by mutableFloatStateOf((screenSize.height - size.height) / 2)
    var yStartCoordinate
        get() = translationY
        set(value) {
            translationY = value
        }

    var yMidCoordinate
        get() = translationY + size.height / 2
        set(value) {
            translationY += value - (translationY + size.height / 2)
        }

    var yEndCoordinate
        get() = translationY + size.height
        set(value) {
            translationY += value - (translationY + size.height)
        }


    suspend fun handleDrag(
        dragAmount: Offset,
        screenState: ScreenState
    ) {
        screenState.isDeleteActive = true
        coroutineScope {
            launch {
                handleDragX(
                    dragAmount = dragAmount.x,
                    xSnapCoordinates = screenState.xSnapCoordinates,
                    onHideCoordinate = { screenState.visibleXSnapCoordinate = 0f },
                    onShowCoordinate = { screenState.visibleXSnapCoordinate = it }
                )
            }
            launch {
                handleDragY(
                    dragAmount = dragAmount.y,
                    ySnapCoordinates = screenState.ySnapCoordinates,
                    onHideCoordinate = { screenState.visibleYSnapCoordinate = 0f },
                    onShowCoordinate = { screenState.visibleYSnapCoordinate = it }
                )
            }
        }
    }

    fun handleDragX(
        dragAmount: Float,
        xSnapCoordinates: List<Float>,
        onHideCoordinate: () -> Unit,
        onShowCoordinate: (Float) -> Unit
    ) {
        handleXDragInit(dragAmount, onHideCoordinate)
        checkIfShouldSnapX(dragAmount, xSnapCoordinates, onShowCoordinate)
    }

    private fun handleXDragInit(dragAmount: Float, onHideCoordinate: () -> Unit) {
        if (isSnappedX && abs(dragAmount) < dragAmountToLeaveSnap) return
        else {
            //clean up snap effects
            isSnappedX = false
            onHideCoordinate()
        }
        translationX += dragAmount
    }

    private fun checkIfShouldSnapX(
        dragAmount: Float,
        xSnapCoordinates: List<Float>,
        onShowCoordinate: (Float) -> Unit
    ) {
        if (abs(dragAmount) > maxDragAmountToShowSnapLines) return
        xSnapCoordinates.forEach { xSnapCoordinate ->
            //check the start of the composable
            if (xSnapCoordinate in (xStartCoordinate - snapDistance)..(xStartCoordinate + snapDistance)) {
                xStartCoordinate = xSnapCoordinate
                onShowCoordinate(xSnapCoordinate)
                isSnappedX = true
            }
            //check the mid of the composable
            else if (xSnapCoordinate in (xMidCoordinate - snapDistance)..(xMidCoordinate + snapDistance)) {
                xMidCoordinate = xSnapCoordinate
                onShowCoordinate(xSnapCoordinate)
                isSnappedX = true
            }
            //check the end of the composable
            else if (xSnapCoordinate in (xEndCoordinate - snapDistance)..(xEndCoordinate + snapDistance)) {
                xEndCoordinate = xSnapCoordinate
                onShowCoordinate(xSnapCoordinate)
                isSnappedX = true
            }
        }
    }


    fun handleDragY(
        dragAmount: Float,
        ySnapCoordinates: List<Float>,
        onHideCoordinate: () -> Unit,
        onShowCoordinate: (Float) -> Unit
    ) {
        handleYDragInit(dragAmount, onHideCoordinate)
        checkIfShouldSnapY(dragAmount, ySnapCoordinates, onShowCoordinate)
    }

    private fun handleYDragInit(dragAmount: Float, onHideCoordinate: () -> Unit) {
        if (isSnappedY && abs(dragAmount) < dragAmountToLeaveSnap) return
        else {
            //clean up snap effects
            isSnappedY = false
            onHideCoordinate()
        }
        translationY += dragAmount
        if (translationY >= screenSize.height * 0.95f) scale = 0.1f else scale = 1f
    }

    private fun checkIfShouldSnapY(
        dragAmount: Float,
        ySnapCoordinates: List<Float>,
        onShowCoordinate: (Float) -> Unit
    ) {
        if (abs(dragAmount) > maxDragAmountToShowSnapLines) return
        ySnapCoordinates.forEach { ySnapCoordinate ->
            //check the start of the composable
            if (ySnapCoordinate in (yStartCoordinate - snapDistance)..(yStartCoordinate + snapDistance)) {
                yStartCoordinate = ySnapCoordinate
                onShowCoordinate(ySnapCoordinate)
                isSnappedY = true
            }
            //check the mid of the composable
            else if (ySnapCoordinate in (yMidCoordinate - snapDistance)..(yMidCoordinate + snapDistance)) {
                yMidCoordinate = ySnapCoordinate
                onShowCoordinate(ySnapCoordinate)
                isSnappedY = true
            }
            //check the end of the composable
            else if (ySnapCoordinate in (yEndCoordinate - snapDistance)..(yEndCoordinate + snapDistance)) {
                yEndCoordinate = ySnapCoordinate
                onShowCoordinate(ySnapCoordinate)
                isSnappedY = true
            }
        }
    }
}