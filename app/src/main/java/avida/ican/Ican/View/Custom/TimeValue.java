package avida.ican.Ican.View.Custom;

/**
 * Created by AtrasVida on 2018-06-30 at 5:22 PM
 */

public class TimeValue {
    private final static long secondsInMilli = 1000;
    private final static long minutesInMilli = secondsInMilli * 60;
    private final static long hoursInMilli = minutesInMilli * 60;
    private final static long daysInMilli = hoursInMilli * 24;

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
