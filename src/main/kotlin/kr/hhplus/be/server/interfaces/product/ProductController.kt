package kr.hhplus.be.server.interfaces.product

import kr.hhplus.be.server.interfaces.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.LocalDate

@RestController
@RequestMapping("api/v1/products")
class ProductController : ProductSpec {
    @GetMapping
    override fun getList(): Response<ProductResponse.GetProductList> {
        return Response.success(
            ProductResponse.GetProductList(
                products = listOf(
                    ProductResponse.GetProductList.ProductDTO(
                        id = 1862,
                        name = "Josefina Blevins",
                        price = BigDecimal.valueOf(3000),
                        stock = 6349
                    )
                )
            )
        )
    }

    @GetMapping("popular")
    override fun getRankedList(): Response<ProductResponse.GetRankedProductList> {
        return Response.success(
            ProductResponse.GetRankedProductList(
                products = listOf(
                    ProductResponse.GetRankedProductList.RankedProductDTO(
                        productId = 3680,
                        name = "Garth Vargas",
                        price = BigDecimal.valueOf(1354),
                        stockNumber = 4191,
                        totalSellingCount = 3535,
                        totalIncome = BigDecimal.valueOf(4861),
                        rank = 8604,
                        createdDate = LocalDate.now()
                    )
                )
            ),
        )
    }
}