DROP TABLE IF EXISTS station CASCADE;

CREATE TABLE station
(
    id          bigint NOT NULL,
    continent   character varying(2),
    countrycode character varying(2),
    elevation   real,
    idstation   character varying(255),
    latitude    real,
    longitude   real,
    name        character varying(255),
    network     character varying(255),
    wmoid       integer,
    PRIMARY KEY (id)
);
