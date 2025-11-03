package com.tepuytech.fitzon.domain.model.auth

data class AuthApiException(val errorMessage: String) : Exception(errorMessage)