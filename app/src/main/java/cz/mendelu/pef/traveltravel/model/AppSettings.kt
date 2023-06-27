package cz.mendelu.pef.traveltravel.model

const val SETTING_LANG_EN = "en"
const val SETTING_LANG_CS = "cs"
const val SETTING_THEME_IS_DARK = false
const val SETTING_SCREEN_SEARCH = "search_screen_search"
const val SETTING_SCREEN_WANT_TO_VISIT = "want_to_visit_cities_screen"
const val SETTING_FONT_SIZE_SMALL = "Small"
const val SETTING_FONT_SIZE_LARGE = "Large"

data class AppSettings(
    var language: String = SETTING_LANG_EN,
    var darkTheme: Boolean = SETTING_THEME_IS_DARK,
    var defaultScreenRoute: String = SETTING_SCREEN_SEARCH,
    var fontSize: String = SETTING_FONT_SIZE_SMALL,
    // List of string resources' id from AppPreference instances
    var selectedOptionIDs: List<Int> = listOf()
)
