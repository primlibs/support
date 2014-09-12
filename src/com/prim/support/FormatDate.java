/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prim.support;

import com.prim.support.filterValidator.ChainValidator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * статические методы для работы с датами
 *
 * @author Pavel Rice
 */
public class FormatDate {

  public final static String SMALL_FORMAT = "dd.MM.yyyy";
  public final static String FULL_FORMAT = "dd.MM.yyyy HH:mm";

  private FormatDate() {
  }

  /**
   * возвращает текущую дату в формате MySQL
   *
   * @return текущая дата в формате mysql
   */
  public static String getCurrentDateInMysql() {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return formatter.format(getCurrentDate());
  }

  /**
   * возвращает дату в формате MySQL
   *
   * @param date
   * @return дата в формате MySQL
   */
  public static String getDateInMysql(Date date) {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return formatter.format(date);
  }

  /**
   * возвращает дату в формате Mysql, либо null, если начальна строка передана в
   * неправильном формате
   *
   * @param str дата в виде строки
   * @return дата в формате mysql или null
   */
  public static String formatDateInMysql(Object str) {
    HashMap<String, Object> params = new HashMap();
    params.put("format", FormatDate.getFormatMysql());
    ChainValidator chain = ChainValidator.getInstance();
    chain.addChain("DateToFormatFilter", params);
    chain.addChain("DateFormatValidator", params);
    chain.execute(str);
    if (chain.getErrors().isEmpty()) {
      return chain.getData().toString();
    } else {
      return null;
    }
  }

  /**
   * возвращает формат MySQL
   *
   * @return формат mysql
   */
  public static String getFormatMysql() {
    return "yyyy-MM-dd HH:mm:ss";
  }

  /**
   * принимает дату в виде строки, возвращает объект Date. Если дата передана в
   * неправильном формате, то возвращает null
   *
   * @param str дата в виде строки
   * @return объект Date или null
   */
  public static Date getDateFromString(Object str) {
    Date resDate = null;
    Calendar calendar = Calendar.getInstance();
    ChainValidator chain = ChainValidator.getInstance();
    HashMap<String, Object> hs = new HashMap<String, Object>();
    hs.put("format", FormatDate.getFormatMysql());
    chain.addChain("DateToFormatFilter", hs);
    chain.addChain("DateFormatValidator", hs);
    try {
    if (chain.execute(str)) {
      SimpleDateFormat formatter = new SimpleDateFormat(FormatDate.getFormatMysql());
      resDate = formatter.parse(chain.getData().toString());
    }
    } catch (Exception e) { }
    return resDate;
  }

  /**
   * возвращает текущую дату
   *
   * @return текущая дата
   */
  public static Date getCurrentDate() {
    return new Date();
  }


  
  /**
   * форматировать дату
   * @param date дата в виде строки
   * @param format формат даты
   * @return дата в виде строки, если формат неправильный - то пустая строка
   */
  public static String format(Object date, String format) {
    String res = "";
    if (date != null) {
      date.toString();
      ChainValidator cv = ChainValidator.getInstance();
      HashMap<String, Object> hs = new HashMap<String, Object>();
      hs.put("format", format);
      cv.addChain("DateToFormatFilter", hs);
      cv.execute(date);
      if (cv.getData() != null) {
        res = cv.getData().toString();
      }
    }
    return res;
  }
  
  /**
   * форматировать дату
   * @param date дата 
   * @param format формат
   * @return дата в виде строки, если формат неправильный - то пустая строка 
   */
  public static String formatByDate(Date date, String format) {
    return format(getDateInMysql(date), format);
  }

  public static Date getDateFrom(Date dt, Boolean after, Object days) {
    Date resDate = null;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dt);
    Integer day = Integer.parseInt(MyString.getString(days));
    if (after == true) {
      calendar.add(Calendar.DAY_OF_MONTH, day);
    } else {
      calendar.add(Calendar.DAY_OF_MONTH, -day);
    }
    resDate = calendar.getTime();
    return resDate;
  }

  /**
   * установить календарь на начало дня
   * @param cl 
   */
  public static void setStartOfDate(Calendar cl) {
    cl.set(Calendar.HOUR_OF_DAY, 00);
    cl.set(Calendar.MINUTE, 00);
    cl.set(Calendar.SECOND, 00);
    cl.set(Calendar.MILLISECOND, 0);
  }

  /**
   * установить календарь на конец дня
   * @param cl 
   */
  public static void setEndOfDate(Calendar cl) {
    cl.set(Calendar.HOUR_OF_DAY, 23);
    cl.set(Calendar.MINUTE, 59);
    cl.set(Calendar.SECOND, 59);
    cl.set(Calendar.MILLISECOND, 999);
  }

  /**
   * установить дату на начало дня
   * @param dt
   * @return 
   */
  public static Date getStartOfDate(Date dt) {
    Calendar ca = Calendar.getInstance();
    ca.setTime(dt);
    ca.set(Calendar.HOUR_OF_DAY, 00);
    ca.set(Calendar.MINUTE, 00);
    ca.set(Calendar.SECOND, 00);
    ca.set(Calendar.MILLISECOND, 0);
    return ca.getTime();
  }

  /**
   * установить дату на конец дня
   * @param dt
   * @return 
   */
  public static Date getEndOfDate(Date dt) {
    Calendar ca = Calendar.getInstance();
    ca.setTime(dt);
    ca.set(Calendar.HOUR_OF_DAY, 23);
    ca.set(Calendar.MINUTE, 59);
    ca.set(Calendar.SECOND, 59);
    ca.set(Calendar.MILLISECOND, 999);
    return ca.getTime();
  }
}
