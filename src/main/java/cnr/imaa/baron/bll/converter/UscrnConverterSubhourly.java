package cnr.imaa.baron.bll.converter;

import cnr.imaa.baron.model.UscrnObj;
import cnr.imaa.baron.model.UscrnSubHourly;

import java.util.ArrayList;
import java.util.List;

public class UscrnConverterSubhourly extends UscrnConverter implements Converter {
    @Override
    public Object convert(Object input) {
        String[] rows = (String[]) input;

        List<UscrnSubHourly> uscrnSubHourlyList = new ArrayList<>();

        for (String row : rows) {
            UscrnSubHourly uscrnSubhourly = new UscrnSubHourly();

            setFields(uscrnSubhourly, row);

            uscrnSubhourly.setDate_of_observation_utc(getInstant(uscrnSubhourly.getUtc_date(), uscrnSubhourly.getUtc_time()));
            uscrnSubhourly.setDate_of_observation_lst(getInstant(uscrnSubhourly.getLst_date(), uscrnSubhourly.getLst_time()));

            uscrnSubHourlyList.add(uscrnSubhourly);
        }

        return uscrnSubHourlyList;
    }

    @Override
    protected void setField(UscrnObj uscrnObj, String field, int idx) {
        UscrnSubHourly uscrnSubhourly = (UscrnSubHourly) uscrnObj;

        switch (idx) {
            case 0:
                uscrnSubhourly.setWbanno(Integer.parseInt(field));
                break;
            case 1:
                uscrnSubhourly.setUtc_date(field);
                break;
            case 2:
                uscrnSubhourly.setUtc_time(field);
                break;
            case 3:
                uscrnSubhourly.setLst_date(field);
                break;
            case 4:
                uscrnSubhourly.setLst_time(field);
                break;
            case 5:
                uscrnSubhourly.setCrx_vn(field);
                break;
            case 6:
                uscrnSubhourly.setLongitude(Double.parseDouble(field));
                break;
            case 7:
                uscrnSubhourly.setLatitude(Double.parseDouble(field));
                break;
            case 8:
                uscrnSubhourly.setAir_temperature(Double.parseDouble(field));
                break;
            case 9:
                uscrnSubhourly.setPrecipitation(Double.parseDouble(field));
                break;
            case 10:
                uscrnSubhourly.setSolar_radiation(Double.parseDouble(field));
                break;
            case 11:
                uscrnSubhourly.setSr_flag(field);
                break;
            case 12:
                uscrnSubhourly.setSurface_temperature(Double.parseDouble(field));
                break;
            case 13:
                uscrnSubhourly.setSt_type(field);
                break;
            case 14:
                uscrnSubhourly.setSt_flag(field);
                break;
            case 15:
                uscrnSubhourly.setRelative_humidity(Double.parseDouble(field));
                break;
            case 16:
                uscrnSubhourly.setRh_flag(field);
                break;
            case 17:
                uscrnSubhourly.setSoil_moisture_5(Double.parseDouble(field));
                break;
            case 18:
                uscrnSubhourly.setSoil_temperature_5(Double.parseDouble(field));
                break;
            case 19:
                uscrnSubhourly.setWetness(Double.parseDouble(field));
                break;
            case 20:
                uscrnSubhourly.setWet_flag(field);
                break;
            case 21:
                uscrnSubhourly.setWind_1_5(Double.parseDouble(field));
                break;
            case 22:
                uscrnSubhourly.setWind_flag(field);
                break;
        }
    }
}
