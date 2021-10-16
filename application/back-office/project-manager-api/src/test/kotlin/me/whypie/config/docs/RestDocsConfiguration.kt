package me.whypie.config.docs

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.snippet.Attributes.Attribute

/**
 * @author : choi-ys
 * @date : 2021-10-17 오전 4:18
 */
@TestConfiguration
class RestDocsConfiguration {

    @Bean
    fun restDocumentationResultHandler(): RestDocumentationResultHandler {
        return MockMvcRestDocumentation.document(
            "{class-name}/{method-name}",
            Preprocessors.preprocessRequest(
                Preprocessors.modifyUris()
                    .scheme(HTTP)
                    .host(DEV_HOST)
                    .removePort(),
                Preprocessors.prettyPrint()
            ),
            Preprocessors.preprocessResponse(
                Preprocessors.modifyUris()
                    .scheme(HTTP)
                    .host(DEV_HOST)
                    .removePort(),
                Preprocessors.prettyPrint())
        )
    }

    companion object {
        val HTTP: String = "http"
        val HTTPS: String = "https"
        val BASE_HOST: String = "whypie.me"
        val DEV_HOST: String = "dev-project-api.$BASE_HOST"
        val STG_HOST: String = "stg-project-api.$BASE_HOST"
        val PRD_HOST: String = "prd-project-api.$BASE_HOST"

        fun format(value: String): Attribute = Attribute("format", value)
    }
}