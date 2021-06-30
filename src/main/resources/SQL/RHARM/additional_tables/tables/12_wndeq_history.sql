DROP TABLE IF EXISTS wndeq_history CASCADE;

CREATE TABLE wndeq_history
(
    begday   integer,
    beghour  integer,
    begin    timestamp without time zone,
    begmonth integer,
    begyear  integer,
    code     integer,
    enddate  timestamp without time zone,
    endday   integer,
    endhour  integer,
    endmonth integer,
    endyear  integer,
    fullrow  character varying(255),
    igraid   character varying(255),
    id       integer NOT NULL,
    PRIMARY KEY (id)
);
