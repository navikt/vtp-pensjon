package no.nav.pensjon.vtp.mocks.unleash

import org.springframework.data.repository.Repository

interface FeatureRepository : Repository<Feature, String> {
    fun findAll(): List<Feature>
    fun save(feature: Feature): Feature
    fun deleteByName(name: String)
}
