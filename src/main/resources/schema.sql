CREATE TABLE product
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(15)   NOT NULL,
    price     INT           NOT NULL CHECK (price >= 0),
    image_url VARCHAR(1024) NOT NULL
);

CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL
);

CREATE TABLE wish (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1 CHECK (quantity > 0)
);