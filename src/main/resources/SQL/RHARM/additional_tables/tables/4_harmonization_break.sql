DROP TABLE IF EXISTS harmonization_break CASCADE;

CREATE TABLE harmonization_break
(
    id        serial           NOT NULL,
    idstation character(11),
    fabio     double precision NOT NULL,
    ecvref    integer          NOT NULL,
    zenref    integer          NOT NULL,
    year      integer          NOT NULL,
    month     integer          NOT NULL,
    day       integer          NOT NULL,
    hour      integer          NOT NULL,
    press     double precision NOT NULL,
    PRIMARY KEY (id)
);
