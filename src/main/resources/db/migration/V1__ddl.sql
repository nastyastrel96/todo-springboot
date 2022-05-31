create table users
(
    id         serial                                                   primary key,
    first_name varchar(255)                                                not null,
    last_name  varchar(255)                                                not null,
    login      varchar(255)                                                not null
        constraint login                                                     unique,
    password   varchar(255)                                                not null,
    enabled    boolean default true                                        not null
);

create table items
(
    id            serial                                                   primary key,
    description   varchar(255)                                                not null,
    state         varchar(255)                                                not null,
    creation_date timestamp                                                   not null,
    users_id      integer                                                     not null
        constraint todo_items_user_fkey references users
);

create table roles
(
    id   serial                                                  primary key,
    role varchar(255)                                                not null
);


create table user_role
(
    users_id integer not null        constraint user_role_user_id_fkey references users,
    roles_id integer not null        constraint user_role_role_id_fkey references roles
);


create table tags
(
    id      serial                                                                  primary key,
    tag      varchar(255)                                                              not null,
    users_id integer                                                                    not null
constraint todo_tags_todo_users_id_fkey references users, constraint unique_tag_for_user unique (tag, users_id)
);


create table item_tag
(
    items_id integer not null constraint item_tag_todo_items_id_fkey references items,
    tags_id  integer not null constraint item_tag_todo_tags_id_fkey references tags
);


