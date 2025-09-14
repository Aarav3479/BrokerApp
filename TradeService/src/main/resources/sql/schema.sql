CREATE TABLE IF NOT EXISTS trade (
    trade_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    email VARCHAR(255),
    stock_symbol VARCHAR(255),
    quantity INTEGER,
    price DOUBLE PRECISION,
    order_timestamp TIMESTAMP WITH TIME ZONE,
    trade_timestamp TIMESTAMP WITH TIME ZONE,
    order_type VARCHAR(10),
    PRIMARY KEY (trade_id, order_id)
);
