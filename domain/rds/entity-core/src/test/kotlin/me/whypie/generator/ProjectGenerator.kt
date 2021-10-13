package me.whypie.generator

import me.whypie.model.entity.member.Member
import me.whypie.model.entity.project.Project
import me.whypie.model.entity.project.ProjectType
import me.whypie.repository.ProjectRepo
import org.springframework.boot.test.context.TestComponent
import org.springframework.data.domain.PageImpl
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
    fun savedProject(member: Member): Project {
        return projectRepo.save(generateProject(member))
    }

    fun savedProject(name: String, type: ProjectType, member: Member): Project {
        return projectRepo.save(generateProject(name, type, member))
    }

    fun generateProjectList(member: Member, count: Int) {
        for (i: Int in 1..count) {
            val type = if (i % 2 == 0) {
                ProjectType.SERVICE
            } else {
                ProjectType.BACK_OFFICE
            }
            savedProject((NAME + i), type, member)
        }
    }

    companion object {
        val NAME = "Cloud:M"
        val DOMAIN = "cloudm.co.kr"
        val TYPE = ProjectType.SERVICE

        fun generateProjectMock(): Project {
            return Project(name = NAME, domain = DOMAIN, type = TYPE, member = MemberGenerator.member())
        }

        fun generateProject(member: Member): Project {
            return Project(name = NAME, domain = DOMAIN, type = TYPE, member = member)
        }

        fun generateProject(name: String, type: ProjectType, member: Member): Project {
            return Project(name = name, domain = DOMAIN, type = type, member = member)
        }

        fun generateProjectListMock(count: Int): List<Project> {
            val listMock = mutableListOf<Project>()
            for (i: Int in 1..count) {
                listMock.add(generateProject("mocking project $i", ProjectType.SERVICE, MemberGenerator.member()))
            }
            return listMock
        }

        fun generateProjectPageMock(count: Int): PageImpl<Project> {
            return PageImpl(generateProjectListMock(count))
        }
    }
}