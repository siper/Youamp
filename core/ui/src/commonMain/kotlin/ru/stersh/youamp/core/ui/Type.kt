package ru.stersh.youamp.core.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import youamp.core.ui.generated.resources.Res
import youamp.core.ui.generated.resources.montserrat_bold
import youamp.core.ui.generated.resources.montserrat_italic
import youamp.core.ui.generated.resources.montserrat_light
import youamp.core.ui.generated.resources.montserrat_medium
import youamp.core.ui.generated.resources.montserrat_regular

@Composable
private fun MontserratFontFamily() =
    FontFamily(
        Font(
            Res.font.montserrat_light,
            FontWeight.Light,
        ),
        Font(
            Res.font.montserrat_regular,
            FontWeight.Normal,
        ),
        Font(
            Res.font.montserrat_italic,
            FontWeight.Normal,
            FontStyle.Italic,
        ),
        Font(
            Res.font.montserrat_medium,
            FontWeight.Medium,
        ),
        Font(
            Res.font.montserrat_bold,
            FontWeight.Bold,
        ),
    )

@Composable
internal fun Typography(): Typography = Typography().defaultFontFamily(fontFamily = MontserratFontFamily())

private fun Typography.defaultFontFamily(fontFamily: FontFamily): Typography =
    this.copy(
        displayLarge = this.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = this.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = this.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = this.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = this.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = this.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = this.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = this.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = this.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = this.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = this.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = this.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = this.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = this.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = this.labelSmall.copy(fontFamily = fontFamily),
    )
