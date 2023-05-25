create TABLE
    student (
        id SERIAL PRIMARY KEY,
        name TEXT NOT NULL
);

insert into student (id, name) values (1, 'Ade'), (2, 'Ola'), (3, 'David');

-- Test
drop table IF EXISTS user_authority;
drop table IF EXISTS authority;
drop table IF EXISTS users;

create TABLE users
(
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    username            VARCHAR(200) UNIQUE NOT NULL,
    password            VARCHAR(256)     NOT NULL,
    first_name          VARCHAR(100),
    last_name           VARCHAR(100),
    email               VARCHAR(50)
);

create TABLE authority
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

create TABLE user_authority
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT comment 'user authority id',
    user_id      BIGINT comment 'User id',
    authority_id VARCHAR(50) comment 'authority id'
);

insert into users (id, username, password, first_name, last_name, email)
values (100, 'admin@example.com', '$2a$04$Ot6tX0QK8xzo/xW5A/J3F.QZDS7eio095dN5IoQjWJDOySs42f1S.','admin','admin', 'admin@example.com'),
       (200, 'user@example.com', '$2a$04$Ot6tX0QK8xzo/xW5A/J3F.QZDS7eio095dN5IoQjWJDOySs42f1S.', 'user','user','user@example.com');

insert into authority (id, name)
values (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER');

insert into user_authority (user_id, authority_id)
values (100, 1),
       (200, 2);