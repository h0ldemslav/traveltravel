package cz.mendelu.pef.traveltravel.extensions

import android.content.Context
import android.content.Intent

// Thanks to Ma3x
// https://stackoverflow.com/questions/72731148/how-can-i-open-gmail-when-click-the-button-in-jetpack-compose
// And also folks from this post:
// https://stackoverflow.com/questions/3312438/how-to-open-email-program-via-intents-but-only-an-email-program

fun Context.sendMail(to: String, subject: String) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.type = "vnd.android.cursor.item/email"

    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    startActivity(intent)
}