package no.nav.pensjon.vtp.configuration.graphql.model

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import java.util.Date

data class Navn(
    val fornavn: String,
    val mellomnavn: String?,
    val etternavn: String,
    val forkortetNavn: String?,
    val gyldigFraOgMed: Date?,
    val metadata: Metadata
)

class NavnDataFetcher : DataFetcher<Navn> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Person>().navn
}

class FornavnDataFetcher : DataFetcher<String> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Navn>().fornavn
}

class MellomnavnDataFetcher : DataFetcher<String> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Navn>().mellomnavn
}

class EtternavnDataFetcher : DataFetcher<String> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Navn>().etternavn
}

class ForkortetNavnDataFetcher : DataFetcher<String> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Navn>().forkortetNavn
}

class GyldigFraOgMedNavnDataFetcher : DataFetcher<Date> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Navn>().gyldigFraOgMed
}

class NavnMetadataDataFetcher : DataFetcher<Metadata> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Navn>().metadata
}
