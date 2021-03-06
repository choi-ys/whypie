package me.whypie.domain.model.entity.project

import me.whypie.domain.model.dto.request.project.PatchProjectRequest
import me.whypie.domain.model.entity.member.Member
import me.whypie.model.entity.base.Auditor
import javax.persistence.*

/**
 * @author : choi-ys
 * @date : 2021/10/12 2:06 오후
 */
@Entity
@Table(
    name = "project_tb",
    uniqueConstraints = [
        UniqueConstraint(
            name = "PROJECT_NAME_UNIQUE",
            columnNames = ["name"]
        )
    ]
)
data class Project(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "name", nullable = false, length = 50)
    var name: String,

    @Column(name = "domain", nullable = false, length = 50)
    var domain: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    var type: ProjectType,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    var status: ProjectStatus = ProjectStatus.DISABLE,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member,
) : Auditor() {

    fun updateStatus(newStatus: ProjectStatus) {
        status = newStatus
    }

    fun update(patchProjectRequest: PatchProjectRequest) {
        name = patchProjectRequest.name
        domain = patchProjectRequest.domain
        type = patchProjectRequest.type
    }
}
// TODO: 용석(2021/10/12) 프로젝트 관련 정보 수정 처리