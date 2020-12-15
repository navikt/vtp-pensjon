package no.nav.pensjon.vtp.felles;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.ZoneId;

import static java.util.GregorianCalendar.from;
import static javax.xml.datatype.DatatypeFactory.newInstance;

public interface ConversionUtils {
    static XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        try {
            return newInstance().newXMLGregorianCalendar(from(localDate.atStartOfDay(ZoneId.systemDefault())));
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
