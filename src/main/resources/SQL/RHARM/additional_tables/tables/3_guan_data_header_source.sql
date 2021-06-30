DROP TABLE IF EXISTS guan_data_header_source CASCADE;

CREATE TABLE guan_data_header_source
(
    code  character varying(15)  NOT NULL,
    value character varying(200) NOT NULL,
    PRIMARY KEY (code)
);
