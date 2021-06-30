DROP TABLE IF EXISTS subhourly CASCADE;

CREATE UNLOGGED TABLE subhourly
(
    id                  SERIAL,
    sitename            VARCHAR(50),
    wbanno              integer,
    utc_date            VARCHAR(20),
    utc_time            VARCHAR(20),
    lst_date            VARCHAR(20),
    lst_time            VARCHAR(20),
    crx_vn              VARCHAR(6),
    longitude           FLOAT,
    latitude            FLOAT,
    air_temperature     FLOAT,
    precipitation       FLOAT,
    solar_radiation     FLOAT,
    sr_flag             VARCHAR(1),
    surface_temperature FLOAT,
    st_type             VARCHAR(1),
    st_flag             VARCHAR(1),
    relative_humidity   FLOAT,
    rh_flag             VARCHAR(1),
    soil_moisture_5     FLOAT,
    soil_temperature_5  FLOAT,
    wetness             FLOAT,
    wet_flag            VARCHAR(1),
    wind_1_5            FLOAT,
    wind_flag           VARCHAR(1),
    date_of_observation_UTC timestamp with time zone,
    date_of_observation_LST timestamp with time zone,
    lastmod             timestamp with time zone
) PARTITION BY RANGE (date_of_observation_UTC);

create or replace function uscrn_subhourly_partition_creation(date, date)
    returns void as
$$
declare
    create_h_data text;
begin
    for create_h_data in select 'create unlogged table subhourly_'
                                    ||
                                to_char(d, 'YYYY')
                                    ||
                                ' PARTITION OF subhourly FOR VALUES FROM (''' ||
                                to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||
                                to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');'
                         from generate_series($1, $2, '1 year') as d
        loop
            execute create_h_data;
        end loop;
end ;
$$
    language plpgsql;

select uscrn_subhourly_partition_creation('2006-01-01', '2021-01-01');

DROP TABLE IF EXISTS hourly CASCADE;

CREATE UNLOGGED TABLE hourly
(
    id                SERIAL,
    sitename          VARCHAR(50),
    wbanno            integer,
    utc_date          VARCHAR(20),
    utc_time          VARCHAR(20),
    lst_date          VARCHAR(20),
    lst_time          VARCHAR(20),
    crx_vn            VARCHAR(6),
    longitude         FLOAT,
    latitude          FLOAT,
    t_calc            FLOAT,
    t_hr_avg          FLOAT,
    t_max             FLOAT,
    t_min             FLOAT,
    p_calc            FLOAT,
    solarad           FLOAT,
    solarad_flag      VARCHAR(1),
    solarad_max       FLOAT,
    solarad_max_flag  VARCHAR(1),
    solarad_min       FLOAT,
    solarad_min_flag  VARCHAR(1),
    sur_temp_type     VARCHAR(1),
    sur_temp          FLOAT,
    sur_temp_flag     VARCHAR(1),
    sur_temp_max      FLOAT,
    sur_temp_max_flag VARCHAR(1),
    sur_temp_min      FLOAT,
    sur_temp_min_flag VARCHAR(1),
    rh_hr_avg         FLOAT,
    rh_hr_avg_flag    VARCHAR(1),
    soil_moisture_5   FLOAT,
    soil_moisture_10  FLOAT,
    soil_moisture_20  FLOAT,
    soil_moisture_50  FLOAT,
    soil_moisture_100 FLOAT,
    soil_temp_5       FLOAT,
    soil_temp_10      FLOAT,
    soil_temp_20      FLOAT,
    soil_temp_50      FLOAT,
    soil_temp_100     FLOAT,
    date_of_observation_UTC timestamp with time zone,
    date_of_observation_LST timestamp with time zone,
    lastmod             timestamp with time zone
) PARTITION BY RANGE (date_of_observation_UTC);

create or replace function uscrn_hourly_partition_creation(date, date)
    returns void as
$$
declare
    create_h_data text;
begin
    for create_h_data in select 'create unlogged table hourly_'
                                    ||
                                to_char(d, 'YYYY')
                                    ||
                                ' PARTITION OF hourly FOR VALUES FROM (''' ||
                                to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||
                                to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');'
                         from generate_series($1, $2, '1 year') as d
        loop
            execute create_h_data;
        end loop;
end ;
$$
    language plpgsql;

select uscrn_hourly_partition_creation('2006-01-01', '2021-01-01');

DROP TABLE IF EXISTS daily CASCADE;

CREATE UNLOGGED TABLE daily
(
    id                      SERIAL,
    sitename                VARCHAR(50),
    wbanno                  INTEGER,
    lst_date                VARCHAR(20),
    crx_vn                  VARCHAR(6),
    longitude               FLOAT,
    latitude                FLOAT,
    t_daily_max             FLOAT,
    t_daily_min             FLOAT,
    t_daily_mean            FLOAT,
    t_daily_avg             FLOAT,
    p_daily_calc            FLOAT,
    solarad_daily           FLOAT,
    sur_temp_daily_type     VARCHAR(1),
    sur_temp_daily_max      FLOAT,
    sur_temp_daily_min      FLOAT,
    sur_temp_daily_avg      FLOAT,
    rh_daily_max            FLOAT,
    rh_daily_min            FLOAT,
    rh_daily_avg            FLOAT,
    soil_moisture_5_daily   FLOAT,
    soil_moisture_10_daily  FLOAT,
    soil_moisture_20_daily  FLOAT,
    soil_moisture_50_daily  FLOAT,
    soil_moisture_100_daily FLOAT,
    soil_temp_5_daily       FLOAT,
    soil_temp_10_daily      FLOAT,
    soil_temp_20_daily      FLOAT,
    soil_temp_50_daily      FLOAT,
    soil_temp_100_daily     FLOAT,
    date_of_observation_LST timestamp with time zone,
    lastmod             timestamp with time zone
) PARTITION BY RANGE (date_of_observation_LST);

create or replace function uscrn_daily_partition_creation(date, date)
    returns void as
$$
declare
    create_h_data text;
begin
    for create_h_data in select 'create unlogged table daily_'
                                    ||
                                to_char(d, 'YYYY')
                                    ||
                                ' PARTITION OF daily FOR VALUES FROM (''' ||
                                to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||
                                to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');'
                         from generate_series($1, $2, '1 year') as d
        loop
            execute create_h_data;
        end loop;
end ;
$$
    language plpgsql;

select uscrn_daily_partition_creation('2006-01-01', '2021-01-01');

DROP TABLE IF EXISTS monthly CASCADE;

CREATE UNLOGGED TABLE monthly
(
    id                    SERIAL PRIMARY KEY,
    sitename              VARCHAR(50),
    wbanno                INTEGER,
    lst_yrmo              VARCHAR(6),
    crx_vn_monthly        VARCHAR(6),
    precise_longitude     FLOAT,
    precise_latitude      FLOAT,
    t_monthly_max         FLOAT,
    t_monthly_min         FLOAT,
    t_monthly_mean        FLOAT,
    t_monthly_avg         FLOAT,
    p_monthly_calc        FLOAT,
    solrad_monthly_avg    FLOAT,
    sur_temp_monthly_type VARCHAR(1),
    sur_temp_monthly_max  FLOAT,
    sur_temp_monthly_min  FLOAT,
    sur_temp_monthly_avg  FLOAT,
    date_of_observation_LST timestamp with time zone,
    lastmod             timestamp with time zone
);

DROP TABLE IF EXISTS unc_subhourly CASCADE;

CREATE UNLOGGED TABLE unc_subhourly
(
    id          SERIAL,
    wbanno      INTEGER,
    sitename    VARCHAR(50),
    date        VARCHAR(8),
    time        VARCHAR(4),
    t           FLOAT,
    terr_p      FLOAT,
    terr_m      FLOAT,
    random_pm   FLOAT,
    sys_p       FLOAT,
    sys_m       FLOAT,
    quasisys_pm FLOAT,
    date_of_observation timestamp with time zone,
    latitude real,
    longitude real
) PARTITION BY RANGE (date_of_observation);

create or replace function uscrn_unc_subhourly_partition_creation(date, date)
    returns void as
$$
declare
    create_h_data text;
begin
    for create_h_data in select 'create unlogged table unc_subhourly_'
                                    ||
                                to_char(d, 'YYYY')
                                    ||
                                ' PARTITION OF unc_subhourly FOR VALUES FROM (''' ||
                                to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||
                                to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');'
                         from generate_series($1, $2, '1 year') as d
        loop
            execute create_h_data;
        end loop;
end ;
$$
    language plpgsql;

select uscrn_unc_subhourly_partition_creation('2006-01-01', '2021-01-01');

DROP TABLE IF EXISTS unc_hourly CASCADE;

CREATE UNLOGGED TABLE unc_hourly
(
    id         SERIAL,
    wbanno     INTEGER,
    sitename   VARCHAR(50),
    date       VARCHAR(8),
    time       VARCHAR(4),
    t          FLOAT,
    terr_p     FLOAT,
    terr_m     FLOAT,
    tmaxerr_p  FLOAT,
    tmaxerr_m  FLOAT,
    tminerr_p  FLOAT,
    tminerr_m  FLOAT,
    tcalcerr_p FLOAT,
    tcalcerr_m FLOAT,
    random_pm  FLOAT,
    sys_p      FLOAT,
    sys_m      FLOAT,
    quasisys_p FLOAT,
    quasisys_m FLOAT,
    date_of_observation timestamp with time zone,
    latitude real,
    longitude real
) PARTITION BY RANGE (date_of_observation);

create or replace function uscrn_unc_hourly_partition_creation(date, date)
    returns void as
$$
declare
    create_h_data text;
begin
    for create_h_data in select 'create unlogged table unc_hourly_'
                                    ||
                                to_char(d, 'YYYY')
                                    ||
                                ' PARTITION OF unc_hourly FOR VALUES FROM (''' ||
                                to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||
                                to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');'
                         from generate_series($1, $2, '1 year') as d
        loop
            execute create_h_data;
        end loop;
end ;
$$
    language plpgsql;

select uscrn_unc_hourly_partition_creation('2006-01-01', '2021-01-01');

DROP TABLE IF EXISTS unc_daily CASCADE;

CREATE UNLOGGED TABLE unc_daily
(
    id         SERIAL,
    wbanno     INTEGER,
    sitename   VARCHAR(50),
    date       VARCHAR(8),
    t          FLOAT,
    terr_p     FLOAT,
    terr_m     FLOAT,
    tmaxerr_p  FLOAT,
    tmaxerr_m  FLOAT,
    tminerr_p  FLOAT,
    tminerr_m  FLOAT,
    tmeanerr_p FLOAT,
    tmeanerr_m FLOAT,
    random_p   FLOAT,
    random_m   FLOAT,
    sys_p      FLOAT,
    sys_m      FLOAT,
    quasisys_p FLOAT,
    quasisys_m FLOAT,
    date_of_observation timestamp with time zone,
    latitude real,
    longitude real
) PARTITION BY RANGE (date_of_observation);

create or replace function uscrn_unc_daily_partition_creation(date, date)
    returns void as
$$
declare
    create_h_data text;
begin
    for create_h_data in select 'create unlogged table unc_daily_'
                                    ||
                                to_char(d, 'YYYY')
                                    ||
                                ' PARTITION OF unc_daily FOR VALUES FROM (''' ||
                                to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||
                                to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');'
                         from generate_series($1, $2, '1 year') as d
        loop
            execute create_h_data;
        end loop;
end ;
$$
    language plpgsql;

select uscrn_unc_daily_partition_creation('2006-01-01', '2021-01-01');

DROP TABLE IF EXISTS unc_monthly CASCADE;

CREATE UNLOGGED TABLE unc_monthly
(
    id         SERIAL,
    wbanno     INTEGER,
    sitename   VARCHAR(50),
    date       VARCHAR(8),
    t          FLOAT,
    terr_p     FLOAT,
    terr_m     FLOAT,
    tmaxerr_p  FLOAT,
    tmaxerr_m  FLOAT,
    tminerr_p  FLOAT,
    tminerr_m  FLOAT,
    tcalcerr_p FLOAT,
    tcalcerr_m FLOAT,
    random_p   FLOAT,
    random_m   FLOAT,
    sys_p      FLOAT,
    sys_m      FLOAT,
    quasisys_p FLOAT,
    quasisys_m FLOAT,
    date_of_observation timestamp with time zone,
    latitude real,
    longitude real
) PARTITION BY RANGE (date_of_observation);

DROP TABLE IF EXISTS wbanno CASCADE;

CREATE TABLE wbanno
(
    wbanno integer primary key,
    statid integer
);

DROP TABLE IF EXISTS curves CASCADE;

CREATE TABLE curves
(
    statid      integer,
    sitename    varchar(200),
    date_change integer,
    curve1      varchar(30),
    curve2      varchar(30),
    curve3      varchar(30),
    datalogger  varchar(6),
    id          serial primary key
);

DROP TABLE IF EXISTS coeffs CASCADE;

CREATE TABLE coeffs
(
    statid      integer,
    sitename    varchar(200),
    date_change integer,
    coeff1      varchar(15) default null,
    coeff2      varchar(15) default null,
    coeff3      varchar(15) default null,
    coeff4      varchar(15) default null,
    coeff5      varchar(15) default null,
    coeff6      varchar(15) default null,
    coeff7      varchar(15) default null,
    coeff8      varchar(15) default null,
    coeff9      varchar(15) default null,
    coeff10     varchar(15) default null,
    coeff11     varchar(15) default null,
    coeff12     varchar(15) default null
);
