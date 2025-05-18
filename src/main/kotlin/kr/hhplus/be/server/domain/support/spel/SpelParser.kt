package kr.hhplus.be.server.domain.support.spel

import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component

@Component
class SpelParser(private val parser: SpelExpressionParser = SpelExpressionParser()) {
    fun parse(parameterNames: Array<String>, args: Array<Any>, spel: String): String {
        val context = StandardEvaluationContext()
        for (i in parameterNames.indices) {
            context.setVariable(parameterNames[i], args[i])
        }
        return try {
            parser.parseExpression(spel).getValue(context, String::class.java)!!
        } catch (e: Exception) {
            throw IllegalArgumentException("SPEL이 비정상적입니다.$spel", e)
        }
    }
}