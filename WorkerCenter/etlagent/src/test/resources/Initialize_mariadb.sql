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

INSERT INTO product VALUES
    (1, 'Apple', STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T'), STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T')),
    (2, 'Watch', STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T'), STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T')),
    (3, 'iPhone', STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T'), STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T')),
    (4, 'Mac mini', STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T'), STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T')),
    (5, 'iMac', STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T'), STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T')),
    (6, 'Mac Pro', STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T'), STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T')),
    (7, 'iPad', STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T'), STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T')),
    (8, 'AirPods', STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T'), STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T')),
    (9, 'MacBook', STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T'), STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T')),
    (10, 'HomePod', STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T'), STR_TO_DATE('2023-09-11 12:34:56', '%Y-%m-%d %T'))
;

INSERT INTO product_workflow_main VALUES
    (1, 1, STR_TO_DATE('2023-09-11 12:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 12:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 12:22:33', '%Y-%m-%d %T')),
    (2, 2, STR_TO_DATE('2023-09-11 13:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 13:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 13:22:33', '%Y-%m-%d %T')),
    (3, 3, STR_TO_DATE('2023-09-11 14:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 14:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 14:22:33', '%Y-%m-%d %T')),
    (4, 4, STR_TO_DATE('2023-09-11 15:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 15:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 15:22:33', '%Y-%m-%d %T')),
    (5, 5, STR_TO_DATE('2023-09-11 16:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 16:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 16:22:33', '%Y-%m-%d %T')),
    (6, 6, STR_TO_DATE('2023-09-11 17:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 17:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 17:22:33', '%Y-%m-%d %T')),
    (7, 7, STR_TO_DATE('2023-09-11 18:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 18:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 18:22:33', '%Y-%m-%d %T')),
    (8, 2, STR_TO_DATE('2023-09-12 09:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 09:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 09:22:33', '%Y-%m-%d %T')),
    (9, 4, STR_TO_DATE('2023-09-12 10:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 10:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 10:22:33', '%Y-%m-%d %T')),
    (10, 6, STR_TO_DATE('2023-09-12 11:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 11:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 11:22:33', '%Y-%m-%d %T')),
    (11, 8, STR_TO_DATE('2023-09-12 12:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 12:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 12:22:33', '%Y-%m-%d %T')),
    (12, 10, STR_TO_DATE('2023-09-12 13:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 13:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 13:22:33', '%Y-%m-%d %T')),
    (13, 8, STR_TO_DATE('2023-09-13 02:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-13 02:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-13 02:22:33', '%Y-%m-%d %T')),
    (14, 9, STR_TO_DATE('2023-09-13 03:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-13 03:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-13 03:22:33', '%Y-%m-%d %T'))
;

INSERT INTO product_workflow_attr VALUES
    (1, 'inspDt','20230911_122233', STR_TO_DATE('2023-09-11 12:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 12:22:33', '%Y-%m-%d %T')),
    (2, 'receive','a optical',STR_TO_DATE('2023-09-11 13:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 13:22:33', '%Y-%m-%d %T')),
    (3,'inspDt','20230911_142233',STR_TO_DATE('2023-09-11 14:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 14:22:33', '%Y-%m-%d %T')),
    (4,'inspDt','20230911_152233',STR_TO_DATE('2023-09-11 15:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 15:22:33', '%Y-%m-%d %T')),
    (5,'inspDt','20230911_162233',STR_TO_DATE('2023-09-11 16:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 16:22:33', '%Y-%m-%d %T')),
    (6,'receive','other optical',STR_TO_DATE('2023-09-11 17:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 17:22:33', '%Y-%m-%d %T')),
    (7,'inspDt','20230911_182233',STR_TO_DATE('2023-09-11 18:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-11 18:22:33', '%Y-%m-%d %T')),
    (8,'inspDt','20230912_092233',STR_TO_DATE('2023-09-12 09:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 09:22:33', '%Y-%m-%d %T')),
    (9,'receive','yet optical',STR_TO_DATE('2023-09-12 10:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 10:22:33', '%Y-%m-%d %T')),
    (10,'inspDt','20230912_112233',STR_TO_DATE('2023-09-12 11:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 11:22:33', '%Y-%m-%d %T')),
    (11,'inspDt','20230912_122233',STR_TO_DATE('2023-09-12 12:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 12:22:33', '%Y-%m-%d %T')),
    (12,'inspDt','20230912_132233',STR_TO_DATE('2023-09-12 13:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-12 13:22:33', '%Y-%m-%d %T')),
    (13,'receive','fine optical',STR_TO_DATE('2023-09-13 02:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-13 02:22:33', '%Y-%m-%d %T')),
    (14,'inspDt','20230913_032233',STR_TO_DATE('2023-09-13 03:22:33', '%Y-%m-%d %T'),STR_TO_DATE('2023-09-13 03:22:33', '%Y-%m-%d %T'))
;