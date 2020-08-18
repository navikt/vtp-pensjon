package no.nav.foreldrepenger.vtp.server.rest.azuread.navansatt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AzureADGroupMapping {
    private static final Logger LOG = LoggerFactory.getLogger(AzureADGroupMapping.class);
    
    private static final Map<String, String> GROUPS;
    static {
        GROUPS = new HashMap<>();
        GROUPS.put("0000-GA-PENSJON_SAKSBEHANDLER", "8bb9b8d1-f46a-4ade-8ee8-5895eccdf8cf");
        GROUPS.put("0000-GA-PENSJON_VEILEDER", "a3f91493-1ab8-4b64-a544-0a77cbba9240");
        GROUPS.put("0000-GA-PENSJON_UTVIDET", "b1e04468-a53a-48fb-a3f7-996c06c8c163");
        GROUPS.put("0000-GA-PENSJON_KODE6", "b5731451-5bf6-46c4-9a13-44a97460ea39");
        GROUPS.put("0000-GA-PENSJON_KODE7", "c8aa7671-a081-4db4-852f-c522da20958d");
        GROUPS.put("0000-GA-PENSJON_KLAGEBEH", "02505029-3f2e-48a9-b2a8-ff274dacca52");
        GROUPS.put("0000-GA-PENSJON_BRUKERHJELPA", "9e122ddd-e542-4cc1-a7eb-92c99140fe0e");
        GROUPS.put("0000-GA-PENSJON_BEGRENSET_VEILEDER", "d88b4c51-79a3-4823-8f26-9c888d3264c6");
        GROUPS.put("0000-GA-PENSJON_OKONOMI", "05ae245b-7b47-428a-9074-4ff6b59eddbc");
        GROUPS.put("0000-GA-PENSJON_UTLAND", "bda6bd68-77e6-4c00-96b9-7ecf5df7413c");
        GROUPS.put("0000-GA-PENSJON_NDU", "45095575-4a2b-4784-8f5d-c4ccfc9192ea");
        GROUPS.put("0000-GA-PENSJON_FULLMAKTADM", "70e57df1-416b-40b3-b78d-3bda82f7e1bd");
        GROUPS.put("0000-GA-GOSYS_PERSONOPPLYSNING_ENDRER", "42f313b8-d888-4344-86d6-adcaea4ed381");
        GROUPS.put("0000-GA-PENSJON_SPESIAL", "cd74a6e7-5c09-4e1a-b875-4777278185f4");
        GROUPS.put("0000-GA-PENSJON_SPESIAL_PGI", "8882e483-cab3-4cc1-a09b-cf362002929e");
        GROUPS.put("0000-GA-PENSJON_SPESIAL_BEREGNING_RESTPENSJON_PENSJONSBEHOLDNING", "2797a4ab-b0d2-472a-a741-2b70705acca0");
        GROUPS.put("0000-GA-PENSJON_ATTESTERING", "63f46f74-84a8-4d1c-87a8-78532ab3ae60");
        GROUPS.put("0000-GA-PENSJON_NASJONAL_M_LOGG", "753805ea-65a7-4855-bdc3-e6130348df9f");
        GROUPS.put("0000-GA-PENSJON_NASJONAL_U_LOGG", "ea7411eb-8b48-41a0-bc56-7b521fbf0c25");
        GROUPS.put("0000-GA-PENSJON_UFORE", "b0cc96b6-f9b3-4af9-b699-69a99e8a2d18");
        GROUPS.put("0000-GA-PESYS_CHROME", "10731768-a28c-4b35-b2a4-8e59fdba73e3");
    }
    
    static String toAzureGroupId(String ldapGroupName) {
        if (GROUPS.containsKey(ldapGroupName)) {
            return GROUPS.get(ldapGroupName);
        } else {
            String errorMessage = "error: unknown Azure group ID for group " + ldapGroupName;
            LOG.error(errorMessage);
            return errorMessage;
        }
    }
}
