package no.nav.pensjon.vtp.mocks.virksomhet.person.v3

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.kodeverk.Rolle
import no.nav.pensjon.vtp.testmodell.personopplysning.*
import no.nav.tjeneste.virksomhet.person.v3.binding.HentGeografiskTilknytningPersonIkkeFunnet
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonPersonIkkeFunnet
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonhistorikkPersonIkkeFunnet
import no.nav.tjeneste.virksomhet.person.v3.binding.PersonV3
import no.nav.tjeneste.virksomhet.person.v3.feil.PersonIkkeFunnet
import no.nav.tjeneste.virksomhet.person.v3.informasjon.*
import no.nav.tjeneste.virksomhet.person.v3.meldinger.*
import javax.jws.*
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/tpsws/ws/Person/v3"])
@Addressing
@WebService(name = "Person_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3")
@HandlerChain(file = "/Handler-chain.xml")
class PersonServiceMockImpl(
    private val personModellRepository: PersonModellRepository,
    private val personIndeks: PersonIndeks
) : PersonV3 {
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentPerson",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentPerson"
    )
    @ResponseWrapper(
        localName = "hentPersonResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonResponse"
    )
    @Throws(
        HentPersonPersonIkkeFunnet::class
    )
    override fun hentPerson(@WebParam(name = "request") hentPersonRequest: HentPersonRequest): HentPersonResponse {
        val bruker = finnPerson(hentPersonRequest.aktoer)

        val personopplysninger = personIndeks.findById(bruker.ident)
            ?: throw HentPersonPersonIkkeFunnet(
                "Person med ident " + bruker.ident + " ikke funnet",
                PersonIkkeFunnet().withFeilmelding("Person med ident " + bruker.ident + " ikke funnet")
            )

        val erBarnet = personopplysninger.familierelasjoner
            .any { familierelasjon: FamilierelasjonModell ->
                familierelasjon.rolle == Rolle.BARN &&
                    personModellRepository.findById(familierelasjon.til)
                    ?.let { it.aktørIdent == bruker.aktørIdent }
                    ?: throw RuntimeException("Unable to locate child with ident $familierelasjon.til")
            }

        val person = fra(bruker)

        val response = HentPersonResponse()

        person.harFraRolleI +=
            when {
                // TODO HACK for annenpart (annenpart burde ha en egen personopplysning fil eller liknende)
                personopplysninger.annenPart?.let { it.aktørIdent == bruker.aktørIdent } == true -> {
                    tilFamilerelasjon(personopplysninger.familierelasjonerAnnenPart)
                }
                // TODO HACK Familierelasjon for barnet
                erBarnet -> {
                    tilFamilerelasjon(personopplysninger.familierelasjonerBarn)
                }
                else -> {
                    tilFamilerelasjon(personopplysninger.familierelasjoner)
                }
            }

        response.person = person
        return response
    }

    fun tilFamilerelasjon(relasjoner: Collection<FamilierelasjonModell>): List<Familierelasjon> =
        tilFamilerelasjon2(
            relasjoner.map {
                Pair(
                    it,
                    personModellRepository.findById(it.til)
                        ?: throw java.lang.RuntimeException("Unable to locate persion with ident " + it.til)
                )
            }
        )

    fun finnPerson(aktoer: Aktoer) = when (aktoer) {
        is PersonIdent -> {
            personModellRepository.findById(aktoer.ident.ident)
                ?: throw HentPersonPersonIkkeFunnet(
                    "BrukerModell ikke funnet:${aktoer.ident.ident}",
                    PersonIkkeFunnet()
                )
        }
        is AktoerId -> {
            personModellRepository.findByAktørIdent(aktoer.aktoerId)
                ?: throw HentPersonPersonIkkeFunnet(
                    "BrukerModell ikke funnet:${aktoer.aktoerId}",
                    PersonIkkeFunnet()
                )
        }
        else -> {
            throw HentPersonPersonIkkeFunnet(
                "Usupportert ident type ${aktoer.javaClass.simpleName}",
                PersonIkkeFunnet()
            )
        }
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentGeografiskTilknytningRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentGeografiskTilknytning",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentGeografiskTilknytning"
    )
    @ResponseWrapper(
        localName = "hentGeografiskTilknytningResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentGeografiskTilknytningResponse"
    )
    @Throws(
        HentGeografiskTilknytningPersonIkkeFunnet::class
    )
    override fun hentGeografiskTilknytning(@WebParam(name = "request") hentGeografiskTilknytningRequest: HentGeografiskTilknytningRequest) =
        try {
            val bruker = finnPerson(hentGeografiskTilknytningRequest.aktoer)
            HentGeografiskTilknytningResponse().apply {
                diskresjonskode = bruker.diskresjonskode?.let { tilDiskresjonskode(it) }
                geografiskTilknytning = tilGeografiskTilknytning(bruker)
            }
        } catch (e: HentPersonPersonIkkeFunnet) {
            throw HentGeografiskTilknytningPersonIkkeFunnet(e.message, PersonIkkeFunnet())
        }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentVergeRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentVerge",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentVerge"
    )
    @ResponseWrapper(
        localName = "hentVergeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentVergeResponse"
    )
    override fun hentVerge(@WebParam(name = "request") var1: HentVergeRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentEkteskapshistorikkRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentEkteskapshistorikk",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentEkteskapshistorikk"
    )
    @ResponseWrapper(
        localName = "hentEkteskapshistorikkResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentEkteskapshistorikkResponse"
    )
    override fun hentEkteskapshistorikk(@WebParam(name = "request") hentEkteskapshistorikkRequest: HentEkteskapshistorikkRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonerMedSammeAdresseRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentPersonerMedSammeAdresse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonerMedSammeAdresse"
    )
    @ResponseWrapper(
        localName = "hentPersonerMedSammeAdresseResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonerMedSammeAdresseResponse"
    )
    override fun hentPersonerMedSammeAdresse(@WebParam(name = "request") var1: HentPersonerMedSammeAdresseRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonhistorikkRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentPersonhistorikk",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonhistorikk"
    )
    @ResponseWrapper(
        localName = "hentPersonhistorikkResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonhistorikkResponse"
    )
    override fun hentPersonhistorikk(@WebParam(name = "request") hentPersonhistorikkRequest: HentPersonhistorikkRequest): HentPersonhistorikkResponse =
        try {
            val bruker: PersonModell = finnPerson(hentPersonhistorikkRequest.aktoer)

            HentPersonhistorikkResponse()
                .withAktoer(hentPersonhistorikkRequest.aktoer)
                .withPersonstatusListe(hentPersonstatusPerioder(bruker))
                .withBostedsadressePeriodeListe(hentBostedadressePerioder(bruker))
                .withStatsborgerskapListe(hentStatsborgerskapPerioder(bruker))
        } catch (e: HentPersonPersonIkkeFunnet) {
            throw HentPersonhistorikkPersonIkkeFunnet(e.message, PersonIkkeFunnet())
        }

    private fun hentBostedadressePerioder(bruker: PersonModell): List<BostedsadressePeriode> {
        return fra(bruker.getAdresser(AdresseType.BOSTEDSADRESSE))
    }

    private fun hentStatsborgerskapPerioder(bruker: PersonModell) =
        statsborgerskap(bruker.statsborgerskap ?: emptyList())

    private fun hentPersonstatusPerioder(bruker: PersonModell) =
        statsborgerskap(bruker.personstatus ?: emptyList())

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.PingResponse"
    )
    override fun ping() = Unit

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonnavnBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentPersonnavnBolk",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonnavnBolk"
    )
    @ResponseWrapper(
        localName = "hentPersonnavnBolkResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonnavnBolkResponse"
    )
    override fun hentPersonnavnBolk(@WebParam(name = "request") var1: HentPersonnavnBolkRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentSikkerhetstiltakRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentSikkerhetstiltak",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentSikkerhetstiltak"
    )
    @ResponseWrapper(
        localName = "hentSikkerhetstiltakResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
        className = "no.nav.tjeneste.virksomhet.person.v3.HentSikkerhetstiltakResponse"
    )
    override fun hentSikkerhetstiltak(@WebParam(name = "request") var1: HentSikkerhetstiltakRequest) =
        throw NotImplementedException()
}
