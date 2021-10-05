package me.whypie.model.entity.base

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

/**
 * @author : choi-ys
 * @date : 2021-10-02 오후 9:05
 */
@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class Auditor(

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    open var createdBy: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    open var createdAt: LocalDateTime? = null,

    @LastModifiedBy
    @Column(name = "modified_by")
    open var updatedBy: String? = null,

    @UpdateTimestamp
    @Column(name = "modified_at")
    open var updatedAt: LocalDateTime? = null,
)