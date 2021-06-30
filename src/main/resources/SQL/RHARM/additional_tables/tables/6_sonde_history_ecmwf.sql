DROP TABLE IF EXISTS sonde_history_ecmwf CASCADE;

CREATE TABLE sonde_history_ecmwf
(
    id                   bigint NOT NULL,
    wmoid                integer,
    nrep                 integer,
    nwt                  integer,
    nww                  integer,
    n99                  integer,
    latitude             real,
    longitude            real,
    radiosonde_type      integer,
    maxl                 integer,
    ndp                  integer,
    ndt                  integer,
    bufr_nrep            integer,
    bufr_nwt             integer,
    bufr_nww             integer,
    bufr_n99             integer,
    bufr_nhr             integer,
    bufr_latitude        real,
    bufr_longitude       real,
    bufr_radiosonde_type integer,
    bufr_maxl            integer,
    bufr_ndp             integer,
    bufr_ndt             integer,
    notes                character varying(255),
    date                 timestamp without time zone,
    verified             boolean DEFAULT false,
    PRIMARY KEY (id)
);
