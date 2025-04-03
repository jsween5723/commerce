## 상품 조회 API

상품 재고(ProductInventoryEntity)는 이벤트 기반으로 저장하여 수정이 불가능하도록 합니다.
1일 단위로 스케쥴링을 통해 정리합니다.
이전 히스토리 추적을 위해 상품재고 히스토리 테이블도 관리합니다.
조회 후 애플리케이션에서 추산해 재고를 파악하는 데 쓰입니다.
이후 Product 도메인 엔티티 생성에 해당 결과값을 사용합니다.

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

## 인기 상품 스케쥴링
```mermaid
sequenceDiagram
    participant RankProductScheduler
    participant PeriodPayedOrderItemList
    participant OrderRepository
    participant ProductRepository
    participant Database
    RankProductScheduler ->>+ OrderRepository: PeriodPayedOrderItemList 조회
    OrderRepository ->>+ Database: 근 3일의 OrderItemEntity 조회 (PAYED)
    Database -->>- OrderRepository: List<OrderItemEntity>
    OrderRepository -->>- RankProductScheduler: PeriodPayedOrderItemList
    RankProductScheduler ->>+ PeriodPayedOrderItemList: reduceToCumulative
    PeriodPayedOrderItemList ->> PeriodPayedOrderItemList: Map<Long[productId], List<OrderItem>> 
    PeriodPayedOrderItemList ->> PeriodPayedOrderItemList: 누계를 계산 후 List<OrderItem>
    PeriodPayedOrderItemList -->>- RankProductScheduler: List<OrderItem>
    RankProductScheduler ->> ProductRepository: List<OrderItem>으로 rank 반영
    ProductRepository ->>+ Database: RankedProductEntity bulk insert
```