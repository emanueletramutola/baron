package cnr.imaa.baron.bll.converter;

import cnr.imaa.baron.model.UscrnObj;

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.IntStream;

public abstract class UscrnConverter {
    protected void setFields(UscrnObj uscrnObj, String row) {
        String[] fields = Arrays.stream(row.split(" "))
                .filter(field -> !field.trim().equals(""))
                .toArray(String[]::new);

        IntStream.range(0, fields.length)
                .forEach(idx -> setField(uscrnObj, fields[idx], idx));
    }

    protected abstract void setField(UscrnObj uscrnObj, String field, int idx);

    protected Instant getInstant(String date_YYMMDD, String time_HHMM) {
        String year = date_YYMMDD.substring(0, 4);
        String month = date_YYMMDD.substring(4, 6);
        String day = date_YYMMDD.substring(6, 8);
        String hour = "00";
        String minute = "00";

        if (time_HHMM != null) {
            hour = time_HHMM.substring(0, 2);
            minute = time_HHMM.substring(2, 4);
        }
        return Instant.parse(year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":00Z");
    }
}
