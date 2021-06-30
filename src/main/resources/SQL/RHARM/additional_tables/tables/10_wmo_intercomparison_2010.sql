DROP TABLE IF EXISTS wmo_intercomparison_2010 CASCADE;

CREATE TABLE wmo_intercomparison_2010
(
    id         bigint       NOT NULL,
    press      real         NOT NULL,
    sonde_id   varchar(255) NOT NULL,
    sonde_code integer      NOT NULL,
    sonde_name varchar(255) NOT NULL,
    mean       real         NOT NULL,
    std_dev    real         NOT NULL,
    ecv        varchar(10)  NOT NULL,
    isday      boolean      NOT NULL,
    PRIMARY KEY (id)
);