package no.nav.config
/**
 * Leser og holder miljøvariabler brukt i appen.
 */
data class Environment (
    val texasEndpoint: String = getEnvVar("NAIS_TOKEN_INTROSPECTION_ENDPOINT", "http://localhost:8080/auth"),
    val brumFrontEndUrl: String = getEnvVar("BRUM_FRONTEND_URL", "localhost:3000")

)
fun getEnvVar(varName: String, defaultValue: String? = null) =
    System.getenv(varName)
        ?: defaultValue
        ?: throw RuntimeException("\"Mangler miljøvariabler \"")

