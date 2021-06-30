DROP TABLE IF EXISTS gruan_data_header CASCADE;

CREATE UNLOGGED TABLE gruan_data_header
(
    g_product_id bigint NOT NULL,
    comment character varying(255),
    conventions character varying(255),
    g_ascent_balloon_number character varying(255),
    g_ascent_balloon_type character varying(255),
    g_ascent_burstpoint_altitude character varying(255),
    g_ascent_burstpoint_pressure character varying(255),
    g_ascent_filling_weight character varying(255),
    g_ascent_gross_weight character varying(255),
    g_ascent_id character varying(255),
    g_ascent_include_descent character varying(255),
    g_ascent_payload character varying(255),
    g_ascent_precipitable_water_column character varying(255),
    g_ascent_precipitable_water_columnu character varying(255),
    g_ascent_standard_time character varying(255),
    g_ascent_start_time character varying(255),
    g_ascent_tropopause_height character varying(255),
    g_ascent_tropopause_pot_temperature character varying(255),
    g_ascent_tropopause_pressure character varying(255),
    g_ascent_tropopause_temperature character varying(255),
    g_ascent_unwinder_type character varying(255),
    g_general_file_type_version character varying(255),
    g_general_site_code character varying(255),
    g_general_site_institution character varying(255),
    g_general_site_name character varying(255),
    g_general_site_wmo_id character varying(255),
    g_general_timestamp character varying(255),
    g_instrument_comment character varying(255),
    g_instrument_manufacturer character varying(255),
    g_instrument_serial_number character varying(255),
    g_instrument_software_version character varying(255),
    g_instrument_telemetry_sonde character varying(255),
    g_instrument_type character varying(255),
    g_instrument_type_family character varying(255),
    g_instrument_weight character varying(255),
    g_measuring_system_altitude real,
    g_measuring_system_id character varying(255),
    g_measuring_system_latitude real,
    g_measuring_system_longitude real,
    g_measuring_system_type character varying(255),
    g_product_code character varying(255),
    g_product_history character varying(255),
    g_product_level character varying(255),
    g_product_level_description character varying(255),
    g_product_name character varying(255),
    g_product_org_resolution character varying(255),
    g_product_processing_code character varying(255),
    g_product_producer character varying(255),
    g_product_references character varying(255),
    g_product_status character varying(255),
    g_product_status_description character varying(255),
    g_product_version character varying(255),
    g_surface_obs_pressure character varying(255),
    g_surface_obs_relative_humidity character varying(255),
    g_surface_obs_temperature character varying(255),
    history character varying(255),
    institution character varying(255),
    references_ character varying(255),
    source character varying(255),
    title character varying(255),
    date_of_observation timestamp with time zone
) PARTITION BY RANGE (date_of_observation);

create or replace function gruan_data_header_partition_creation(date, date)
    returns void as
$$
declare
    create_h text;
    index_h  text;
begin
    for create_h, index_h in select 'create unlogged table gruan_data_header_'
                                        ||
                                    to_char(d, 'YYYY')
                                        ||
                                    ' PARTITION OF gruan_data_header FOR VALUES FROM (''' ||
                                    to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||
                                    to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');',
                                    'create index gruan_data_header_'
                                        ||
                                    to_char(d, 'YYYY')
                                        || '_sitecode_idx on gruan_data_header_'
                                        ||
                                    to_char(d, 'YYYY')
                                        || ' ( g_general_site_code );'
                             from generate_series($1, $2, '1 year') as d
        loop
            execute create_h;
            execute index_h;
        end loop;
end ;
$$
    language plpgsql;

DO
$$
    BEGIN
        perform gruan_data_header_partition_creation('2005-01-01', '2026-01-01');
    END;
$$;

DROP TABLE IF EXISTS gruan_data_value CASCADE;

CREATE UNLOGGED TABLE gruan_data_value
(
    id serial,
    alt real,
    asc_ real,
    cor_rh real,
    cor_temp real,
    fp real,
    geopot real,
    lat real,
    lon real,
    press real,
    res_rh real,
    rh real,
    swrad real,
    temp real,
    "time" real,
    u real,
    u_alt real,
    u_cor_rh real,
    u_cor_temp real,
    u_press real,
    u_rh real,
    u_std_rh real,
    u_std_temp real,
    u_swrad real,
    u_temp real,
    u_wdir real,
    u_wspeed real,
    v real,
    wdir real,
    wspeed real,
    wvmr real,
    gruan_data_header_id bigint,
    date_of_observation timestamp with time zone,
    g_general_site_code char(3)
) PARTITION BY RANGE (date_of_observation);

create or replace function gruan_data_value_partition_creation(date, date)
    returns void as
$$
declare
    create_h_data text;
    index_h_data  text;
begin
    for create_h_data, index_h_data in select 'create unlogged table gruan_data_value_'
                                                  ||
                                              to_char(d, 'YYYY')
                                                  ||
                                              ' PARTITION OF gruan_data_value FOR VALUES FROM (''' ||
                                              to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||
                                              to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');',
                                              'create index gruan_data_value_'
                                                  ||
                                              to_char(d, 'YYYY')
                                                  || '_sitecode_idx on gruan_data_value_'
                                                  ||
                                              to_char(d, 'YYYY')
                                                  || ' ( g_general_site_code );'
                                       from generate_series($1, $2, '1 year') as d
        loop
            execute create_h_data;
            execute index_h_data;
        end loop;
end ;
$$
    language plpgsql;

DO
$$
    BEGIN
        perform gruan_data_value_partition_creation('2005-01-01', '2026-01-01');
    END;
$$;
/*
create index harmonized_data_1978_id_idx on harmonized_data_1978(id);
create index harmonized_data_1979_id_idx on harmonized_data_1979(id);
create index harmonized_data_1980_id_idx on harmonized_data_1980(id);
create index harmonized_data_1981_id_idx on harmonized_data_1981(id);
create index harmonized_data_1982_id_idx on harmonized_data_1982(id);
create index harmonized_data_1983_id_idx on harmonized_data_1983(id);
create index harmonized_data_1984_id_idx on harmonized_data_1984(id);
create index harmonized_data_1985_id_idx on harmonized_data_1985(id);
create index harmonized_data_1986_id_idx on harmonized_data_1986(id);
create index harmonized_data_1987_id_idx on harmonized_data_1987(id);
create index harmonized_data_1988_id_idx on harmonized_data_1988(id);
create index harmonized_data_1989_id_idx on harmonized_data_1989(id);
create index harmonized_data_1990_id_idx on harmonized_data_1990(id);
create index harmonized_data_1991_id_idx on harmonized_data_1991(id);
create index harmonized_data_1992_id_idx on harmonized_data_1992(id);
create index harmonized_data_1993_id_idx on harmonized_data_1993(id);
create index harmonized_data_1994_id_idx on harmonized_data_1994(id);
create index harmonized_data_1995_id_idx on harmonized_data_1995(id);
create index harmonized_data_1996_id_idx on harmonized_data_1996(id);
create index harmonized_data_1997_id_idx on harmonized_data_1997(id);
create index harmonized_data_1998_id_idx on harmonized_data_1998(id);
create index harmonized_data_1999_id_idx on harmonized_data_1999(id);
create index harmonized_data_2000_id_idx on harmonized_data_2000(id);
create index harmonized_data_2001_id_idx on harmonized_data_2001(id);
create index harmonized_data_2002_id_idx on harmonized_data_2002(id);
create index harmonized_data_2003_id_idx on harmonized_data_2003(id);
create index harmonized_data_2004_id_idx on harmonized_data_2004(id);
create index harmonized_data_2005_id_idx on harmonized_data_2005(id);
create index harmonized_data_2006_id_idx on harmonized_data_2006(id);
create index harmonized_data_2007_id_idx on harmonized_data_2007(id);
create index harmonized_data_2008_id_idx on harmonized_data_2008(id);
create index harmonized_data_2009_id_idx on harmonized_data_2009(id);
create index harmonized_data_2010_id_idx on harmonized_data_2010(id);
create index harmonized_data_2011_id_idx on harmonized_data_2011(id);
create index harmonized_data_2012_id_idx on harmonized_data_2012(id);
create index harmonized_data_2013_id_idx on harmonized_data_2013(id);
create index harmonized_data_2014_id_idx on harmonized_data_2014(id);
create index harmonized_data_2015_id_idx on harmonized_data_2015(id);
create index harmonized_data_2016_id_idx on harmonized_data_2016(id);
create index harmonized_data_2017_id_idx on harmonized_data_2017(id);
create index harmonized_data_2018_id_idx on harmonized_data_2018(id);
create index harmonized_data_2019_id_idx on harmonized_data_2019(id);
create index harmonized_data_2020_id_idx on harmonized_data_2020(id);
*/