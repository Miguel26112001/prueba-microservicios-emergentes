-- =====================================================
-- PRODUCTS
-- =====================================================

CREATE TABLE products
(
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(150) NOT NULL,

    price NUMERIC(10,2) NOT NULL
        CHECK (price > 0),

    stock INTEGER NOT NULL
        CHECK (stock >= 0),

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- =====================================================
-- ORDERS
-- =====================================================

CREATE TABLE orders
(
    id BIGSERIAL PRIMARY KEY,

    profile_id BIGINT NOT NULL,

    order_date TIMESTAMP NOT NULL,

    total NUMERIC(12,2) NOT NULL
        CHECK (total >= 0),

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- =====================================================
-- ORDER DETAILS
-- =====================================================

CREATE TABLE order_details
(
    id BIGSERIAL PRIMARY KEY,

    order_id BIGINT NOT NULL,

    product_id BIGINT NOT NULL,

    quantity INTEGER NOT NULL
        CHECK (quantity >= 1),

    subtotal NUMERIC(10,2) NOT NULL
        CHECK (subtotal >= 0),

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_order_details_order
        FOREIGN KEY (order_id)
            REFERENCES orders(id)
            ON DELETE CASCADE
);