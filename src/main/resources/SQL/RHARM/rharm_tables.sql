DROP TABLE IF EXISTS header CASCADE;

CREATE UNLOGGED TABLE header
(
    header_id              serial,
    conventions            character varying(255),
    title                  character varying(255),
    source                 character varying(1000),
    history                character varying(255),
    references_            character varying(1000),
    disclaimer             character varying(255),
    id                     character(11),
    year                   int,
    month                  int,
    day                    int,
    hour                   int,
    reltime                timestamp with time zone,
    numlev                 int,
    lat                    real,
    lon                    real,
    name                   character varying(255),
    state                  character varying(255),
    elevation              real,
    wmo_index              int,
    radiosonde_code        int,
    radiosonde_code_source character varying(255),
    equipment_code         int,
    equipment_code_source  character varying(255),
    date_of_observation    timestamp with time zone,
    radiosonde_id          int,
    radiosonde_name        character varying(255)
) PARTITION BY RANGE (date_of_observation);

create or replace function rharm_header_partition_creation(date, date)
    returns void as
$$
declare
    create_h text;
    index_h  text;
begin
    for create_h, index_h in select 'create unlogged table header_'
                                        ||
                                    to_char(d, 'YYYY')
                                        ||
                                    ' PARTITION OF header FOR VALUES FROM (''' ||
                                    to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||
                                    to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');',
                                    'create index header_'
                                        ||
                                    to_char(d, 'YYYY')
                                        || '_id_idx on header_'
                                        ||
                                    to_char(d, 'YYYY')
                                        || ' ( id );'
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
        perform rharm_header_partition_creation('1978-01-01', '2026-01-01');
    END;
$$;
-- select rharm_header_partition_creation('1978-01-01', '2026-01-01');

DROP TABLE IF EXISTS harmonized_data CASCADE;

CREATE UNLOGGED TABLE harmonized_data
(
    id                                      serial,
    header_id                               bigint NOT NULL,
    time                                    real,
    press                                   real,
    geopot                                  real,
    lvltyp1                                 real,
    lvltyp2                                 real,
    pflag                                   character varying(255),
    zflag                                   character varying(255),
    tflag                                   character varying(255),
    dpdp                                    real,
    fp                                      real,
    asc_                                    real,
    sza                                     real,
    temp                                    real,
    temp_product                            real,
    temp_product_cor_temp                   real,
    temp_product_u_cor_temp                 real,
    temp_product_cor_temp_tl                real,
    temp_product_u_cor_temp_tl              real,
    temp_product_cor_intercomparison_temp   real,
    temp_product_u_cor_intercomparison_temp real,
    temp_h                                  real,
    err_temp_h                              real,
    rh                                      real,
    rh_product                              real,
    rh_product_cor_rh                       real,
    rh_product_u_cor_rh                     real,
    rh_product_cor_rh_tl                    real,
    rh_product_u_cor_rh_tl                  real,
    rh_product_cor_intercomparison_rh       real,
    rh_product_u_cor_intercomparison_rh     real,
    rh_h                                    real,
    err_rh_h                                real,
    u                                       real,
    u_product                               real,
    u_product_cor_u                         real,
    u_product_u_cor_u                       real,
    u_product_cor_u_rs92                    real,
    u_product_u_cor_u_rs92                  real,
    u_product_cor_u_not_Rs92                real,
    u_product_u_cor_u_not_Rs92              real,
    u_h                                     real,
    err_u_h                                 real,
    v                                       real,
    v_product                               real,
    v_product_cor_v                         real,
    v_product_u_cor_v                       real,
    v_product_cor_v_rs92                    real,
    v_product_u_cor_v_rs92                  real,
    v_product_cor_v_not_Rs92                real,
    v_product_u_cor_v_not_Rs92              real,
    v_h                                     real,
    err_v_h                                 real,
    wvmr                                    real,
    wvmr_product                            real,
    wvmr_h                                  real,
    wdir                                    real,
    wdir_h                                  real,
    err_wdir_h                              real,
    wspeed                                  real,
    wspeed_h                                real,
    err_wspeed_h                            real,
    date_of_observation                     timestamp with time zone,
    idstation                               character(11),
    reltime                                 timestamp with time zone,
    check_values                            integer[]
) PARTITION BY RANGE (date_of_observation);

create or replace function rharm_harmonized_data_creation(date, date)
    returns void as
$$
declare
    create_h_data   text;
    index_h_data    text;
    index_h_fk_data text;
begin
    for create_h_data, index_h_data, index_h_fk_data in select 'create unlogged table harmonized_data_'
                                                                   ||
                                                               to_char(d, 'YYYY')
                                                                   ||
                                                               ' PARTITION OF harmonized_data FOR VALUES FROM (''' ||
                                                               to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||
                                                               to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');',
                                                               'create index harmonized_data_'
                                                                   ||
                                                               to_char(d, 'YYYY')
                                                                   || '_idstation_idx on harmonized_data_'
                                                                   ||
                                                               to_char(d, 'YYYY')
                                                                   || ' ( idstation );',
                                                               'create index harmonized_data_'
                                                                   ||
                                                               to_char(d, 'YYYY')
                                                                   || '_fk_idx on harmonized_data_'
                                                                   ||
                                                               to_char(d, 'YYYY')
                                                                   || ' ( header_id );'
                                                        from generate_series($1, $2, '1 year') as d
        loop
            execute create_h_data;
            execute index_h_data;
            execute index_h_fk_data;
        end loop;
end ;
$$
    language plpgsql;

DO
$$
    BEGIN
        perform rharm_harmonized_data_creation('1978-01-01', '2026-01-01');
    END;
$$;

-- select rharm_harmonized_data_creation('1978-01-01', '2026-01-01');
