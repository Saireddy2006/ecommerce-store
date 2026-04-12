-- V1__init.sql

CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          price NUMERIC(10,2) NOT NULL,
                          stock INTEGER NOT NULL
);

CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        customer_name VARCHAR(255) NOT NULL,

                        order_status VARCHAR(50) NOT NULL DEFAULT 'PLACED',
                        payment_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                        shipping_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',

                        subtotal NUMERIC(10,2) NOT NULL DEFAULT 0,
                        shipping_fee NUMERIC(10,2) NOT NULL DEFAULT 0,
                        total_amount NUMERIC(10,2) NOT NULL DEFAULT 0,

                        created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE order_items (
                             id BIGSERIAL PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             product_id BIGINT NOT NULL,
                             product_name VARCHAR(255) NOT NULL,
                             unit_price NUMERIC(10,2) NOT NULL,
                             quantity INTEGER NOT NULL,
                             line_total NUMERIC(10,2) NOT NULL,

                             CONSTRAINT fk_order_items_order
                                 FOREIGN KEY (order_id) REFERENCES orders (id),

                             CONSTRAINT fk_order_items_product
                                 FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_orders_status ON orders(order_status);
