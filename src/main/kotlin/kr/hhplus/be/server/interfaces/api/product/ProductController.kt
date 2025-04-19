package kr.hhplus.be.server.interfaces.api.product

import kr.hhplus.be.server.application.product.ProductFacade
import kr.hhplus.be.server.domain.product.ProductQuery
import kr.hhplus.be.server.interfaces.api.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/products")
class ProductController(private val productFacade: ProductFacade) : ProductSpec {
    @GetMapping
    override fun getList(): Response<ProductResponse.GetProductList> {
        val products =
            productFacade.findAllProducts().products.map { ProductResponse.GetProductList.ProductDTO.from(it) }
        return Response.success(
            ProductResponse.GetProductList(
                products = products
            )
        )
    }

    @GetMapping("popular")
    override fun getRankedList(query: ProductQuery.Ranked): Response<ProductResponse.GetRankedProductList> {
        val rankedProducts = productFacade.findRankedProducts(query).rankedProducts.map {
            ProductResponse.GetRankedProductList.RankedProductDTO.from(it)
        }
        return Response.success(
            ProductResponse.GetRankedProductList(
                products = rankedProducts
            )
        )

    }
}