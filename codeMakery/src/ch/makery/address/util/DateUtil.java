package ch.makery.address.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by PanD
 */

public class DateUtil {

    private static final String DATE_PATTERN = "dd.MM.yyyy";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * description: localDate -> String
     * date: 2020/5/7 21:18
     * @param date
     * @return java.lang.String
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_TIME_FORMATTER.format(date);
    }

    /**
     * description: String -> Localdate
     * date: 2020/5/7 21:20
     * @param dateString
     * @return java.time.LocalDate
     */
    public static LocalDate parse(String dateString) {
        return DATE_TIME_FORMATTER.parse(dateString, LocalDate::from);
    }

    public static boolean validDate(String dateString) {
        return DateUtil.parse(dateString)!=null;
    }
}
