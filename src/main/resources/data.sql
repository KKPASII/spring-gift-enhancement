INSERT INTO product (name, price, image_url) VALUES ('hamburger', 4500, 'https://media.istockphoto.com/id/182744943/ko/%EC%82%AC%EC%A7%84/burger.jpg?s=2048x2048&w=is&k=20&c=Phev16dEc55V8v9t7_ty9fk7T-yq_Fs9Q7wMnPmhMJg=');
INSERT INTO product (name, price, image_url) VALUES ('egg', 2000, 'https://cdn.pixabay.com/photo/2018/07/23/18/43/hens-egg-3557544_1280.jpg');

INSERT INTO member (email, password, role) VALUES ('ham@email.com', '$2a$10$ISTjRWOs8SFhtJzqvE9u5Oh3TwvQxnxsI8RmKFNeSlNZOxOKCLhx.', 'ADMIN');
INSERT INTO member (email, password, role) VALUES ('dam@email.com', '$2a$10$ISTjRWOs8SFhtJzqvE9u5Oh3TwvQxnxsI8RmKFNeSlNZOxOKCLhx.', 'USER');

INSERT INTO wish (member_id, product_id, quantity) VALUES (2, 1, 1);
INSERT INTO wish (member_id, product_id, quantity) VALUES (2, 2, 2);