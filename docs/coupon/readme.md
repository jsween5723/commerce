## 선착순 쿠폰 발급 API

쿠폰 재고는 이벤트 기반으로 저장하여 수정이 불가능하도록 합니다.
1일 단위로 스케쥴링을 통해 정리합니다.
이전 히스토리 추적을 위해 쿠폰재고 히스토리 테이블도 관리합니다.
조회 후 애플리케이션에서 추산해 재고를 파악하는 데 쓰입니다.
이후 Coupon 도메인 엔티티 생성에 해당 결과값을 사용합니다.

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant CouponController
    participant CouponService
    participant Coupon
    participant CouponRepository
    participant Database
    User ->>+ CouponController: POST /api/v1/coupons/{id}/me
    CouponController ->>+ CouponService: registerCoupon
    CouponService ->>+ CouponRepository: Coupon 조회
    CouponRepository ->>+ Database: Coupon 조회
    Database -->>- CouponRepository: CouponEntity or null
    break 반환값이 null이면
        CouponRepository -->> User: 404 Not Found
    end
    CouponRepository ->>+ Database: CouponInventoryEntity 목록 조회
    Database -->>- CouponRepository: CouponInventoryEntity 목록
    CouponRepository -->>- CouponService: Coupon
    CouponService ->>+ Coupon: register
    break Coupon의 amount가 모자라면
        Coupon -->> User: 400 Bad Request
    end
    break Coupon의 from > now or to < now
        Coupon -->> User: 400 Bad Request
    end
    Coupon -->>- CouponService: RegisteredCouponCreate
    CouponService ->>+ CouponRepository: registerCoupon(RegisteredCouponCreate)
    CouponRepository ->> Database: CouponInventoryEntity insert
    CouponRepository ->> Database: CouponInventoryHistoryEntity insert
    CouponRepository ->>+ Database: insert RegisteredCouponEntity
    Database -->>- CouponRepository: RegisteredCouponEntity with id
    CouponRepository -->>- CouponService: RegisteredCoupon
    CouponService -->>- CouponController: RegisteredCoupon
    CouponController -->>- User: RegisterCouponResponse
```

## 보유 쿠폰 조회 API

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant CouponController
    participant CouponService
    participant CouponRepository
    participant Database
    User ->>+ CouponController: GET /api/v1/coupons/me
    CouponController ->>+ CouponService: RegisteredCoupon 목록 조회
    CouponService ->>+ CouponRepository: RegisteredCoupon 목록 조회
    CouponRepository ->>+ Database: RegisteredCouponEntity 목록 조회 (join CouponEntity)
    Database -->>- CouponRepository: RegisteredCoupon 목록
    CouponRepository -->>- CouponService: RegisteredCoupon 목록
    CouponService -->>- CouponController: RegisteredCoupon 목록
    CouponController -->>- User: MyRegisteredCouponResponse
```

## 쿠폰 재고 스케쥴링

```mermaid
sequenceDiagram
    participant CouponInventoryScheduler
    participant PeriodCouponInventoryList
    participant CouponRepository
    participant Database
    CouponInventoryScheduler ->>+ CouponRepository: 일정기간 생성된 CouponInventory 조회
    CouponRepository ->>+ Database: CouponInventoryEntity 목록 조회
    Database -->>- CouponRepository: CouponInventoryEntity 목록
    CouponRepository -->>- CouponInventoryScheduler: PeriodCouponInventoryList
    CouponInventoryScheduler ->>+ PeriodCouponInventoryList: reduceToCumulative
    PeriodCouponInventoryList ->> PeriodCouponInventoryList: Map<Long, List<CouponInventory>>
    PeriodCouponInventoryList ->> PeriodCouponInventoryList: 누계를 계산 후 List<CouponInventory>
    PeriodCouponInventoryList -->>- CouponInventoryScheduler: List<CouponInventory>
    CouponInventoryScheduler ->>+ CouponRepository: List<CouponInventory> bulk insert
    CouponRepository ->>- Database: List<CouponInventory> bulk insert
    CouponInventoryScheduler ->>+ CouponRepository: PeriodCouponInventoryList 의 id 목록 bulk delete
    CouponRepository ->>- Database: CouponInventoryEntity bulk delete
```