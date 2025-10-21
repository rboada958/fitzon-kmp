package com.tepuytech.fitzon

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform