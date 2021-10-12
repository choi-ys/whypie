package me.whypie.model.entity

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

    @Column(name = "type", nullable = false, length = 50)
    var type: ProjectType,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    var status: ProjectStatus = ProjectStatus.DISABLE,

    ) : Auditor()
// TODO: 용석(2021/10/12) 프로젝트 관련 정보 수정 처리