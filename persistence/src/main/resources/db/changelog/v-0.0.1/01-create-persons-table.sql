create table persons
(
    id           varchar(36) not null,
    color_number smallint,
    created_date timestamp,
    first_name   varchar(128),
    gender       varchar(16),
    info         clob,
    last_name    varchar(128),
    other_names  varchar(384),
    patronymic   varchar(128),
    photo_id     varchar(64),
    trust_level  varchar(16),
    updated_date timestamp,
    dob_cal_id   int,
    project_id   int,
    primary key (id)
);

alter table persons
    add constraint fk_dob_cal_id foreign key (dob_cal_id) references calendar_units;

alter table persons
    add constraint fk_project_id foreign key (project_id) references projects;

GO