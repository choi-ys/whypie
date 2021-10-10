package me.whypie.model.vo

/**
 * @author : choi-ys
 * @date : 2021-10-10 오전 3:43
 */
data class Principal(
    var identifier: String,
    var authorities: String,
) {
    companion object {
        fun <T> mapTo(identifier: String, set: Set<T>): Principal {
            return Principal(identifier, set.joinToString(","))
        }
    }
}