package com.prim.support.filterValidator.entity;

import java.text.*;
import java.util.*;

/**
 * проверка формата даты
 *
 * @author Pavel Rice
 */
public class DateFormatValidator extends ValidatorAbstract {

  private String format = "yyyy-MM-dd HH:mm:ss";
  static final long serialVersionUID = 6653319690828991986L;

  public boolean execute() {
    boolean isValid = true;
    try {
      // привести строку к дате, а дату обратно к строке
      // сравнить начальную и конечную строки
      // если дата не соответствует формату - либо не совпадут строки, либо будет Exception
      DateFormat formatter = new SimpleDateFormat(format);
      String sourceString = data.toString().trim();
      Date checkDate = formatter.parse(sourceString);
      String resultString = formatter.format(checkDate);
      if (!resultString.equals(sourceString)) {
        isValid = false;
      }
    } catch (ParseException e) {
      isValid = false;
    }

    if (!isValid) {
      setErrorMessage("дата не соответствует формату " + format);
    }
    return isValid;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(Object format) {
    this.format = format.toString();
  }
}
