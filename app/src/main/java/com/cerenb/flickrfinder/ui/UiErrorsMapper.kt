package com.cerenb.flickrfinder.ui

import android.content.res.Resources
import com.cerenb.flickrfinder.R
import java.net.ConnectException
import java.net.UnknownHostException

class UiErrorsMapper(private val resources: Resources) {

    fun map(error: Throwable?): String =
        when (error) {
            is ConnectException -> resources.getString(R.string.connect_error)
            is UnknownHostException -> resources.getString(R.string.unknown_host_error)
            else -> resources.getString(R.string.unknown_network_error)
        }
}