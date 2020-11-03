package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering;

import org.apache.commons.beanutils.BeanUtils;

import java.util.Arrays;

class CountFieldsMatching {
    private CountFieldsMatching() {
    } //No instantiation

    static Long count(Object o, String[] fields) {
        return Arrays.stream(fields).filter(field -> {
            try {
                return isNotBlank(BeanUtils.getProperty(o, field));
            } catch (Exception e) {
                throw new IllegalStateException("Kunne ikke telle antall felter");
            }
        }).count();
    }

    public static boolean isNotBlank(final String string) {
        return !isBlank(string);
    }

    public static boolean isBlank(final String string) {
        return string.chars().allMatch(Character::isWhitespace);
    }
}
