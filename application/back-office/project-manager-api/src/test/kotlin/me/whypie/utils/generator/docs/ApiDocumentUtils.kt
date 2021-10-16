package me.whypie.utils.generator.docs

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors

/**
 * @author : choi-ys
 * @date : 2021-10-17 오전 2:40
 */
class ApiDocumentUtils {
    companion object {
        val documentRequest: OperationRequestPreprocessor?
            get() = Preprocessors.preprocessRequest(
                Preprocessors.modifyUris()
                    .scheme(HTTP)
                    .host(DEV_HOST)
                    .removePort(),
                Preprocessors.prettyPrint())
        val documentResponse: OperationResponsePreprocessor?
            get() {
                return Preprocessors.preprocessResponse(
                    Preprocessors.modifyUris()
                        .scheme(HTTP)
                        .host(DEV_HOST)
                        .removePort(),
                    Preprocessors.prettyPrint())
            }
        val HTTP: String = "http"
        val HTTPS: String = "https"
        val BASE_HOST: String = "whypie.me"
        val DEV_HOST: String = "dev-project-api.$BASE_HOST"
        val STG_HOST: String = "stg-project-api.$BASE_HOST"
        val PRD_HOST: String = "prd-project-api.$BASE_HOST"
    }
}