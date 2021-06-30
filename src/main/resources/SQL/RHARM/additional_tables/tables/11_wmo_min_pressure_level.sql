DROP TABLE IF EXISTS wmo_min_pressure_level CASCADE;

CREATE TABLE wmo_min_pressure_level
(
    id         integer               NOT NULL,
    sonde_id   varchar(255)          NOT NULL,
    sonde_code varchar(255)          NOT NULL,
    sonde_name varchar(255)          NOT NULL,
    ecv        character varying(10) NOT NULL,
    isday      boolean               NOT NULL,
    min_press  real                  NOT NULL,
    PRIMARY KEY (id)
);
