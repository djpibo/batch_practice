INSERT INTO orders (order_id, customer_name, order_date, total_amount)
VALUES
    (1, 'John Doe', '2022-01-01', 50.00),
    (2, 'Jane Smith', '2022-01-02', 75.99),
    (3, 'Alice Johnson', '2022-01-03', 120.50);

INSERT INTO settlements (settlement_id, order_id, settlement_date, amount)
VALUES
    (101, 1, '2022-01-05', 50.00),
    (102, 2, '2022-01-06', 75.99),
    (103, 3, '2022-01-08', 120.50);
