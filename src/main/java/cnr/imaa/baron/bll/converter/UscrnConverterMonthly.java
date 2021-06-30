package cnr.imaa.baron.bll.converter;

import cnr.imaa.baron.model.UscrnMonthly;
import cnr.imaa.baron.model.UscrnObj;

import java.util.ArrayList;
import java.util.List;

public class UscrnConverterMonthly extends UscrnConverter implements Converter {
    @Override
    public Object convert(Object input) {
        String[] rows = (String[]) input;

        List<UscrnMonthly> uscrnMonthlyList = new ArrayList<>();

        for (String row : rows) {
            UscrnMonthly uscrnMonthly = new UscrnMonthly();

            setFields(uscrnMonthly, row);

            uscrnMonthly.setDate_of_observation_lst(getInstant(uscrnMonthly.getLst_yrmo() + "01", null));

            uscrnMonthlyList.add(uscrnMonthly);
        }

        return uscrnMonthlyList;
    }

    @Override
    protected void setField(UscrnObj uscrnObj, String field, int idx) {
        UscrnMonthly uscrnMonthly = (UscrnMonthly) uscrnObj;

        switch (idx) {
            case 0:
                uscrnMonthly.setWbanno(Integer.parseInt(field));
                break;
            case 1:
                uscrnMonthly.setLst_yrmo(field);
                break;
            case 2:
                uscrnMonthly.setCrx_vn_monthly(field);
                break;
            case 3:
                uscrnMonthly.setPrecise_longitude(Double.parseDouble(field));
                break;
            case 4:
                uscrnMonthly.setPrecise_latitude(Double.parseDouble(field));
                break;
            case 5:
                uscrnMonthly.setT_monthly_max(Double.parseDouble(field));
                break;
            case 6:
                uscrnMonthly.setT_monthly_min(Double.parseDouble(field));
                break;
            case 7:
                uscrnMonthly.setT_monthly_mean(Double.parseDouble(field));
                break;
            case 8:
                uscrnMonthly.setT_monthly_avg(Double.parseDouble(field));
                break;
            case 9:
                uscrnMonthly.setP_monthly_calc(Double.parseDouble(field));
                break;
            case 10:
                uscrnMonthly.setSolrad_monthly_avg(Double.parseDouble(field));
                break;
            case 11:
                uscrnMonthly.setSur_temp_monthly_type(field);
                break;
            case 12:
                uscrnMonthly.setSur_temp_monthly_max(Double.parseDouble(field));
                break;
            case 13:
                uscrnMonthly.setSur_temp_monthly_min(Double.parseDouble(field));
                break;
            case 14:
                uscrnMonthly.setSur_temp_monthly_avg(Double.parseDouble(field));
                break;
        }
    }
}
