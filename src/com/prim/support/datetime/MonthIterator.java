/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prim.support.datetime;

import com.prim.support.FormatDate;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * класс для последовательного перебора месяцев в заданном периоде.
 *
 *
 * @author Rice Pavel
 */
public class MonthIterator {

  private boolean existMonth = false;
  private Calendar startAllPeriod;
  private Calendar endAllPeriod;
  private Calendar startMonthPeriod;
  private Calendar endMonthPeriod;
  private int iterationNumber = 1;
  
  /**
   *
   * @param clStart начало периода
   * @param clEnd конец периода
   */
  public MonthIterator(Calendar clStart, Calendar clEnd) {
    this.startAllPeriod = clStart;
    this.endAllPeriod = clEnd;
  }

  /**
   * сдвинуть внутренний указатель на месяц вперед
   */
  public boolean nextMonth() {
    if (iterationNumber == 1) {
      setStart();
      iterationNumber++;
    } else {
      if (existMonth) {
        // найти начало следующего месяца
        Calendar startMonth = Calendar.getInstance();
        startMonth.setTime(startMonthPeriod.getTime());
        startMonth.add(Calendar.MONTH, 1);
        startMonth.set(Calendar.DAY_OF_MONTH, 1);
        FormatDate.setStartOfDate(startMonth);
        // если начало след. месяца не позже, чем дата конца
        if (!startMonth.after(endAllPeriod)) {
          // начало периода след. месяца
          startMonthPeriod = startMonth;
          // конец периода след. месяца
          Calendar endMonth = Calendar.getInstance();
          endMonth.setTime(startMonth.getTime());
          endMonth.set(Calendar.DAY_OF_MONTH, endMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
          FormatDate.setEndOfDate(endMonth);
          endMonthPeriod = endMonth.before(endAllPeriod) ? endMonth : endAllPeriod;
          iterationNumber++;
        } else {
          existMonth = false;
          startMonthPeriod = null;
          endMonthPeriod = null;
        }
      }
    }
    return existMonth;
  }

  /**
   * получить начало периода текущего месяца. Если нет текущего месяца -
   * возвращает null
   *
   * @return
   */
  public Calendar getStartMonthPeriod() {
    return startMonthPeriod;
  }

  /**
   * получить конец периода текущего месяца. Если нет текущего месяца -
   * возвращает null
   *
   * @return
   */
  public Calendar getEndMonthPeriod() {
    return endMonthPeriod;
  }

  /**
   * получить номер текущего месяца, от 0 до 11. Если нет текущего месяца -
   * возвращает -1
   *
   * @return
   */
  public int getMonth() {
    if (existMonth) {
      return startMonthPeriod.get(Calendar.MONTH);
    } else {
      return -1;
    }
  }

  /**
   * получить номер текущего года. Если нет текущего месяца - возвращает -1
   *
   * @return
   */
  public int getYear() {
    if (existMonth) {
      return startMonthPeriod.get(Calendar.YEAR);
    } else {
      return -1;
    }
  }

  private void setStart() {
    // конец первого месяца
    Calendar endMonth = Calendar.getInstance();
    endMonth.setTime(startAllPeriod.getTime());
    endMonth.set(Calendar.DAY_OF_MONTH, endMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
    FormatDate.setEndOfDate(endMonth);
    // если дата старта не позже, чем дата конца
    if (!startAllPeriod.after(endAllPeriod)) {
      // начало периода первого месяца
      startMonthPeriod = startAllPeriod;
      // конец периода первого месяца
      endMonthPeriod = endMonth.before(endAllPeriod) ? endMonth : endAllPeriod;
      existMonth = true;
    }
  }
}
