package com.arguvio.tp2Kotlin.ui.activities

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.layout.FoldingFeature
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


@Composable
fun Activity.rememberWindowSize(): DpSize {
    val displayMetrics = DisplayMetrics()
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(displayMetrics)

    // Convert pixels to dp
    val density = resources.displayMetrics.density
    val widthDp = (displayMetrics.widthPixels / density).dp
    val heightDp = (displayMetrics.heightPixels / density).dp

    // Remember the width and height in Dp and return as DpSize
    return remember { DpSize(widthDp, heightDp) }
}

enum class WindowSize { COMPACT, MEDIUM, EXPANDED }

fun getWindowSizeClass(windowDpSize: DpSize): WindowSize = when {
    windowDpSize.width < 600.dp -> WindowSize.COMPACT
    windowDpSize.width < 840.dp -> WindowSize.MEDIUM
    else -> WindowSize.EXPANDED
}


@Composable
fun Activity.rememberWindowSizeClass(): WindowSize {
    val windowSize = rememberWindowSize()

    // Pas besoin de convertir windowSize, car il est déjà en DpSize
    return getWindowSizeClass(windowSize)
}

sealed interface DevicePosture {
    object NormalPosture : DevicePosture

    data class TableTopPosture(val hingePosition: Rect) : DevicePosture

    data class BookPosture(val hingePosition: Rect) : DevicePosture

    data class Separating(
        val hingePosition: Rect,
        var orientation: FoldingFeature.Orientation
    ) : DevicePosture
}

@OptIn(ExperimentalContracts::class)
fun isTableTopPosture(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return foldFeature?.state == FoldingFeature.State.HALF_OPENED &&
            foldFeature.orientation == FoldingFeature.Orientation.HORIZONTAL
}

@OptIn(ExperimentalContracts::class)
fun isBookPosture(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return foldFeature?.state == FoldingFeature.State.HALF_OPENED &&
            foldFeature.orientation == FoldingFeature.Orientation.VERTICAL
}

@OptIn(ExperimentalContracts::class)
fun isSeparating(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return foldFeature?.state == FoldingFeature.State.FLAT && foldFeature.isSeparating
}
