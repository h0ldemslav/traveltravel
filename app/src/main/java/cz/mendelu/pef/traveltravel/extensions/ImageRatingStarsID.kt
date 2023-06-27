package cz.mendelu.pef.traveltravel.extensions

import cz.mendelu.pef.traveltravel.R

fun String.getImageRatingStarsID(): Int {
    val imageIDs = mapOf(
        R.drawable.stars_small_0 to 1,
        R.drawable.stars_small_1 to 1.5,
        R.drawable.stars_small_1_half to 2,
        R.drawable.stars_small_2 to 2.5,
        R.drawable.stars_small_2_half to 3,
        R.drawable.stars_small_3 to 3.5,
        R.drawable.stars_small_3_half to 4,
        R.drawable.stars_small_4 to 4.5,
        R.drawable.stars_small_4_half to 5,
        R.drawable.stars_small_5 to 99
    )

    var imageID = imageIDs.keys.first()
    val ratingDouble = this.toDoubleOrNull()

    if (ratingDouble != null) {

        for ((key, value) in imageIDs) {
            if (ratingDouble < value.toDouble()) {
                imageID = key
                break
            }
        }

    }

    return imageID
}