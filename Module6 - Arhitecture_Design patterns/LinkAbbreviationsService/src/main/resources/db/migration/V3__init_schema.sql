drop table temporary_links_pair;

alter table links_pair add column expired_in timestamp with time zone;