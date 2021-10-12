package me.whypie.generator

import me.whypie.model.entity.Project
import me.whypie.model.entity.ProjectType
import me.whypie.repository.ProjectRepo
import org.springframework.boot.test.context.TestComponent
import org.springframework.test.context.TestConstructor

/**
 * @author : choi-ys
 * @date : 2021/10/12 4:37 오후
 */
@TestComponent
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ProjectGenerator(
    private val projectRepo: ProjectRepo,
) {
    fun savedProject(): Project {
        return projectRepo.save(generateProjectMock())
    }

    companion object {
        val NAME = "Cloud:M"
        val DOMAIN = "cloudm.co.kr"
        val TYPE = ProjectType.SERVICE

        fun generateProjectMock(): Project {
            return Project(name = NAME, domain = DOMAIN, type = TYPE)
        }
    }
}