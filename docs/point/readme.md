## 잔액 충전 API

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant PointController
    participant PointService
    participant PointRepository
    participant Point
    participant Database
    User ->>+ PointController: POST /api/v1/points/charge
    PointController ->>+ PointService: 포인트 충전
    break userId가 1 미만일 때
        PointService -->> User: 400 Bad Request
    end
    PointService ->>+ PointRepository: 포인트 조회
    PointRepository ->>+ Database: 포인트 조회
    Database -->>- PointRepository: 포인트 조회 결과
    alt 포인트가 DB에 없으면
        PointRepository ->> Database: default 0으로 포인트 저장
    end
    PointRepository -->>- PointService: Point로 변환 후 반환
    PointService ->>+ Point: 충전
    break amount가 1 미만일 때
        Point -->> User: 400 Bad Request
    end
    break amount + point > MAX일 때
        Point -->> User: 400 Bad Request
    end
    Point -->>- PointService: OK
    PointService ->>+ PointRepository: Point 저장
    PointRepository ->>+ Database: Point 저장
    Database -->>- PointRepository: 저장 결과
    PointRepository -->>- PointService: OK
    PointService -->>- PointController: 최종 Point
    PointController -->>- User: 200 OK 충전된 결과
```

## 잔액 조회 API

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant PointController
    participant PointService
    participant PointRepository
    participant Point
    participant Database
    User ->>+ PointController: GET /api/v1/points/me
    PointController ->>+ PointService: 포인트 충전
    break userId가 1 미만일 때
        PointService -->> User: 400 Bad Request
    end
    PointService ->>+ PointRepository: 포인트 조회
    PointRepository ->>+ Database: 포인트 조회
    Database -->>- PointRepository: 포인트 조회 결과
    alt 포인트가 DB에 없으면
        PointRepository ->> Database: default 0으로 포인트 저장
    end
    PointRepository -->>- PointService: Point로 변환 후 반환
    PointService -->>- PointController: 최종 Point
    PointController -->>- User: 200 OK 현재 포인트
```