package no.nav.pensjon.vtp.testmodell.util;

import no.nav.pensjon.vtp.testmodell.personopplysning.AnnenPartModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonNavn;
import no.nav.pensjon.vtp.testmodell.personopplysning.SivilstandModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.SøkerModell;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class TestdataUtil {
    static public LocalDate generateRandomPlausibleBirtdayParent() {
        LocalDate startRange = LocalDate.of(1960, 1, 1);
        LocalDate endRange = LocalDate.of(2000, 1, 1);

        long startEpoch = startRange.toEpochDay();
        long endEpoch = endRange.toEpochDay();

        Random random = new Random();

        long randomEpochDay = startEpoch + (long) (random.nextDouble() * endEpoch - startEpoch);

        LocalDate randomLocalDate = LocalDate.ofEpochDay(randomEpochDay);

        return randomLocalDate;

    }

    static public LocalDate generateBirtdayBetweenYears(int start, int end) {
        LocalDate startRange = LocalDate.of(start, 1, 1);
        LocalDate endRange = LocalDate.of(end, 12, 31);

        long startEpoch = startRange.toEpochDay();
        long endEpoch = endRange.toEpochDay();

        Random random = new Random();

        long randomEpochDay = random.longs(startEpoch, endEpoch).findFirst().orElse(0L);

        return LocalDate.ofEpochDay(randomEpochDay);

    }

    static public LocalDate generateBirthdateNowMinusThreeYears() {

        LocalDate endRange = LocalDateTime.now().toLocalDate();
        LocalDate startRange = endRange.minus(3, ChronoUnit.YEARS);

        long startEpoch = startRange.toEpochDay();
        long endEpoch = endRange.toEpochDay();

        Random random = new Random();

        long randomEpochDay = startEpoch + (long) (random.nextDouble() * (endEpoch - startEpoch));

        LocalDate randomLocalDate = LocalDate.ofEpochDay(randomEpochDay);

        return randomLocalDate;
    }

    static public LocalDate generateBirthdayYoungerThanSixMonths() {
        //Note: this actually returns younger than five months, but ok since it's also younger than six months...
        LocalDate endRange = LocalDateTime.now().toLocalDate();
        LocalDate startRange = endRange.minus(5, ChronoUnit.MONTHS);

        long startEpoch = startRange.toEpochDay();
        long endEpoch = endRange.toEpochDay();

        Random random = new Random();

        long randomEpochDay = startEpoch + (long) (random.nextDouble() * (endEpoch - startEpoch));

        LocalDate randomLocalDate = LocalDate.ofEpochDay(randomEpochDay);

        return randomLocalDate;
    }

    static public PersonNavn getSokerName(SøkerModell søker) {
        PersonNavn personNavn;
        if (søker.getKjønn().equals(BrukerModell.Kjønn.K)) {
            personNavn = FiktivtNavn.getRandomFemaleName();
        } else {
            personNavn = FiktivtNavn.getRandomMaleName();
        }
        return personNavn;
    }

    /**
     *  Tar høyde for at gifte folk deler etternavn.
     */
    static public PersonNavn getAnnenPartName(SøkerModell søker, AnnenPartModell annenPart) {
        PersonNavn personNavn;
        if (søker.getSivilstand().getKode().equals(SivilstandModell.Sivilstander.GIFT)) {
            if (annenPart.getKjønn().equals(BrukerModell.Kjønn.K)) {
                personNavn = FiktivtNavn.getRandomFemaleName(søker.getEtternavn());
            } else {
                personNavn = FiktivtNavn.getRandomMaleName(søker.getEtternavn());
            }
        } else {
            if (annenPart.getKjønn().equals(BrukerModell.Kjønn.K)) {
                personNavn = FiktivtNavn.getRandomFemaleName();
            } else {
                personNavn = FiktivtNavn.getRandomMaleName();
            }
        }
        return personNavn;
    }
}
