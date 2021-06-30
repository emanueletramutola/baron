CREATE
USER baron_uscrn WITH NOSUPERUSER NOCREATEDB NOCREATEROLE ENCRYPTED PASSWORD 'YVvy38T87xsxVDxS';

CREATE SCHEMA IF NOT EXISTS uscrn AUTHORIZATION baron_uscrn;

GRANT
ALL
ON ALL TABLES IN SCHEMA uscrn TO baron_uscrn;
GRANT ALL
ON ALL SEQUENCES IN SCHEMA uscrn TO baron_uscrn;

ALTER
DEFAULT PRIVILEGES IN SCHEMA uscrn GRANT ALL ON TABLES TO baron_uscrn;
ALTER
DEFAULT PRIVILEGES IN SCHEMA uscrn GRANT ALL ON SEQUENCES TO baron_uscrn;

--- tabelle principali per lo store dei sorgenti
--- ############################################
create table uscrn.subhourly
(
    id                  SERIAL PRIMARY KEY,
    sitename            VARCHAR(50),
    lastmod             VARCHAR(20),
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
    wind_flag           VARCHAR(1)
);

create table uscrn.hourly
(
    id                SERIAL PRIMARY KEY,
    sitename          VARCHAR(50),
    lastmod           VARCHAR(20),
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
    soil_temp_100     FLOAT
);

create table uscrn.daily
(
    id                      SERIAL PRIMARY KEY,
    sitename                VARCHAR(50),
    lastmod                 VARCHAR(20),
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
    soil_temp_100_daily     FLOAT
);

create table uscrn.monthly
(
    id                    SERIAL PRIMARY KEY,
    sitename              VARCHAR(50),
    lastmod               VARCHAR(20),
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
    sur_temp_monthly_avg  FLOAT
);

--- ############################################

--- tabella di appoggio da utilizzare per ricerca rapida ai fini di update
create table uscrn.summary
(
    id              SERIAL PRIMARY KEY,
    sitename        VARCHAR(50),
    last_source_mod VARCHAR(20),
    year            integer,
    kind            VARCHAR(20),
    wbanno          integer
);
--- ######################################################################

--- tabelle per lo store dei prodotti finali di incertezza
--- ######################################################
create
unlogged table uscrn.unc_subhourly (id SERIAL PRIMARY KEY, wbanno INTEGER, sitename VARCHAR(50), date VARCHAR(8), time VARCHAR(4), t FLOAT, terr_p FLOAT, terr_m FLOAT, random_pm FLOAT, sys_p FLOAT, sys_m FLOAT, quasisys_pm FLOAT);

create
unlogged table uscrn.unc_hourly (id SERIAL PRIMARY KEY, wbanno INTEGER, sitename VARCHAR(50), date VARCHAR(8), time VARCHAR(4), t FLOAT, terr_p FLOAT, terr_m FLOAT, tmaxerr_p FLOAT, tmaxerr_m FLOAT, tminerr_p FLOAT, tminerr_m FLOAT, tcalcerr_p FLOAT, tcalcerr_m FLOAT, random_pm FLOAT, sys_p FLOAT, sys_m FLOAT, quasisys_p FLOAT, quasisys_m FLOAT);

create
unlogged table uscrn.unc_daily (id SERIAL PRIMARY KEY, wbanno INTEGER, sitename VARCHAR(50), date VARCHAR(8), t FLOAT, terr_p FLOAT, terr_m FLOAT, tmaxerr_p FLOAT, tmaxerr_m FLOAT, tminerr_p FLOAT, tminerr_m FLOAT, tmeanerr_p FLOAT, tmeanerr_m FLOAT, random_p FLOAT, random_m FLOAT, sys_p FLOAT, sys_m FLOAT, quasisys_p FLOAT, quasisys_m FLOAT);

create
unlogged table uscrn.unc_monthly (id SERIAL PRIMARY KEY, wbanno INTEGER, sitename VARCHAR(50), date VARCHAR(8), t FLOAT, terr_p FLOAT, terr_m FLOAT, tmaxerr_p FLOAT, tmaxerr_m FLOAT, tminerr_p FLOAT, tminerr_m FLOAT, tcalcerr_p FLOAT, tcalcerr_m FLOAT, random_p FLOAT, random_m FLOAT, sys_p FLOAT, sys_m FLOAT, quasisys_p FLOAT, quasisys_m FLOAT);

--- ######################################################

--- tabella per memorizare il contenuto del file metadati "T calibration metadata.xlsx"
create table uscrn.wbanno
(
    wbanno integer primary key,
    statid integer
);
copy uscrn.wbanno from '/home/emanuele/owncloud/src/uscrn/venv/wbanno.csv' with (format csv);

create table uscrn.curves
(
    statid      integer,
    sitename    varchar(200),
    date_change integer,
    curve1      varchar(30),
    curve2      varchar(30),
    curve3      varchar(30),
    datalogger  varchar(6)
);
copy uscrn.curves from '/home/emanuele/owncloud/src/uscrn/venv/curves.csv' with (format csv);
alter table uscrn.curves
    add column id serial primary key;

--- NOTE: before importing the csv file of the coefficients it is necessary to process it to change the decimal from comma to point and transform all cells into text
create table uscrn.coeffs
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
copy uscrn.coeffs from '/home/emanuele/owncloud/src/uscrn/venv/coeffs.csv' with (format csv);
alter table uscrn.coeffs
    add column id serial primary key;

--table listing the stations to be processed
--query to be launched after ingesting all the data
select distinct wbanno, sitename
into unlogged uscrn.stations_to_process
from uscrn.summary;
