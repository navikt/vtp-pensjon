package no.nav.pensjon.vtp.felles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import static java.util.Date.from;
import static java.util.GregorianCalendar.from;
import static javax.xml.datatype.DatatypeFactory.newInstance;

public interface ConversionUtils {
    static XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        try {
            final GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));

            return newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

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

    static LocalDate convertToLocalDate(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }

        return xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime().toLocalDate();
    }
}
