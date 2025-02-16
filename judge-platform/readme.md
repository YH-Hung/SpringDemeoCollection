# Spring Boot Webflux and R2DBC

## Database setup

### Postgres

#### Create container

```
docker run --detach --name some-postgres -p 5432:5432 --env POSTGRES_PASSWORD=postgres postgres:15.4-alpine
```

#### Create tables

```postgresql
CREATE TABLE shape_recognize_main (
    uid serial primary key,
    picture_id integer not null,
    recognize_date timestamp
);

CREATE TABLE shape_recognize_attr (
    uid serial primary key,
    recognize_id integer not null,
    img_url varchar(256),
    shape_code_auto char(2) not null,
    shape_code_man char(2),
    update_dt timestamp
);

CREATE INDEX shape_recognize_attr_index_rid ON shape_recognize_attr (recognize_id);
```