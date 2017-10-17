package lnyswz.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * �����������ַ������໥ת��
 */
public class DateUtil
{

    /**
     * Base ISO 8601 Date format yyyyMMdd i.e., 20021225 for the 25th day of December in the year 2002
     */
    public static final String ISO_DATE_FORMAT = "yyyyMMdd";

    /**
     * Expanded ISO 8601 Date format yyyy-MM-dd i.e., 2002-12-25 for the 25th day of December in the year 2002
     */
    public static final String ISO_EXPANDED_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * yyyy-MM-dd hh:mm:ss
     */
    public static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    public static String DATETIME_NOSECOND_PATTERN = "yyyy-MM-dd HH:mm";

    /**
     * Default lenient setting for getDate.
     */
    private static boolean LENIENT_DATE = false;

    /**
     * ��ʱ����
     * @param JD
     * @return
     */
    protected static final float normalizedJulian(float JD)
    {

        float f = Math.round(JD + 0.5f) - 0.5f;

        return f;
    }

    /**
     * ����ֵת�������ڸ�ʽ<br>
     * ��ʱ����
         * Returns the Date from a julian. The Julian date will be converted to noon GMT,
         * such that it matches the nearest half-integer (i.e., a julian date of 1.4 gets
     * changed to 1.5, and 0.9 gets changed to 0.5.)
     *
     * @param JD the Julian date
     * @return the Gregorian date
     */
    public static final Date toDate(float JD)
    {

        /* To convert a Julian Day Number to a Gregorian date, assume that it is for 0 hours, Greenwich time (so
         * that it ends in 0.5). Do the following calculations, again dropping the fractional part of all
         * multiplicatons and divisions. Note: This method will not give dates accurately on the
         * Gregorian Proleptic Calendar, i.e., the calendar you get by extending the Gregorian
             * calendar backwards to years earlier than 1582. using the Gregorian leap year
         * rules. In particular, the method fails if Y<400. */
        float Z = (normalizedJulian(JD)) + 0.5f;
        float W = (int) ( (Z - 1867216.25f) / 36524.25f);
        float X = (int) (W / 4f);
        float A = Z + 1 + W - X;
        float B = A + 1524;
        float C = (int) ( (B - 122.1) / 365.25);
        float D = (int) (365.25f * C);
        float E = (int) ( (B - D) / 30.6001);
        float F = (int) (30.6001f * E);
        int day = (int) (B - D - F);
        int month = (int) (E - 1);

        if (month > 12)
        {
            month = month - 12;
        }

        int year = (int) (C - 4715); //(if Month is January or February) or C-4716 (otherwise)

        if (month > 2)
        {
            year--;
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1); // damn 0 offsets
        c.set(Calendar.DATE, day);

        return c.getTime();
    }

    /**
     * Returns the days between two dates. Positive values indicate that
     * the second date is after the first, and negative values indicate, well,
     * the opposite. Relying on specific times is problematic.
     *
     * @param early the "first date"
     * @param late the "second date"
     * @return the days between the two dates
     */
    public static final int daysBetween(Date early, Date late)
    {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(early);
        c2.setTime(late);

        return daysBetween(c1, c2);
    }
/*
    public static final int monthsBetween(Date early, Date late)
    {
        if (early == null || late == null)
            return -1;
        int early_year = early.getYear();
        int early_month = early.getMonth();
        int late_year = late.getYear();
        int late_month = late.getMonth();
        if (early_month == 0)
        {
            early_month = 12;
            early_year = early_year - 1;
        }
        if (late_month == 0)
        {
            late_month = 12;
            late_year = late_year - 1;
        }
        int month_def;
        month_def = (late_year - early_year) * 12 + (late_month - early_month);

        return month_def;
    }
*/
    /**
     * Returns the days between two dates. Positive values indicate that
     * the second date is after the first, and negative values indicate, well,
     * the opposite.
     *
     * @param early
     * @param late
     * @return the days between two dates.
     */
    public static final int daysBetween(Calendar early, Calendar late)
    {

        return (int) (toJulian(late) - toJulian(early));
    }
    
    public static final int daysBetween(String early, String late){
		  
        Calendar fromCalendar = Calendar.getInstance();  
        fromCalendar.setTime(DateUtil.stringToDate(early));  
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);  
        fromCalendar.set(Calendar.MINUTE, 0);  
        fromCalendar.set(Calendar.SECOND, 0);  
        fromCalendar.set(Calendar.MILLISECOND, 0);  
  
        Calendar toCalendar = Calendar.getInstance();  
        toCalendar.setTime(DateUtil.stringToDate(late));  
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);  
        toCalendar.set(Calendar.MINUTE, 0);  
        toCalendar.set(Calendar.SECOND, 0);  
        toCalendar.set(Calendar.MILLISECOND, 0);  
  
        return (int)(toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);  
    }

    /**
     * Return a Julian date based on the input parameter. This is
     * based from calculations found at
     * <a href="http://quasar.as.utexas.edu/BillInfo/JulianDatesG.html">Julian Day Calculations
     * (Gregorian Calendar)</a>, provided by Bill Jeffrys.
     * @param c a calendar instance
     * @return the julian day number
     */
    public static final float toJulian(Calendar c)
    {

        int Y = c.get(Calendar.YEAR);
        int M = c.get(Calendar.MONTH);
        int D = c.get(Calendar.DATE);
        int A = Y / 100;
        int B = A / 4;
        int C = 2 - A + B;
        float E = (int) (365.25f * (Y + 4716));
        float F = (int) (30.6001f * (M + 1));
        float JD = C + D + E + F - 1524.5f;

        return JD;
    }

    /**
     * ��ʱ����
     * Return a Julian date based on the input parameter. This is
     * based from calculations found at
     * <a href="http://quasar.as.utexas.edu/BillInfo/JulianDatesG.html">Julian Day Calculations
     * (Gregorian Calendar)</a>, provided by Bill Jeffrys.
     * @param date
     * @return the julian day number
     */
    public static final float toJulian(Date date)
    {

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return toJulian(c);
    }

    /**
     * ��������
     * @param isoString �����ַ�
     * @param fmt ��ʽ
     * @param field ��/��/�� Calendar.YEAR/Calendar.MONTH/Calendar.DATE
     * @param amount ��������
     * @return
     * @throws ParseException
     */
    public static final String dateIncrease(String isoString, String fmt,
                                            int field, int amount)
    {

        try
        {
            Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
                "GMT"));
            cal.setTime(stringToDate(isoString, fmt, true));
            cal.add(field, amount);

            return dateToString(cal.getTime(), fmt);

        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /**
     * Time Field Rolling function.
     * Rolls (up/down) a single unit of time on the given time field.
     *
     * @param isoString
     * @param field the time field.
     * @param up Indicates if rolling up or rolling down the field value.
     * @param expanded use formating char's
     * @exception ParseException if an unknown field value is given.
     */
    public static final String roll(String isoString, String fmt, int field,
                                    boolean up) throws ParseException
    {

        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
            "GMT"));
        cal.setTime(stringToDate(isoString, fmt));
        cal.roll(field, up);

        return dateToString(cal.getTime(), fmt);
    }

    /**
     * Time Field Rolling function.
     * Rolls (up/down) a single unit of time on the given time field.
     *
     * @param isoString
     * @param field the time field.
     * @param up Indicates if rolling up or rolling down the field value.
     * @exception ParseException if an unknown field value is given.
     */
    public static final String roll(String isoString, int field, boolean up) throws
        ParseException
    {
        return roll(isoString, DATETIME_PATTERN, field, up);
    }

/*
    public static Date stringToDateWithTime(String s)
    {
        int year = 0;
        int month = 0;
        int date = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;

        if (s == null)
            return new Date();

        try
        {
            int pos = 0;
            pos = s.indexOf("-");
            if (pos < 0)
                return new Date();
            else
            {
                year = Integer.parseInt(s.substring(0, pos));
            }

            s = s.substring(pos + 1);
            pos = s.indexOf("-");
            if (pos < 0)
                return new Date();
            else
            {
                month = Integer.parseInt(s.substring(0, pos));
            }

            try
            {
                s = s.substring(pos + 1);
                pos = s.indexOf(" ");
                if (pos < 0)
                {
                    date = Integer.parseInt(s);
                    s = null;
                }
                else
                {
                    date = Integer.parseInt(s.substring(0, pos));
                }
            }
            catch (Exception e)
            {
                s = null;
            }

            try
            {
                s = s.substring(pos + 1);
                pos = s.indexOf(":");
                if (pos < 0)
                {
                    hour = Integer.parseInt(s);
                    s = null;
                }
                else
                {
                    hour = Integer.parseInt(s.substring(0, pos));
                }
            }
            catch (Exception e)
            {
                s = null;
            }

            try
            {
                s = s.substring(pos + 1);
                pos = s.indexOf(":");
                if (pos < 0)
                {
                    minute = Integer.parseInt(s);
                    s = null;
                }
                else
                {
                    minute = Integer.parseInt(s.substring(0, pos));
                }
            }
            catch (Exception e)
            {
                s = null;
            }

            try
            {
                s = s.substring(pos + 1);
                pos = s.indexOf(".");
                if (pos < 0)
                {
                    second = Integer.parseInt(s);
                    s = null;
                }
                else
                {
                    second = Integer.parseInt(s.substring(0, pos));
                }
            }
            catch (Exception e)
            {
            }
        }
        catch (Exception e)
        {
            return new Date();
        }

        Date time = new Date(year-1900, month-1, date, hour, minute, second);
        return time;
    }
*/
    /**
     * �ַ�ת��Ϊ����java.util.Date
     * @param dateText �ַ�
     * @param format ���ڸ�ʽ
     * @param lenient ����Խ���־
     * @return
     */
    public static Date stringToDate(String dateText, String format,
                                    boolean lenient)
    {

        if (dateText == null)
        {

            return null;
        }

        DateFormat df = null;

        try
        {

            if (format == null)
            {
                df = new SimpleDateFormat();
            }
            else
            {
                df = new SimpleDateFormat(format);
            }

            // setLenient avoids allowing dates like 9/32/2001
            // which would otherwise parse to 10/2/2001
            df.setLenient(false);

            return df.parse(dateText);
        }
        catch (ParseException e)
        {

            return null;
        }
    }

    /**
     * �ַ�ת��Ϊ����java.util.Date
     * @param dateText �ַ�
     * @param format ���ڸ�ʽ
     * @return
     */
    public static Date stringToDate(String dateString, String format)
    {

        return stringToDate(dateString, format, LENIENT_DATE);
    }

    /**
     * �ַ�ת��Ϊ����java.util.Date
     * @param dateText �ַ�
     */
    public static Date stringToDate(String dateString)
    {
        return stringToDate(dateString, ISO_EXPANDED_DATE_FORMAT, LENIENT_DATE);
    }

    /** ���ʱ���������ʱ���ַ�
     * @return ����ʱ���ַ�
     * @param pattern ʱ���ַ���ʽ
     * @param date ʱ�����
     */
    public static String dateToString(Date date, String pattern)
    {

        if (date == null)
        {

            return null;
        }

        try
        {

            SimpleDateFormat sfDate = new SimpleDateFormat(pattern);
            sfDate.setLenient(false);

            return sfDate.format(date);
        }
        catch (Exception e)
        {

            return null;
        }
    }

    /**
     * ���ʱ���������ʱ���ַ� yyyy-MM-dd
     * @param date
     * @return
     */
    public static String dateToString(Date date)
    {
        return dateToString(date, ISO_EXPANDED_DATE_FORMAT);
    }

    /** ���ص�ǰʱ��
     * @return ���ص�ǰʱ��
     */
    public static Date getCurrentDateTime()
    {
        java.util.Calendar calNow = java.util.Calendar.getInstance();
        java.util.Date dtNow = calNow.getTime();

        return dtNow;
    }

    /**
     * ���ص�ǰ�����ַ�
     * @param pattern �����ַ���ʽ
     * @return
     */
    public static String getCurrentDateString(String pattern)
    {
        return dateToString(getCurrentDateTime(), pattern);
    }

    /**
     * ���ص�ǰ�����ַ� yyyy-MM-dd
     * @return
     */
    public static String getCurrentDateString()
    {
        return dateToString(getCurrentDateTime(), ISO_EXPANDED_DATE_FORMAT);
    }

    /**
     * ���ص�ǰ����+ʱ���ַ� yyyy-MM-dd hh:mm:ss
     * @param date
     * @return
     */
    public static String dateToStringWithTime(Date date)
    {

        return dateToString(date, DATETIME_PATTERN);
    }
    public static String dateToStringWithTime(Date date,String pattern)
    {

        return dateToString(date, pattern);
    }
    /**yyyy-MM-dd HH:mm:ss
     * ��������-��������
     * @param date
     * @param days
     * @return java.util.Date
     */
    public static Date dateIncreaseByDay(Date date, int days)
    {

        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
            "GMT"));
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    /**
     * ��������-��������
     * @param date
     * @param days
     * @return java.util.Date
     */
    public static Date dateIncreaseByMonth(Date date, int mnt)
    {

        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
            "GMT"));
        cal.setTime(date);
        cal.add(Calendar.MONTH, mnt);

        return cal.getTime();
    }

    /**
     * ��������-��������
     * @param date
     * @param mnt
     * @return java.util.Date
     */
    public static Date dateIncreaseByYear(Date date, int mnt)
    {

        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
            "GMT"));
        cal.setTime(date);
        cal.add(Calendar.YEAR, mnt);

        return cal.getTime();
    }

    /**
     * ��������
     * @param date �����ַ� yyyy-MM-dd
     * @param days
     * @return �����ַ� yyyy-MM-dd
     */
    public static String dateIncreaseByDay(String date, int days)
    {
        return dateIncreaseByDay(date, ISO_DATE_FORMAT, days);
    }

    /**
     * ��������
     * @param date �����ַ�
     * @param fmt ���ڸ�ʽ
     * @param days
     * @return
     */
    public static String dateIncreaseByDay(String date, String fmt, int days)
    {
        return dateIncrease(date, fmt, Calendar.DATE, days);
    }

    /**
     * �����ַ��ʽת��
     * @param src �����ַ�
     * @param srcfmt Դ���ڸ�ʽ
     * @param desfmt Ŀ�����ڸ�ʽ
     * @return
     */
    public static String stringToString(String src, String srcfmt,
                                        String desfmt)
    {
        return dateToString(stringToDate(src, srcfmt), desfmt);
    }

    /**
     * ȡ��ǰ���ڵ����
     * @param date ��ǰ����
     * @return string
     */
    public static String getYear(Date date)
    {
        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy");
        String cur_year = formater.format(date);
        return cur_year;
    }

    /**
     * ȡ��ǰ���ڵ����
     * @param date ��ǰ����
     * @return string
     */
    public static String getMonth(Date date)
    {
        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("MM");
        String cur_month = formater.format(date);
        return cur_month;
    }

    /**
     * ȡ��ǰ���ڵ����
     * @param date ��ǰ����
     * @return string
     */
    public static String getDay(Date date)
    {
        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("dd");
        String cur_day = formater.format(date);
        return cur_day;
    }

    /**
     * ȡ��ǰ���ڵ����
     * @param date ��ǰ����
     * @return string
     */
    public static String getHour(Date date)
    {
        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("hh");
        String cur_hour = formater.format(date);
        return cur_hour;
    }

    /**
     * ȡ���·�����
     * @param date Date
     * @return String
     */
    public static String getDaysInMonth(Date date)
    {
      String days_in_month = "";
      String month = getMonth(date);

      if (month.equals("02"))
      {
        int year = Integer.parseInt(getMonth(date));

        if ((year % 4 == 0) && (year % 100 != 0))
        {
          days_in_month = "29";
        }
        else
        {
            days_in_month = "28";
        }
      }
      else if (month.equals("04") || month.equals("06")
          || month.equals("09") || month.equals("11"))
      {
        days_in_month = "30";
      }
      else
      {
          days_in_month = "31";
      }

      return days_in_month;
    }
    /**
     * ȡ���·�����
     * @param date Date
     * @return String
     */
    public static String getDaysInMonth(String str_date)
    {
      String days_in_month = "";
//      String month = getMonth(date);
      String str_year = str_date.substring(0,4);
      String str_month = str_date.substring(5,7);

      if (str_month.equals("02"))
      {
        int year = Integer.parseInt(str_year);

        if ((year % 4 == 0) && (year % 100 != 0))
        {
          days_in_month = "29";
        }
        else
        {
            days_in_month = "28";
        }
      }
      else if (str_month.equals("04") || str_month.equals("06")
          || str_month.equals("09") || str_month.equals("11"))
      {
        days_in_month = "30";
      }
      else
      {
          days_in_month = "31";
      }

      return days_in_month;
    }

    /**
     * ��ȡ��ǰ ��
     * @return int
     */
    public static int getYear()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        int year = calendar.get(calendar.YEAR);
        return year;
    }

    /**
     * ��ȡ��ǰ ��
     * @return int
     */
    public static int getMonth()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        int month = calendar.get(calendar.MONTH);
        return month;
    }
    
    
    public static String getMonthPattern(){
    	java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("MM");
        String cur_month = formater.format(new java.util.Date());
        return cur_month;
    }

    /**
     * ��ȡ��ǰ ��
     * @return int
     */
    public static int getDay()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        int day = calendar.get(calendar.DATE);
        return day;
    }

    /**
     * ��ȡ��ǰ ʱ
     * @return int
     */
    public static int getHour()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        int hour = calendar.get(calendar.HOUR_OF_DAY);
        return hour;
    }


    /**
     * ��ȡ��ǰ ��
     * @return int
     */
    public static int getMinute()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        int minut = calendar.get(calendar.MINUTE);
        return minut;
    }

    /**
     * ��ȡ��ǰ ��
     * @return int
     */
    public static int getSecond()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        int second = calendar.get(calendar.SECOND);
        return second;
    }

//    public static void main(String[] args)
//    {
//        /*RegisterManager x = new RegisterManager();
//                 try {
//            int tempInt = x.Register("1234", "13331016539", 12, "wanghua",
//                                     "cowboywh@sina.com", "33333", "12345");
//            System.out.println("" + tempInt);
//                 }
//                 catch (Exception e) {
//            System.out.println("wrong");
//                 }*/
//
//        String str = "2005-03-12";
//        System.out.println(stringToDate(str));
//        System.out.print(getDay(stringToDate(str)));
//        java.util.Date date = getCurrentDateTime();
//        System.out.print(date) ;
//        System.out.println(date.getYear() + " " + date.getMonth());
//
//        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
//            "GMT"));
//        cal.setTime(date);
//        System.out.print(cal.YEAR) ;
//        System.out.print(date) ;
//
//        java.text.SimpleDateFormat fort = new java.text.SimpleDateFormat("dd");
//        String cur = fort.format(date);
//        System.out.print(cur) ;
////System.out.println(getCurrentDateString());
//        java.util.Date test_date = new java.util.Date(2004, 3, 31);
////        System.out.println(dateIncreaseByMonth(test_date, -4).getYear() + " " +
////                           dateIncreaseByMonth(test_date, -4).getMonth() + " " +
////                           dateIncreaseByMonth(test_date, -4).getDate());
//    }

    /**
 * yy-mm-dd HH:mm:SS��ʽ��ʱ��
 * @return String Date
 */
public static String getNowDate(){
    Date dateTime=new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String strTime = format.format(dateTime);
    return strTime;
}
/**
 * yyyy-mm-dd hh:mm��ʽ��ʱ��
 * @return StringDate
 */

public static String getStringDate() {
    Date dateTime = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String strTime = format.format(dateTime);
    return strTime;
}

/**
 * yy-mm-dd��ʽ��ʱ��
 * @return StringDate
 */
public static String getStringDateShort() {
    Date dateTime = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String strTime = format.format(dateTime);
    return strTime;
}

  public static String getFirstDateWithTimeInMonth(Date date) {

    String month = getMonth(date);
    String year = getYear(date);
    String first_day = year + "-" + month + "-01 00:00:00";
    return first_day;
  }
  
  public static String getFirstDateInMonth(Date date) {

    String month = getMonth(date);
    String year = getYear(date);
    String first_day = year + "-" + month + "-01";
    return first_day;
  }

  /** 
   * 当月最后一天 
   *  
   * @return 
   */  
  public static Date getLastDayInMonth(Date date) {  
	  Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
      cal.set(Calendar.DAY_OF_MONTH, value);
      return cal.getTime();
  }
}
