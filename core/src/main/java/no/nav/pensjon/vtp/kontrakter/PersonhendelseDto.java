package no.nav.pensjon.vtp.kontrakter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = PersonhendelseDeserializer.class)
public interface PersonhendelseDto {

    String getType();
}
