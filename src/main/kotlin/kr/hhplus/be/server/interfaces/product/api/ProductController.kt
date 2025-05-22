package kr.hhplus.be.server.interfaces.product.api

import kr.hhplus.be.server.domain.product.ProductQuery
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.interfaces.support.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/products")
class ProductController(private val productService: ProductService) : ProductSpec {
    @GetMapping
    override fun getList(): Response<ProductResponse.GetProductList> {
        val products =
            productService.findAll().map { ProductResponse.GetProductList.ProductDTO.from(it) }
        return Response.success(
            ProductResponse.GetProductList(
                products = products
            )
        )
    }

    @GetMapping("popular")
    override fun getRankedList(query: ProductQuery.Ranked): Response<ProductResponse.GetRankedProductList> {
        val rankedProducts = productService.findRankedBy(query).map {
            ProductResponse.GetRankedProductList.RankedProductDTO.from(it)
        }
        return Response.success(
            ProductResponse.GetRankedProductList(
                products = rankedProducts
            )
        )
    }
}