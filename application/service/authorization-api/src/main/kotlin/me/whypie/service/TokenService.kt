package me.whypie.service

import me.whypie.component.TokenProvider
import me.whypie.component.TokenVerifier
import me.whypie.model.entity.BlackListTokenCache
import me.whypie.model.entity.WhiteListTokenCache
import me.whypie.model.vo.Principal
import me.whypie.model.vo.Token
import me.whypie.repository.BlackListTokenCacheRepo
import me.whypie.repository.WhiteListTokenCacheRepo
import org.springframework.stereotype.Service

/**
 * @author : choi-ys
 * @date : 2021-10-08 오전 7:04
 */
@Service
class TokenService(
    private val tokenProvider: TokenProvider,
    private val tokenVerifier: TokenVerifier,
    private val blackListTokenCacheRepo: BlackListTokenCacheRepo,
    private val whiteListTokenCacheRepo: WhiteListTokenCacheRepo,
) {

    fun issue(principal: Principal): Token {
        val token = tokenProvider.createToken(principal)
        addBlackList(principal.identifier)
        whiteListTokenCacheRepo.save(WhiteListTokenCache(principal.identifier, token))
        return token
    }

    fun refresh(refreshToken: String): Token {
        val verifyResult = tokenVerifier.verify(refreshToken)
        val principal = verifyResult.principal
        addBlackList(principal.identifier)
        val token = tokenProvider.createToken(principal)
        whiteListTokenCacheRepo.save(WhiteListTokenCache(principal.identifier, token))
        return token
    }

    fun expire(accessToken: String) {
        val principal = tokenVerifier.verify(accessToken).principal
        addBlackList(principal.identifier)
    }

    private fun addBlackList(username: String) {
        val optionalWhiteListTokenCache = whiteListTokenCacheRepo.findById(username)
        if (optionalWhiteListTokenCache.isPresent) {
            val whiteListTokenCache = optionalWhiteListTokenCache.get()
            blackListTokenCacheRepo.save(BlackListTokenCache(whiteListTokenCache.id, whiteListTokenCache.token))
        }
    }
}