package ru.stersh.youamp.feature.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

internal val AppIcon: ImageVector
    get() {
        if (_AppIcon != null) {
            return _AppIcon!!
        }
        _AppIcon = Builder(
            name = "AppIcon", defaultWidth = 512.0.dp, defaultHeight = 512.0.dp,
            viewportWidth = 512.0f, viewportHeight = 512.0f
        )
            .apply {
                group {
                    path(
                        fill = linearGradient(
                            0.0f to Color(0xFF275EFF), 0.0f to Color(0xFF0006DB),
                            1.0f to Color(0xFF8300FF), 1.0f to Color(0xFFFF446D), start =
                                Offset(401.91f, 187.36f), end = Offset(124.09f, 420.48f)
                        ), stroke = null,
                        strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f, pathFillType = NonZero
                    ) {
                        moveTo(346.5f, 247.72f)
                        lineTo(178.14f, 344.96f)
                        curveTo(146.95f, 363.09f, 108.24f, 340.65f, 108.02f, 304.85f)
                        lineTo(107.91f, 379.75f)
                        curveTo(108.01f, 425.58f, 157.52f, 454.11f, 197.1f, 431.31f)
                        lineTo(411.57f, 307.21f)
                        curveTo(451.15f, 284.41f, 451.0f, 227.25f, 411.55f, 204.5f)
                        lineTo(346.6f, 167.15f)
                        curveTo(377.73f, 185.06f, 377.41f, 229.83f, 346.5f, 247.72f)
                        close()
                    }
                    path(
                        fill = linearGradient(
                            0.0f to Color(0xFF275EFF), 0.0f to Color(0xFF3D0095),
                            1.0f to Color(0xFF00E0EB), 1.0f to Color(0xFFFF446D), start =
                                Offset(274.64f, 72.69f), end = Offset(274.64f, 344.82f)
                        ), stroke = null,
                        strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f, pathFillType = NonZero
                    ) {
                        moveTo(108.19f, 132.02f)
                        lineTo(108.19f, 207.12f)
                        curveTo(108.19f, 171.2f, 146.83f, 148.89f, 178.13f, 166.85f)
                        lineTo(346.57f, 264.0f)
                        curveTo(377.6f, 281.96f, 377.6f, 326.86f, 346.57f, 344.82f)
                        lineTo(411.61f, 307.27f)
                        curveTo(451.07f, 284.41f, 451.07f, 227.26f, 411.61f, 204.67f)
                        lineTo(197.18f, 80.86f)
                        curveTo(187.65f, 75.14f, 177.31f, 72.69f, 167.51f, 72.69f)
                        curveTo(136.49f, 72.69f, 108.19f, 97.46f, 108.19f, 132.02f)
                        close()
                    }
                    path(
                        fill = linearGradient(
                            0.0f to Color(0xFF0024FF), 0.0f to Color(0xFF00B8D8),
                            1.0f to Color(0xFF7400AA), 1.0f to Color(0xFF275EFF), start =
                                Offset(185.07f, 72.69f), end = Offset(185.07f, 351.39f)
                        ), stroke = null,
                        strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f, pathFillType = NonZero
                    ) {
                        moveTo(108.15f, 132.05f)
                        lineTo(108.15f, 304.57f)
                        curveTo(108.15f, 340.52f, 146.89f, 363.09f, 178.1f, 344.98f)
                        lineTo(192.04f, 336.89f)
                        lineTo(192.04f, 158.53f)
                        curveTo(192.04f, 122.86f, 231.06f, 100.28f, 261.99f, 118.12f)
                        lineTo(197.06f, 80.77f)
                        curveTo(187.58f, 75.2f, 177.27f, 72.69f, 167.51f, 72.69f)
                        curveTo(136.58f, 72.69f, 108.15f, 97.5f, 108.15f, 132.05f)
                        close()
                    }
                    path(
                        fill = linearGradient(
                            0.0f to Color(0xFF275EFF), 0.0f to Color(0xFF275EFF),
                            1.0f to Color(0xFF00FFF0), 1.0f to Color(0xFFFF446D), start =
                                Offset(205.76f, 285.05f), end = Offset(264.03f, 226.78f)
                        ), stroke = null,
                        strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f, pathFillType = NonZero
                    ) {
                        moveTo(224.27f, 213.71f)
                        curveTo(224.21f, 213.77f, 224.21f, 213.77f, 224.21f, 213.77f)
                        curveTo(223.16f, 213.77f, 222.05f, 213.94f, 221.01f, 214.17f)
                        curveTo(220.66f, 214.29f, 220.31f, 214.41f, 219.96f, 214.52f)
                        curveTo(219.43f, 214.7f, 218.91f, 214.87f, 218.38f, 215.16f)
                        curveTo(214.01f, 217.32f, 210.81f, 221.81f, 210.81f, 227.4f)
                        lineTo(210.81f, 284.38f)
                        curveTo(210.75f, 294.93f, 222.17f, 301.45f, 231.32f, 296.27f)
                        lineTo(265.69f, 276.34f)
                        lineTo(265.81f, 276.34f)
                        curveTo(265.69f, 276.34f, 265.69f, 276.34f, 265.69f, 276.34f)
                        lineTo(280.67f, 267.78f)
                        curveTo(285.21f, 265.1f, 287.48f, 260.49f, 287.48f, 255.95f)
                        curveTo(287.48f, 251.29f, 285.21f, 246.69f, 280.67f, 244.12f)
                        lineTo(265.69f, 235.44f)
                        curveTo(265.75f, 235.5f, 265.75f, 235.5f, 265.75f, 235.5f)
                        lineTo(246.23f, 224.25f)
                        lineTo(231.26f, 215.57f)
                        curveTo(229.1f, 214.35f, 226.77f, 213.77f, 224.5f, 213.71f)
                        curveTo(224.38f, 213.71f, 224.33f, 213.77f, 224.27f, 213.71f)
                        close()
                        moveTo(265.69f, 235.44f)
                        curveTo(265.69f, 235.44f, 265.69f, 235.44f, 265.69f, 235.44f)
                        lineTo(265.69f, 235.44f)
                        close()
                        moveTo(265.69f, 276.34f)
                        curveTo(265.69f, 276.34f, 265.69f, 276.34f, 265.69f, 276.34f)
                        lineTo(265.69f, 276.34f)
                        close()
                    }
                }
            }
            .build()
        return _AppIcon!!
    }

private var _AppIcon: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = AppIcon, contentDescription = "")
    }
}
