# Table Schema

```sql
create table public.job_record_left
(
    seq_id    serial
        primary key,
    category  varchar(10) not null,
    data_time timestamp   not null
);

alter table public.job_record_left
    owner to postgres;

create index job_record_left_category_data_time_index
    on public.job_record_left (category, data_time);

create table public.job_record_right
(
    seq_id    serial
        primary key,
    category  varchar(10) not null,
    data_time timestamp   not null
);

alter table public.job_record_right
    owner to postgres;

create index job_record_right_category_data_time_index
    on public.job_record_right (category, data_time);

create table public.job_data_left
(
    seq_id    serial
        primary key,
    category  varchar(10),
    data_time timestamp
);

alter table public.job_data_left
    owner to postgres;

create table public.job_data_right
(
    seq_id    serial
        primary key,
    category  varchar(10),
    data_time timestamp
);

alter table public.job_data_right
    owner to postgres;


```