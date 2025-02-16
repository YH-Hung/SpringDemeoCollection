# Spring Boot Multiple Datasource with Testcontainers

## Purpose

Assessing the implementation details for following items.

- Multiple datasource with different drivers (MariaDB and Postgres)
- Provide data base url and password via environment variables
- Testcontainers instead of in-memory H2
- Native build

## Database setup

### MariaDB

#### Create container

```
docker run --detach --name some-mariadb -p 3306:3306 --env MARIADB_USER=example-user --env MARIADB_PASSWORD=my_cool_secret --env MARIADB_ROOT_PASSWORD=my-secret-pw  mariadb:lts
```

#### Initialize user

- login as example-user, logout
- login as root

```mariadb
CREATE DATABASE mall;
GRANT ALL ON mall.* TO 'example-user'@'%';
```

- login as example-user

#### Create table

```mariadb
CREATE TABLE product
(
    product_id         INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    product_name       VARCHAR(128)  NOT NULL,
    created_date       TIMESTAMP    NOT NULL,
    last_modified_date TIMESTAMP    NOT NULL
);

CREATE TABLE product_workflow_main
(
    wid                INT AUTO_INCREMENT PRIMARY KEY,
    product_id         INT NOT NULL,
    start_date         TIMESTAMP,
    create_date        TIMESTAMP,
    last_modified_date TIMESTAMP,
    CONSTRAINT fk_product_workflow
        FOREIGN KEY (product_id) REFERENCES product (product_id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT
);

CREATE TABLE product_workflow_attr
(
    wid                INT NOT NULL,
    workflow_type      VARCHAR(20),
    val                CHAR(17),
    create_date        TIMESTAMP,
    last_modified_date TIMESTAMP,
    PRIMARY KEY (wid, workflow_type),
    CONSTRAINT fk_workflow_main
        FOREIGN KEY (wid) REFERENCES product_workflow_main (wid)
            ON DELETE CASCADE
            ON UPDATE RESTRICT
);

CREATE TABLE product_workflow_adopt
(
    uid                INT AUTO_INCREMENT PRIMARY KEY,
    product_id         INT NOT NULL,
    inspect_date       TIMESTAMP,
    adopt              CHAR(1),
    wid                INT,
    create_date        TIMESTAMP,
    last_modified_date TIMESTAMP,
    CONSTRAINT fk_product_workflow_adopt_productId
        FOREIGN KEY (product_id) REFERENCES product (product_id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT  
);
```

### Postgres

#### Create container

```
docker run --detach --name some-postgres -p 5432:5432 --env POSTGRES_PASSWORD=postgres postgres:15.4-alpine
```

#### Create table

```postgresql
CREATE TABLE product_Inspect
(
    uid                serial primary key,
    product_id         int not null,
    inspect_date       timestamp,
    create_date        timestamp,
    last_modified_date timestamp
);
```
