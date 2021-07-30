create table persons (
   uuid varchar(36) not null,
    body clob,
    created_date timestamp,
    first_name varchar(128),
    gender varchar(32),
    last_name varchar(128),
    updated_date timestamp,
    dob_cal_id bigint,
    primary key (uuid)
)
GO
alter table persons add constraint fk_dob_cal_id foreign key (dob_cal_id) references calendar_units
