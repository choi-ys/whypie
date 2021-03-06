package me.whypie.model.vo

/**
 * @author : choi-ys
 * @date : 2021-10-07 오후 11:30
 */
enum class ClaimKey(val value: String) {
    ISS("iss"),
    SUB("sub"),
    AUD("aud"),
    IAT("iat"),
    EXP("exp"),
    USE("use"),
    PRINCIPAL("principal");
}
