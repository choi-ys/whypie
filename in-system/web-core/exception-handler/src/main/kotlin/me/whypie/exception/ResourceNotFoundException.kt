package me.whypie.exception

import me.whypie.error.ErrorCode

/**
 * @author : choi-ys
 * @date : 2021/10/05 5:47 오후
 */
class ResourceNotFoundException : IllegalArgumentException(ErrorCode.RESOURCE_NOT_FOUND.message)