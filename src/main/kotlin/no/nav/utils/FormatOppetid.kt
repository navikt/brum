package no.nav.utils

import kotlin.time.Duration
import kotlin.time.DurationUnit

/**
 * Extension for å formatere oppetid som HH:mm:ss.
 */
fun Duration.tilOppetid(): String {
    val timer = this.toLong(DurationUnit.HOURS)
    val minutter = this.toLong(DurationUnit.MINUTES) % 60
    val sekunder = this.toLong(DurationUnit.SECONDS) % 60
    return "%02d:%02d:%02d".format(timer, minutter, sekunder)
}
