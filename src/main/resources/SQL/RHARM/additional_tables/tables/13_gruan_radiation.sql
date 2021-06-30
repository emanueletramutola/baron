DROP TABLE IF EXISTS gruan_radiation CASCADE;

CREATE TABLE gruan_radiation
(
    id     bigint           NOT NULL,
    clear  double precision NOT NULL,
    cloudy double precision NOT NULL,
    quota  double precision NOT NULL,
    sza    double precision NOT NULL,
    PRIMARY KEY (id)
);
