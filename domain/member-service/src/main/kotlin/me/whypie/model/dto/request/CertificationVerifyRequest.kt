package me.whypie.model.dto.request

import javax.validation.constraints.Digits

/**
 * @author : choi-ys
 * @date : 2021-10-04 오전 7:00
 */
data class CertificationVerifyRequest(
    @field:Digits(integer = 6, fraction = 0, message = "인증번호 6자리를 입력해주세요")
    val certificationNumber: Int,
)