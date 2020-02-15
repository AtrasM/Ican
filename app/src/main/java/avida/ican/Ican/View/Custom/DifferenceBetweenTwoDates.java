package avida.ican.Ican.View.Custom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by AtrasVida on 2018-04-24 at 11:13 AM
 */

public class DifferenceBetweenTwoDates {
    private Date startDate;
    private Date endDate;
    private SimpleDateFormat simpleDateFormat;
    private long secondsInMilli = 1000;
    private long minutesInMilli = secondsInMilli * 60;
    private long hoursInMilli = minutesInMilli * 60;
    private long daysInMilli = hoursInMilli * 24;

    /**
     * @param strSimpleDateFormat example:"dd/M/yyyy hh:mm:ss"
     * @param startDate           example:"10/10/2013 11:30:10"
     * @param lastDate            example:"13/10/2013 20:35:55"
     */
    public DifferenceBetweenTwoDates(String strSimpleDateFormat, String startDate, String lastDate) {
        simpleDateFormat = new SimpleDateFormat(strSimpleDateFormat);
        try {
            /*this.startDate = simpleDateFormat.parse(startDate);
            this.endDate = simpleDateFormat.parse(lastDate);*/

            if (CustomFunction.ValidationDateFormat(startDate, strSimpleDateFormat)) {
                startDate = CustomFunction.StandardizeTheDateFormat(startDate);
            }
            this.startDate = new Date(startDate);

            if (CustomFunction.ValidationDateFormat(lastDate, strSimpleDateFormat)) {
                lastDate = CustomFunction.StandardizeTheDateFormat(lastDate);
            }
            this.endDate = new Date(lastDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public long getElapsedDays() {
        long different = Check();
        long elapsedDays = different / daysInMilli;
        return elapsedDays;
    }

    public long getElapsedHours() {
        long different = getElapsedDays();
        different = different % daysInMilli;
        long elapsedHours = different / hoursInMilli;
        return elapsedHours;
    }

    public long getElapsedMinutes() {
        long different = getElapsedHours();
        different = different % hoursInMilli;
        long elapsedMinutes = different / minutesInMilli;
        return elapsedMinutes;
    }

    public long getElapsedSeconds() {
        long different = getElapsedMinutes();
        long elapsedSeconds = different / secondsInMilli;
        return elapsedSeconds;
    }

    private long Check() {
        return endDate.getTime() - startDate.getTime();
    }
}
