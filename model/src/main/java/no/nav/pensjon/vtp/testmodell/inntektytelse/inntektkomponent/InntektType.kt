package no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent

/*
Ref: https://app-t11.adeo.no/inntektstub/api/v1/kodeverk/Inntektstype
[
{"navn":"Loennsinntekt","term":"Lønnsinntekt"},
{"navn":"Naeringsinntekt","term":"Næringsinntekt"},
{"navn":"PensjonEllerTrygd","term":"Pensjon eller trygd"},
{"navn":"YtelseFraOffentlige","term":"Ytelse fra offentlige"}]
 */
enum class InntektType {
    Lønnsinntekt,
    Næringsinntekt,
    PensjonEllerTrygd,
    YtelseFraOffentlige,
}
