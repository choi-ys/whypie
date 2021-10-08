package me.whypie.component

import com.auth0.jwt.interfaces.Claim
import me.whypie.model.ClaimKey
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.stream.Collectors

data class VerifyResult(
    val issuer: String,
    val subject: String,
    val audience: String,
    val issuedAt: Long,
    val expiresAt: Long,
    val use: String,
    val username: String,
    val authorities: Set<SimpleGrantedAuthority>? = hashSetOf()
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
                username = claims[ClaimKey.USERNAME.value]!!.asString(),
                authorities = claims[ClaimKey.AUTHORITIES.value]?.let { it ->
                    it.asString().split(",")
                        .stream().map { SimpleGrantedAuthority(it) }
                        .collect(Collectors.toSet())
                }
            )
        }
    }
}