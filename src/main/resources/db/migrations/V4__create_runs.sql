create table runs
(
    id         bigint   not null auto_increment,
    created_at datetime not null,
    updated_at datetime,
    limit_date date,
    prout      varchar(255),
    status     varchar(255),
    member_id  bigint,
    step_id    bigint,
    primary key (id)
) engine = InnoDB;

alter table runs
    add constraint runs_project_member_foreign_key foreign key (member_id) references project_members (id);

alter table runs
    add constraint runs_step_foreign_key foreign key (step_id) references steps (id);


create table run_task
(
    id        bigint not null auto_increment,
    abandoned bit    not null,
    run_id    bigint,
    task_id   bigint,
    primary key (id)
) engine = InnoDB;

alter table run_task
    add constraint run_tasks_run_foreign_key foreign key (run_id) references runs (id);

alter table run_task
    add constraint run_task_task_foreign_key foreign key (task_id) references tasks (id);
