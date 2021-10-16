package me.whypie.utils.generator.docs

import me.whypie.utils.generator.docs.ApiDocumentUtils.Companion.documentRequest
import me.whypie.utils.generator.docs.ApiDocumentUtils.Companion.documentResponse
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields

/**
 * @author : choi-ys
 * @date : 2021-10-17 오전 2:38
 */
class ProjectDocumentGenerator {
    companion object {
        fun generateCreateProjectDocument(): RestDocumentationResultHandler {
            return document("{class-name}/{method-name}",
                documentRequest,
                documentResponse,
                requestFields(
                    PayloadDocumentation.fieldWithPath("name").description("프로젝트 이름"),
                    PayloadDocumentation.fieldWithPath("domain").description("프로젝트 도메인"),
                    PayloadDocumentation.fieldWithPath("type").description("프로젝트 타입")
                ),
                responseFields(
                    PayloadDocumentation.fieldWithPath("id").description("프로젝트 ID"),
                    PayloadDocumentation.fieldWithPath("name").description("프로젝트 이름"),
                    PayloadDocumentation.fieldWithPath("domain").description("프로젝트 도메인"),
                    PayloadDocumentation.fieldWithPath("type").description("프로젝트 타입"),
                    PayloadDocumentation.fieldWithPath("status").description("프로젝트 상태"),
                    PayloadDocumentation.fieldWithPath("creator.id").description("생성자 ID"),
                    PayloadDocumentation.fieldWithPath("creator.email").description("생성자 이메일"),
                    PayloadDocumentation.fieldWithPath("creator.name").description("생성자 이름"),
                    PayloadDocumentation.fieldWithPath("creator.nickname").description("생성자 닉네임"),
                )
            )
        }
    }
}