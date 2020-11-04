package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering;

import static java.util.Arrays.stream;

import static org.springframework.beans.PropertyAccessorFactory.forBeanPropertyAccess;

import org.springframework.beans.BeanWrapper;

class CountFieldsMatching {
    public static Long count(Object o, String[] fields) {
        final BeanWrapper beanWrapper = forBeanPropertyAccess(o);

        return stream(fields)
                .map(beanWrapper::getPropertyValue)
                .filter(CountFieldsMatching::isNotBlank)
                .count();
    }

    static boolean isNotBlank(final Object object) {
        return !isBlank(object);
    }

    static boolean isBlank(final Object object) {
        return object == null || object.toString().chars().allMatch(Character::isWhitespace);
    }
}
