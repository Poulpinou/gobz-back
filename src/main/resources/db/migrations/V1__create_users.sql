create table users
(
    id             bigint       not null auto_increment,
    created_at     datetime     not null,
    updated_at     datetime,
    email          varchar(255) not null,
    email_verified bit          not null,
    image_url      varchar(255),
    name           varchar(255) not null,
    password       varchar(255),
    provider       varchar(255),
    provider_id    varchar(255),
    primary key (id)
) engine = InnoDB;

alter table users
    add constraint users_email_unique_constraint unique (email)
