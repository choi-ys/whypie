package me.whypie.component

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import me.whypie.model.vo.ClaimKey
import me.whypie.model.vo.Principal
import me.whypie.model.vo.Token
import me.whypie.model.vo.TokenType
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021-10-07 오후 10:34
 */
@Service
class TokenProvider : InitializingBean {

    @Value("\${jwt.signature}")
    private val SIGNATURE: String? = null
    private var ALGORITHM: Algorithm? = null

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

    override fun afterPropertiesSet() {
        ALGORITHM = Algorithm.HMAC256(SIGNATURE)
    }

    fun createToken(principal: Principal): Token {
        val currentTimeMillis = System.currentTimeMillis()
        val accessExpired = Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM!! * 1000)
        val refreshExpired = Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_IN_SECONDS_TERM!! * 1000)
        val accessToken: String = tokenBuilder(currentTimeMillis, TokenType.ACCESS, principal)
        val refreshToken: String = tokenBuilder(currentTimeMillis, TokenType.REFRESH, principal)
        return Token(accessToken, refreshToken, accessExpired, refreshExpired)
    }

    private fun tokenBuilder(currentTimeMillis: Long, tokenType: TokenType, principal: Principal): String {
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
            .sign(ALGORITHM)
    }

}