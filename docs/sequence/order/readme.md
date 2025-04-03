## 주문 및 결제 유저플로우
퍼시스턴스 레이어는 생략합니다.
1. 주문 생성은 인터파크 등 쇼핑몰에서 장바구니에서 주문하기를 눌렀을 때
출력되는 주문 화면에 필요한 요소들로 정의합니다.
2. 쿠폰은 장바구니에서 지정하여 주문 화면에는 적용된 가격이 표시되는 것으로 정의합니다.
3. 결제하기는 주문 화면에서 결제수단을 선택 후 결제하기를 눌렀을 때 동작하는 것으로 정의합니다.
   4. 현재 요구사항에선 결제수단이 내부 포인트만 있는 것으로 정의합니다.


1. 주문 생성 API를 호출합니다.
    1. 주문 상품 id목록으로 상품 목록을 조회 후 검증합니다.
        1. 조회된 상품목록이 주문 상품 id목록을 모두 포함하는지
       2. 재고 수량이 주문량을 충족하는지
    3. 쿠폰 Id 목록으로 등록한 쿠폰 목록을 조회 후 검증합니다.
       4. 모두 포함하는지
    5. 상품목록은 OrderItemList로 변환합니다.
   6. OrderItemList에 등록한 쿠폰 목록을 전달하여 할인가를 포함한 Order를 생성합니다.
   7. 반환합니다.
8. 결제 API를 호출합니다.
   9. order id로 가져오고 유효한 주문(PENDING_PAYMENT)인지 확인합니다.
   10. userid로 포인트를 조회합니다.
   11. PaymentService에 주문과 포인트를 전달해 결제를 수행합니다.
       12. 주문의 결제함수에 포인트를 전달합니다.
       13. 포인트의 사용함수를 동작시킵니다.
           14. 잔액이 충분한지 검증후 차감합니다.
       15. 주문결과를 반환합니다.
   16. 주문 결과를 기반으로 외부 데이터 플랫폼에 전달합니다.
   17. 결제 결과를 반환합니다.
   
```mermaid
sequenceDiagram
    autonumber
    actor User
    User ->>+ OrderController: POST /api/v1/orders
    OrderController ->>+ OrderService: 주문하기
    OrderService ->>+ ProductService: ProductList 조회
    ProductService ->>- OrderService: ProductList
    OrderService ->>+ ProductList: OrderItemList 생성
    break productList not contains orderItemIds  
        ProductList -->> User : 400 Bad Request
    end
    break productList 요소들의 재고가 orderItem의 양보다 적을 때 
        ProductList -->> User : 400 Bad Request
    end
    ProductList -->>- OrderService: OrderItemList
    OrderService ->>+ CouponService: RegisteredCouponList 조회
    CouponService -->>- OrderService: RegisteredCouponList
    OrderService ->>+ RegisteredCouponList: 검증
    break RegisteredCouponList not contains orderCouponList  
        RegisteredCouponList -->> User: 400 Bad Request
    end
    OrderService ->>+ OrderItemList: Order 생성 (PENDING_PAYMENT)
    OrderItemList ->>+ RegisteredCouponList: 할인가 구하기
    RegisteredCouponList -->>- OrderItemList: 할인 적용 가격
    OrderItemList -->>- OrderService: Order
    OrderService ->>+ OrderItemList: (Product Id, amount) List 조회
    OrderItemList -->>- OrderService: (Product Id, amount) List
    OrderService ->>+ ProductService: (Product Id, amount) List 재고 차감
    OrderService -->>- OrderController: Order
    OrderController -->>- User: CreateOrderResponse
    User ->>+ OrderController: POST /api/v1/orders/{id}/payments
    OrderController ->>+ OrderService: 주문 결제하기
    OrderService -->> OrderService: Order or null
    break null일 때 
        OrderService -->> User: 404 Not Found
    end
    OrderService ->>+ PointService: UserPoint 조회
    PointService -->>- OrderService: UserPoint
    OrderService ->>+ PaymentService: 결제하기
    PaymentService ->>+ Order: 지불하기
    Order ->> UserPoint: 지불하기
    break Order 가격보다 UserPoint가 적을 때
        OrderItemList -->> User: 400 Bad Request
    end
    Order ->> Order: 상태 변경 (PAYED)
    Order -->>- PaymentService: OrderPayment
    PaymentService -->>- OrderService: OrderPayment
    OrderService ->> ExternalDataPlatform: OrderPayment 전송
    OrderService -->>- OrderController: OrderPayment
    OrderController -->>- User: PayOrderResponse 
```

