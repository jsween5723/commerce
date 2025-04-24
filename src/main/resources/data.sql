set session cte_max_recursion_depth = 1000;


insert into products (name, stock_number, price, created_at, updated_at)
with recursive cte AS (select 1                                                AS n,
                              left(md5(rand()), 20)                            as name,
                              floor(rand() * 10) + 1                           as stock_number,
                              round(rand() * 990 + 10, 2)                      as price,
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 90) DAY) AS created_at,
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 7) DAY)  AS updated_at
                       union all
                       select n + 1,
                              left(md5(rand()), 20),
                              floor(rand() * 10) + 1,
                              round(rand() * 990 + 10, 2),
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 90) DAY),
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 7) DAY)
                       from cte
                       where n < 1000)
select name,
       stock_number,
       price,
       created_at,
       updated_at
from cte;


insert into ranked_products (product_id, total_selling_count, total_income, created_at, updated_at, created_date)
with recursive cte AS (select 1                                                AS n,
                              floor(rand() * 100) + 1                          as product_id,
                              floor(rand() * 1000) + 1                         as total_selling_count,
                              round(rand() * 99000 + 1000, 2)                  as total_income,
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 90) DAY) AS created_at,
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 7) DAY)  AS updated_at,
                              CURDATE()                                        as created_date
                       union all
                       select n + 1,
                              floor(rand() * 100) + 1,
                              floor(rand() * 1000) + 1,
                              round(rand() * 99000 + 1000, 2),
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 90) DAY),
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 7) DAY),
                              CURDATE()
                       from cte
                       where n < 1000)
select product_id,
       total_selling_count,
       total_income,
       created_at,
       updated_at,
       created_date
from cte;


INSERT INTO coupons
(name, description, publish_from, publish_to,
 expire_duration, discount_type, discount_amount,
 stock, created_at, updated_at)
WITH RECURSIVE cte AS (SELECT 1                                                                 AS n,
                              CONCAT('Coupon ', LPAD(FLOOR(RAND() * 9000) + 1000, 4, '0'))      AS name,
                              CONCAT('Description ', LPAD(FLOOR(RAND() * 9000) + 1000, 4, '0')) AS description,
                              -- publish_from: 과거 0~6일 사이 임의
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 7) DAY)                   AS publish_from,
                              -- publish_to: 미래 0~13일 사이 임의
                              DATE_ADD(NOW(), INTERVAL FLOOR(RAND() * 14) DAY)                  AS publish_to,
                              FLOOR(RAND() * 2592000)                                           AS expire_duration,
                              ELT(FLOOR(RAND() * 2) + 1, 'FIXED', 'PERCENT')                    AS discount_type,
                              ROUND(RAND() * 1000 + 1, 2)                                       AS discount_amount,
                              FLOOR(RAND() * 100 + 1)                                           AS stock,
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)                 AS created_at,
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)                 AS updated_at
                       UNION ALL
                       SELECT n + 1,
                              CONCAT('Coupon ', LPAD(FLOOR(RAND() * 9000) + 1000, 4, '0')),
                              CONCAT('Description ', LPAD(FLOOR(RAND() * 9000) + 1000, 4, '0')),
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 7) DAY),
                              DATE_ADD(NOW(), INTERVAL FLOOR(RAND() * 14) DAY),
                              FLOOR(RAND() * 2592000),
                              ELT(FLOOR(RAND() * 2) + 1, 'FIXED', 'PERCENT'),
                              ROUND(RAND() * 1000 + 1, 2),
                              FLOOR(RAND() * 100 + 1),
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY),
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)
                       FROM cte
                       WHERE n < 1000)
SELECT name,
       description,
       publish_from,
       publish_to,
       expire_duration,
       discount_type,
       discount_amount,
       stock,
       created_at,
       updated_at
FROM cte;