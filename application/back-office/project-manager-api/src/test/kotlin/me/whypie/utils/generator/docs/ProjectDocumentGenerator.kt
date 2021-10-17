package me.whypie.utils.generator.docs

import me.whypie.config.docs.RestDocsConfiguration.Companion.format
import me.whypie.domain.model.entity.project.ProjectType
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.payload.PayloadDocumentation.*
import java.util.*
import java.util.stream.Collectors


/**
 * @author : choi-ys
 * @date : 2021-10-17 오전 2:38
 */
class ProjectDocumentGenerator {
    companion object {
        fun generateCreateProjectDocument(restDocsConfiguration: RestDocumentationResultHandler): RestDocumentationResultHandler {
            return restDocsConfiguration.document(
                requestFields(
                    fieldWithPath("name").description("프로젝트 이름").attributes(format("1~10")),
                    fieldWithPath("domain").description("프로젝트 도메인").attributes(format("5~30")),
                    fieldWithPath("type").description("프로젝트 타입").attributes(format(projectTypeCode()))
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

        private fun projectTypeCode() =
            Arrays.stream(ProjectType.values())
                .map { type -> java.lang.String.format("%s: %s", type.name, type.description) }
                .collect(Collectors.joining("\n"))

        fun generateInvalidCreateProjectDocument(restDocsConfiguration: RestDocumentationResultHandler): RestDocumentationResultHandler {
            return restDocsConfiguration.document(
                responseFields(
                    fieldWithPath("timestamp").description("에러 발생 일시"),
                    fieldWithPath("code").description("에러 분류 코드"),
                    fieldWithPath("message").description("에러 메세지"),
                    fieldWithPath("method").description("요청 Http Method"),
                    fieldWithPath("path").description("요청 URI"),
                    fieldWithPath("errorDetails[*]").description("에러 상세 정보(배열)"),
                    fieldWithPath("errorDetails[*].objectName").description("에러 객체 명"),
                    fieldWithPath("errorDetails[*].field").description("에러 필드"),
                    fieldWithPath("errorDetails[*].code").description("에러 상세 코드"),
                    fieldWithPath("errorDetails[*].rejectMessage").description("에러 메세지"),
                    fieldWithPath("errorDetails[*].rejectedValue").description("에러 요청 값"),
                )
            )
        }

        fun generateProjectPaginationDocument(restDocsConfiguration: RestDocumentationResultHandler): RestDocumentationResultHandler {
            return restDocsConfiguration.document(
                responseFields(
                    fieldWithPath("totalPages").description("조회된 전체 페이지의 수"),
                    fieldWithPath("totalElementCount").description("조회된 전체 컨텐츠 개수"),
                    fieldWithPath("currentPage").description("현재 페이지 번호"),
                    fieldWithPath("currentElementCount").description("현재 페이제의 컨텐츠 개수"),
                    fieldWithPath("perPageNumber").description("조회 페이지당 컨텐츠 개수"),
                    fieldWithPath("firstPage").description("첫번째 페이지 여부"),
                    fieldWithPath("lastPage").description("마지막 페이지 여부"),
                    fieldWithPath("hasNextPage").description("다음 페이지 존재 여부"),
                    fieldWithPath("hasPrevious").description("이전 페이지 존재 여부"),
                    fieldWithPath("embedded[*]").description("컨텐츠 정보(배열)"),
                )
            )
        }
    }
}