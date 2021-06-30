package cnr.imaa.baron.bll.converter;

import cnr.imaa.baron.model.UscrnDaily;
import cnr.imaa.baron.model.UscrnObj;

import java.util.ArrayList;
import java.util.List;

public class UscrnConverterDaily extends UscrnConverter implements Converter {
    @Override
    public Object convert(Object input) {
        String[] rows = (String[]) input;

        List<UscrnDaily> uscrnDailyList = new ArrayList<>();

        for (String row : rows) {
            UscrnDaily uscrnDaily = new UscrnDaily();

            setFields(uscrnDaily, row);

            uscrnDaily.setDate_of_observation_lst(getInstant(uscrnDaily.getLst_date(), null));

            uscrnDailyList.add(uscrnDaily);
        }

        return uscrnDailyList;
    }

    @Override
    protected void setField(UscrnObj uscrnObj, String field, int idx) {
        UscrnDaily uscrnDaily = (UscrnDaily) uscrnObj;

        switch (idx) {
            case 0:
                uscrnDaily.setWbanno(Integer.parseInt(field));
                break;
            case 1:
                uscrnDaily.setLst_date(field);
                break;
            case 2:
                uscrnDaily.setCrx_vn(field);
                break;
            case 3:
                uscrnDaily.setLongitude(Double.parseDouble(field));
                break;
            case 4:
                uscrnDaily.setLatitude(Double.parseDouble(field));
                break;
            case 5:
                uscrnDaily.setT_daily_max(Double.parseDouble(field));
                break;
            case 6:
                uscrnDaily.setT_daily_min(Double.parseDouble(field));
                break;
            case 7:
                uscrnDaily.setT_daily_mean(Double.parseDouble(field));
                break;
            case 8:
                uscrnDaily.setT_daily_avg(Double.parseDouble(field));
                break;
            case 9:
                uscrnDaily.setP_daily_calc(Double.parseDouble(field));
                break;
            case 10:
                uscrnDaily.setSolarad_daily(Double.parseDouble(field));
                break;
            case 11:
                uscrnDaily.setSur_temp_daily_type(field);
                break;
            case 12:
                uscrnDaily.setSur_temp_daily_max(Double.parseDouble(field));
                break;
            case 13:
                uscrnDaily.setSur_temp_daily_min(Double.parseDouble(field));
                break;
            case 14:
                uscrnDaily.setSur_temp_daily_avg(Double.parseDouble(field));
                break;
            case 15:
                uscrnDaily.setRh_daily_max(Double.parseDouble(field));
                break;
            case 16:
                uscrnDaily.setRh_daily_min(Double.parseDouble(field));
                break;
            case 17:
                uscrnDaily.setRh_daily_avg(Double.parseDouble(field));
                break;
            case 18:
                uscrnDaily.setSoil_moisture_5_daily(Double.parseDouble(field));
                break;
            case 19:
                uscrnDaily.setSoil_moisture_10_daily(Double.parseDouble(field));
                break;
            case 20:
                uscrnDaily.setSoil_moisture_20_daily(Double.parseDouble(field));
                break;
            case 21:
                uscrnDaily.setSoil_moisture_50_daily(Double.parseDouble(field));
                break;
            case 22:
                uscrnDaily.setSoil_moisture_100_daily(Double.parseDouble(field));
                break;
            case 23:
                uscrnDaily.setSoil_temp_5_daily(Double.parseDouble(field));
                break;
            case 24:
                uscrnDaily.setSoil_temp_10_daily(Double.parseDouble(field));
                break;
            case 25:
                uscrnDaily.setSoil_temp_20_daily(Double.parseDouble(field));
                break;
            case 26:
                uscrnDaily.setSoil_temp_50_daily(Double.parseDouble(field));
                break;
            case 27:
                uscrnDaily.setSoil_temp_100_daily(Double.parseDouble(field));
                break;
        }
    }
}
