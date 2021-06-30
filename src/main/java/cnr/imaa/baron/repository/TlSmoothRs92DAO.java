package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.TlSmoothRs92;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TlSmoothRs92DAO extends BaseObjDAO {
    private final String FIELDS = "pressure,cor_temp_tl_bias,u_cor_temp_tl,cor_rh_tl_night,cor_rh_tl_day,u_cor_rh_tl_night,u_cor_rh_tl_day";
    private final String TABLE_NAME = "tl_smooth_rs_92";

    public TlSmoothRs92DAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new TlSmoothRs92();
    }

    @Override
    protected Object build(ResultSet rs) {
        TlSmoothRs92 tlSmoothRs92 = new TlSmoothRs92();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "pressure":
                        tlSmoothRs92.setPressure(rs.getObject("pressure") != null ? rs.getFloat("pressure") : null);
                        break;
                    case "cor_temp_tl_bias":
                        tlSmoothRs92.setCor_temp_tl_bias(rs.getObject("cor_temp_tl_bias") != null ? rs.getFloat("cor_temp_tl_bias") : null);
                        break;
                    case "u_cor_temp_tl":
                        tlSmoothRs92.setU_cor_temp_tl(rs.getObject("u_cor_temp_tl") != null ? rs.getFloat("u_cor_temp_tl") : null);
                        break;
                    case "cor_rh_tl_night":
                        tlSmoothRs92.setCor_rh_tl_night(rs.getObject("cor_rh_tl_night") != null ? rs.getFloat("cor_rh_tl_night") : null);
                        break;
                    case "cor_rh_tl_day":
                        tlSmoothRs92.setCor_rh_tl_day(rs.getObject("cor_rh_tl_day") != null ? rs.getFloat("cor_rh_tl_day") : null);
                        break;
                    case "u_cor_rh_tl_night":
                        tlSmoothRs92.setU_cor_rh_tl_night(rs.getObject("u_cor_rh_tl_night") != null ? rs.getFloat("u_cor_rh_tl_night") : null);
                        break;
                    case "u_cor_rh_tl_day":
                        tlSmoothRs92.setU_cor_rh_tl_day(rs.getObject("u_cor_rh_tl_day") != null ? rs.getFloat("u_cor_rh_tl_day") : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return tlSmoothRs92;
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;

        for (String key : params.keySet()) {
            switch (key) {
                case "pressure":
                case "cor_temp_tl_bias":
                case "u_cor_temp_tl":
                case "cor_rh_tl_night":
                case "cor_rh_tl_day":
                case "u_cor_rh_tl_night":
                case "u_cor_rh_tl_day":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                default:
                    throw new RuntimeException("Unexpected column name: '" + key + "'");
            }
        }
    }

    @Override
    protected LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand) {
        TlSmoothRs92 tlSmoothRs92 = (TlSmoothRs92) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (tlSmoothRs92.getPressure() != null) params.put("pressure", tlSmoothRs92.getPressure());
        if (tlSmoothRs92.getCor_temp_tl_bias() != null)
            params.put("cor_temp_tl_bias", tlSmoothRs92.getCor_temp_tl_bias());
        if (tlSmoothRs92.getU_cor_temp_tl() != null) params.put("u_cor_temp_tl", tlSmoothRs92.getU_cor_temp_tl());
        if (tlSmoothRs92.getCor_rh_tl_night() != null) params.put("cor_rh_tl_night", tlSmoothRs92.getCor_rh_tl_night());
        if (tlSmoothRs92.getCor_rh_tl_day() != null) params.put("cor_rh_tl_day", tlSmoothRs92.getCor_rh_tl_day());
        if (tlSmoothRs92.getU_cor_rh_tl_night() != null)
            params.put("u_cor_rh_tl_night", tlSmoothRs92.getU_cor_rh_tl_night());
        if (tlSmoothRs92.getU_cor_rh_tl_day() != null) params.put("u_cor_rh_tl_day", tlSmoothRs92.getU_cor_rh_tl_day());

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
    protected String getUpdateSetStatement(Object obj) {
        TlSmoothRs92 tlSmoothRs92 = (TlSmoothRs92) obj;
        StringBuilder updateSetStatement = new StringBuilder();

        List<String> updateSetStatementList = new ArrayList<>();

        if (tlSmoothRs92.getPressure() != null) updateSetStatementList.add("pressure = ?");
        if (tlSmoothRs92.getCor_temp_tl_bias() != null) updateSetStatementList.add("cor_temp_tl_bias = ?");
        if (tlSmoothRs92.getU_cor_temp_tl() != null) updateSetStatementList.add("u_cor_temp_tl = ?");
        if (tlSmoothRs92.getCor_rh_tl_night() != null) updateSetStatementList.add("cor_rh_tl_night = ?");
        if (tlSmoothRs92.getCor_rh_tl_day() != null) updateSetStatementList.add("cor_rh_tl_day = ?");
        if (tlSmoothRs92.getU_cor_rh_tl_night() != null) updateSetStatementList.add("u_cor_rh_tl_night = ?");
        if (tlSmoothRs92.getU_cor_rh_tl_day() != null) updateSetStatementList.add("u_cor_rh_tl_day = ?");

        for (int i = 0; i < updateSetStatementList.size(); i++) {
            if (i > 0)
                updateSetStatement.append(",");

            updateSetStatement.append(updateSetStatementList.get(i));
        }

        return updateSetStatement.toString();
    }
}
