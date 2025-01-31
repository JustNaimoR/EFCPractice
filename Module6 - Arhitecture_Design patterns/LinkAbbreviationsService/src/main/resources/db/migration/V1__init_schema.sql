CREATE TABLE IF NOT EXISTS links_pair
(
    short_link VARCHAR(255) NOT NULL,
    src_link   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_links_pair PRIMARY KEY (short_link)
);

CREATE TABLE IF NOT EXISTS temporary_links_pair
(
    short_link VARCHAR(255) NOT NULL,
    expired_in TIME WITHOUT TIME ZONE,
    CONSTRAINT pk_temporary_links_pair PRIMARY KEY (short_link)
);

ALTER TABLE links_pair DROP CONSTRAINT IF EXISTS uc_links_pair_src_link;
ALTER TABLE links_pair ADD CONSTRAINT uc_links_pair_src_link UNIQUE (src_link) ;

ALTER TABLE temporary_links_pair DROP CONSTRAINT IF EXISTS FK_TEMPORARY_LINKS_PAIR_ON_SHORT_LINK;
ALTER TABLE temporary_links_pair
    ADD CONSTRAINT FK_TEMPORARY_LINKS_PAIR_ON_SHORT_LINK FOREIGN KEY (short_link) REFERENCES links_pair (short_link);

-- отдельная последовательность для создания уникальных укороченных ссылок
CREATE SEQUENCE IF NOT EXISTS shorten_link_id_seq AS BIGINT START WITH 1 INCREMENT BY 1;