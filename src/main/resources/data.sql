alter table ranked_products
    add index ranked_products_total_selling_count (total_selling_count);
alter table products
    add index products_price (price);

alter table published_coupons
    add index pc_created_at (created_at);
set session cte_max_recursion_depth = 1000000;
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
                       where n < 1000000)
select name,
       stock_number,
       price,
       created_at,
       updated_at
from cte;



insert into ranked_products (product_id, total_selling_count, total_income, created_at, updated_at, created_date)
with recursive cte AS (select 1                                                AS n,
                              floor(rand() * 100000) + 1                       as product_id,
                              floor(rand() * 1000) + 1                         as total_selling_count,
                              round(rand() * 99000 + 1000, 2)                  as total_income,
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 90) DAY) AS created_at,
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 7) DAY)  AS updated_at,
                              CURDATE()                                        as created_date
                       union all
                       select n + 1,
                              floor(rand() * 100000) + 1,
                              floor(rand() * 1000) + 1,
                              round(rand() * 99000 + 1000, 2),
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 90) DAY),
                              DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 7) DAY),
                              CURDATE()
                       from cte
                       where n < 1000000)
select product_id,
       total_selling_count,
       total_income,
       created_at,
       updated_at,
       created_date
from cte;



