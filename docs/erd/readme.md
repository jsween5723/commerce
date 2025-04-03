## ERD

관계만 표시합니다.


```mermaid
    erDiagram
        user_points 
        registered_coupons }|--|| coupons : registered
        coupon_inventories }|--|| coupons : stocked
        coupon_inventory_histories }|--||coupons : logged
        products ||--|{ order_items : ordered
        products ||--o{ product_inventories : stoked
        products ||--o{ product_inventory_histories: logged
        products ||--o| ranked_products: ranked
        order_items }|--|| orders: includes
        orders ||--|| order_payments: payed
```