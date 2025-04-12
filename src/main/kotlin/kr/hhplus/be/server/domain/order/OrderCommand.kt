package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.product.Product

class OrderCommand {
    data class Create(
        val releaseItems: List<Product.ReleaseInfo>,
        val authentication: Authentication
    )
}
