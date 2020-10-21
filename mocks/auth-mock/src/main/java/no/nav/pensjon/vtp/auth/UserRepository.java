package no.nav.pensjon.vtp.auth;

import java.util.ArrayList;
import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;

public class UserRepository {
    private final InitialDirContext ctx;
    private final LdapName base;

    public UserRepository(InitialDirContext ctx, LdapName ldapName) {
        this.ctx = ctx;
        this.base = ldapName;
    }

    public static LdapName defaultLdapName() throws InvalidNameException {
        return new LdapName("ou=NAV,ou=BusinessUnits,dc=test,dc=local");
    }

    public List<SearchResult> getAllUsers() {
        try {
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            controls.setCountLimit(50);

            NamingEnumeration<SearchResult> result = ctx.search(base, "cn=*", controls);

            List<SearchResult> usernames = new ArrayList<>();

            while (result.hasMore()) {
                usernames.add(result.next());
            }
            return usernames;
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
