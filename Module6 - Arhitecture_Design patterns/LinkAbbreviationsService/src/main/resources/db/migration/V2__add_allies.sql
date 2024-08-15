CREATE TABLE IF NOT EXISTS link_allies
(
    short_link VARCHAR(255) NOT NULL,
    ally_name  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_link_allies PRIMARY KEY (ally_name)
);

ALTER TABLE link_allies DROP CONSTRAINT IF EXISTS fk_link_allies_on_links_pair;
ALTER TABLE link_allies
    ADD CONSTRAINT fk_link_allies_on_links_pair FOREIGN KEY (short_link) REFERENCES links_pair (short_link);