package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering.rest;

public class ErrorResponse {
    private final String uuid;
    private final String feilmelding;

    public ErrorResponse(String uuid, String feilmelding) {
        this.uuid = uuid;
        this.feilmelding = feilmelding;
    }

    public String getFeilmelding() {
        return feilmelding;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "uuid='" + uuid + '\'' +
                ", feilmelding='" + feilmelding + '\'' +
                '}';
    }
}
