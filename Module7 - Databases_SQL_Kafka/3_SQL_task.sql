-- Q1
-- Релизы с Винилом
with vinyl12_medium_releases(release_id) as (
    select med.release from medium med
                                join medium_format form on med.format = form.id
    where form.name = '12" Vinyl'
),
-- Релизы до 1970 в UK
     releases_before_1970_UK(release_id) as (
         select rel.release, rel.date_year from release_info rel
                                                    join area a on rel.area = a.id
         where a.name = 'United Kingdom' and rel.date_year < 1970
     ),
-- Beatles releases
     beatles_releases(release_id) as (
         select rel.id from artist_credit creds
                                join release rel on creds.id = rel.artist_credit
         where creds.name = 'The Beatles'
     )

select rel.name as RELEASE_NAME, min(date_year) as RELEASE_YEAR
from release rel
         join vinyl12_medium_releases cte1 on rel.id = cte1.release_id
         join releases_before_1970_UK cte2 on rel.id = cte2.release_id
         join beatles_releases cte3 on rel.id = cte3.release_id
group by rel.name
order by RELEASE_YEAR, RELEASE_NAME;



-- Q2
-- Релизы с Cassette форматом
with cassette_releases(release_id) as (
    select med.release from medium med
                                join medium_format form on med.format = form.id
    where form.name = 'Cassette'
),
-- Релизы - данные со временем и их исполнители
     releases_with_info(release_id, release_name, artist_name, release_year, release_month, release_day) as (
         select r.id, r.name, ac.name, ri.date_year, ri.date_month, ri.date_day from release r
                                                                                         join artist_credit ac on r.artist_credit = ac.id
                                                                                         join release_info ri on r.id = ri.release
         where date_year is not null and date_month is not null and date_day is not null
     )

select release_name, artist_name, release_year
from releases_with_info rwi
         join cassette_releases cr on rwi.release_id = cr.release_id
order by release_year desc, release_month desc, release_day desc, release_name, artist_name
limit 10;



select * from artist_credit;

-- Q3
-- Список людей с Elvis
with people(artist_id, artist_name) as (
    select a.id, a.name from artist a
                                 join artist_type at on a.type = at.id
    where at.name = 'Person' and a.name like 'Elvis%'
),
-- Релизы каждого артиста
     artists_releases(artist_id, release_id, release_month) as (
         select a.id, r.id, ro.date_month from artist a
                                                   join artist_credit_name acn on a.id = acn.artist
                                                   join artist_credit ac on acn.artist_credit = ac.id
                                                   join release r on ac.id = r.artist_credit
                                                   join release_info ro on ro.release = r.id
         where ro.date_month is not null
     ),
-- Общий join таблиц
     tables_join(artist_id, artist_name, release_id, release_month) as (
         select p.artist_id, p.artist_name, ar.release_id, ar.release_month from people p
                                                                                     join artists_releases ar on p.artist_id = ar.artist_id
     ),
-- Группировка по каждому артисту и месяцу + количество
     table_info(artist_id, artist_name, release_month, count) as (
         select tj.artist_id, tj.artist_name, tj.release_month, count(*)
         from tables_join tj
         group by tj.artist_id, tj.artist_name, tj.release_month
     )

select tj.artist_name                          as ARTIST_NAME,
       (select release_month
        from table_info ti
        where ti.artist_name = tj.artist_name
        order by ti.count desc, ti.release_month
        limit 1)                               as RELEASE_MONTH,
       (select max(count)
        from table_info ti
        where ti.artist_name = tj.artist_name) as NUM_RELEASES
from tables_join tj
group by tj.artist_name
order by NUM_RELEASES desc, ARTIST_NAME;



-- Q4
-- Таблица диапазона от 1900 до 2000
with recursive years (year) as (
    select 1900
    union all
    select year + 10 from years
    where year < 2000
),
-- Список групп
               groups as (
                   select * from artist a
                                     join artist_type at on a.type = at.id
                   where at.name = 'Group' and begin_date_year is not null and end_date_year is not null
               )

select concat(year, 's') as DECADE,
       (select count(*) from groups where begin_date_year between year and year + 10 and end_date_year > year + 10) as NUM_ARTIST_GROUP
from years
order by 1;



-- Q5
-- Список людей с именем на %John
with people (artist_id, artist_name) as (
    select a.id, a.name
    from artist a
             join artist_type at on a.type = at.id
    where at.name = 'Person' and a.name like '%John'
),
-- join с aliases с псевдонимами без John
     people_aliases (artist_id, artist_name, aliases_id, aliases_name) as (
         select artist_id, artist_name, aa.id, aa.name
         from people p
                  join artist_alias aa on p.artist_id = aa.artist
         where aa.name not like '%John%' and aa.name not like '%JOHN%'
     )

select artist_name, count(*) as NUM_ALIASES, string_agg(aliases_name, ', ') as COMMA_SEPARATED_LIST_OF_ALIASES
from people_aliases pa
group by artist_name
order by 1;