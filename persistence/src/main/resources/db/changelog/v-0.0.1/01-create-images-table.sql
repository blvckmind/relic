create table images
(
    checksum varchar(64) not null,
    bytes    BLOB        not null,
    primary key (checksum)
)
GO
