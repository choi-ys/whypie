package me.whypie.repository

import me.whypie.model.entity.Project
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author : choi-ys
 * @date : 2021/10/12 2:18 오후
 */
interface ProjectRepo : JpaRepository<Project, Long> {
    fun existsByName(name: String): Boolean
}