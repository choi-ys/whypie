package me.whypie.utils.generator.docs

import me.whypie.config.docs.RestDocsConfiguration.Companion.field
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
                    fieldWithPath("name").description("프로젝트 이름").attributes(field("length", "1~10")),
                    fieldWithPath("domain").description("프로젝트 도메인").attributes(field("length", "5~30")),
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

        fun generateInvalidCreateProjectDocument(restDocsConfiguration: RestDocumentationResultHandler): RestDocumentationResultHandler {
            return restDocsConfiguration.document(
                responseFields(
                    fieldWithPath("timestamp").description("에러 발생 일시"),
                    fieldWithPath("code").description("에러 분류 코드"),
                    fieldWithPath("message").description("에러 메세지"),
                    fieldWithPath("method").description("요청 Http Method"),
                    fieldWithPath("path").description("요청 URI"),
                    fieldWithPath("errorDetails[0].objectName").description("에러 객체 명"),
                    fieldWithPath("errorDetails[0].field").description("에러 필드"),
                    fieldWithPath("errorDetails[0].code").description("에러 상세 코드"),
                    fieldWithPath("errorDetails[0].rejectMessage").description("에러 메세지"),
                    fieldWithPath("errorDetails[0].rejectedValue").description("에러 요청 값"),
                )
            )
        }
    }
}