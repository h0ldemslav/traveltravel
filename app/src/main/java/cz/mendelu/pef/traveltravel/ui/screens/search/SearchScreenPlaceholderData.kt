package cz.mendelu.pef.traveltravel.ui.screens.search

import cz.mendelu.pef.traveltravel.R
import cz.mendelu.pef.traveltravel.services.api.BAD_REQUEST
import cz.mendelu.pef.traveltravel.services.api.INTERNAL_SERVER_ERROR
import cz.mendelu.pef.traveltravel.services.api.SERVICE_UNAVAILABLE
import cz.mendelu.pef.traveltravel.services.api.TOO_MANY_REQUESTS

class SearchScreenPlaceholderData {
    var imageID: Int? = null
        private set
    var textID: Int? = null
        private set
    var supportingTextID: Int? = null
        private set

    fun setHTTPErrorPlaceholder(code: Int) {
        when (code) {
            BAD_REQUEST -> {
                imageID = R.drawable.undraw_void
                textID = R.string.city_not_found
            }

            TOO_MANY_REQUESTS -> {
                imageID = R.drawable.undraw_time_management
                textID = R.string.too_many_requests
                supportingTextID = R.string.too_many_requests_supporting_text
            }

            INTERNAL_SERVER_ERROR -> {
                imageID = R.drawable.undraw_cancel
                textID = R.string.internal_server_error
                supportingTextID = R.string.internal_server_error_supporting_text
            }

            SERVICE_UNAVAILABLE -> {
                imageID = R.drawable.undraw_server_down
                textID = R.string.service_unavailable
            }

            else -> {
                imageID = R.drawable.undraw_lost
                textID = R.string.other_http_error
                supportingTextID = R.string.other_http_error_supporting_text
            }
        }
    }

    fun setEmptyBusinessesPlaceholder() {
        imageID = R.drawable.undraw_no_data
        textID = R.string.empty_result
        supportingTextID = R.string.empty_result_supporting_text
    }

    fun setNoInternetPlaceholder() {
        imageID = R.drawable.undraw_signal_searching
        textID = R.string.no_internet_connection
        supportingTextID = R.string.no_internet_connection_supporting_text
    }

    fun clearPlaceholder() {
        imageID = null
        textID = null
        supportingTextID = null
    }
}
