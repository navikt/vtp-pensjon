package no.nav.pensjon.vtp.mocks.tps.proxy.api.v1.innsyn.dto;

/*
 * TPS-PROXY API
 * <h4>Api for Tps-Proxy</h4><a href=\"https://confluence.adeo.no/display/FEL/TPS+-+Tjeneste+MQ+S301+-+Hent+Innsynsopplysninger+for+en+person\">Confluence for tps innsyn</a>
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * Informasjon om postadresse
 */
@ApiModel(description = "Informasjon om postadresse")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2019-11-27T10:31:32.432+01:00")
public class Postadresse   {
    @JsonProperty("adresse1")
    private String adresse1 = null;

    @JsonProperty("adresse2")
    private String adresse2 = null;

    @JsonProperty("adresse3")
    private String adresse3 = null;

    @JsonProperty("datoFraOgMed")
    private String datoFraOgMed = null;

    @JsonProperty("kilde")
    private String kilde = null;

    @JsonProperty("land")
    private String land = null;

    @JsonProperty("postnummer")
    private String postnummer = null;

    public Postadresse adresse1(String adresse1) {
        this.adresse1 = adresse1;
        return this;
    }

    /**
     * Adresse1
     * @return adresse1
     **/
    @JsonProperty("adresse1")
    @ApiModelProperty(example = "8 TEST ROAD", value = "Adresse1")
    public String getAdresse1() {
        return adresse1;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public Postadresse adresse2(String adresse2) {
        this.adresse2 = adresse2;
        return this;
    }

    /**
     * Adresse2
     * @return adresse2
     **/
    @JsonProperty("adresse2")
    @ApiModelProperty(example = "TESTBURGH", value = "Adresse2")
    public String getAdresse2() {
        return adresse2;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public Postadresse adresse3(String adresse3) {
        this.adresse3 = adresse3;
        return this;
    }

    /**
     * Adresse3
     * @return adresse3
     **/
    @JsonProperty("adresse3")
    @ApiModelProperty(example = "EH12 8RP TEST, UK", value = "Adresse3")
    public String getAdresse3() {
        return adresse3;
    }

    public void setAdresse3(String adresse3) {
        this.adresse3 = adresse3;
    }

    public Postadresse datoFraOgMed(String datoFraOgMed) {
        this.datoFraOgMed = datoFraOgMed;
        return this;
    }

    /**
     * Dato gyldig, format (ISO-8601): yyyy-MM-dd
     * @return datoFraOgMed
     **/
    @JsonProperty("datoFraOgMed")
    @ApiModelProperty(example = "2015-12-15", value = "Dato gyldig, format (ISO-8601): yyyy-MM-dd")
    public String getDatoFraOgMed() {
        return datoFraOgMed;
    }

    public void setDatoFraOgMed(String datoFraOgMed) {
        this.datoFraOgMed = datoFraOgMed;
    }

    public Postadresse kilde(String kilde) {
        this.kilde = kilde;
        return this;
    }

    /**
     * Get kilde
     * @return kilde
     **/
    @JsonProperty("kilde")
    @ApiModelProperty(value = "")
    public String getKilde() {
        return kilde;
    }

    public void setKilde(String kilde) {
        this.kilde = kilde;
    }

    public Postadresse land(String land) {
        this.land = land;
        return this;
    }

    /**
     * Land
     * @return land
     **/
    @JsonProperty("land")
    @ApiModelProperty(example = "GBR", value = "Land")
    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public Postadresse postnummer(String postnummer) {
        this.postnummer = postnummer;
        return this;
    }

    /**
     * Postnummer
     * @return postnummer
     **/
    @JsonProperty("postnummer")
    @ApiModelProperty(example = "9521", value = "Postnummer")
    public String getPostnummer() {
        return postnummer;
    }

    public void setPostnummer(String postnummer) {
        this.postnummer = postnummer;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Postadresse postadresse = (Postadresse) o;
        return Objects.equals(this.adresse1, postadresse.adresse1) &&
                Objects.equals(this.adresse2, postadresse.adresse2) &&
                Objects.equals(this.adresse3, postadresse.adresse3) &&
                Objects.equals(this.datoFraOgMed, postadresse.datoFraOgMed) &&
                Objects.equals(this.kilde, postadresse.kilde) &&
                Objects.equals(this.land, postadresse.land) &&
                Objects.equals(this.postnummer, postadresse.postnummer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adresse1, adresse2, adresse3, datoFraOgMed, kilde, land, postnummer);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Postadresse {\n");

        sb.append("    adresse1: ").append(toIndentedString(adresse1)).append("\n");
        sb.append("    adresse2: ").append(toIndentedString(adresse2)).append("\n");
        sb.append("    adresse3: ").append(toIndentedString(adresse3)).append("\n");
        sb.append("    datoFraOgMed: ").append(toIndentedString(datoFraOgMed)).append("\n");
        sb.append("    kilde: ").append(toIndentedString(kilde)).append("\n");
        sb.append("    land: ").append(toIndentedString(land)).append("\n");
        sb.append("    postnummer: ").append(toIndentedString(postnummer)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
