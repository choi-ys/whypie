package me.whypie.domain.model.entity.project

/**
 * @author : choi-ys
 * @date : 2021/10/12 2:06 오후
 */
enum class ProjectStatus(val description: String) {
    ENABLE("사용 가능"),
    DISABLE("사용 불가능");
}