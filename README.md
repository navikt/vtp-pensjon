Virtuell Tjeneste Plattform (VTP) for pensjonsområdet
=================================

![alt text](vtp.png "VTP Logo")


#### vtp-pensjon hjelper deg med å: 
- virtualisere grensesnitt rundt applikasjonene brukt i pensjonsområdet. 
- instansierer og holde testdata konsistente på tvers av grensesnittene
- ha en plattform for å simulere grensesnitt over REST, SOAP, LDAP.
- sikkerhetshåndtering over OAuth2
- REST-grensesnitt for å programatisk opprette testdata (for automatiske tester) 
- GUI for å opprette testdata for mennesker.

## Henvendelser
- Team Pensjon

## For NAV-ansatte
Interne henvendelser kan sendes via Slack i kanalen #vtp-chatten

## Teknologi som må installeres
- Java 11 (https://adoptopenjdk.net/)
- Node.js 14 (https://nodejs.org/en/download/)
- Maven 3.6 (http://maven.apache.org/)

## Starte applikasjon
For utvikling på vtp-pensjon benytt oppsett for å starte server gjennom IDE.
I verdikjedetester benyttes oftest Docker-image av vtp-pensjon. Dette bygges i vtp-pensjon sin pipeline. 

#### Starte backend-server via IDE
* IntelliJ naviger til klassen VtpPensjonApplication og start main-metoden

#### Kjøre via docker run / docker-compose
##### For å bygge docker image lokalt: 
Bygg prosjektet med `mvn clean install`, bygg deretter docker-imaget med `docker build -t vtp-pensjon .` 
Imaget blir da tilgjengelig som vtp-pensjon:latest

##### Hente docker-image bygget i pipe: 
docker pull docker.pkg.github.com/navikt/vtp-pensjon/vtp-pensjon

#### Utvikle front-end separat
I modulen frontend (/frontend)
* installer avhengigheter med `npm install`
* Bygg og kjør utvikling med `npm run dev`.

## Opprette testdata 
* Opprett testdata ved å legge scenario i /model/scenarios. Innledende tall brukes som referanse for å få instansiert scenario.
* REST API dokumentasjon ligger på [Swagger UI](http://localhost:8060/rest/swagger-ui/index.html)  (Bruk HTTP for kall)


## Sertifikater for SSL/TLS
For lokal utvikling på Team Pensjon bruker vi key- og truststore som hentes ved oppstart fra prosjekt-mappen. 
Kopien av vtp sin public-nøkkelen er importert inn i PEN og POPP sine lokale truststores.
For å generere et nytt gyldig nøkkelpar og   se ytterligere instruksjoner, kjør `KeyStoreTool.main`.

## Konfigurere nye tjenester
Se SoapWebServerConfig for liste over url til genererte wsdl'er. Nye webtjenester registreres her. 
Se ApplicationConfig for liste over registrerte REST-tjenester. Nye REST-tjenester registreres her.     


## Verbose logging

Backend støtter verbose logging av HTTP spørringer. Dette slås på ved å sette `verbose=true`. Dette kan
gjøres ved å legge til `-Dverbose=true` i VM Options.
