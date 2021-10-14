package me.whypie.model.dto.response.page

import org.springframework.data.domain.Page

/**
 * @author : choi-ys
 * @date : 2021-10-14 오전 6:12
 */
data class PageResponse(
    val totalPages: Int,
    val totalElementCount: Long,
    val currentPage: Int,
    val currentElementCount: Int,
    val perPageNumber: Int,

    val firstPage: Boolean,
    val lastPage: Boolean,
    val hasNextPage: Boolean,
    val hasPrevious: Boolean,

    val embedded: List<*>,
) {
    companion object {
        fun mapTo(page: Page<*>, embedded: List<*>): PageResponse {
            return PageResponse(
                totalPages = page.totalPages,
                totalElementCount = page.totalElements,
                currentPage = page.number + 1,
                currentElementCount = page.numberOfElements,
                perPageNumber = page.size,
                firstPage = page.isFirst,
                lastPage = page.isLast,
                hasNextPage = page.hasNext(),
                hasPrevious = page.hasPrevious(),
                embedded = embedded
            )
        }
    }
}