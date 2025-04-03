package kr.hhplus.be.server.api.product

import kr.hhplus.be.server.api.Response
import kr.hhplus.be.server.api.SuccessResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/products")
class ProductController : ProductSpec {
    @GetMapping
    override fun getList(): Response<GetProductListResponse> {
        return SuccessResponse(
            GetProductListResponse(
                listOf(
                    GetProductListResponse.Product(
                        id = 6702, name = "Robert Chan", price = 3301, stock = 6416
                    )
                )
            )
        )
    }

    @GetMapping("popular")
    override fun getRankedList(): Response<GetRankedProductListResponse> {
        return SuccessResponse(
            GetRankedProductListResponse(
                listOf(
                    GetRankedProductListResponse.RankedProduct(
                        id = 6241, name = "Rodger Soto", price = 1308, stock = 2596, rank = 1
                    )
                )
            )
        )
    }
}