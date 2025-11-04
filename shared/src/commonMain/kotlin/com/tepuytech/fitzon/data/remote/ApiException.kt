package com.tepuytech.fitzon.data.remote

data class ApiException(val errorMessage: String) : Exception(errorMessage)