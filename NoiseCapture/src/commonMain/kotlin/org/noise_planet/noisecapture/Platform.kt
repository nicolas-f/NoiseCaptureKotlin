package org.noise_planet.noisecapture

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform