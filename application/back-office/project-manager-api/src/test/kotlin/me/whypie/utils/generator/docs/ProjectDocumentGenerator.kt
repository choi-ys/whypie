package me.whypie.utils.generator.docs

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.payload.PayloadDocumentation.*

/**
 * @author : choi-ys
 * @date : 2021-10-17 오전 2:38
 */
class ProjectDocumentGenerator {
    companion object {
        fun generateCreateProjectDocument(restDocsConfiguration: RestDocumentationResultHandler): RestDocumentationResultHandler {
            return restDocsConfiguration.document(
                requestFields(
                    fieldWithPath("name").description("프로젝트 이름"),
                    fieldWithPath("domain").description("프로젝트 도메인"),
                    fieldWithPath("type").description("프로젝트 타입")
                ),
                responseFields(
                    fieldWithPath("id").description("프로젝트 ID"),
                    fieldWithPath("name").description("프로젝트 이름"),
                    fieldWithPath("domain").description("프로젝트 도메인"),
                    fieldWithPath("type").description("프로젝트 타입"),
                    fieldWithPath("status").description("프로젝트 상태"),
                    fieldWithPath("creator.id").description("생성자 ID"),
                    fieldWithPath("creator.email").description("생성자 이메일"),
                    fieldWithPath("creator.name").description("생성자 이름"),
                    fieldWithPath("creator.nickname").description("생성자 닉네임"),
                )
            )
        }
    }
}