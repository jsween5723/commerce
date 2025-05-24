package kr.hhplus.be.server.interfaces.support.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
class LoggingFilter : OncePerRequestFilter() {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val wrappedRequest = ContentCachingRequestWrapper(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        val startTime = System.currentTimeMillis()
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse)
        } finally {
            val duration = System.currentTimeMillis() - startTime
            logRequest(wrappedRequest)
            logResponse(wrappedResponse, duration)
            // 복사한 응답 바이트를 실제 response에 다시 써줘야 클라이언트가 응답을 받습니다
            wrappedResponse.copyBodyToResponse()
        }
    }

    private fun logRequest(request: ContentCachingRequestWrapper) {
        val sb = StringBuilder()
        sb.append("\n--- HTTP REQUEST ---\n")
        sb.append("${request.method} ${request.requestURI}")
        request.queryString?.let { sb.append("?$it") }
        sb.append("\nHeaders:\n")
        request.headerNames.toList().forEach { name ->
            sb.append("  $name: ${request.getHeader(name)}\n")
        }
        val payload = request.contentAsByteArray
        if (payload.isNotEmpty()) {
            sb.append("Body:\n  ${String(payload, Charsets.UTF_8)}\n")
        }
        sb.append("--------------------")
        logger.info(sb.toString())
    }

    private fun logResponse(response: ContentCachingResponseWrapper, duration: Long) {
        val sb = StringBuilder()
        sb.append("\n--- HTTP RESPONSE ---\n")
        sb.append("Status: ${response.status}\n")
        sb.append("Headers:\n")
        response.headerNames.forEach { name ->
            sb.append("  $name: ${response.getHeader(name)}\n")
        }
        val payload = response.contentAsByteArray
        if (payload.isNotEmpty()) {
            sb.append("Body:\n  ${String(payload, Charsets.UTF_8)}\n")
        }
        sb.append("Duration: ${duration}ms\n")
        sb.append("---------------------")
        logger.info(sb.toString())
    }

}