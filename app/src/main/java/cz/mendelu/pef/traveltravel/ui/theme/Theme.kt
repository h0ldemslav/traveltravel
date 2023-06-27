package cz.mendelu.pef.traveltravel.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.compose.material3.Typography

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xffa6c8ff),
    secondary = Color(0xffbdc7dc),
    tertiary = Color(0xffdabde2),
    background = Color(0xff1a1c1e),
    surface = Color(0xff1a1c1e),
    onPrimary = Color(0xff003060),
    onSecondary = Color(0xff273141),
    onTertiary = Color(0xff3e2846),
    onBackground = Color(0xffe3e2e6),
    onSurface = Color(0xffe3e2e6),
    primaryContainer = Color(0xff004787),
    onPrimaryContainer = Color(0xffd5e3ff)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xff005eb1),
    secondary = Color(0xff555f71),
    tertiary = Color(0xff6e5676),
    background = Color(0xfffdfbff),
    surface = Color(0xfffdfbff),
    onPrimary = Color(0xffffffff),
    onSecondary = Color(0xffffffff),
    onTertiary = Color(0xffffffff),
    onBackground = Color(0xff1a1c1e),
    onSurface = Color(0xff1a1c1e),
    primaryContainer = Color(0xffd5e3ff),
    onPrimaryContainer = Color(0xff001c3b)
)

@Composable
fun TravelTravelTheme(
    darkTheme: Boolean,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    typography: Typography,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}