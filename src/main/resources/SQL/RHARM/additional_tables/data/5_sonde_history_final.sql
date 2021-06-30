DROP TABLE IF EXISTS sonde_history_final cascade;

--sonde_history
select igraid as idstation, code as tac_code, begin as date_from, enddate as date_to
	into sonde_history_final 
from sonde_history;

alter table sonde_history_final add column id_table3685 integer;
alter table sonde_history_final add column bufr_code integer;
		
--sonde_history_ecmwf
insert into sonde_history_final (idstation, tac_code, bufr_code, date_from, date_to)
select idstation, radiosonde_type, bufr_radiosonde_type, date, (date + 1 * interval '1 month') - interval '1 day'
from sonde_history_ecmwf
inner join 
	station on sonde_history_ecmwf.wmoid = station.wmoid 
		and network = 'GUAN' 
where verified = true;

update sonde_history_final
set id_table3685 = table3685.id, bufr_code = table3685.bufr_code
from table3685
where sonde_history_final.tac_code = table3685.tac_code 
		and sonde_history_final.date_from between table3685.date_from and coalesce(table3685.date_to, now());
		
delete from sonde_history_final
where id_table3685 is null;
