package com.tepuytech.fitzon.data.remote.api

import com.tepuytech.fitzon.data.local.SessionManager
import io.ktor.client.HttpClient

class BoxApi(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager
) {

}