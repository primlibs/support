package com.prim.support.filterValidator.entity;

import java.text.*;
import java.util.Date;

/**
 * сравнение дат
 *
 * @author Pavel Rice
 */
public class DateCompareValidator extends ValidatorAbstract {

  private String format = "yyyy-MM-dd HH:mm:ss";
  private String etalon = "";
  // флаги, которые определют тип валидации
  private boolean bigger = false;
  private boolean lesser = false;
  private boolean equals = false;
  private DateFormatValidator valid = new DateFormatValidator();
  static final long serialVersionUID = 12345L;

  public boolean execute() {
    boolean isValid = true;

    // проверка, соответствуют ли даты формату
    valid.setFormat(format);
    valid.setData(data);
    boolean validData = valid.execute();
    valid.setData(etalon);
    boolean validEtalon = valid.execute();

    if (validData & validEtalon) {
      try {
        DateFormat formatter = new SimpleDateFormat(format);
        Date sourceDate = formatter.parse(data.toString());
        Date etalonDate = formatter.parse(etalon);
        // сравнение даты
        int compare = sourceDate.compareTo(etalonDate);
        if (bigger & compare <= 0) {
          isValid = false;
          setErrorMessage("дата должна быть больше, чем " + etalon);
        } else if (lesser & 0 <= compare) {
          isValid = false;
          setErrorMessage("дата должна быть меньше, чем " + etalon);
        } else if (equals & 0 != compare) {
          isValid = false;
          setErrorMessage("дата должна быть равна " + etalon);
        }
      } catch (Exception exc) {
        isValid = false;
        setErrorMessage("неправильный формат дат");
      }
    } else {
      isValid = false;
      setErrorMessage("неправильный формат дат");
    }

    return isValid;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(Object format) {
    this.format = format.toString();
  }

  public String getEtalon() {
    return etalon;
  }

  public void setEtalon(Object etalon) {
    this.etalon = etalon.toString();
  }

  public boolean getLesser() {
    return lesser;
  }

  public void setLesser(Object lesser) {
    this.lesser = Boolean.parseBoolean(lesser.toString());
  }

  public boolean getEquals() {
    return equals;
  }

  public void setEquals(Object equals) {
    this.equals = Boolean.parseBoolean(equals.toString());
  }

  public boolean getBigger() {
    return bigger;
  }

  public void setBigger(Object bigger) {
    this.bigger = Boolean.parseBoolean(bigger.toString());
  }
}
