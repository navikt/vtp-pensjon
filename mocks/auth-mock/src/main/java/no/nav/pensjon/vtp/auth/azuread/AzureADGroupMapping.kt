package no.nav.pensjon.vtp.auth.azuread

import org.slf4j.LoggerFactory

object AzureADGroupMapping

private val LOG = LoggerFactory.getLogger(AzureADGroupMapping::class.java)

private val GROUPS = mapOf(
    "0000-GA-PENSJON_SAKSBEHANDLER" to "8bb9b8d1-f46a-4ade-8ee8-5895eccdf8cf",
    "0000-GA-PENSJON_VEILEDER" to "a3f91493-1ab8-4b64-a544-0a77cbba9240",
    "0000-GA-PENSJON_UTVIDET" to "b1e04468-a53a-48fb-a3f7-996c06c8c163",
    "0000-GA-PENSJON_KODE6" to "b5731451-5bf6-46c4-9a13-44a97460ea39",
    "0000-GA-PENSJON_KODE7" to "c8aa7671-a081-4db4-852f-c522da20958d",
    "0000-GA-PENSJON_KLAGEBEH" to "02505029-3f2e-48a9-b2a8-ff274dacca52",
    "0000-GA-PENSJON_BRUKERHJELPA" to "9e122ddd-e542-4cc1-a7eb-92c99140fe0e",
    "0000-GA-PENSJON_BEGRENSET_VEILEDER" to "d88b4c51-79a3-4823-8f26-9c888d3264c6",
    "0000-GA-PENSJON_OKONOMI" to "05ae245b-7b47-428a-9074-4ff6b59eddbc",
    "0000-GA-PENSJON_UTLAND" to "bda6bd68-77e6-4c00-96b9-7ecf5df7413c",
    "0000-GA-PENSJON_NDU" to "45095575-4a2b-4784-8f5d-c4ccfc9192ea",
    "0000-GA-PENSJON_FULLMAKTADM" to "70e57df1-416b-40b3-b78d-3bda82f7e1bd",
    "0000-GA-GOSYS_PERSONOPPLYSNING_ENDRER" to "42f313b8-d888-4344-86d6-adcaea4ed381",
    "0000-GA-PENSJON_SPESIAL" to "cd74a6e7-5c09-4e1a-b875-4777278185f4",
    "0000-GA-PENSJON_SPESIAL_PGI" to "8882e483-cab3-4cc1-a09b-cf362002929e",
    "0000-GA-PENSJON_SPESIAL_BEREGNING_RESTPENSJON_PENSJONSBEHOLDNING" to "2797a4ab-b0d2-472a-a741-2b70705acca0",
    "0000-GA-PENSJON_ATTESTERING" to "63f46f74-84a8-4d1c-87a8-78532ab3ae60",
    "0000-GA-PENSJON_NASJONAL_M_LOGG" to "753805ea-65a7-4855-bdc3-e6130348df9f",
    "0000-GA-PENSJON_NASJONAL_U_LOGG" to "ea7411eb-8b48-41a0-bc56-7b521fbf0c25",
    "0000-GA-PENSJON_UFORE" to "b0cc96b6-f9b3-4af9-b699-69a99e8a2d18",
    "0000-GA-PESYS_CHROME" to "10731768-a28c-4b35-b2a4-8e59fdba73e3",
)

fun toAzureGroupId(ldapGroupName: String): String {
    return GROUPS[ldapGroupName]
        ?: errorMessage(ldapGroupName)
}

private fun errorMessage(ldapGroupName: String): String {
    val errorMessage = "error: unknown Azure group ID for group $ldapGroupName"
    LOG.error(errorMessage)
    return errorMessage
}
