/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prim.support.filterValidator.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.prim.support.filterValidator.ChainValidator;
import com.prim.support.FormatDate;

/**
 * вычисляет время завершения процесса, учитывая только рабочее время.
 *
 *
 * свойства класса – начало и конец рабочего дня, продолжительность процесса. В
 * качестве параметра принимает строку, которая представляет собой дату и время.
 * Если продолжительность duration == 0, то возвращает ближайшее рабочее время .
 * Если duration > 0, то возвращает время окончания процесса, при этом
 * учитывается только рабочее время.
 *
 * Пример использования: в автоматической постановке задач сотрудникам, для
 * расчета времени старта задачи и времени дедлайна задачи.
 *
 */
public class WorkingHoursFilter extends ValidatorAbstract {

  /**
   * продолжительность процесса, в часах
   *
   * @var float
   */
  private double duration = 0;
  /**
   * время начала рабочего дня
   *
   * @var int
   */
  private int beginHour = 9;
  private int beginMinute = 0;
  /**
   * время завершения рабочего дня
   *
   * @var int
   */
  private int endHour = 18;
  private int endMinute = 0;
  /**
   * учитывать ли выходные
   *
   * @var boolean
   */
  private boolean weekend = true;
  private Date beginDate;
  private Date endDate;
  private Date currentDate;

  public double getDuration() {
    return duration;
  }

  public void setDuration(Object duration) {
    this.duration = Double.parseDouble(duration.toString());
  }

  public int getBeginHour() {
    return beginHour;
  }

  public void setBeginHour(Object beginHour) {
    this.beginHour = Integer.parseInt(beginHour.toString());
  }

  public int getBeginMinute() {
    return beginMinute;
  }

  public void setBeginMinute(Object beginMinute) {
    this.beginMinute = Integer.parseInt(beginMinute.toString());
  }

  public int getEndHour() {
    return endHour;
  }

  public void setEndHour(Object endHour) {
    this.endHour = Integer.parseInt(endHour.toString());
  }

  public int getEndMinute() {
    return endMinute;
  }

  public void setEndMinute(Object endMinute) {
    this.endMinute = Integer.parseInt(endMinute.toString());
  }

  public boolean isWeekend() {
    return weekend;
  }

  public void setWeekend(Object weekend) {
    this.weekend = Boolean.parseBoolean(weekend.toString());
  }

  @Override
  public boolean execute() {
    if (checkData()) {
      calculate();
      if (errorMessage == null || errorMessage.isEmpty()) {
        return true;
      }
    }
    return false;
  }

  private boolean checkData() {
    boolean result = false;
    ChainValidator chain =ChainValidator.getInstance();
    DateToFormatFilter filter = new DateToFormatFilter();
    filter.setFormat(FormatDate.getFormatMysql());
    DateFormatValidator validator = new DateFormatValidator();
    validator.setFormat(FormatDate.getFormatMysql());
    chain.addChain(filter);
    chain.addChain(validator);
    result = chain.execute(data);

    if (result) {
      SimpleDateFormat formatter = new SimpleDateFormat(FormatDate.getFormatMysql());
      try {
        currentDate = formatter.parse(chain.getData().toString());
      } catch (Exception e) {
        return false;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.HOUR_OF_DAY, beginHour);
      calendar.set(Calendar.MINUTE, beginMinute);
      beginDate = calendar.getTime();

      calendar = Calendar.getInstance();
      calendar.set(Calendar.HOUR_OF_DAY, endHour);
      calendar.set(Calendar.MINUTE, endMinute);
      endDate = calendar.getTime();
    }
    return result;
  }

  private void calculate() {
    if (beginDate.compareTo(endDate) == 0) {
      addErrorMessage("Ошибка: время начала рабочего дня равно времени его завершения");
    } else if (beginDate.compareTo(endDate) < 0) {
      calcDay();
    } else if (beginDate.compareTo(endDate) > 0) {
      calcNight();
    }
  }

  // елси дневная смена
  private void calcDay() {

    // продолжительность рабочего дня 
    long lengthMs = endDate.getTime() - beginDate.getTime();
    Double dm = duration * 60.00 * 60.00 * 1000.00;
    long durationMs = dm.longValue();

    // вычислить дату старта
    Calendar current = Calendar.getInstance();
    current.setTime(currentDate);
    Calendar begin = Calendar.getInstance();
    begin.setTime(currentDate);
    begin.set(Calendar.HOUR_OF_DAY, beginHour);
    begin.set(Calendar.MINUTE, beginMinute);
    begin.set(Calendar.SECOND, 0);
    Calendar end = Calendar.getInstance();
    end.setTime(currentDate);
    end.set(Calendar.HOUR_OF_DAY, endHour);
    end.set(Calendar.MINUTE, endMinute);
    end.set(Calendar.SECOND, 0);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(current.getTime());

    if ((begin.before(current) || begin.equals(current)) && current.before(end)) {
      // если дата старта - рабочее время, оставить так
    } else if (current.before(begin)) {
      // если дата старта - нерабочее время, то определить как начало рабочего дня
      calendar.set(Calendar.HOUR_OF_DAY, beginHour);
      calendar.set(Calendar.MINUTE, beginMinute);
      calendar.set(Calendar.SECOND, 0);
    } else if (end.before(current) || end.equals(current)) {
      calendar.set(Calendar.HOUR_OF_DAY, beginHour);
      calendar.set(Calendar.MINUTE, beginMinute);
      calendar.set(Calendar.SECOND, 0);
      calendar.add(Calendar.DAY_OF_YEAR, 1);
    }

    // учесть выходные
    if (weekend) {
      if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        calendar.add(Calendar.DAY_OF_YEAR, 1);
      }
      if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
        calendar.add(Calendar.DAY_OF_YEAR, 2);
      }
    }
    Calendar start = Calendar.getInstance();
    start.setTime(calendar.getTime());

    // вычислить количество дней
    BigDecimal d = new BigDecimal(durationMs / lengthMs);
    BigInteger days = d.toBigInteger();

    // вычислить остаток
    long ms = durationMs - (days.longValue() * lengthMs);

    // прибавить к начальной дате количество дней
    calendar.add(Calendar.DAY_OF_YEAR, days.intValue());
    // найти время окончания рабочего дня
    Calendar endDay = Calendar.getInstance();
    endDay.setTime(calendar.getTime());
    endDay.set(Calendar.HOUR_OF_DAY, endHour);
    endDay.set(Calendar.MINUTE, endMinute);
    endDay.set(Calendar.SECOND, 0);
    // сосчитать время до конца рабочего дня
    long time = endDay.getTimeInMillis() - calendar.getTimeInMillis();
    // если время больше остатка
    if (time >= ms) {
      // прибавить остаток
      calendar.add(Calendar.MILLISECOND, (int) ms);
    } // иначе
    else {
      // вычислить их разность
      long diff = ms - time;
      // найти начало следующего дня
      calendar.add(Calendar.DAY_OF_YEAR, 1);
      calendar.set(Calendar.HOUR_OF_DAY, beginHour);
      calendar.set(Calendar.MINUTE, beginMinute);
      calendar.set(Calendar.SECOND, 0);
      // прибавить разность к началу следующего дня
      calendar.add(Calendar.MILLISECOND, (int) diff);
    }

    if (weekend) {
      calendar = calсFinalDateToWeekend(start, calendar);
    }

    data = FormatDate.getDateInMysql(calendar.getTime());
  }

  private void calcNight() {
    // продолжительность дня
    Calendar current = Calendar.getInstance();
    current.setTime(currentDate);
    Calendar begin = Calendar.getInstance();
    begin.setTime(currentDate);
    begin.set(Calendar.HOUR_OF_DAY, beginHour);
    begin.set(Calendar.MINUTE, beginMinute);
    Calendar end = Calendar.getInstance();
    end.setTime(currentDate);
    end.set(Calendar.HOUR_OF_DAY, endHour);
    end.set(Calendar.MINUTE, endMinute);

    Calendar c1 = Calendar.getInstance();
    c1.setTime(end.getTime());
    c1.set(Calendar.HOUR_OF_DAY, 0);
    c1.set(Calendar.MINUTE, 0);
    c1.set(Calendar.SECOND, 0);
    long l1 = end.getTimeInMillis() - c1.getTimeInMillis();

    Calendar c2 = Calendar.getInstance();
    c2.setTime(begin.getTime());
    c2.set(Calendar.HOUR_OF_DAY, 24);
    c2.set(Calendar.MINUTE, 0);
    c2.set(Calendar.SECOND, 0);
    long l2 = c2.getTimeInMillis() - begin.getTimeInMillis();

    long lengthMs = l1 + l2;
    Double dm = duration * 60.00 * 60.00 * 1000.00;
    long durationMs = dm.longValue();

    // вычислить дату старта
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(current.getTime());
    if (current.before(end) || (begin.before(current) || begin.equals(current))) {
      // если дата старта - рабочее время, оставить всё как есть
    } else {
      // если дата старта - нерабочее время, то определить как начало рабочего дня
      calendar.set(Calendar.HOUR_OF_DAY, beginHour);
      calendar.set(Calendar.MINUTE, beginMinute);
      calendar.set(Calendar.SECOND, 0);
    }
    // учесть выходные
    if (weekend) {
      if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        calendar.add(Calendar.DAY_OF_YEAR, 1);
      }
      if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
        calendar.add(Calendar.DAY_OF_YEAR, 2);
      }
    }
    Calendar start = Calendar.getInstance();
    start.setTime(calendar.getTime());

    // количество дней
    BigDecimal d = new BigDecimal(durationMs / lengthMs);
    BigInteger days = d.toBigInteger();
    // вычислить остаток
    long ms = durationMs - (days.longValue() * lengthMs);
    // прибавить к начальной дате количество дней
    calendar.add(Calendar.DAY_OF_YEAR, days.intValue());
    // прибавить к начальной дате количество дней
    calendar.add(Calendar.DAY_OF_YEAR, days.intValue());

    // найти конец рабочего дня
    Calendar endDay = Calendar.getInstance();
    endDay.setTime(calendar.getTime());
    endDay.set(Calendar.HOUR_OF_DAY, endHour);
    endDay.set(Calendar.MINUTE, endMinute);
    endDay.set(Calendar.SECOND, 0);
    if (calendar.before(endDay) || calendar.equals(endDay)) {
    } else {
      endDay.add(Calendar.DAY_OF_YEAR, 1);
    }
    // сосчитать время до конца рабочего дня
    long time = endDay.getTimeInMillis() - calendar.getTimeInMillis();

    // если время больше остатка
    if (time >= ms) {
      // прибавить остаток
      calendar.add(Calendar.MILLISECOND, (int) ms);
    } // иначе
    else {
      // вычислить их разность
      long diff = ms - time;
      // найти начало следующего рабочего дня
      Calendar startDay = Calendar.getInstance();
      startDay.setTime(calendar.getTime());
      startDay.set(Calendar.HOUR_OF_DAY, beginHour);
      startDay.set(Calendar.MINUTE, beginMinute);
      startDay.set(Calendar.SECOND, 0);
      if (calendar.before(startDay) || calendar.equals(startDay)) {
      } else {
        startDay.add(Calendar.DAY_OF_YEAR, 1);
      }
      // прибавить разность к началу следующего рабочего дня
      calendar.add(Calendar.MILLISECOND, (int) diff);
    }

    if (weekend) {
      calendar = calсFinalDateToWeekend(start, calendar);
    }

    data = FormatDate.getDateInMysql(calendar.getTime());
  }

  private Calendar calсFinalDateToWeekend(Calendar start, Calendar calendar) {
    // количество выходных, которые попали между начальной и конечной датой
    int weekend = 0;
    Calendar s = Calendar.getInstance();
    s.setTime(start.getTime());
    Calendar e = Calendar.getInstance();
    e.setTime(calendar.getTime());
    s.set(Calendar.MINUTE, 0);
    s.set(Calendar.HOUR_OF_DAY, 12);
    s.set(Calendar.SECOND, 0);
    e.set(Calendar.MINUTE, 0);
    e.set(Calendar.HOUR_OF_DAY, 12);
    e.set(Calendar.SECOND, 0);
    while (s.before(e) || s.equals(e)) {
      if (s.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || s.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
        weekend++;
      }
      s.add(Calendar.DAY_OF_YEAR, 1);
    }
    // прибавить дату на количество выходных
    while (weekend > 0) {
      calendar.add(Calendar.DAY_OF_YEAR, 1);
      if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
        weekend--;
      }
    }
    return calendar;
  }
}
