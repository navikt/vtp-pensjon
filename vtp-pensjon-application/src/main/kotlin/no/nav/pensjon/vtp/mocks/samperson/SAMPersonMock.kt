package no.nav.pensjon.vtp.mocks.samperson

import no.nav.inf.HentPersonFaultStoGeneriskMsg
import no.nav.inf.HentPersonFaultStoPersonIkkeFunnetMsg
import no.nav.inf.ObjectFactory
import no.nav.inf.SAMPerson
import no.nav.lib.sto.sam.asbo.ASBOStoPerson
import no.nav.lib.sto.sam.asbo.person.*
import no.nav.lib.sto.sam.fault.FaultStoGenerisk
import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import no.nav.pensjon.vtp.util.asGregorianCalendar
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-sto-sam-personWeb/sca/SAMPersonWSEXP"])
@WebService(targetNamespace = "http://nav-cons-sto-sam-person/no/nav/inf", name = "SAMPerson")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.lib.sto.sam.asbo.person.ObjectFactory::class,
    no.nav.lib.sto.sam.asbo.ObjectFactory::class,
    no.nav.lib.sto.sam.fault.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class SAMPersonMock(
    private val personModellRepository: PersonModellRepository
) : SAMPerson {
    @WebMethod
    @RequestWrapper(
        localName = "hentPerson",
        targetNamespace = "http://nav-cons-sto-sam-person/no/nav/inf",
        className = "no.nav.inf.HentPerson"
    )
    @ResponseWrapper(
        localName = "hentPersonResponse",
        targetNamespace = "http://nav-cons-sto-sam-person/no/nav/inf",
        className = "no.nav.inf.HentPersonResponse"
    )
    @WebResult(name = "hentPersonResponse", targetNamespace = "")
    override fun hentPerson(
        @WebParam(name = "hentPersonRequest", targetNamespace = "")
        hentPersonRequest: ASBOStoHentPersonRequest?
    ): ASBOStoPerson {
        return getASBOPenPerson(hentPersonRequest?.fnr ?: throw HentPersonFaultStoGeneriskMsg("Missing `fnr` param", FaultStoGenerisk()))
            ?: throw HentPersonFaultStoPersonIkkeFunnetMsg("Person with fnr=${hentPersonRequest.fnr} not found")
    }

    fun getASBOPenPerson(fodselsnummer: String): ASBOStoPerson? {
        return personModellRepository.findById(fodselsnummer)
            ?.let {
                ASBOStoPerson().apply {
                    fnr = it.ident

                    kortnavn = null
                    fornavn = it.fornavn
                    mellomnavn = null
                    etternavn = it.etternavn
                    statusKode = null
                    status = null
                    diskresjonskode = it.diskresjonskode?.name ?: ""

                    dodsdato = it.dødsdato?.asGregorianCalendar()

                    umyndiggjortDato = null
                    sivilstand = null

                    sivilstandDato = null
                    tlfPrivat = null
                    tlfJobb = null
                    tlfMobil = null
                    epost = null
                    sprakKode = null
                    sprakBeskrivelse = null

                    sprakDatoFom = null
                    navEnhet = null
                    erEgenansatt = it.egenansatt
                    bostedsAdresse = null
                    postAdresse = null
                    tilleggsAdresse = null
                    utenlandsAdresse = null
                    utbetalingsinformasjon = null
                    personUtland = null
                    brukerprofil = null
                }
            }
    }
}
