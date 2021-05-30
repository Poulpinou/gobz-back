create table projects
(
    id          bigint       not null auto_increment,
    created_at  datetime     not null,
    created_by  bigint       not null,
    updated_by  bigint,
    updated_at  datetime,
    description varchar(255),
    name        varchar(255) not null,
    shared      bit,
    primary key (id)
) engine = InnoDB;

create table project_members
(
    id         bigint not null auto_increment,
    role       varchar(255),
    project_id bigint,
    user_id    bigint,
    primary key (id)
) engine = InnoDB;

alter table project_members
    add constraint project_members_project_user_unique_constraint unique (project_id, user_id);

alter table project_members
    add constraint project_members_projects_foreign_key foreign key (project_id) references projects (id);

alter table project_members
    add constraint project_members_users_foreign_key foreign key (user_id) references users (id);

