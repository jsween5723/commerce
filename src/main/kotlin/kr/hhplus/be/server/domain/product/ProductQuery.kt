package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.OrderBy
import java.time.LocalDate

class ProductQuery {

    data class Ranked(
        val day: LocalDate = LocalDate.now(), val orders: Set<RankedProductOrderBy> = linkedSetOf(
            RankedProductOrderBy(
                RankedProductField.TOTAL_SELLING_COUNT, OrderBy.Type.DESC
            )
        )
    )
}

data class RankedProductOrderBy(override val field: RankedProductField, override val type: Type) :
    OrderBy(field = field, type = type)

enum class RankedProductField(override val field: String) : OrderBy.OrderField {
    TOTAL_SELLING_COUNT("totalSellingCount"), TOTAL_INCOME("totalIncome")
}