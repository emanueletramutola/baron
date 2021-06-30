DROP TABLE IF EXISTS guan_data_header CASCADE;

CREATE UNLOGGED TABLE guan_data_header
(
    guandataheader_id   SERIAL,
    day                 integer,
    hour                integer,
    idstation           character(11),
    lat                 real,
    lon                 real,
    month               integer,
    np_src              character varying(255),
    numlev              integer,
    p_src               character varying(255),
    reltime             integer,
    year                integer,
    date_of_observation timestamp with time zone,
    version             integer default 2
) PARTITION BY RANGE (date_of_observation);

create or replace function igra_header_partition_creation(date, date)
    returns void as
$$
declare
    create_h text;
    index_h  text;
begin
    for create_h, index_h in select 'create unlogged table guan_data_header_'
                                        ||
                                    to_char(d, 'YYYY')
                                        ||
                                    ' PARTITION OF guan_data_header FOR VALUES FROM (''' ||
                                    to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||
                                    to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');',
                                    'create index guan_data_header_'
                                        ||
                                    to_char(d, 'YYYY')
                                        || '_id_idx on guan_data_header_'
                                        ||
                                    to_char(d, 'YYYY')
                                        || ' ( idstation );'
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
        perform igra_header_partition_creation('1978-01-01', '2026-01-01');
    END;
$$;

DROP TABLE IF EXISTS guan_data_value CASCADE;

CREATE UNLOGGED TABLE guan_data_value
(
    id                  SERIAL,
    dpdp                real,
    etime               integer,
    gph                 integer,
    lvltyp1             integer,
    lvltyp2             integer,
    pflag               character varying(255),
    press               integer,
    rh                  real,
    temp                real,
    tflag               character varying(255),
    wdir                integer,
    wspd                real,
    zflag               character varying(255),
    guan_data_header_id bigint,
    idstation           character(11),
    date_of_observation timestamp with time zone,
    version             integer default 2
) PARTITION BY RANGE (date_of_observation);

create or replace function igra_data_partition_creation(date, date)
    returns void as
$$
declare
    create_h_data text;
    index_h_data  text;
    index_h_fk_data  text;
begin
    for create_h_data, index_h_data, index_h_fk_data in select 'create unlogged table guan_data_value_'
                                                  ||
                                              to_char(d, 'YYYY')
                                                  ||
                                              ' PARTITION OF guan_data_value FOR VALUES FROM (''' ||
                                              to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||
                                              to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');',
                                              'create index guan_data_value_'
                                                  ||
                                              to_char(d, 'YYYY')
                                                  || '_idstation_idx on guan_data_value_'
                                                  ||
                                              to_char(d, 'YYYY')
                                                  || ' ( idstation );',
                                              'create index guan_data_value_'
                                                  ||
                                              to_char(d, 'YYYY')
                                                  || '_fk_idx on guan_data_value_'
                                                  ||
                                              to_char(d, 'YYYY')
                                                  || ' ( guan_data_header_id );'
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
        perform igra_data_partition_creation('1978-01-01', '2026-01-01');
    END;
$$;
