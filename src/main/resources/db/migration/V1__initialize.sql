-- Таблица пользователей
CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY NOT NULL,
    version  BIGINT                NOT NULL,
    username VARCHAR(255)          NOT NULL,
    password VARCHAR(255)          NOT NULL
);
CREATE INDEX IX_users_username ON users (username);

-- Таблица ролей
CREATE TABLE roles
(
    id      BIGSERIAL PRIMARY KEY NOT NULL,
    version BIGINT                NOT NULL,
    name    VARCHAR(255)          NOT NULL
);
CREATE INDEX IX_roles_name ON roles (name);

-- Таблица сопоставления пользователя и роли
CREATE TABLE users_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    UNIQUE (user_id, role_id)
);

-- Таблица заказчиков
CREATE TABLE customers
(
    id         BIGSERIAL PRIMARY KEY NOT NULL,
    version    BIGINT                NOT NULL,
    user_id    BIGINT                NOT NULL,
    first_name VARCHAR(255)          NOT NULL,
    last_name  VARCHAR(255)          NOT NULL,
    email      VARCHAR(255) UNIQUE,
    address    VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX IX_customer_first_name ON customers (first_name);
CREATE INDEX IX_customer_last_name ON customers (last_name);
CREATE INDEX IX_customer_first_name_last_name ON customers (first_name, last_name);
CREATE INDEX IX_customer_email ON customers (email);

-- Таблица продуктов
CREATE TABLE products
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    version     BIGINT                NOT NULL,
    name        VARCHAR(255)          NOT NULL,
    description VARCHAR(255),
    price       DECIMAL(15, 2)
);
CREATE INDEX IX_product_name ON products (name);

-- Таблица заказов
CREATE TABLE orders
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    version     BIGINT                NOT NULL,
    number      VARCHAR(255)          NOT NULL,
    create_date TIMESTAMP             NOT NULL,
    customer_id BIGINT                NOT NULL,
    recipient   VARCHAR(255),
    address     VARCHAR(255),
    quantity    DECIMAL(15, 3),
    sum         DECIMAL(15, 2),
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);
CREATE INDEX IX_orders_number ON orders (number);

-- Таблица строк заказа
CREATE TABLE orders_item
(
    id         BIGSERIAL PRIMARY KEY NOT NULL,
    version    BIGINT                NOT NULL,
    order_id   BIGINT                NOT NULL,
    product_id BIGINT                NOT NULL,
    quantity   DECIMAL(15, 3)        NOT NULL,
    price      DECIMAL(15, 2)        NOT NULL,
    sum        DECIMAL(15, 3)        NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);

INSERT INTO users (version, username, password)
VALUES (0, 'user', '$2y$12$m0reT42iKtR8v/BoS4hykuZpQI9dFx.cj1u2t/R4sAEA7Qqoygqfe'),
       (0, 'admin', '$2y$12$m0reT42iKtR8v/BoS4hykuZpQI9dFx.cj1u2t/R4sAEA7Qqoygqfe');

INSERT INTO roles (version, name)
VALUES (0, 'ROLE_USER'),
       (0, 'ROLE_ADMIN');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
       (2, 2);

INSERT INTO customers (version, first_name, last_name, email, user_id)
VALUES (0, 'Василий', 'Пупкин', 'pupkin@gmail.com', 1),
       (0, 'Генадий', 'Дудкин', 'dudkin@gmail.com', 1),
       (0, 'Петр', 'Петрович', 'petrovich@gmail.com', 1),
       (0, 'Анатолий', 'Семеныч', 'semenich@gmail.com', 1),
       (0, 'Федор', 'Степаныч', 'stepanich@gmail.com', 2),
       (0, 'Иван', 'Иванов', 'ivanov@gmail.com', 1),
       (0, 'Николай', 'Разрабов', 'razrabov@gmail.com', 2),
       (0, 'Роман', 'Гикбрайнсов', 'geekbrainsov@gmail.com', 2);

INSERT INTO products (version, name, description, price)
VALUES (0, 'Product 1', 'Product 1', 100),
       (0, 'Product 2', 'Product 2', 200),
       (0, 'Product 3', 'Product 3', 300),
       (0, 'Product 4', 'Product 4', 400),
       (0, 'Product 5', 'Product 5', 500),
       (0, 'Product 6', 'Product 6', 600),
       (0, 'Product 7', 'Product 7', 700),
       (0, 'Product 8', 'Product 8', 800),
       (0, 'Product 9', 'Product 9', 900),
       (0, 'Product 10', 'Product 10', 1000),
       (0, 'Product 11', 'Product 11', 2000),
       (0, 'Product 12', 'Product 12', 3000),
       (0, 'Product 13', 'Product 13', 4000),
       (0, 'Product 14', 'Product 14', 5000),
       (0, 'Product 15', 'Product 15', 6000),
       (0, 'Product 16', 'Product 16', 7000),
       (0, 'Product 17', 'Product 17', 8000),
       (0, 'Product 18', 'Product 18', 9000),
       (0, 'Product 19', 'Product 19', 10000),
       (0, 'Product 20', 'Product 20', 11000),
       (0, 'Product 21', 'Product 21', 12000),
       (0, 'Product 22', 'Product 22', 13000),
       (0, 'Product 23', 'Product 23', 14000),
       (0, 'Product 24', 'Product 24', 15000),
       (0, 'Product 25', 'Product 25', 16000),
       (0, 'Product 26', 'Product 26', 17000),
       (0, 'Product 27', 'Product 27', 18000),
       (0, 'Product 28', 'Product 28', 19000),
       (0, 'Product 29', 'Product 29', 20000),
       (0, 'Product 30', 'Product 30', 21000),
       (0, 'Product 31', 'Product 31', 22000),
       (0, 'Product 32', 'Product 32', 23000),
       (0, 'Product 33', 'Product 33', 24000),
       (0, 'Product 34', 'Product 34', 25000),
       (0, 'Product 35', 'Product 35', 26000),
       (0, 'Product 36', 'Product 36', 27000);