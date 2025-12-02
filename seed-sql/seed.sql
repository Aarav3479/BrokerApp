SELECT 'CREATE DATABASE order_service'
  WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'order_service')
\gexec

\connect order_service;

CREATE TABLE IF NOT EXISTS orders (
    order_id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255),
    stock_symbol VARCHAR(50),
    quantity INT,
    price DOUBLE PRECISION,
    type VARCHAR(20),
    timestamp TIMESTAMPTZ NULL
);

SELECT 'CREATE DATABASE user_service'
  WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'user_service')
\gexec

\connect user_service;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255)
);

SELECT 'CREATE DATABASE trade_service'
  WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'trade_service')
\gexec

\connect trade_service;

CREATE SEQUENCE trade_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS trade (
    trade_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    email VARCHAR(255),
    stock_symbol VARCHAR(255),
    quantity INTEGER,
    price DOUBLE PRECISION,
    order_timestamp TIMESTAMPTZ,
    trade_timestamp TIMESTAMPTZ,
    order_type VARCHAR(10),
    PRIMARY KEY (trade_id, order_id)
);

SELECT 'CREATE DATABASE portfolio_service'
  WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'portfolio_service')
\gexec

\connect portfolio_service;

CREATE TABLE IF NOT EXISTS portfolio (
    portfolio_id BIGSERIAL PRIMARY KEY,
    total_value DOUBLE PRECISION,
    email VARCHAR(255),
    last_updated TIMESTAMPTZ NULL
);

CREATE TABLE IF NOT EXISTS portfolio_stock (
    stock_id BIGSERIAL PRIMARY KEY,
    stock_symbol VARCHAR(50),
    quantity INT,
    average_price DOUBLE PRECISION,
    last_updated TIMESTAMPTZ NULL,
    portfolio_id BIGINT,
    CONSTRAINT fk_portfolio_stock_portfolio
        FOREIGN KEY (portfolio_id)
        REFERENCES portfolio(portfolio_id)
        ON DELETE CASCADE
);

