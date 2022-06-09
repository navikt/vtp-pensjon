package no.nav.pensjon.vtp.mocks.saf.graphql

data class GraphQLRequest(val query: String, val variables: Map<String, Any>)