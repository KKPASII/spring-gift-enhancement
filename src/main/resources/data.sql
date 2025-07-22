INSERT INTO product (name, price, image_url) VALUES ('eggsA1', 1000, 'https://cdn.pixabay.com/photo/2018/07/23/18/43/hens-egg-3557544_1280.jpg');
INSERT INTO product (name, price, image_url) VALUES ('eggsB1', 2000, 'https://cdn.pixabay.com/photo/2018/07/23/18/43/hens-egg-3557544_1280.jpg');
INSERT INTO product (name, price, image_url) VALUES ('eggsC1', 3000, 'https://cdn.pixabay.com/photo/2018/07/23/18/43/hens-egg-3557544_1280.jpg');

INSERT INTO member (email, password, role) VALUES ('ham@email.com', 'qwer123!', 'ADMIN');
INSERT INTO member (email, password, role) VALUES ('dam@email.com', 'qwer123!', 'USER');

INSERT INTO wish (member_id, product_id, quantity) VALUES (2, 1, 1);

INSERT INTO option (product_id, name, quantity) VALUES (1, '계란 (특란)', 2);
INSERT INTO option (product_id, name, quantity) VALUES (1, '계란 (왕란)', 3);

INSERT INTO option (product_id, name, quantity) VALUES (2, '계란 (중란)', 1);
INSERT INTO option (product_id, name, quantity) VALUES (3, '계란 (대란)', 3);
