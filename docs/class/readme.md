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