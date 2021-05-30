create table chapters
(
    id          bigint       not null auto_increment,
    created_at  datetime     not null,
    created_by  bigint       not null,
    updated_by  bigint,
    updated_at  datetime,
    description varchar(255),
    name        varchar(255) not null,
    project_id  bigint,
    primary key (id)
) engine = InnoDB;

alter table chapters
    add constraint chapters_project_foreign_key foreign key (project_id) references projects (id);


create table steps
(
    id          bigint       not null auto_increment,
    created_at  datetime     not null,
    created_by  bigint       not null,
    updated_by  bigint,
    updated_at  datetime,
    description varchar(255),
    name        varchar(255) not null,
    chapter_id  bigint,
    primary key (id)
) engine = InnoDB;

alter table steps
    add constraint steps_chapter_foreign_key foreign key (chapter_id) references chapters (id);


create table tasks
(
    id         bigint       not null auto_increment,
    created_at datetime     not null,
    created_by bigint       not null,
    updated_by bigint,
    updated_at datetime,
    done       bit,
    text       varchar(255) not null,
    step_id    bigint,
    primary key (id)
) engine = InnoDB;

alter table tasks
    add constraint tasks_step_foreign_key foreign key (step_id) references steps (id)

