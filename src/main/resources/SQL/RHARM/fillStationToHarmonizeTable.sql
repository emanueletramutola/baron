DROP TABLE IF EXISTS station_to_harmonize_new;

select idstation, count(*)
into station_to_harmonize_new
from guan_data_header
where idstation in
      (select distinct idstation
       from sonde_history_for_product)
group by idstation;

truncate table station_to_harmonize;

insert into station_to_harmonize (idstation, launches)
select idstation, count
from station_to_harmonize_new;

drop table station_to_harmonize_new;