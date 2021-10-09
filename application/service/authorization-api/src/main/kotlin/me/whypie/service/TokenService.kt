package me.whypie.service

import me.whypie.component.TokenProvider
import me.whypie.component.TokenVerifier
import me.whypie.model.entity.BlackListTokenCache
import me.whypie.model.entity.WhiteListTokenCache
import me.whypie.model.vo.Token
import me.whypie.repository.BlackListTokenCacheRepo
import me.whypie.repository.WhiteListTokenCacheRepo
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
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

    fun issue(userDetails: UserDetails): Token {
        val token = tokenProvider.createToken(userDetails)
        addBlackList(userDetails.username)
        whiteListTokenCacheRepo.save(WhiteListTokenCache(userDetails.username, token))
        return token
    }

    fun refresh(refreshToken: String): Token {
        val verifyResult = tokenVerifier.verify(refreshToken)
        addBlackList(verifyResult.username)
        val token = tokenProvider.createToken(User(verifyResult.username, "", verifyResult.authorities))
        whiteListTokenCacheRepo.save(WhiteListTokenCache(verifyResult.username, token))
        return token
    }

    fun expire(accessToken: String) {
        val verifyResult = tokenVerifier.verify(accessToken)
        addBlackList(verifyResult.username)
    }

    private fun addBlackList(username: String) {
        val optionalWhiteListTokenCache = whiteListTokenCacheRepo.findById(username)
        if (optionalWhiteListTokenCache.isPresent) {
            val whiteListTokenCache = optionalWhiteListTokenCache.get()
            blackListTokenCacheRepo.save(BlackListTokenCache(whiteListTokenCache.id, whiteListTokenCache.token))
        }
    }
}