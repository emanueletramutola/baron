DROP VIEW IF EXISTS sonde_history_for_product;

create view sonde_history_for_product as
select *
from sonde_history_final
where
--WMO CIMO
        id_table3685 in (131,41,116,6,18,129,23,24,43,47,130,33,34,66,84)
--RS92
   or id_table3685 in (12,77,112,113,114);