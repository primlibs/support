/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prim.support;

import com.prim.support.FormatDate;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * класс преобразует строки из БД в разные типы данных
 *
 * @author Rice Pavel
 */
public final class Converter {

  private Converter() {
  }

  /**
   * принимает дату в виде строки, возвращает объект Date. Поддерживает основные форматы даты. Если дата передана в
   * неправильном формате - возвращает null
   *
   * @param dateString дата в виде строки
   * @return объект Date
   */
  public static Date parseDate(String dateString) {
    return FormatDate.getDateFromString(dateString);
  }

  /**
   * возвращает объект Calendar, который соответствует переданному объекту Date
   *
   * @param date
   * @return
   */
  public static Calendar getCalendar(Date date) {
    Calendar cl = Calendar.getInstance();
    cl.setTime(date);
    return cl;
  }

  /**
   * проверяет строки, являются ли они датами
   *
   * @param str строки для проверки
   * @return если все строки являются датами - то true, иначе false.
   */
  public static boolean checkDate(String... str) {
    for (String strDate : str) {
      if (!checkDatePrivate(strDate)) {
        return false;
      }
    }
    return true;
  }

  /**
   * получить значение double.
   *
   * @param dateString double в виде строки
   * @return значение double. Если строка передана в неправильном формате - фозвращает null
   */
  public static Double parseDouble(String dateString) {
    Double d = null;
    try {
      d = Double.parseDouble(dateString);
    } catch (Exception e) {
    }
    return d;
  }

  /**
   * проверка, является ли строка значением double
   *
   * @param dateString double в виде строки
   * @return является ли строка значением double
   */
  public static boolean checkDouble(String dateString) {
    if (parseDouble(dateString) != null) {
      return true;
    } else {
      return false;
    }
  }

  /**
   *
   * @param dateString integer в виде строки
   * @return значение integer. Если строка передана в неправильном формате - то null
   */
  public static Integer parseInteger(String dateString) {
    Integer i = null;
    try {
      i = Integer.parseInt(dateString);
    } catch (Exception e) {
    }
    return i;
  }

  /**
   *
   * @param str Integer в виде строки
   * @return является ли строка значением Integer
   */
  public static boolean checkInteger(String... str) {
    for (String strDate : str) {
      if (!checkInteger(strDate)) {
        return false;
      }
    }
    return true;
  }

  /**
   *
   * @param str integer в виде строки
   * @return является ли строка значением Integer
   */
  public static boolean checkInteger(String str) {
    if (parseInteger(str) != null) {
      return true;
    } else {
      return false;
    }
  }

  /**
   *
   * @param value значение double
   * @param def значение по умолчанию
   * @return возвращает значение double. Если исходное значение равно null или передано в неправильном формате, то
   * возвращает значение по умолчанию.
   */
  public static double getDouble(Object value, double def) {
    double d = def;
    if (value != null && checkDouble(value.toString())) {
      d = parseDouble(value.toString());
    }
    return d;
  }

  /**
   * 
   * @param value значение integer
   * @param def значение по умолчанию
   * @return возвращает значение integer. Если исходное значение равно null или передано в неправильном формате, то
   * возвращает значение по умолчанию.
   */
  public static int getInteger(Object value, int def) {
    int d = def;
    if (value != null && checkInteger(value.toString())) {
      d = parseInteger(value.toString());
    }
    return d;
  }
  
  /**
   * 
   * @param value
   * @return значение в виде строки. Если переданное значение равно null, то возвращает пустую строку.
   */
  public static String getString(Object value) {
    return MyString.getString(value);
  }

  private static boolean checkDatePrivate(String dateString) {
    if (parseDate(dateString) != null) {
      return true;
    } else {
      return false;
    }
  }
}
