#!/bin/sh

#PATHSCRIPT="/home/emanuele/ownCloud/src/baron/src/main/resources/SQL"
PATHSCRIPT="/Dati_2TB/owncloud/src/baron/src/main/resources/SQL"

dropdb --if-exists baron

psql -q -f $PATHSCRIPT/initUsers.sql

createdb baron -O baron

psql baron -q -f $PATHSCRIPT/GRUAN/gruan_tables.sql
psql baron -q -f $PATHSCRIPT/IGRA/igra_tables.sql
psql baron -q -f $PATHSCRIPT/RHARM/rharm_tables.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/tables/1_table3685.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/tables/2_station.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/tables/3_guan_data_header_source.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/tables/4_harmonization_break.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/tables/5_sonde_history.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/tables/6_sonde_history_ecmwf.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/tables/7_stationToHarmonize.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/tables/8_tl_smooth_rs_92.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/tables/9_wind_rs92_corr.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/tables/10_wmo_intercomparison_2010.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/tables/11_wmo_min_pressure_level.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/tables/12_wndeq_history.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/tables/13_gruan_radiation.sql

psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/data/1_station.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/data/2_table3685.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/data/3_sonde_history.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/data/4_sonde_history_ecmwf.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/data/5_sonde_history_final.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/data/6_sonde_history_for_product.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/data/7_guan_data_header_source.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/data/8_tl_smooth_rs_92.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/data/9_wind_rs92_corr.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/data/10_wmo_intercomparison_2010.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/data/11_wmo_min_pressure_level.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/data/12_wndeq_history.sql
psql baron -q -f $PATHSCRIPT/RHARM/additional_tables/data/13_gruan_radiation.sql

for tbl in `psql -qAt -c "select tablename from pg_tables where schemaname = 'public';" baron` ; do  psql -q -c "alter table \"$tbl\" owner to baron" baron ; done
for fct in `psql -qAt -c "SELECT routine_name FROM information_schema.routines WHERE routine_type='FUNCTION' AND specific_schema='public' AND specific_catalog = 'baron';" baron` ; do  psql -q -c "alter function \"$fct\" owner to baron" baron ; done

echo "installation completed successfully"

#psql baron -q -f $PATHSCRIPT/communication_method.sql
#psql baron -q -f $PATHSCRIPT/crs.sql
#psql baron -q -f $PATHSCRIPT/subregion.sql
#psql baron -q -f $PATHSCRIPT/id_scheme.sql
#psql baron -q -f $PATHSCRIPT/platform_type.sql
#psql baron -q -f $PATHSCRIPT/station_type.sql
#psql baron -q -f $PATHSCRIPT/observing_programme.sql
#psql baron -q -f $PATHSCRIPT/observing_frequency.sql
#psql baron -q -f $PATHSCRIPT/applicationArea.sql
#psql baron -q -f $PATHSCRIPT/quality_flag.sql
#psql baron -q -f $PATHSCRIPT/tl_smooth_night_rs_92_corr.sql
#psql baron -q -f $PATHSCRIPT/tl_smooth_rs_92.sql
#psql baron -q -f $PATHSCRIPT/wind_rs92_corr.sql
#psql baron -q -f $PATHSCRIPT/data_policy_license.sql
#psql baron -q -f $PATHSCRIPT/observation_value_significance.sql
#psql baron -q -f $PATHSCRIPT/units.sql
#psql baron -q -f $PATHSCRIPT/conversion_flag.sql
#psql baron -q -f $PATHSCRIPT/spatial_representativeness.sql
#psql baron -q -f $PATHSCRIPT/instrument_exposure_quality.sql
#psql baron -q -f $PATHSCRIPT/processing_level.sql
#psql baron -q -f $PATHSCRIPT/traceability.sql
#psql baron -q -f $PATHSCRIPT/observation_code_table.sql
#psql baron -q -f $PATHSCRIPT/network_data.sql
#psql baron -q -f $PATHSCRIPT/source_data.sql
#psql baron -q -f $PATHSCRIPT/sonde_history.sql
#psql baron -q -f $PATHSCRIPT/wndeq_history.sql
#psql baron -q -f $PATHSCRIPT/gruanradiation.sql
#psql baron -q -f $PATHSCRIPT/guan_data_header_source.sql
#psql baron -q -f $PATHSCRIPT/network_data_files_available.sql
#psql baron -q -f $PATHSCRIPT/stationToHarmonize.sql
#psql baron -q -f $PATHSCRIPT/statisticStation.sql
#psql baron -q -f $PATHSCRIPT/calibration_status.sql
#psql baron -q -f $PATHSCRIPT/process_log.sql
#
#psql baron -q -f $PATHSCRIPT/station.sql
#psql baron -q -f $PATHSCRIPT/guan_stations.sql
#psql baron -q -f $PATHSCRIPT/gruan_stations.sql
#
#psql baron -q -f $PATHSCRIPT/guan_data_header.sql
#psql baron -q -f $PATHSCRIPT/guan_data_value.sql
#
#psql baron -q -f $PATHSCRIPT/gruan_data_header.sql
#psql baron -q -f $PATHSCRIPT/gruan_data_value.sql
#
#psql baron -q -f $PATHSCRIPT/header.sql
#psql baron -q -f $PATHSCRIPT/harmonized_data.sql
#psql baron -q -f $PATHSCRIPT/variable_netcdf.sql
#psql baron -q -f $PATHSCRIPT/harmonization_break.sql
#psql baron -q -f $PATHSCRIPT/sonde_history_ecmwf.sql
#psql baron -q -f $PATHSCRIPT/sonde_history_final.sql
#psql baron -q -f $PATHSCRIPT/sonde_history_for_product.sql
#psql baron -q -f $PATHSCRIPT/wmo_intercomparison_2010.sql
#psql baron -q -f $PATHSCRIPT/wmo_min_pressure_level.sql
#psql baron -q -f $PATHSCRIPT/plausabilityCheck.sql
#
#psql baron -q -f $PATHSCRIPT/setUsersPermissions.sql

