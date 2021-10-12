package me.whypie.model

import com.auth0.jwt.interfaces.Claim
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.whypie.model.vo.ClaimKey
import me.whypie.model.vo.Principal

data class VerifyResult(
    val issuer: String,
    val subject: String,
    val audience: String,
    val issuedAt: Long,
    val expiresAt: Long,
    val use: String,
    val principal: Principal
) {
    companion object {
        fun mapTo(claims: Map<String, Claim>): VerifyResult {
            return VerifyResult(
                issuer = claims[ClaimKey.ISS.value]!!.asString(),
                subject = claims[ClaimKey.SUB.value]!!.asString(),
                audience = claims[ClaimKey.AUD.value]!!.asString(),
                issuedAt = claims[ClaimKey.IAT.value]!!.asLong(),
                expiresAt = claims[ClaimKey.EXP.value]!!.asLong(),
                use = claims[ClaimKey.USE.value]!!.asString(),
                principal = jacksonObjectMapper().convertValue(
                    claims[ClaimKey.PRINCIPAL.value]!!.asMap(),
                    object : TypeReference<Principal>() {}),
            )
        }
    }
}