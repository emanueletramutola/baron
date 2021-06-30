DROP TABLE IF EXISTS wind_rs92_corr CASCADE;

CREATE TABLE wind_rs92_corr
(
    id       int     NOT NULL,
    pressure real    NOT NULL,
    cor_u    real    NOT NULL,
    u_cor_u  real    NOT NULL,
    cor_v    real    NOT NULL,
    u_cor_v  real    NOT NULL,
    isday    boolean NOT NULL,
    PRIMARY KEY (id)
);
