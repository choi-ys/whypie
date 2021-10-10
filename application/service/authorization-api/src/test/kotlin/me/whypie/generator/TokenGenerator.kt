package me.whypie.generator

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Claim
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import me.whypie.component.TokenProvider
import me.whypie.component.VerifyResult
import me.whypie.model.entity.MemberRole
import me.whypie.model.vo.ClaimKey
import me.whypie.model.vo.Principal
import me.whypie.model.vo.Token
import me.whypie.model.vo.TokenType
import org.springframework.boot.test.context.TestComponent
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021-10-09 오후 5:21
 */
@TestComponent
class TokenGenerator(
    private val tokenProvider: TokenProvider
) {
    fun generateTokenMock(): Token {
        return tokenProvider.createToken(generatePrincipalMock())
    }

    fun generateTokenMock(principal: Principal): Token {
        return tokenProvider.createToken(principal)
    }

    companion object {
        val SIGNATURE = "test-case-signature"
        val ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM = 600L
        val USERNAME = "whypie"
        val ROLES = setOf(MemberRole.CERTIFIED_MEMBER)

        fun generatePrincipalMock(): Principal {
            return Principal(USERNAME, ROLES.joinToString(","))
        }

        fun generateJwtMock(): String {
            val currentTimeMillis = System.currentTimeMillis()
            val principal = Principal(USERNAME, ROLES.joinToString(","))

            return JWT.create()
                .withIssuer("ISSUER")
                .withSubject("SUBJECT")
                .withAudience("AUDIENCE")
                .withIssuedAt(Date(currentTimeMillis))
                .withExpiresAt(Date(currentTimeMillis + ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM * 1000))
                .withClaim(ClaimKey.USE.value, TokenType.ACCESS.name)
                .withClaim(ClaimKey.PRINCIPAL.value, ObjectMapper().convertValue(principal, object :
                    TypeReference<Map<String, Any>>() {}))
                .sign(Algorithm.HMAC256(SIGNATURE))
        }

        fun generateTokenMock(): Token {
            return Token(generateJwtMock(), generateJwtMock(), Date(), Date())
        }

        fun getVerifyResult(token: String): VerifyResult {
            return VerifyResult.mapTo(getClaims(token));
        }

        private fun getClaims(token: String): Map<String, Claim> {
            return JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token).claims
        }
    }
}