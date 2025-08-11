package yuping.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import scala.util.Try

object DateUtil :
  val DATE_PATTERN = "dd.MM.yyyy"
  val DATE_FORMATTER =  DateTimeFormatter.ofPattern(DATE_PATTERN)

  extension (date: LocalDate)
    /**
     * Returns the given date as a well formatted String. The above defined 
     * {@link DateUtil# DATE_PATTERN} is used. 
     *
     * @param date the date to be returned as a string 
     * @return formatted string 
     */
    def asString: String =
      if (date == null)
        return null;
      return DATE_FORMATTER.format(date);

  extension (data : String)
    /**
     * Converts a String in the format of the defined {@link DateUtil#DATE_PATTERN}
     * to a {@link LocalDate} object. 
     *
     * Returns null if the String could not be converted. 
     *
     * @param dateString the date as String 
     * @return the date object or null if it could not be converted 
     */
    def parseLocalDate: Option[LocalDate] =
      val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
      Try(LocalDate.parse(data, formatter)).toOption

    def isValid : Boolean =
      data.parseLocalDate != null 
