package kr.hhplus.be.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import kr.hhplus.be.server.domain.auth.AuthException
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.interfaces.api.Response
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.ErrorResponse
import org.springframework.web.method.HandlerMethod


@Configuration
class SwaggerConfig(private val objectMapper: ObjectMapper) {

    @Bean
    fun customGlobalHeaders(): OperationCustomizer {
        return OperationCustomizer { operation: Operation, handlerMethod: HandlerMethod ->
            // 메소드의 파라미터 중 Authentication 클래스가 있는지 검사
            val hasAuthenticationParameter = handlerMethod.methodParameters.any { parameter ->
                Authentication::class.java.isAssignableFrom(parameter.parameterType)
            }

            if (hasAuthenticationParameter) {
                val authHeader =
                    Parameter().`in`("header").schema(StringSchema()).name("Authorization").description("{userId}")
                        .required(true)
                operation.addParametersItem(authHeader)
                val apiResponses: ApiResponses = operation.responses ?: ApiResponses()
                val unauthorizedResponse = ApiResponse().apply {
                    description = "유효하지 않은 사용자입니다."
                    content = Content().apply {
                        addMediaType("application/json", MediaType().apply {
                            schema = Schema<ErrorResponse>().apply {
                                `$ref` = "#/components/schemas/ErrorResponse"
                            }
                            val example = Example().apply {
                                value = objectMapper.writeValueAsString(
                                    Response.error<AuthException>(
                                        exception = AuthException.InvalidAuthenticationException()
                                    )
                                )
                            }
                            examples = mapOf("unauthorizedExample" to example)
                        })
                    }
                }
                apiResponses.addApiResponse("401", unauthorizedResponse)
                operation.responses(apiResponses)
            }
            operation
        }
    }
}