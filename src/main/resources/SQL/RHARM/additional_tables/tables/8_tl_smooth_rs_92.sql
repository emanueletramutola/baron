DROP TABLE IF EXISTS tl_smooth_rs_92 CASCADE;

CREATE TABLE tl_smooth_rs_92
(
    pressure          real NOT NULL,
    cor_temp_tl_bias  real,
    u_cor_temp_tl     real,
    cor_rh_tl_night   real,
    cor_rh_tl_day     real,
    u_cor_rh_tl_night real,
    u_cor_rh_tl_day   real,
    PRIMARY KEY (pressure)
);
