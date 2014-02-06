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
   * @return
   */
  public static String getCurrentDateInMysql() {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return formatter.format(getCurrentDate());
  }

  /**
   * возвращает дату в формате MySQL
   *
   * @param date
   * @return
   */
  public static String getDateInMysql(Date date) {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return formatter.format(date);
  }

  /**
   * возвращает дату в формате Mysql, либо null, если начальна строка передана в
   * неправильном формате
   *
   * @param str
   * @return
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
   * @return
   */
  public static String getFormatMysql() {
    return "yyyy-MM-dd HH:mm:ss";
  }

  /**
   * принимает дату в виде строки, возвращает объект Date. Если дата передана в неправильном формате, то возвращает null
   *
   * @param str
   * @return
   * @throws Exception
   */
  public static Date getDateFromString(Object str) throws Exception {
    Date resDate = null;
    Calendar calendar = Calendar.getInstance();
    ChainValidator chain = ChainValidator.getInstance();
    HashMap<String, Object> hs = new HashMap<String, Object>();
    hs.put("format", FormatDate.getFormatMysql());
    chain.addChain("DateToFormatFilter", hs);
    chain.addChain("DateFormatValidator", hs);
    if (chain.execute(str)) {
      SimpleDateFormat formatter = new SimpleDateFormat(FormatDate.getFormatMysql());
      resDate = formatter.parse(chain.getData().toString());
    }
    return resDate;
  }

  /**
   * возвращает текущую дату
   *
   * @return
   */
  public static Date getCurrentDate() {
    return new Date();
  }

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
}
