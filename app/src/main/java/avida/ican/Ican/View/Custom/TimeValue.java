package avida.ican.Ican.View.Custom;

/**
 * Created by AtrasVida on 2018-06-30 at 5:22 PM
 */

public class TimeValue {
    private final static long secondsInMilli = 1000;
    private final static long minutesInMilli = 1000 * 60;
    private final static long hoursInMilli = (1000 * 60) * 60;
    private final static long daysInMilli = ((1000 * 60) * 60) * 24;

    public static long SecondsInMilli() {
        return secondsInMilli;
    }

    public static long MinutesInMilli() {
        return minutesInMilli;
    }

    public static long HoursInMilli() {
        return hoursInMilli;
    }

    public static long DayInMilli() {
        return daysInMilli;
    }
}
