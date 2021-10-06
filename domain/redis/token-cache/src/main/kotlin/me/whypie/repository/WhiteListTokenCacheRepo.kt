package me.whypie.repository

import me.whypie.model.entity.WhiteListTokenCache
import org.springframework.data.repository.CrudRepository

/**
 * @author : choi-ys
 * @date : 2021/10/06 5:12 오후
 */
interface WhiteListTokenCacheRepo : CrudRepository<WhiteListTokenCache, String> {
}