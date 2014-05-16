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
  
  private Converter() {}
  
  public static Date parseDate(String dateString) {
    return FormatDate.getDateFromString(dateString);
  }
  
  public static Calendar getCalendar(Date date) {
    Calendar cl = Calendar.getInstance();
    cl.setTime(date);
    return cl;
  }
  
  public static boolean checkDate(String ... str) {
    for (String strDate: str) {
      if (!checkDatePrivate(strDate)) {
        return false;
      }
    }
    return true;
  }
  
  public static Double parseDouble(String dateString) {
    Double d = null;
    try {
      d = Double.parseDouble(dateString);
    } catch (Exception e) { }
    return d;
  }
  
  public static boolean checkDouble(String dateString) {
    if (parseDouble(dateString) != null) {
      return true;
    } else {
      return false;
    }
  }
  
  public static Integer parseInteger(String dateString) {
    Integer i = null;
    try {
      i = Integer.parseInt(dateString);
    } catch (Exception e) { }
    return i;
  }
  
  public static boolean checkInteger(String ... str) {
    for (String strDate: str) {
      if (!checkInteger(strDate)) {
        return false;
      }
    }
    return true;
  }
  
  public static boolean checkInteger(String dateString) {
    if (parseInteger(dateString) != null) {
      return true;
    } else {
      return false;
    }
  }
  

  
 
  
  private static boolean checkDatePrivate(String dateString) {
    if (parseDate(dateString) != null) {
      return true;
    } else {
      return false;
    }
  }
  
  
  
  
  
}
