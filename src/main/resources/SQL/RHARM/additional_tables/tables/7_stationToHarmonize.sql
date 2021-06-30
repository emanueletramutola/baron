DROP TABLE IF EXISTS public.station_to_harmonize CASCADE;

CREATE TABLE public.station_to_harmonize
(
    idstation   character(11) NOT NULL,
    launches    integer,
    initlist    integer,
    readigra    integer,
    product     integer,
    calch       integer,
    endseth     integer,
    interpfinal integer,
    db          integer,
    total       integer,
    status      character(1),
    lastupdate  timestamp with time zone,
    PRIMARY KEY (idstation)
);

