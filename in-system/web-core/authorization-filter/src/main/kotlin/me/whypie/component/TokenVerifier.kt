package me.whypie.component

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import me.whypie.model.LoginUserAdapter
import me.whypie.model.VerifyResult
import me.whypie.utils.LocalDateTimeUtils
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import javax.servlet.http.HttpServletRequest

/**
 * @author : choi-ys
 * @date : 2021-10-08 오후 2:39
 */
@Component
class TokenVerifier : InitializingBean {
    @Value("\${jwt.signature}")
    private val SIGNATURE: String? = null
    private var ALGORITHM: Algorithm? = null

    override fun afterPropertiesSet() {
        ALGORITHM = Algorithm.HMAC256(SIGNATURE)
    }

    fun verify(token: String?): VerifyResult {
        return try {
            val verify = JWT.require(ALGORITHM).build().verify(token)
            val claims = verify.claims
            VerifyResult.mapTo(claims)
        } catch (e: TokenExpiredException) { // 유효기간이 만료된 토큰
            val decode = JWT.decode(token)
            val verifyResult: VerifyResult = VerifyResult.mapTo(decode.claims)
            throw TokenExpiredException("유효기간이 만료 되었습니다." + LocalDateTimeUtils.timestampToLocalDateTime(verifyResult.expiresAt))
        } catch (e: JWTDecodeException) { // 유효하지 못한 형식의 토큰
            throw JWTDecodeException("잘못된 형식의 토큰입니다.")
        } catch (e: SignatureVerificationException) { // 잘못된 서명을 가진 토큰
            throw SignatureVerificationException(ALGORITHM, e.cause)
        }
    }

    fun getAuthentication(token: String): Authentication {
        val verifyResult = verify(token)
        val principal = verifyResult.principal.convert()
        val loginUserAdapter = LoginUserAdapter(principal.username, principal.authorities)
        return UsernamePasswordAuthenticationToken(loginUserAdapter, null, loginUserAdapter.authorities)
    }

    companion object {
        fun resolve(httpServletRequest: HttpServletRequest): String {
            val bearerToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)
            return if (!StringUtils.hasText(bearerToken)) {
                ""
            } else {
                val isBearerToken = bearerToken.startsWith("Bearer ")
                if (isBearerToken) {
                    bearerToken.substring(7, bearerToken.length)
                } else {
                    ""
                }
            }
        }
    }
}