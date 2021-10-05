package me.whypie.model.dto.request

/**
 * @author : choi-ys
 * @date : 2021-10-04 오전 7:00
 */
data class CertificationVerifyRequest(
    val id: Long,
    val email: String,
    val certificationNumber: Int
)