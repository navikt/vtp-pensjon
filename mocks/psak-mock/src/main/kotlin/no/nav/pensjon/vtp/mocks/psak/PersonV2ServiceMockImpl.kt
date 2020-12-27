package no.nav.pensjon.vtp.mocks.psak

import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson
import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.virksomhet.part.person.v2.Diskresjonskode
import no.nav.virksomhet.part.person.v2.Navn
import no.nav.virksomhet.part.person.v2.PersonIdent
import no.nav.virksomhet.part.person.v2.Personstatus
import no.nav.virksomhet.tjenester.person.meldinger.v2.HentPersonRequest
import no.nav.virksomhet.tjenester.person.meldinger.v2.HentPersonResponse
import no.nav.virksomhet.tjenester.person.meldinger.v2.HentUtenlandskIdentitetListeRequest
import no.nav.virksomhet.tjenester.person.meldinger.v2.RegistrereAdresseForDodsboRequest
import no.nav.virksomhet.tjenester.person.v2.HentPersonPersonIkkeFunnet
import no.nav.virksomhet.tjenester.person.v2.ObjectFactory
import no.nav.virksomhet.tjenester.person.v2.Person
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-tjeneste-person_v2Web/sca/PersonWSEXP"])
@WebService(targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2", name = "Person")
@HandlerChain(file = "/Handler-chain.xml")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.virksomhet.part.person.v2.ObjectFactory::class,
    no.nav.virksomhet.tjenester.person.meldinger.v2.ObjectFactory::class,
    no.nav.virksomhet.tjenester.person.feil.v2.ObjectFactory::class
)
class PersonV2ServiceMockImpl(private val psakpselvPersonAdapter: PsakpselvPersonAdapter) : Person {
    @WebMethod
    @RequestWrapper(
        localName = "registrereAdresseForDodsbo",
        targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2",
        className = "no.nav.virksomhet.tjenester.person.v2.RegistrereAdresseForDodsbo"
    )
    @ResponseWrapper(
        localName = "registrereAdresseForDodsboResponse",
        targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2",
        className = "no.nav.virksomhet.tjenester.person.v2.RegistrereAdresseForDodsboResponse"
    )
    override fun registrereAdresseForDodsbo(@WebParam(name = "request") registrereAdresseForDodsboRequest: RegistrereAdresseForDodsboRequest) =
        throw NotImplementedException()

    @WebMethod
    @RequestWrapper(
        localName = "hentPerson",
        targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2",
        className = "no.nav.virksomhet.tjenester.person.v2.HentPerson"
    )
    @ResponseWrapper(
        localName = "hentPersonResponse",
        targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2",
        className = "no.nav.virksomhet.tjenester.person.v2.HentPersonResponse"
    )
    @WebResult(name = "response")
    @Throws(
        HentPersonPersonIkkeFunnet::class
    )
    override fun hentPerson(@WebParam(name = "request") hentPersonRequest: HentPersonRequest) =
        HentPersonResponse().apply {
            person = psakpselvPersonAdapter.getASBOPenPerson(hentPersonRequest.ident)
                ?.asPersonV2()
                ?: throw HentPersonPersonIkkeFunnet()
        }

    @WebMethod
    @RequestWrapper(
        localName = "hentUtenlandskIdentitetListe",
        targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2",
        className = "no.nav.virksomhet.tjenester.person.v2.HentUtenlandskIdentitetListe"
    )
    @ResponseWrapper(
        localName = "hentUtenlandskIdentitetListeResponse",
        targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2",
        className = "no.nav.virksomhet.tjenester.person.v2.HentUtenlandskIdentitetListeResponse"
    )
    @WebResult(name = "response")
    override fun hentUtenlandskIdentitetListe(@WebParam(name = "request") hentUtenlandskIdentitetListeRequest: HentUtenlandskIdentitetListeRequest) =
        throw NotImplementedException()

    private fun ASBOPenPerson.asPersonV2(): no.nav.virksomhet.part.person.v2.Person {
        val asboPenPerson = this

        return no.nav.virksomhet.part.person.v2.Person().apply {
            personIdent = PersonIdent().apply {
                ident = asboPenPerson.fodselsnummer
            }

            personstatus = Personstatus().apply {
                kode = asboPenPerson.statusKode
                utvidelse = null
            }

            navn = Navn().apply {
                fornavn = asboPenPerson.fornavn
                etternavn = asboPenPerson.etternavn
                kortnavn = asboPenPerson.kortnavn
                mellomnavn = asboPenPerson.mellomnavn
            }

            diskresjonskode = Diskresjonskode().apply {
                kode = asboPenPerson.diskresjonskode
            }
        }
    }
}
