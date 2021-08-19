create table projects
(
    id           varchar(36) not null,
    created_date timestamp,
    description  varchar(1024),
    photo_id     varchar(64),
    title        varchar(128),
    primary key (id)
)
GO
