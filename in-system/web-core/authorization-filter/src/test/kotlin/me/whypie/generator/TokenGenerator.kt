package me.whypie.generator

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import me.whypie.model.vo.ClaimKey
import me.whypie.model.vo.Principal
import me.whypie.model.vo.TokenType
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestComponent
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021-10-13 오후 6:05
 */
@TestComponent
class TokenGenerator {

    @Value("\${jwt.signature}")
    private val SIGNATURE: String? = null

    @Value("\${jwt.issuer}")
    private val ISSUER: String? = null

    @Value("\${jwt.subject}")
    private val SUBJECT: String? = null

    @Value("\${jwt.audience}")
    private val AUDIENCE: String? = null

    @Value("\${jwt.access-token-validity-in-seconds-term}")
    private val ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM: Long? = null

    @Value("\${jwt.refresh-token-validity-in-seconds-term}")
    private val REFRESH_TOKEN_VALIDITY_IN_SECONDS_TERM: Long? = null

    fun <T> accessToken(identifier: String, set: Set<T>): String {
        val principal = Principal.mapTo(identifier, set)
        return generateToken(TokenType.ACCESS, principal)
    }

    fun generateToken(tokenType: TokenType, principal: Principal): String {
        val currentTimeMillis = System.currentTimeMillis()
        val tokenValidityInSecondsTerm =
            if (tokenType == TokenType.ACCESS) ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM!! else REFRESH_TOKEN_VALIDITY_IN_SECONDS_TERM!!
        return JWT.create()
            .withIssuer(ISSUER)
            .withSubject(SUBJECT)
            .withAudience(AUDIENCE)
            .withIssuedAt(Date(currentTimeMillis))
            .withExpiresAt(Date(currentTimeMillis + tokenValidityInSecondsTerm * 1000))
            .withClaim(ClaimKey.USE.value, TokenType.ACCESS.name)
            .withClaim(ClaimKey.PRINCIPAL.value, ObjectMapper().convertValue(principal, object :
                TypeReference<Map<String, Any>>() {}))
            .sign(Algorithm.HMAC256(SIGNATURE))
    }

    companion object {
        fun getBearerToken(token: String): String {
            return "Bearer $token"
        }
    }
}