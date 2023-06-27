package cz.mendelu.pef.traveltravel.model

data class AppPreference(
    var nameID: Int,
    var optionIDs: List<Int>,
    var selectedOptionID: Int
)
