# 주문-결제 도메인

```mermaid
classDiagram
    class Order {
        +Long id
        +Receipt receipt
        +Long userId
        +Payment payment
        +Status status
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +BigDecimal getTotalPrice()
        +Payment.Status getPaymentStatus()
        +OrderInfo.Pay pay(Authentication authentication)
        +OrderInfo.Cancel cancel(Authentication authentication)
        -void authorize(Authentication authentication)
    }
    class OrderItem {
        +Long id
        +Long productId
        +String name
        +BigDecimal priceOfOne
        +Long quantity
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +BigDecimal getTotalPrice()
    }
    class Receipt {
        +List~OrderItem~ items
        +BigDecimal getTotalPrice()
    }
    class Payment {
        +Long id
        +BigDecimal amount
        +Long userId
        +Status status
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +PaymentInfo.Cancel cancel(Authentication authentication)
        +PaymentInfo.Pay pay(Authentication authentication)
        -void authorize(Authentication authentication)
    }
    Order <|-- Receipt: "포함"
    Order <|-- Payment: "포함"
    Receipt <|-- OrderItem: "포함"
```

# 포인트 도메인

```mermaid
classDiagram
    class UserPoint {
        +Long id
        +Long userId
        +BigDecimal point
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +void charge(BigDecimal amount, Authentication authentication)
        +void use(BigDecimal amount, Authentication authentication)
        -void validate()
        -void authorize(Authentication authentication)
    }
```

# 인증 도메인

```mermaid
classDiagram
    class Authentication {
        +Long userId
        +Boolean isSuper
        // 다른 도메인의 경우 생략
    }

    UserPoint <-- Authentication: "인가"
    Order <-- Authentication: "인가"
    Payment <-- Authentication: "인가"
```

# 상품

```mermaid
classDiagram
    class Product {
        +Long id
        +String name
        +BigDecimal price
        +Long stockNumber
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +ReleaseInfo release(Long amount)
        +void restock(Long amount)
        -void validate()
    }

    class RankedProduct {
        +Long id
        +Long productId
        +Long totalSellingCount
        +BigDecimal totalIncome
        +LocalDate createdDate
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
    }
    Product <|-- RankedProduct: "연관"
```

# 쿠폰

```mermaid
classDiagram
%% Coupon 엔티티
    class Coupon {
        +String name
        +String description
        +LocalDateTime publishFrom
        +LocalDateTime publishTo
        +Duration expireDuration
        +Coupon.Type type
        +BigDecimal amount
        +Long stock
        +Long id
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +List~PublishedCoupon~ publishedCoupons
        -DiscountPolicy discountPolicy
        +publish(userId: UserId, publishedAt: LocalDateTime): PublishedCoupon
        +discount(target: BigDecimal): BigDecimal
    }

%% Coupon.Type 열거형
    class Type {
        <<enumeration>>
        +PERCENT
        +FIXED
    }

%% PublishedCoupon 엔티티
    class PublishedCoupon {
        +UserId userId
        +LocalDateTime expireAt
        +LocalDateTime usedAt
        +Long id
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +boolean used
        +use(time: LocalDateTime, authentication: Authentication): void
        +deuse(): void
        +discount(target: BigDecimal): BigDecimal
    }

%% UsedCoupons (Embeddable)
    class UsedCoupons {
        +List~UsedCouponToOrder~ items
        +discount(target: BigDecimal): BigDecimal
        +deuse(): void
    }

%% UsedCouponToOrder 엔티티
    class UsedCouponToOrder {
        +PublishedCoupon publishedCoupon
        +Long id
        +discount(target: BigDecimal): BigDecimal
        +deuse(): void
    }

    class Order {
        // Order 관련 필드들(생략)
    }

%% 관계 정의
    Type --|> Coupon: 할인타입
    Coupon --|> PublishedCoupon: publishedCoupons
    UsedCoupons <|-- UsedCouponToOrder: items
    UsedCouponToOrder <|-- PublishedCoupon: publishedCoupon
    UsedCoupons --|> Order: order
```