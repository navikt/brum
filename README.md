# Brum - NAV Bærum Tiltaksoversikt

En backend-applikasjon for oversikt over tiltak i NAV Bærum. Applikasjonen tilbyr API-er for å hente data om gjennomføringer, ukentlige antall og brukerinformasjon.

## 🎯 Oversikt

Brum er en Kotlin-basert web-applikasjon bygget med Ktor som tilbyr REST API-er for å hente og analysere data om NAV Bærum sine tiltak. Applikasjonen integrerer med Google BigQuery for datalagring og bruker Texas for autentisering.

### Hovedfunksjonaliteter(NB både Gjennomføringsoversikt og Ukentlig statistikk er mock data og laget for kun mvp og representerer ikke ekte/riktig data) :
- **Gjennomføringsoversikt**: Hente informasjon om pågående og planlagte tiltak
- **Ukentlig statistikk**: Detaljert oversikt over antall deltakere per uke, innsatsgruppe og avdeling
- **Brukeradministrasjon**: Håndtering av innloggede brukere via NAV-identitet

## 🚀 Teknologier/pakker brukt 

- **Kotlin** - Hovedprogrammeringsspråk
- **Ktor** - Web framework for HTTP server og klient
- **Google BigQuery** - Database for tiltaksdata
- **Jackson** - JSON-serialisering
- **JWT** - Token-håndtering
- **Texas** - NAV's autentiseringstjeneste
- **Gradle** - Build-verktøy
- **Docker** - Containerisering


## 🛠 Installasjon

### 1. Klon repositoryet
```bash
git clone git@github.com:navikt/brum.git
cd brum
```

### 2. Installer avhengigheter
```bash
./gradlew build
```

### Lokal utvikling
```bash
./gradlew run
```

### Med Docker
```bash
# Bygg applikasjonen
./gradlew build

# Bygg Docker-image
docker build -t brum .

# Kjør container
docker run -p 8080:8080 brum
```

Applikasjonen vil være tilgjengelig på `http://localhost:8080`


### Endepunkter

#### 🔒 Autentisering påkrevd
Følgende endepunkter krever gyldig Bearer-token:

- `GET /bruker-info` - Henter informasjon om innlogget bruker

#### 🔓 Offentlige endepunkter
Alle endpunkter er offentlige fordi dette er bare en mvp. Men authentisering er lagt opp til å brukes ved vidreutvikling. 
- `GET /status` - Helsesjekk og oppetid
- `GET /gjennomforinger` - Henter liste over gjennomføringer
- `GET /ukeAntall?aar={år}&uke={uke}` - Henter ukentlig statistikk
- `GET /testData?dataset={type}` - Henter testdata (Real/Mini)

## 🔐 Autentisering

Applikasjonen bruker NAV's Texas-tjeneste for autentisering:

1. **Bearer Token**: Klient sender JWT-token i Authorization-header
2. **Token-validering**: Token valideres mot Texas-endepunkt
3. **Brukerdata**: NAV-ident, e-post og navn ekstraheres fra token
(se nais docs for mer)


## 👨‍💻 Utvikling

### Prosjektstruktur
```
src/main/kotlin/no/nav/
├── Main.kt                 # Hovedklasse og applikasjonskonfigurasjon
├── api/                    # HTTP-ruter og kontrollere
│   ├── brukerRute/        # Brukerrelaterte endepunkter
│   ├── gjennomforing/     # Gjennomføringsendepunkter
│   ├── UkeAntallRecord/   # Ukentlig statistikk
│   └── status/            # Helsesjekk-endepunkter
├── auth/                  # Autentisering og autorisasjon
├── config/                # Konfigurasjonsklasser
├── models/                # Datamodeller og DTOs
├── service/               # Forretningslogikk
└── utils/                 # Hjelpefunksjoner
```



## 🆘 Feilsøking og se logg
```bash
# Følg logger i utvikling
nais login
kubectl logs -fl app=brum 
