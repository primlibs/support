package com.prim.support.filterValidator.entity;

import java.text.*;
import java.util.Date;

/**
 * приведение даты к нужному формату
 *
 * @author Pavel Rice
 */
public class DateToFormatFilter extends ValidatorAbstract {

  /**
   * формат, к которому нужно привести дату
   */
  private String format = "yyyy-MM-dd HH:mm:ss";
  static final long serialVersionUID = -2006245014226078294L;
  private DateFormatValidator valid = new DateFormatValidator();

  public boolean execute() {
    boolean isValid = false;
    try {

      // допустимые форматы для начальной даты
      String[] formats = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd H:mm:ss", "yyyy-MM-dd H:mm", "yyyy-MM-dd HH:mm", "yyyy-MM-dd",
        "yyyy-MM-dd HH.mm.ss", "yyyy-MM-dd H.mm.ss", "yyyy-MM-dd HH.mm", "yyyy-MM-dd H.mm", "dd.MM.yyyy HH:mm", "dd.MM.yyyy HH:mm:ss","dd.MM.yyyy H:mm:ss","dd.MM.yyyy H:mm", "dd.MM.yyyy"};

      for (String sourceFormat : formats) {
        // определяем формат начальной даты
        // проверяем, совпадает ли начальная дата с каким-либо из разрешенных форматов
        valid.setFormat(sourceFormat);
        valid.setData(data.toString().trim());
        // если совпадает - то преобразовать дату 
        if (valid.execute() == true) {
          SimpleDateFormat f1 = new SimpleDateFormat(sourceFormat);
          Date sourceDate = f1.parse(data.toString().trim());
          SimpleDateFormat f2 = new SimpleDateFormat(format);
          data = f2.format(sourceDate);
          isValid = true;
          break;
        }
      }
    } catch (ParseException exc) {
      isValid = false;
    }
    if (!isValid) {
      setErrorMessage("дата передана в неправильном формате");
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
