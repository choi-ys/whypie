package me.whypie.utils.page

import org.springframework.data.domain.Pageable

/**
 * @author : choi-ys
 * @date : 2021-10-14 오전 7:48
 */
class PageUtils {
    companion object {
        fun pageNumberToIndex(pageable: Pageable): Pageable =
            if (pageable.pageNumber != 0) {
                pageable.withPage(pageable.pageNumber - 1)
            } else {
                pageable
            }
    }
}