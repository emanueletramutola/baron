package cnr.imaa.baron.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cnr.imaa.baron.repository.DataSource;
import cnr.imaa.baron.utils.FileResourceUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

public class DBManager {
    DataSource dataSource = new DataSource(new FileResourceUtils("application.properties"));

    public enum TABLE {
        IGRA,
        GRUAN,
        RHARM
    }

    public void initDB(TABLE table) {
        dropTable_header(table);
        createTable_header(table);
        createFunction_header_partition_creation(table);
        createTablesPartitioned_header(table);
        dropTable_data(table);
        createTable_data(table);
        createFunction_data_partition_creation(table);
        createTablesPartitioned_data(table);
    }

    private void dropTable_header(TABLE table) {
        String sql = "";

        switch (table) {
            case GRUAN:
                sql = "DROP TABLE IF EXISTS gruan_data_header CASCADE";
                break;
            case IGRA:
                sql = "DROP TABLE IF EXISTS guan_data_header CASCADE";
                break;
            case RHARM:
                sql = "DROP TABLE IF EXISTS header CASCADE";
                break;
        }

        executeStatement(sql);
    }

    private void createTable_header(TABLE table) {
        String sql = "";

        switch (table) {
            case GRUAN:
                sql = "CREATE UNLOGGED TABLE gruan_data_header" +
                        "(" +
                        "    g_product_id bigint NOT NULL," +
                        "    comment character varying(255)," +
                        "    conventions character varying(255)," +
                        "    g_ascent_balloon_number character varying(255)," +
                        "    g_ascent_balloon_type character varying(255)," +
                        "    g_ascent_burstpoint_altitude character varying(255)," +
                        "    g_ascent_burstpoint_pressure character varying(255)," +
                        "    g_ascent_filling_weight character varying(255)," +
                        "    g_ascent_gross_weight character varying(255)," +
                        "    g_ascent_id character varying(255)," +
                        "    g_ascent_include_descent character varying(255)," +
                        "    g_ascent_payload character varying(255)," +
                        "    g_ascent_precipitable_water_column character varying(255)," +
                        "    g_ascent_precipitable_water_columnu character varying(255)," +
                        "    g_ascent_standard_time character varying(255)," +
                        "    g_ascent_start_time character varying(255)," +
                        "    g_ascent_tropopause_height character varying(255)," +
                        "    g_ascent_tropopause_pot_temperature character varying(255)," +
                        "    g_ascent_tropopause_pressure character varying(255)," +
                        "    g_ascent_tropopause_temperature character varying(255)," +
                        "    g_ascent_unwinder_type character varying(255)," +
                        "    g_general_file_type_version character varying(255)," +
                        "    g_general_site_code character varying(255)," +
                        "    g_general_site_institution character varying(255)," +
                        "    g_general_site_name character varying(255)," +
                        "    g_general_site_wmo_id character varying(255)," +
                        "    g_general_timestamp character varying(255)," +
                        "    g_instrument_comment character varying(255)," +
                        "    g_instrument_manufacturer character varying(255)," +
                        "    g_instrument_serial_number character varying(255)," +
                        "    g_instrument_software_version character varying(255)," +
                        "    g_instrument_telemetry_sonde character varying(255)," +
                        "    g_instrument_type character varying(255)," +
                        "    g_instrument_type_family character varying(255)," +
                        "    g_instrument_weight character varying(255)," +
                        "    g_measuring_system_altitude real," +
                        "    g_measuring_system_id character varying(255)," +
                        "    g_measuring_system_latitude real," +
                        "    g_measuring_system_longitude real," +
                        "    g_measuring_system_type character varying(255)," +
                        "    g_product_code character varying(255)," +
                        "    g_product_history character varying(255)," +
                        "    g_product_level character varying(255)," +
                        "    g_product_level_description character varying(255)," +
                        "    g_product_name character varying(255)," +
                        "    g_product_org_resolution character varying(255)," +
                        "    g_product_processing_code character varying(255)," +
                        "    g_product_producer character varying(255)," +
                        "    g_product_references character varying(255)," +
                        "    g_product_status character varying(255)," +
                        "    g_product_status_description character varying(255)," +
                        "    g_product_version character varying(255)," +
                        "    g_surface_obs_pressure character varying(255)," +
                        "    g_surface_obs_relative_humidity character varying(255)," +
                        "    g_surface_obs_temperature character varying(255)," +
                        "    history character varying(255)," +
                        "    institution character varying(255)," +
                        "    references_ character varying(255)," +
                        "    source character varying(255)," +
                        "    title character varying(255)," +
                        "    date_of_observation timestamp with time zone" +
                        ") PARTITION BY RANGE (date_of_observation)";
                break;
            case IGRA:
                sql = "CREATE UNLOGGED TABLE guan_data_header" +
                        "(" +
                        "    guandataheader_id   SERIAL," +
                        "    day                 integer," +
                        "    hour                integer," +
                        "    idstation           character(11)," +
                        "    lat                 real," +
                        "    lon                 real," +
                        "    month               integer," +
                        "    np_src              character varying(255)," +
                        "    numlev              integer," +
                        "    p_src               character varying(255)," +
                        "    reltime             integer," +
                        "    year                integer," +
                        "    date_of_observation timestamp with time zone," +
                        "    version             integer default 2" +
                        ") PARTITION BY RANGE (date_of_observation);";
                break;
            case RHARM:
                sql = "CREATE UNLOGGED TABLE header" +
                        "(" +
                        "    header_id              serial," +
                        "    conventions            character varying(255)," +
                        "    title                  character varying(255)," +
                        "    source                 character varying(1000)," +
                        "    history                character varying(255)," +
                        "    references_            character varying(1000)," +
                        "    disclaimer             character varying(255)," +
                        "    id                     character(11)," +
                        "    year                   int," +
                        "    month                  int," +
                        "    day                    int," +
                        "    hour                   int," +
                        "    reltime                timestamp with time zone," +
                        "    numlev                 int," +
                        "    lat                    real," +
                        "    lon                    real," +
                        "    name                   character varying(255)," +
                        "    state                  character varying(255)," +
                        "    elevation              real," +
                        "    wmo_index              int," +
                        "    radiosonde_code        int," +
                        "    radiosonde_code_source character varying(255)," +
                        "    equipment_code         int," +
                        "    equipment_code_source  character varying(255)," +
                        "    date_of_observation    timestamp with time zone," +
                        "    radiosonde_id          int," +
                        "    radiosonde_name        character varying(255)" +
                        ") PARTITION BY RANGE (date_of_observation);";
                break;
        }

        executeStatement(sql);
    }

    private void createFunction_header_partition_creation(TABLE table) {
        String sql = "";

        switch (table) {
            case GRUAN:
                sql = "create or replace function gruan_data_header_partition_creation(date, date)" +
                        "    returns void as" +
                        " $$ " +
                        "declare" +
                        "    create_h text;" +
                        "    index_h  text;" +
                        "begin" +
                        "    for create_h, index_h in select 'create unlogged table gruan_data_header_'" +
                        "                                        ||" +
                        "                                    to_char(d, 'YYYY')" +
                        "                                        ||" +
                        "                                    ' PARTITION OF gruan_data_header FOR VALUES FROM (''' ||" +
                        "                                    to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||" +
                        "                                    to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');'," +
                        "                                    'create index gruan_data_header_'" +
                        "                                        ||" +
                        "                                    to_char(d, 'YYYY')" +
                        "                                        || '_sitecode_idx on gruan_data_header_'" +
                        "                                        ||" +
                        "                                    to_char(d, 'YYYY')" +
                        "                                        || ' ( g_general_site_code );'" +
                        "                             from generate_series($1, $2, '1 year') as d" +
                        "        loop" +
                        "            execute create_h;" +
                        "            execute index_h;" +
                        "        end loop;" +
                        "end ;" +
                        "$$" +
                        "    language plpgsql;";
                break;
            case IGRA:
                sql = "create or replace function igra_header_partition_creation(date, date)" +
                        "    returns void as" +
                        " $$ " +
                        "declare" +
                        "    create_h text;" +
                        "    index_h  text;" +
                        "begin" +
                        "    for create_h, index_h in select 'create unlogged table guan_data_header_'" +
                        "                                        ||" +
                        "                                    to_char(d, 'YYYY')" +
                        "                                        ||" +
                        "                                    ' PARTITION OF guan_data_header FOR VALUES FROM (''' ||" +
                        "                                    to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||" +
                        "                                    to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');'," +
                        "                                    'create index guan_data_header_'" +
                        "                                        ||" +
                        "                                    to_char(d, 'YYYY')" +
                        "                                        || '_id_idx on guan_data_header_'" +
                        "                                        ||" +
                        "                                    to_char(d, 'YYYY')" +
                        "                                        || ' ( idstation );'" +
                        "                             from generate_series($1, $2, '1 year') as d" +
                        "        loop" +
                        "            execute create_h;" +
                        "            execute index_h;" +
                        "        end loop;" +
                        "end ;" +
                        "$$" +
                        "    language plpgsql;";
                break;
            case RHARM:
                sql = "create or replace function rharm_header_partition_creation(date, date)" +
                        "    returns void as" +
                        " $$ " +
                        "declare" +
                        "    create_h text;" +
                        "    index_h  text;" +
                        "begin" +
                        "    for create_h, index_h in select 'create unlogged table header_'" +
                        "                                        ||" +
                        "                                    to_char(d, 'YYYY')" +
                        "                                        ||" +
                        "                                    ' PARTITION OF header FOR VALUES FROM (''' ||" +
                        "                                    to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||" +
                        "                                    to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');'," +
                        "                                    'create index header_'" +
                        "                                        ||" +
                        "                                    to_char(d, 'YYYY')" +
                        "                                        || '_id_idx on header_'" +
                        "                                        ||" +
                        "                                    to_char(d, 'YYYY')" +
                        "                                        || ' ( id );'" +
                        "                             from generate_series($1, $2, '1 year') as d" +
                        "        loop" +
                        "            execute create_h;" +
                        "            execute index_h;" +
                        "        end loop;" +
                        "end ;" +
                        "$$" +
                        "    language plpgsql;";
                break;
        }

        executeStatement(sql);
    }

    private void createTablesPartitioned_header(TABLE table) {
        String sql = "";

        switch (table) {
            case GRUAN:
                sql = "select gruan_data_header_partition_creation('2005-01-01', '2026-01-01')";
                break;
            case IGRA:
                sql = "select igra_header_partition_creation('1978-01-01', '2026-01-01');";
                break;
            case RHARM:
                sql = "select rharm_header_partition_creation('1978-01-01', '2026-01-01');";
                break;
        }

        executeStatement(sql);
    }

    private void dropTable_data(TABLE table) {
        String sql = "";

        switch (table) {
            case GRUAN:
                sql = "DROP TABLE IF EXISTS gruan_data_value CASCADE";
                break;
            case IGRA:
                sql = "DROP TABLE IF EXISTS guan_data_value CASCADE";
                break;
            case RHARM:
                sql = "DROP TABLE IF EXISTS harmonized_data CASCADE";
                break;
        }

        executeStatement(sql);
    }

    private void createTable_data(TABLE table) {
        String sql = "";

        switch (table) {
            case GRUAN:
                sql = "CREATE UNLOGGED TABLE gruan_data_value" +
                        "(" +
                        "    id serial," +
                        "    alt real," +
                        "    asc_ real," +
                        "    cor_rh real," +
                        "    cor_temp real," +
                        "    fp real," +
                        "    geopot real," +
                        "    lat real," +
                        "    lon real," +
                        "    press real," +
                        "    res_rh real," +
                        "    rh real," +
                        "    swrad real," +
                        "    temp real," +
                        "    \"time\" real," +
                        "    u real," +
                        "    u_alt real," +
                        "    u_cor_rh real," +
                        "    u_cor_temp real," +
                        "    u_press real," +
                        "    u_rh real," +
                        "    u_std_rh real," +
                        "    u_std_temp real," +
                        "    u_swrad real," +
                        "    u_temp real," +
                        "    u_wdir real," +
                        "    u_wspeed real," +
                        "    v real," +
                        "    wdir real," +
                        "    wspeed real," +
                        "    wvmr real," +
                        "    gruan_data_header_id bigint," +
                        "    date_of_observation timestamp with time zone," +
                        "    g_general_site_code char(3)" +
                        ") PARTITION BY RANGE (date_of_observation)";
                break;
            case IGRA:
                sql = "CREATE UNLOGGED TABLE guan_data_value" +
                        "(" +
                        "    id                  SERIAL," +
                        "    dpdp                real," +
                        "    etime               integer," +
                        "    gph                 integer," +
                        "    lvltyp1             integer," +
                        "    lvltyp2             integer," +
                        "    pflag               character varying(255)," +
                        "    press               integer," +
                        "    rh                  real," +
                        "    temp                real," +
                        "    tflag               character varying(255)," +
                        "    wdir                integer," +
                        "    wspd                real," +
                        "    zflag               character varying(255)," +
                        "    guan_data_header_id bigint," +
                        "    idstation           character(11)," +
                        "    date_of_observation timestamp with time zone," +
                        "    version             integer default 2" +
                        ") PARTITION BY RANGE (date_of_observation);";
                break;
            case RHARM:
                sql = "CREATE UNLOGGED TABLE harmonized_data" +
                        "(" +
                        "    id                                      serial," +
                        "    header_id                               bigint NOT NULL," +
                        "    time                                    real," +
                        "    press                                   real," +
                        "    geopot                                  real," +
                        "    lvltyp1                                 real," +
                        "    lvltyp2                                 real," +
                        "    pflag                                   character varying(255)," +
                        "    zflag                                   character varying(255)," +
                        "    tflag                                   character varying(255)," +
                        "    dpdp                                    real," +
                        "    fp                                      real," +
                        "    asc_                                    real," +
                        "    sza                                     real," +
                        "    temp                                    real," +
                        "    temp_product                            real," +
                        "    temp_product_cor_temp                   real," +
                        "    temp_product_u_cor_temp                 real," +
                        "    temp_product_cor_temp_tl                real," +
                        "    temp_product_u_cor_temp_tl              real," +
                        "    temp_product_cor_intercomparison_temp   real," +
                        "    temp_product_u_cor_intercomparison_temp real," +
                        "    temp_h                                  real," +
                        "    err_temp_h                              real," +
                        "    rh                                      real," +
                        "    rh_product                              real," +
                        "    rh_product_cor_rh                       real," +
                        "    rh_product_u_cor_rh                     real," +
                        "    rh_product_cor_rh_tl                    real," +
                        "    rh_product_u_cor_rh_tl                  real," +
                        "    rh_product_cor_intercomparison_rh       real," +
                        "    rh_product_u_cor_intercomparison_rh     real," +
                        "    rh_h                                    real," +
                        "    err_rh_h                                real," +
                        "    u                                       real," +
                        "    u_product                               real," +
                        "    u_product_cor_u                         real," +
                        "    u_product_u_cor_u                       real," +
                        "    u_product_cor_u_rs92                    real," +
                        "    u_product_u_cor_u_rs92                  real," +
                        "    u_product_cor_u_not_Rs92                real," +
                        "    u_product_u_cor_u_not_Rs92              real," +
                        "    u_h                                     real," +
                        "    err_u_h                                 real," +
                        "    v                                       real," +
                        "    v_product                               real," +
                        "    v_product_cor_v                         real," +
                        "    v_product_u_cor_v                       real," +
                        "    v_product_cor_v_rs92                    real," +
                        "    v_product_u_cor_v_rs92                  real," +
                        "    v_product_cor_v_not_Rs92                real," +
                        "    v_product_u_cor_v_not_Rs92              real," +
                        "    v_h                                     real," +
                        "    err_v_h                                 real," +
                        "    wvmr                                    real," +
                        "    wvmr_product                            real," +
                        "    wvmr_h                                  real," +
                        "    wdir                                    real," +
                        "    wdir_h                                  real," +
                        "    err_wdir_h                              real," +
                        "    wspeed                                  real," +
                        "    wspeed_h                                real," +
                        "    err_wspeed_h                            real," +
                        "    date_of_observation                     timestamp with time zone," +
                        "    idstation                               character(11)," +
                        "    reltime                                 timestamp with time zone," +
                        "    check_values                            integer[]" +
                        ") PARTITION BY RANGE (date_of_observation);";
                break;
        }

        executeStatement(sql);
    }

    private void createFunction_data_partition_creation(TABLE table) {
        String sql = "";

        switch (table) {
            case GRUAN:
                sql = "create or replace function gruan_data_value_creation(date, date)" +
                        "    returns void as" +
                        " $$ " +
                        "declare" +
                        "    create_h_data text;" +
                        "    index_h_data  text;" +
                        "begin" +
                        "    for create_h_data, index_h_data in select 'create unlogged table gruan_data_value_'" +
                        "                                                  ||" +
                        "                                              to_char(d, 'YYYY')" +
                        "                                                  ||" +
                        "                                              ' PARTITION OF gruan_data_value FOR VALUES FROM (''' ||" +
                        "                                              to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||" +
                        "                                              to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');'," +
                        "                                              'create index gruan_data_value_'" +
                        "                                                  ||" +
                        "                                              to_char(d, 'YYYY')" +
                        "                                                  || '_sitecode_idx on gruan_data_value_'" +
                        "                                                  ||" +
                        "                                              to_char(d, 'YYYY')" +
                        "                                                  || ' ( g_general_site_code );'" +
                        "                                       from generate_series($1, $2, '1 year') as d" +
                        "        loop" +
                        "            execute create_h_data;" +
                        "            execute index_h_data;" +
                        "        end loop;" +
                        "end ;" +
                        "$$" +
                        "    language plpgsql;";
                break;
            case IGRA:
                sql = "create or replace function igra_data_partition_creation(date, date)" +
                        "    returns void as" +
                        " $$ " +
                        "declare" +
                        "    create_h_data text;" +
                        "    index_h_data text;" +
                        "    index_h_fk_data text;" +
                        "begin" +
                        "    for create_h_data, index_h_data, index_h_fk_data in select 'create unlogged table guan_data_value_'" +
                        "                                                  ||" +
                        "                                              to_char(d, 'YYYY')" +
                        "                                                  ||" +
                        "                                              ' PARTITION OF guan_data_value FOR VALUES FROM (''' ||" +
                        "                                              to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||" +
                        "                                              to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');'," +
                        "                                              'create index guan_data_value_'" +
                        "                                                  ||" +
                        "                                              to_char(d, 'YYYY')" +
                        "                                                  || '_idstation_idx on guan_data_value_'" +
                        "                                                  ||" +
                        "                                              to_char(d, 'YYYY')" +
                        "                                                  || ' ( idstation );'," +
                        "                                              'create index guan_data_value_'" +
                        "                                                  ||" +
                        "                                              to_char(d, 'YYYY')" +
                        "                                                  || '_fk_idx on guan_data_value_'" +
                        "                                                  ||" +
                        "                                              to_char(d, 'YYYY')" +
                        "                                                  || ' ( guan_data_header_id );'" +
                        "                                       from generate_series($1, $2, '1 year') as d" +
                        "        loop" +
                        "            execute create_h_data;" +
                        "            execute index_h_data;" +
                        "            execute index_h_fk_data;" +
                        "        end loop;" +
                        "end ;" +
                        "$$" +
                        "    language plpgsql;";
                break;
            case RHARM:
                sql = "create or replace function rharm_harmonized_data_creation(date, date)" +
                        "    returns void as" +
                        " $$ " +
                        "declare" +
                        "    create_h_data text;" +
                        "    index_h_data  text;" +
                        "    index_h_fk_data text;" +
                        "begin" +
                        "    for create_h_data, index_h_data, index_h_fk_data in select 'create unlogged table harmonized_data_'" +
                        "                                                  ||" +
                        "                                              to_char(d, 'YYYY')" +
                        "                                                  ||" +
                        "                                              ' PARTITION OF harmonized_data FOR VALUES FROM (''' ||" +
                        "                                              to_char(d, 'YYYY-MM-DD') || ''') TO (''' ||" +
                        "                                              to_char(d + interval '1 year', 'YYYY-MM-DD') || ''');'," +
                        "                                              'create index harmonized_data_'" +
                        "                                                  ||" +
                        "                                              to_char(d, 'YYYY')" +
                        "                                                  || '_idstation_idx on harmonized_data_'" +
                        "                                                  ||" +
                        "                                              to_char(d, 'YYYY')" +
                        "                                                  || ' ( idstation );'," +
                        "                                              'create index harmonized_data_'" +
                        "                                                  ||" +
                        "                                              to_char(d, 'YYYY')" +
                        "                                                  || '_fk_idx on harmonized_data_'" +
                        "                                                  ||" +
                        "                                              to_char(d, 'YYYY')" +
                        "                                                  || ' ( header_id );'" +
                        "                                       from generate_series($1, $2, '1 year') as d" +
                        "        loop" +
                        "            execute create_h_data;" +
                        "            execute index_h_data;" +
                        "            execute index_h_fk_data;" +
                        "        end loop;" +
                        "end ;" +
                        "$$" +
                        "    language plpgsql;";
                break;
        }

        executeStatement(sql);
    }

    private void createTablesPartitioned_data(TABLE table) {
        String sql = "";

        switch (table) {
            case GRUAN:
                sql = "select gruan_data_value_creation('2005-01-01', '2026-01-01')";
                break;
            case IGRA:
                sql = "select igra_data_partition_creation('1978-01-01', '2026-01-01');";
                break;
            case RHARM:
                sql = "select rharm_harmonized_data_creation('1978-01-01', '2026-01-01');";
                break;
        }

        executeStatement(sql);
    }

    private void executeStatement(String sql) {
        PreparedStatement statement;

        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                DataSource.close(connection);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
