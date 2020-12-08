package no.nav.pensjon.vtp.testmodell.repo.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import no.nav.pensjon.vtp.testmodell.brev.BrevMetadataIndeks
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepository
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseModell

object BasisdataProviderFileImpl {
    val mapper: ObjectMapper = JsonMapper.builder().addModule(KotlinModule()).build()

    fun loadAdresser(): AdresseIndeks {
        val adresseIndeks = AdresseIndeks()
        val adresseMaler : Iterable<AdresseModell> = mapper.readValue(javaClass.getResource("/basedata/adresse-maler.json").openStream())
        adresseMaler.forEach(adresseIndeks::leggTil)
        return adresseIndeks
    }

    fun loadEnheter(): EnheterIndeks {
        val enheterIndeks = EnheterIndeks()
        enheterIndeks.leggTil(mapper.readValue(javaClass.getResource("/basedata/enheter.json").openStream()))
        return enheterIndeks
    }

    fun loadAnsatte(): AnsatteIndeks {
        val ansatteIndeks = AnsatteIndeks()
        ansatteIndeks.saveAll(mapper.readValue(javaClass.getResource("/basedata/navansatte.json").openStream()))
        return ansatteIndeks
    }

    fun loadOrganisasjoner(organisasjonRepository: OrganisasjonRepository) {
        organisasjonRepository.saveAll(mapper.readValue(javaClass.getResource("/basedata/organisasjon.json").openStream()))
    }

    fun loadBrevMetadata(): BrevMetadataIndeks {
        val brevMetadataIndeks = BrevMetadataIndeks()
        brevMetadataIndeks.brevMetadata = mapper.readValue(javaClass.getResource("/basedata/brevmetadata.json").openStream())
        return brevMetadataIndeks
    }
}
