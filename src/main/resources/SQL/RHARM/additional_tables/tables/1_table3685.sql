DROP TABLE IF EXISTS table3685 CASCADE;

CREATE TABLE table3685
(
    id          integer NOT NULL,
    tac_code    integer,
    bufr_code   integer,
    date_from   timestamp without time zone,
    date_to     timestamp without time zone,
    description character varying(255)
);
