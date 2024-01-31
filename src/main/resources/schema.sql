DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    customer_name VARCHAR(255),
    order_date DATE,
    total_amount DECIMAL(10, 2)
);
DROP TABLE IF EXISTS settlements;
CREATE TABLE settlements (
    settlement_id INT PRIMARY KEY,
    order_id INT,
    settlement_date DATE,
    amount DECIMAL(10, 2)
);
