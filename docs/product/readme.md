## 상품 조회 API

```mermaid
sequenceDiagram
    actor User
    participant ProductController
    participant ProductService
    participant ProductRepository
    participant Database
    User ->>+ ProductController: GET /api/v1/products
    ProductController ->>+ ProductService: Product 목록 조회
    ProductService ->>+ ProductRepository: Product 목록 조회
    ProductRepository ->>+ Database: ProductEntity 목록 조회
    Database -->>- ProductRepository: ProductEntity 목록
    ProductRepository ->>+ Database: ProductInventoryEntity 목록 조회 (in ProductEntity ids)
    Database -->>- ProductRepository: ProductInventoryEntity 목록
    ProductRepository -->>- ProductService: Product 목록
    ProductService -->>- ProductController: Product 목록
    ProductController -->>- User: ProductListResponse
```

## 인기 상품 조회 API

```mermaid
sequenceDiagram
    actor User
    participant ProductController
    participant ProductService
    participant ProductRepository
    participant Database
    User ->>+ ProductController: GET /api/v1/products/popular
    ProductController ->>+ ProductService: RankedProduct 목록 조회
    ProductService ->>+ ProductRepository: RankedProduct 목록 조회
    ProductRepository ->>+ Database: RankedProductEntity 목록 조회 (join ProductEntity)
    Database -->>- ProductRepository: RankedProductEntity 목록
    ProductRepository ->>+ Database: ProductInventoryEntity 목록 조회 (in ProductRankEntity ids)
    Database -->>- ProductRepository: ProductInventoryEntity 목록
    ProductRepository -->>- ProductService: RankedProduct 목록
    ProductService -->>- ProductController: RankedProduct 목록
    ProductController -->>- User: RankedProductListResponse
```