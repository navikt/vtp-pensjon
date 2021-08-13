package no.nav.pensjon.vtp.configuration.graphql.model

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import java.util.*

data class Foedsel(
    val foedselsaar: Int? = null,
    val foedselsdato: Date? = null,
    val foedeland: String? = null,
    val foedested: String? = null,
    val foedekommune: String? = null,
    val metadata: Metadata
)

class FoedselDataFetcher : DataFetcher<Foedsel> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Person>().foedsel
}

class FoedselsaarDataFetcher : DataFetcher<Int> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Foedsel>().foedselsaar
}

class FoedselsdatoDataFetcher : DataFetcher<Date> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Foedsel>().foedselsdato
}

class FoedelandDataFetcher : DataFetcher<String> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Foedsel>().foedeland
}

class FoedestadDataFetcher : DataFetcher<String> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Foedsel>().foedested
}

class FoedekommuneDataFetcher : DataFetcher<String> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Foedsel>().foedekommune
}

class FoedselMetadataDataFetcher : DataFetcher<Metadata> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Foedsel>().metadata
}
