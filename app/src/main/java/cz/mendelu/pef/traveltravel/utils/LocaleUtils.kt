@file:Suppress("DEPRECATION")

package cz.mendelu.pef.traveltravel.utils

import android.content.Context
import android.content.res.Configuration
import cz.mendelu.pef.traveltravel.services.api.YELP_CZECH_LOCALE
import cz.mendelu.pef.traveltravel.services.api.YELP_ENGLISH_LOCALE
import java.util.*

object LocaleUtils {

    private const val CZECH = "cs"
    private const val SLOVAK = "sk"

    fun getFormattedLocaleForAPI(): String {
        val language = Locale.getDefault().language

        return if (language.equals(CZECH) || language.equals(SLOVAK)) {
            YELP_CZECH_LOCALE
        } else {
            YELP_ENGLISH_LOCALE
        }
    }

    // Thanks to emAd H
    // https://stackoverflow.com/questions/69874263/how-to-change-language-locale-in-jetpack-compose/71095850#71095850

    fun setLocale(context: Context, value: String) = updateResources(context, value)

    private fun updateResources(context: Context, language: String) {
         context.resources.apply {
             val locale = Locale(language)
             val config = Configuration(configuration)

             context.createConfigurationContext(configuration)
             Locale.setDefault(locale)
             config.setLocale(locale)

             context.resources.updateConfiguration(config, displayMetrics)
         }
    }

}