create table projects
(
    id           int generated by default as identity,
    created_date timestamp,
    description  varchar(1024),
    photo_id     varchar(64),
    type         varchar(64) not null,
    list_order   smallint    not null default 0,
    color        varchar(6),
    title        varchar(128),
    primary key (id)
);
GO
