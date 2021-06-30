package cnr.imaa.baron.bll.converter;

import cnr.imaa.baron.model.UscrnHourly;
import cnr.imaa.baron.model.UscrnObj;

import java.util.ArrayList;
import java.util.List;

public class UscrnConverterHourly extends UscrnConverter implements Converter {
    @Override
    public Object convert(Object input) {
        String[] rows = (String[]) input;

        List<UscrnHourly> uscrnHourlyList = new ArrayList<>();

        for (String row : rows) {
            UscrnHourly uscrnHourly = new UscrnHourly();

            setFields(uscrnHourly, row);

            uscrnHourly.setDate_of_observation_utc(getInstant(uscrnHourly.getUtc_date(), uscrnHourly.getUtc_time()));
            uscrnHourly.setDate_of_observation_lst(getInstant(uscrnHourly.getLst_date(), uscrnHourly.getLst_time()));

            uscrnHourlyList.add(uscrnHourly);
        }

        return uscrnHourlyList;
    }

    @Override
    protected void setField(UscrnObj uscrnObj, String field, int idx) {
        UscrnHourly uscrnHourly = (UscrnHourly) uscrnObj;

        switch (idx) {
            case 0:
                uscrnHourly.setWbanno(Integer.parseInt(field));
                break;
            case 1:
                uscrnHourly.setUtc_date(field);
                break;
            case 2:
                uscrnHourly.setUtc_time(field);
                break;
            case 3:
                uscrnHourly.setLst_date(field);
                break;
            case 4:
                uscrnHourly.setLst_time(field);
                break;
            case 5:
                uscrnHourly.setCrx_vn(field);
                break;
            case 6:
                uscrnHourly.setLongitude(Double.parseDouble(field));
                break;
            case 7:
                uscrnHourly.setLatitude(Double.parseDouble(field));
                break;
            case 8:
                uscrnHourly.setT_calc(Double.parseDouble(field));
                break;
            case 9:
                uscrnHourly.setT_hr_avg(Double.parseDouble(field));
                break;
            case 10:
                uscrnHourly.setT_max(Double.parseDouble(field));
                break;
            case 11:
                uscrnHourly.setT_min(Double.parseDouble(field));
                break;
            case 12:
                uscrnHourly.setP_calc(Double.parseDouble(field));
                break;
            case 13:
                uscrnHourly.setSolarad(Double.parseDouble(field));
                break;
            case 14:
                uscrnHourly.setSolarad_flag(field);
                break;
            case 15:
                uscrnHourly.setSolarad_max(Double.parseDouble(field));
                break;
            case 16:
                uscrnHourly.setSolarad_max_flag(field);
                break;
            case 17:
                uscrnHourly.setSolarad_min(Double.parseDouble(field));
                break;
            case 18:
                uscrnHourly.setSolarad_min_flag(field);
                break;
            case 19:
                uscrnHourly.setSur_temp_type(field);
                break;
            case 20:
                uscrnHourly.setSur_temp(Double.parseDouble(field));
                break;
            case 21:
                uscrnHourly.setSur_temp_flag(field);
                break;
            case 22:
                uscrnHourly.setSur_temp_max(Double.parseDouble(field));
                break;
            case 23:
                uscrnHourly.setSur_temp_max_flag(field);
                break;
            case 24:
                uscrnHourly.setSur_temp_min(Double.parseDouble(field));
                break;
            case 25:
                uscrnHourly.setSur_temp_min_flag(field);
                break;
            case 26:
                uscrnHourly.setRh_hr_avg(Double.parseDouble(field));
                break;
            case 27:
                uscrnHourly.setRh_hr_avg_flag(field);
                break;
            case 28:
                uscrnHourly.setSoil_moisture_5(Double.parseDouble(field));
                break;
            case 29:
                uscrnHourly.setSoil_moisture_10(Double.parseDouble(field));
                break;
            case 30:
                uscrnHourly.setSoil_moisture_20(Double.parseDouble(field));
                break;
            case 31:
                uscrnHourly.setSoil_moisture_50(Double.parseDouble(field));
                break;
            case 32:
                uscrnHourly.setSoil_moisture_100(Double.parseDouble(field));
                break;
            case 33:
                uscrnHourly.setSoil_temp_5(Double.parseDouble(field));
                break;
            case 34:
                uscrnHourly.setSoil_temp_10(Double.parseDouble(field));
                break;
            case 35:
                uscrnHourly.setSoil_temp_20(Double.parseDouble(field));
                break;
            case 36:
                uscrnHourly.setSoil_temp_50(Double.parseDouble(field));
                break;
            case 37:
                uscrnHourly.setSoil_temp_100(Double.parseDouble(field));
                break;
        }
    }
}
