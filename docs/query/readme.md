# 쿼리 개선하기

## 인기상품 쿼리

사용자는 인기상품을 조회할 때 가격을 기준으로 정렬할 수 있다.  
가장 많이 팔린 경우를 인기 상품으로 생각하기 쉽다.  
따라서 해당 쿼리를 개선하기로 했다.

```sql
explain analyze
select *
from ranked_products as rp
         join products as p on rp.product_id = p.id
where price < 100000
order by total_selling_count
limit 20;
```

해당 쿼리는 10만원이하의 상품 중, 가장많이 팔린 순으로 정렬하는 쿼리다.

```sql
 | -> Limit: 20 row(s)  (cost=450105 rows=20) (actual time=515..515 rows=20 loops=1)
    -> Nested loop inner join  (cost=450105 rows=331643) (actual time=515..515 rows=20 loops=1)
        -> Sort: rp.total_selling_count  (cost=101844 rows=995030) (actual time=515..515 rows=20 loops=1)
            -> Table scan on rp  (cost=101844 rows=995030) (actual time=1.12..210 rows=1e+6 loops=1)
        -> Filter: (p.price < 100000.00)  (cost=0.25 rows=0.333) (actual time=0.00398..0.00404 rows=1 loops=20)
            -> Single-row index lookup on p using PRIMARY (id=rp.product_id)  (cost=0.25 rows=1) (actual time=0.00373..0.00375 rows=1 loops=20)
```

정렬 시간에만 515초 가량 소모되는 문제를 확인할 수 있다.

테스트 데이터는 카디널리티를 고려해 랜덤한 데이터를 채택했다.

목표 수치는 250ms이내를 목표로 한다.

### 정렬조건 인덱싱

정렬 조건에 사용되는 total_selling_count에 대해 인덱스를 지정했다.

```sql
| -> Limit: 20 row(s)  (cost=248764 rows=20) (actual time=7.1..7.28 rows=20 loops=1)
    -> Nested loop inner join  (cost=248764 rows=20) (actual time=7.1..7.28 rows=20 loops=1)
        -> Index scan on rp using ranked_products_total_selling_count  (cost=0.263 rows=60) (actual time=7.01..7.03 rows=20 loops=1)
        -> Filter: (p.price < 100000.00)  (cost=0.25 rows=0.333) (actual time=0.0106..0.0116 rows=1 loops=20)
            -> Single-row index lookup on p using PRIMARY (id=rp.product_id)  (cost=0.25 rows=1) (actual time=0.0088..0.00894 rows=1 loops=20)
 |
```

7초로 대폭 줄긴 했지만 인덱스를 타지 않는 것을 볼 수 있다.

### 조건절 인덱싱

이번엔 join된 테이블의 컬럼이며, where절에 사용되는 조건인 products.price 컬럼에 지정해보겠다.

```sql
| -> Limit: 20 row(s)  (cost=917908 rows=20) (actual time=0.233..0.329 rows=20 loops=1)
    -> Nested loop inner join  (cost=917908 rows=20) (actual time=0.232..0.327 rows=20 loops=1)
        -> Index scan on rp using ranked_products_total_selling_count  (cost=0.174 rows=40) (actual time=0.213..0.218 rows=20 loops=1)
        -> Filter: (p.price < 100000.00)  (cost=0.922 rows=0.5) (actual time=0.00507..0.00516 rows=1 loops=20)
            -> Single-row index lookup on p using PRIMARY (id=rp.product_id)  (cost=0.922 rows=1) (actual time=0.00476..0.00479 rows=1 loops=20)
 |
```

총 정렬 시간이 232ms로 요구치를 충족시켰다.  
이것으로 조회비율이 가장 큰 쿼리를 캐싱과 같은 인프라비용 대신 쿼리성능 개선을 통해  
효율적으로 사용자들에게 인기상품 목록을 보여줄 수 있게 됐다.

## PublishedCoupon 조회 인덱싱

PublishedCoupon 조회는 서비스 중 주문에 사용된다.
따라서 lock 점유 시간을 줄이기 위해 쿠폰 조회에 대해 성능개선을 실시한다.

```sql
explain analyze
select *
from published_coupons as pc
where pc.user_id = 4
order by created_at
limit 20;
```

```sql
| -> Limit: 20 row(s)  (cost=1.1 rows=1) (actual time=1.44..1.44 rows=0 loops=1)
    -> Sort: pc.created_at, limit input to 20 row(s) per chunk  (cost=1.1 rows=1) (actual time=1.42..1.42 rows=0 loops=1)
        -> Filter: (pc.user_id = 4)  (cost=1.1 rows=1) (actual time=1.34..1.34 rows=0 loops=1)
            -> Table scan on pc  (cost=1.1 rows=1) (actual time=1.32..1.32 rows=0 loops=1)
 |
```

해당 쿼리는 현재 정렬에만 1초 가량이 걸리고 있다.
따라서 정렬에 사용되는 생성시간에 인덱스를 생성하겠다.

```sql
| -> Limit: 20 row(s)  (cost=1.1 rows=1) (actual time=0.224..0.224 rows=0 loops=1)
    -> Sort: pc.created_at, limit input to 20 row(s) per chunk  (cost=1.1 rows=1) (actual time=0.223..0.223 rows=0 loops=1)
        -> Filter: (pc.user_id = 4)  (cost=1.1 rows=1) (actual time=0.199..0.199 rows=0 loops=1)
            -> Table scan on pc  (cost=1.1 rows=1) (actual time=0.197..0.197 rows=0 loops=1)
 |
```

224ms로 대폭 감소한 것을 볼 수 있다.
해당 쿼리는 조회와 주문기능 모두에서 사용되어 많은 성능적 개선을 이룰 수 있을 것이다.

# 결론 및 권고사항

- 인덱스 활용: 필터와 정렬 조건을 복합 인덱스로 활용해 디스크 I/O를 최소화합니다.

- 통계 정보 갱신: 대용량 업데이트 후 ANALYZE 수행으로 최신 통계 유지가 필수적입니다.

- 쿼리 실행 계획 모니터링: 주기적인 EXPLAIN ANALYZE 검토로 회귀를 방지합니다.

- 인프라 비용 절감: 불필요한 캐싱 대신 DB 레벨 최적화로 비용 효율성을 확보했습니다.

위 최적화 방안을 적용함으로써 인기상품 조회 및 쿠폰 조회 시 응답 시간을 10배 이상 단축하였으며, 사용자 경험 개선과 함께 서버 리소스 절감 효과를 동시에 달성하였습니다.

