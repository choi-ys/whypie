package me.whypie.repository

import me.whypie.model.entity.CertificationMailCache
import org.springframework.data.repository.CrudRepository

/**
 * @author : choi-ys
 * @date : 2021-10-03 오후 10:05
 */
interface CertificationMailCacheRepo : CrudRepository<CertificationMailCache, String> {
}