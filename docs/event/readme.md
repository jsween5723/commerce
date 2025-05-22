# 이벤트 드리븐

## 도메인 범위 지정

![바운디드 컨텍스트](바운디드_컨텍스트.png)

주문의 경우 다른 도메인이 변경돼도 정보가 유지돼야한다.  
따라서 주문 시점의 정보로 해당 도메인에서 엔티티를 생성해 유지 및 사용한다.

쿠폰의 경우 발급시에 정보가 정해진다. 따라서 사용자에게 발급된 쿠폰

## 이벤트 설계 (분산 트랜잭션 적용 전)

실선: BEFORE_COMMIT  
점선: AFTER_COMMIT+Async

### 주문 생성

갈라진 이벤트를 검증하기 위해  
Order에 productStatus, couponStatus 추가  
TODO: 추후 분산 트랜잭션 적용 후 롤백여부 확인을 위해 OrderProcess.CreateStart 처럼 만들고 레디스 활용하여 관리
TODO: 동기 처리중이기 때문에 재고, 쿠폰의 경우 콜렉션 전달하나 분산 트랜잭션 적용 후 단일처리로 변경

![주문 생성](주문생성_이벤트.png)

### 주문 결제

![주문 결제](주문결제_이벤트.png)

## 이벤트 설계 (분산 트랜잭션 적용 gn)

실선: BEFORE_COMMIT  
점선: AFTER_COMMIT+Async

### 주문 생성

TODO: 추후 OrderProcess.CreateStart.PruductDeduct 처럼
병렬작업에 join이 필요한 경우 만들고 레디스 활용하여 관리

![주문 생성](주문생성_이벤트.png)

### 주문 결제

![주문 결제](주문결제_이벤트.png)



