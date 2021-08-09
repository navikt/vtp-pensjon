package no.nav.pensjon.vtp.configuration.graphql.model

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment

data class Metadata(
    val historisk: Boolean,
    val master: String
)

class HistoriskMetadataDataFetcher : DataFetcher<Boolean> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Metadata>().historisk
}

class MasterMetadataDataFetcher : DataFetcher<String> {
    override fun get(env: DataFetchingEnvironment) = env.getSource<Metadata>().master
}
