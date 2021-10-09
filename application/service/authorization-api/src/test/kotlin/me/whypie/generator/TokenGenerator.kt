package me.whypie.generator

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Claim
import me.whypie.component.TokenProvider
import me.whypie.component.VerifyResult
import me.whypie.model.entity.MemberRole
import me.whypie.model.vo.ClaimKey
import me.whypie.model.vo.Token
import me.whypie.model.vo.TokenType
import org.springframework.boot.test.context.TestComponent
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.*
import java.util.stream.Collectors

/**
 * @author : choi-ys
 * @date : 2021-10-09 오후 5:21
 */
@TestComponent
class TokenGenerator(
    private val tokenProvider: TokenProvider
) {
    fun generateTokenMock(): Token {
        return tokenProvider.createToken(generateUserMock())
    }

    fun generateTokenMock(user: User): Token {
        return tokenProvider.createToken(user)
    }

    companion object {
        val SIGNATURE = "test-case-signature"
        val ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM = 600L
        val USERNAME = "whypie"
        val ROLES = setOf(MemberRole.CERTIFIED_MEMBER)

        fun generateUserMock(): User {
            return User(
                USERNAME,
                "",
                ROLES.stream().map { it -> SimpleGrantedAuthority("ROlE_" + it.name) }.collect(Collectors.toSet())
            )
        }

        fun generateJwtMock(): String {
            val currentTimeMillis = System.currentTimeMillis()

            return JWT.create()
                .withIssuer("ISSUER")
                .withSubject("SUBJECT")
                .withAudience("AUDIENCE")
                .withIssuedAt(Date(currentTimeMillis))
                .withExpiresAt(Date(currentTimeMillis + ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM * 1000))
                .withClaim(ClaimKey.USE.value, TokenType.ACCESS.name)
                .withClaim(ClaimKey.USERNAME.value, USERNAME)
                .withClaim(ClaimKey.AUTHORITIES.value, ROLES.joinToString(","))
                .sign(Algorithm.HMAC256(SIGNATURE))
        }

        fun generateTokenMock(): Token {
            return Token(generateJwtMock(), generateJwtMock(), Date(), Date())
        }

        fun getVerifyResult(token: String): VerifyResult {
            return VerifyResult.mapTo(getClaims(token));
        }

        fun getClaims(token: String): Map<String, Claim> {
            return JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token).claims
        }
    }
}