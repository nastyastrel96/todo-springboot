INSERT INTO users(first_name, last_name, login, password, enabled)
VALUES ('Анастасия', 'Стрельцова', 'nastyastrel', '$2a$12$8dezAsJW3mmVORcu/aQpveP.JFakQwJU95ge9afycy6Np6A/B.yZC', true);

INSERT INTO items(description, state, creation_date, users_id)
VALUES ('Записаться к стоматологу', 'DONE', current_timestamp, 1), ('Провести занятие', 'UNDONE', current_timestamp, 1);

INSERT INTO tags(tag, users_id)
VALUES ('Срочно', 1), ('With joke', 1), ('In the evening', 1);

INSERT INTO roles(id, role)
VALUES (1, 'ADMIN'), (2, 'USER');

INSERT INTO item_tag(items_id, tags_id)
VALUES (1, 2), (2, 3);

INSERT INTO user_role(users_id, roles_id)
VALUES (1, 2);