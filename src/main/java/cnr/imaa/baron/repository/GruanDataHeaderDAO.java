package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.GruanDataHeader;
import cnr.imaa.baron.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;

public class GruanDataHeaderDAO extends BaseHeaderDAO {
    private final Logger log = LoggerFactory.getLogger(GruanDataHeaderDAO.class);

    private final String TABLE_NAME = "gruan_data_header";
    private final BaseDataDAO gruanDataValueDAO;
    private final String FIELDS = "g_product_id,g_general_site_code,date_of_observation,comment,conventions,g_ascent_balloon_number,g_ascent_balloon_type,g_ascent_burstpoint_altitude" +
            ",g_ascent_burstpoint_pressure,g_ascent_filling_weight,g_ascent_gross_weight,g_ascent_id,g_ascent_include_descent,g_ascent_payload" +
            ",g_ascent_precipitable_water_column,g_ascent_precipitable_water_columnu,g_ascent_standard_time,g_ascent_start_time,g_ascent_tropopause_height" +
            ",g_ascent_tropopause_pot_temperature,g_ascent_tropopause_pressure,g_ascent_tropopause_temperature,g_ascent_unwinder_type,g_general_file_type_version" +
            ",g_general_site_institution,g_general_site_name,g_general_site_wmo_id,g_general_timestamp,g_instrument_comment,g_instrument_manufacturer" +
            ",g_instrument_serial_number,g_instrument_software_version,g_instrument_telemetry_sonde,g_instrument_type,g_instrument_type_family,g_instrument_weight" +
            ",g_measuring_system_altitude,g_measuring_system_id,g_measuring_system_latitude,g_measuring_system_longitude,g_measuring_system_type,g_product_code" +
            ",g_product_history,g_product_level,g_product_level_description,g_product_name,g_product_org_resolution,g_product_processing_code,g_product_producer" +
            ",g_product_references,g_product_status,g_product_status_description,g_product_version,g_surface_obs_pressure,g_surface_obs_relative_humidity" +
            ",g_surface_obs_temperature,history,institution,references_,source,title";

    public GruanDataHeaderDAO(DataSource dataSource) {
        super(dataSource);
        gruanDataValueDAO = new GruanDataValueDAO(dataSource);
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;

        for (String key : params.keySet()) {
            switch (key) {
                case "g_product_id":
                    setParameter(pstmt, indexParams, params.get(key), Types.BIGINT);
                    indexParams++;
                    break;
                case "g_measuring_system_altitude":
                case "g_measuring_system_latitude":
                case "g_measuring_system_longitude":
                    setParameter(pstmt, indexParams, params.get(key), Types.FLOAT);
                    indexParams++;
                    break;
                case "comment":
                case "conventions":
                case "g_ascent_balloon_number":
                case "g_ascent_balloon_type":
                case "g_ascent_burstpoint_altitude":
                case "g_ascent_burstpoint_pressure":
                case "g_ascent_filling_weight":
                case "g_ascent_gross_weight":
                case "g_ascent_id":
                case "g_ascent_include_descent":
                case "g_ascent_payload":
                case "g_ascent_precipitable_water_column":
                case "g_ascent_precipitable_water_columnu":
                case "g_ascent_standard_time":
                case "g_ascent_start_time":
                case "g_ascent_tropopause_height":
                case "g_ascent_tropopause_pot_temperature":
                case "g_ascent_tropopause_pressure":
                case "g_ascent_tropopause_temperature":
                case "g_ascent_unwinder_type":
                case "g_general_file_type_version":
                case "g_general_site_code":
                case "g_general_site_institution":
                case "g_general_site_name":
                case "g_general_site_wmo_id":
                case "g_general_timestamp":
                case "g_instrument_comment":
                case "g_instrument_manufacturer":
                case "g_instrument_serial_number":
                case "g_instrument_software_version":
                case "g_instrument_telemetry_sonde":
                case "g_instrument_type":
                case "g_instrument_type_family":
                case "g_instrument_weight":
                case "g_measuring_system_id":
                case "g_measuring_system_type":
                case "g_product_code":
                case "g_product_history":
                case "g_product_level":
                case "g_product_level_description":
                case "g_product_name":
                case "g_product_org_resolution":
                case "g_product_processing_code":
                case "g_product_producer":
                case "g_product_references":
                case "g_product_status":
                case "g_product_status_description":
                case "g_product_version":
                case "g_surface_obs_pressure":
                case "g_surface_obs_relative_humidity":
                case "g_surface_obs_temperature":
                case "history":
                case "institution":
                case "references_":
                case "source":
                case "title":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "date_of_observation":
                    setParameter(pstmt, indexParams, params.get(key), Types.TIMESTAMP_WITH_TIMEZONE);
                    indexParams++;
                    break;
                default:
                    throw new RuntimeException("Unexpected column name: '" + key + "'");
            }
        }
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new GruanDataHeader();
    }

    @Override
    protected Object build(ResultSet rs) {
        GruanDataHeader gruanDataHeader = new GruanDataHeader();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "g_product_id":
                        gruanDataHeader.setG_product_id(rs.getObject("g_product_id") != null ? rs.getLong("g_product_id") : null);
                        break;
                    case "comment":
                        gruanDataHeader.setComment(rs.getObject("comment") != null ? rs.getString("comment") : null);
                        break;
                    case "conventions":
                        gruanDataHeader.setConventions(rs.getObject("conventions") != null ? rs.getString("conventions") : null);
                        break;
                    case "g_ascent_balloon_number":
                        gruanDataHeader.setG_ascent_balloon_number(rs.getObject("g_ascent_balloon_number") != null ? rs.getString("g_ascent_balloon_number") : null);
                        break;
                    case "g_ascent_balloon_type":
                        gruanDataHeader.setG_ascent_balloon_type(rs.getObject("g_ascent_balloon_type") != null ? rs.getString("g_ascent_balloon_type") : null);
                        break;
                    case "g_ascent_burstpoint_altitude":
                        gruanDataHeader.setG_ascent_burstpoint_altitude(rs.getObject("g_ascent_burstpoint_altitude") != null ? rs.getString("g_ascent_burstpoint_altitude") : null);
                        break;
                    case "g_ascent_burstpoint_pressure":
                        gruanDataHeader.setG_ascent_burstpoint_pressure(rs.getObject("g_ascent_burstpoint_pressure") != null ? rs.getString("g_ascent_burstpoint_pressure") : null);
                        break;
                    case "g_ascent_filling_weight":
                        gruanDataHeader.setG_ascent_filling_weight(rs.getObject("g_ascent_filling_weight") != null ? rs.getString("g_ascent_filling_weight") : null);
                        break;
                    case "g_ascent_gross_weight":
                        gruanDataHeader.setG_ascent_gross_weight(rs.getObject("g_ascent_gross_weight") != null ? rs.getString("g_ascent_gross_weight") : null);
                        break;
                    case "g_ascent_id":
                        gruanDataHeader.setG_ascent_id(rs.getObject("g_ascent_id") != null ? rs.getString("g_ascent_id") : null);
                        break;
                    case "g_ascent_include_descent":
                        gruanDataHeader.setG_ascent_include_descent(rs.getObject("g_ascent_include_descent") != null ? rs.getString("g_ascent_include_descent") : null);
                        break;
                    case "g_ascent_payload":
                        gruanDataHeader.setG_ascent_payload(rs.getObject("g_ascent_payload") != null ? rs.getString("g_ascent_payload") : null);
                        break;
                    case "g_ascent_precipitable_water_column":
                        gruanDataHeader.setG_ascent_precipitable_water_column(rs.getObject("g_ascent_precipitable_water_column") != null ? rs.getString("g_ascent_precipitable_water_column") : null);
                        break;
                    case "g_ascent_precipitable_water_columnu":
                        gruanDataHeader.setG_ascent_precipitable_water_columnu(rs.getObject("g_ascent_precipitable_water_columnu") != null ? rs.getString("g_ascent_precipitable_water_columnu") : null);
                        break;
                    case "g_ascent_standard_time":
                        gruanDataHeader.setG_ascent_standard_time(rs.getObject("g_ascent_standard_time") != null ? rs.getString("g_ascent_standard_time") : null);
                        break;
                    case "g_ascent_start_time":
                        gruanDataHeader.setG_ascent_start_time(rs.getObject("g_ascent_start_time") != null ? rs.getString("g_ascent_start_time") : null);
                        break;
                    case "g_ascent_tropopause_height":
                        gruanDataHeader.setG_ascent_tropopause_height(rs.getObject("g_ascent_tropopause_height") != null ? rs.getString("g_ascent_tropopause_height") : null);
                        break;
                    case "g_ascent_tropopause_pot_temperature":
                        gruanDataHeader.setG_ascent_tropopause_pot_temperature(rs.getObject("g_ascent_tropopause_pot_temperature") != null ? rs.getString("g_ascent_tropopause_pot_temperature") : null);
                        break;
                    case "g_ascent_tropopause_pressure":
                        gruanDataHeader.setG_ascent_tropopause_pressure(rs.getObject("g_ascent_tropopause_pressure") != null ? rs.getString("g_ascent_tropopause_pressure") : null);
                        break;
                    case "g_ascent_tropopause_temperature":
                        gruanDataHeader.setG_ascent_tropopause_temperature(rs.getObject("g_ascent_tropopause_temperature") != null ? rs.getString("g_ascent_tropopause_temperature") : null);
                        break;
                    case "g_ascent_unwinder_type":
                        gruanDataHeader.setG_ascent_unwinder_type(rs.getObject("g_ascent_unwinder_type") != null ? rs.getString("g_ascent_unwinder_type") : null);
                        break;
                    case "g_general_file_type_version":
                        gruanDataHeader.setG_general_file_type_version(rs.getObject("g_general_file_type_version") != null ? rs.getString("g_general_file_type_version") : null);
                        break;
                    case "g_general_site_code":
                        if (rs.getObject("g_general_site_code") != null) {
                            Station station = new Station();
                            station.setIdStation(rs.getString("g_general_site_code"));

                            gruanDataHeader.setStation(station);
                        }
                        break;
                    case "g_general_site_institution":
                        gruanDataHeader.setG_general_site_institution(rs.getObject("g_general_site_institution") != null ? rs.getString("g_general_site_institution") : null);
                        break;
                    case "g_general_site_name":
                        gruanDataHeader.setG_general_site_name(rs.getObject("g_general_site_name") != null ? rs.getString("g_general_site_name") : null);
                        break;
                    case "g_general_site_wmo_id":
                        gruanDataHeader.setG_general_site_wmo_id(rs.getObject("g_general_site_wmo_id") != null ? rs.getString("g_general_site_wmo_id") : null);
                        break;
                    case "g_general_timestamp":
                        gruanDataHeader.setG_general_timestamp(rs.getObject("g_general_timestamp") != null ? rs.getString("g_general_timestamp") : null);
                        break;
                    case "g_instrument_comment":
                        gruanDataHeader.setG_instrument_comment(rs.getObject("g_instrument_comment") != null ? rs.getString("g_instrument_comment") : null);
                        break;
                    case "g_instrument_manufacturer":
                        gruanDataHeader.setG_instrument_manufacturer(rs.getObject("g_instrument_manufacturer") != null ? rs.getString("g_instrument_manufacturer") : null);
                        break;
                    case "g_instrument_serial_number":
                        gruanDataHeader.setG_instrument_serial_number(rs.getObject("g_instrument_serial_number") != null ? rs.getString("g_instrument_serial_number") : null);
                        break;
                    case "g_instrument_software_version":
                        gruanDataHeader.setG_instrument_software_version(rs.getObject("g_instrument_software_version") != null ? rs.getString("g_instrument_software_version") : null);
                        break;
                    case "g_instrument_telemetry_sonde":
                        gruanDataHeader.setG_instrument_telemetry_sonde(rs.getObject("g_instrument_telemetry_sonde") != null ? rs.getString("g_instrument_telemetry_sonde") : null);
                        break;
                    case "g_instrument_type":
                        gruanDataHeader.setG_instrument_type(rs.getObject("g_instrument_type") != null ? rs.getString("g_instrument_type") : null);
                        break;
                    case "g_instrument_type_family":
                        gruanDataHeader.setG_instrument_type_family(rs.getObject("g_instrument_type_family") != null ? rs.getString("g_instrument_type_family") : null);
                        break;
                    case "g_instrument_weight":
                        gruanDataHeader.setG_instrument_weight(rs.getObject("g_instrument_weight") != null ? rs.getString("g_instrument_weight") : null);
                        break;
                    case "g_measuring_system_altitude":
                        gruanDataHeader.setG_measuring_system_altitude(rs.getObject("g_measuring_system_altitude") != null ? rs.getFloat("g_measuring_system_altitude") : null);
                        break;
                    case "g_measuring_system_id":
                        gruanDataHeader.setG_measuring_system_id(rs.getObject("g_measuring_system_id") != null ? rs.getString("g_measuring_system_id") : null);
                        break;
                    case "g_measuring_system_latitude":
                        gruanDataHeader.setG_measuring_system_latitude(rs.getObject("g_measuring_system_latitude") != null ? rs.getFloat("g_measuring_system_latitude") : null);
                        break;
                    case "g_measuring_system_longitude":
                        gruanDataHeader.setG_measuring_system_longitude(rs.getObject("g_measuring_system_longitude") != null ? rs.getFloat("g_measuring_system_longitude") : null);
                        break;
                    case "g_measuring_system_type":
                        gruanDataHeader.setG_measuring_system_type(rs.getObject("g_measuring_system_type") != null ? rs.getString("g_measuring_system_type") : null);
                        break;
                    case "g_product_code":
                        gruanDataHeader.setG_product_code(rs.getObject("g_product_code") != null ? rs.getString("g_product_code") : null);
                        break;
                    case "g_product_history":
                        gruanDataHeader.setG_product_history(rs.getObject("g_product_history") != null ? rs.getString("g_product_history") : null);
                        break;
                    case "g_product_level":
                        gruanDataHeader.setG_product_level(rs.getObject("g_product_level") != null ? rs.getString("g_product_level") : null);
                        break;
                    case "g_product_level_description":
                        gruanDataHeader.setG_product_level_description(rs.getObject("g_product_level_description") != null ? rs.getString("g_product_level_description") : null);
                        break;
                    case "g_product_name":
                        gruanDataHeader.setG_product_name(rs.getObject("g_product_name") != null ? rs.getString("g_product_name") : null);
                        break;
                    case "g_product_org_resolution":
                        gruanDataHeader.setG_product_org_resolution(rs.getObject("g_product_org_resolution") != null ? rs.getString("g_product_org_resolution") : null);
                        break;
                    case "g_product_processing_code":
                        gruanDataHeader.setG_product_processing_code(rs.getObject("g_product_processing_code") != null ? rs.getString("g_product_processing_code") : null);
                        break;
                    case "g_product_producer":
                        gruanDataHeader.setG_product_producer(rs.getObject("g_product_producer") != null ? rs.getString("g_product_producer") : null);
                        break;
                    case "g_product_references":
                        gruanDataHeader.setG_product_references(rs.getObject("g_product_references") != null ? rs.getString("g_product_references") : null);
                        break;
                    case "g_product_status":
                        gruanDataHeader.setG_product_status(rs.getObject("g_product_status") != null ? rs.getString("g_product_status") : null);
                        break;
                    case "g_product_status_description":
                        gruanDataHeader.setG_product_status_description(rs.getObject("g_product_status_description") != null ? rs.getString("g_product_status_description") : null);
                        break;
                    case "g_product_version":
                        gruanDataHeader.setG_product_version(rs.getObject("g_product_version") != null ? rs.getString("g_product_version") : null);
                        break;
                    case "g_surface_obs_pressure":
                        gruanDataHeader.setG_surface_obs_pressure(rs.getObject("g_surface_obs_pressure") != null ? rs.getString("g_surface_obs_pressure") : null);
                        break;
                    case "g_surface_obs_relative_humidity":
                        gruanDataHeader.setG_surface_obs_relative_humidity(rs.getObject("g_surface_obs_relative_humidity") != null ? rs.getString("g_surface_obs_relative_humidity") : null);
                        break;
                    case "g_surface_obs_temperature":
                        gruanDataHeader.setG_surface_obs_temperature(rs.getObject("g_surface_obs_temperature") != null ? rs.getString("g_surface_obs_temperature") : null);
                        break;
                    case "history":
                        gruanDataHeader.setHistory(rs.getObject("history") != null ? rs.getString("history") : null);
                        break;
                    case "institution":
                        gruanDataHeader.setInstitution(rs.getObject("institution") != null ? rs.getString("institution") : null);
                        break;
                    case "references_":
                        gruanDataHeader.setReferences_(rs.getObject("references_") != null ? rs.getString("references_") : null);
                        break;
                    case "source":
                        gruanDataHeader.setSource(rs.getObject("source") != null ? rs.getString("source") : null);
                        break;
                    case "title":
                        gruanDataHeader.setTitle(rs.getObject("title") != null ? rs.getString("title") : null);
                        break;
                    case "date_of_observation":
                        gruanDataHeader.setDateOfObservation(rs.getObject("date_of_observation") != null ? rs.getTimestamp("date_of_observation").toInstant() : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return gruanDataHeader;
    }

    @Override
    protected LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand) {
        GruanDataHeader gruanDataHeader = (GruanDataHeader) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (sqlCommand != SQL_COMMAND.INSERT) {
            if (gruanDataHeader.getG_product_id() != null)
                params.put("g_product_id", gruanDataHeader.getG_product_id());
            if (gruanDataHeader.getStation() != null && gruanDataHeader.getStation().getIdStation() != null)
                params.put("g_general_site_code", gruanDataHeader.getStation().getIdStation());
            if (gruanDataHeader.getDateOfObservation() != null)
                params.put("date_of_observation", gruanDataHeader.getDateOfObservation());
        } else {
            params.put("g_product_id", gruanDataHeader.getG_product_id());
            params.put("g_general_site_code", gruanDataHeader.getStation().getIdStation());
            params.put("date_of_observation", gruanDataHeader.getDateOfObservation());
            params.put("comment", gruanDataHeader.getComment());
            params.put("conventions", gruanDataHeader.getConventions());
            params.put("g_ascent_balloon_number", gruanDataHeader.getG_ascent_balloon_number());
            params.put("g_ascent_balloon_type", gruanDataHeader.getG_ascent_balloon_type());
            params.put("g_ascent_burstpoint_altitude", gruanDataHeader.getG_ascent_burstpoint_altitude());
            params.put("g_ascent_burstpoint_pressure", gruanDataHeader.getG_ascent_burstpoint_pressure());
            params.put("g_ascent_filling_weight", gruanDataHeader.getG_ascent_filling_weight());
            params.put("g_ascent_gross_weight", gruanDataHeader.getG_ascent_gross_weight());
            params.put("g_ascent_id", gruanDataHeader.getG_ascent_id());
            params.put("g_ascent_include_descent", gruanDataHeader.getG_ascent_include_descent());
            params.put("g_ascent_payload", gruanDataHeader.getG_ascent_payload());
            params.put("g_ascent_precipitable_water_column", gruanDataHeader.getG_ascent_precipitable_water_column());
            params.put("g_ascent_precipitable_water_columnu", gruanDataHeader.getG_ascent_precipitable_water_columnu());
            params.put("g_ascent_standard_time", gruanDataHeader.getG_ascent_standard_time());
            params.put("g_ascent_start_time", gruanDataHeader.getG_ascent_start_time());
            params.put("g_ascent_tropopause_height", gruanDataHeader.getG_ascent_tropopause_height());
            params.put("g_ascent_tropopause_pot_temperature", gruanDataHeader.getG_ascent_tropopause_pot_temperature());
            params.put("g_ascent_tropopause_pressure", gruanDataHeader.getG_ascent_tropopause_pressure());
            params.put("g_ascent_tropopause_temperature", gruanDataHeader.getG_ascent_tropopause_temperature());
            params.put("g_ascent_unwinder_type", gruanDataHeader.getG_ascent_unwinder_type());
            params.put("g_general_file_type_version", gruanDataHeader.getG_general_file_type_version());
            params.put("g_general_site_institution", gruanDataHeader.getG_general_site_institution());
            params.put("g_general_site_name", gruanDataHeader.getG_general_site_name());
            params.put("g_general_site_wmo_id", gruanDataHeader.getG_general_site_wmo_id());
            params.put("g_general_timestamp", gruanDataHeader.getG_general_timestamp());
            params.put("g_instrument_comment", gruanDataHeader.getG_instrument_comment());
            params.put("g_instrument_manufacturer", gruanDataHeader.getG_instrument_manufacturer());
            params.put("g_instrument_serial_number", gruanDataHeader.getG_instrument_serial_number());
            params.put("g_instrument_software_version", gruanDataHeader.getG_instrument_software_version());
            params.put("g_instrument_telemetry_sonde", gruanDataHeader.getG_instrument_telemetry_sonde());
            params.put("g_instrument_type", gruanDataHeader.getG_instrument_type());
            params.put("g_instrument_type_family", gruanDataHeader.getG_instrument_type_family());
            params.put("g_instrument_weight", gruanDataHeader.getG_instrument_weight());
            params.put("g_measuring_system_altitude", gruanDataHeader.getG_measuring_system_altitude());
            params.put("g_measuring_system_id", gruanDataHeader.getG_measuring_system_id());
            params.put("g_measuring_system_latitude", gruanDataHeader.getG_measuring_system_latitude());
            params.put("g_measuring_system_longitude", gruanDataHeader.getG_measuring_system_longitude());
            params.put("g_measuring_system_type", gruanDataHeader.getG_measuring_system_type());
            params.put("g_product_code", gruanDataHeader.getG_product_code());
            params.put("g_product_history", gruanDataHeader.getG_product_history());
            params.put("g_product_level", gruanDataHeader.getG_product_level());
            params.put("g_product_level_description", gruanDataHeader.getG_product_level_description());
            params.put("g_product_name", gruanDataHeader.getG_product_name());
            params.put("g_product_org_resolution", gruanDataHeader.getG_product_org_resolution());
            params.put("g_product_processing_code", gruanDataHeader.getG_product_processing_code());
            params.put("g_product_producer", gruanDataHeader.getG_product_producer());
            params.put("g_product_references", gruanDataHeader.getG_product_references());
            params.put("g_product_status", gruanDataHeader.getG_product_status());
            params.put("g_product_status_description", gruanDataHeader.getG_product_status_description());
            params.put("g_product_version", gruanDataHeader.getG_product_version());
            params.put("g_surface_obs_pressure", gruanDataHeader.getG_surface_obs_pressure());
            params.put("g_surface_obs_relative_humidity", gruanDataHeader.getG_surface_obs_relative_humidity());
            params.put("g_surface_obs_temperature", gruanDataHeader.getG_surface_obs_temperature());
            params.put("history", gruanDataHeader.getHistory());
            params.put("institution", gruanDataHeader.getInstitution());
            params.put("references_", gruanDataHeader.getReferences_());
            params.put("source", gruanDataHeader.getSource());
            params.put("title", gruanDataHeader.getTitle());
        }

        return params;
    }

    @Override
    protected String getFields() {
        return FIELDS;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected BaseDataDAO getBaseDataDAO() {
        return gruanDataValueDAO;
    }

    @Override
    protected String getUpdateSetStatement(Object obj) {
        //TODO
        return null;
    }
}
