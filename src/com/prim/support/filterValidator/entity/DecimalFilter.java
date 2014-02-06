package com.prim.support.filterValidator.entity;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Formatter;

/**
 * преобразовать строку к числу и отформатировать в "денежном" формате
 *
 * @author Pavel Rice
 */
public class DecimalFilter extends ValidatorAbstract {

  static final long serialVersionUID = 12345L;

  @Override
  public boolean execute() {
    String str = data.toString();
    boolean isValid = true;
    Double d;
    try {
      // преобразовать к числу
      try {
        d = Double.parseDouble(str);
      } catch (NumberFormatException exc1) {
        str = str.replaceFirst("[,]", ".");
        str = str.replaceAll("[^0-9-.]*", "");
        d = Double.parseDouble(str);
      }
      // отформатировать
      data = String.format("%.2f", d);
    } catch (NumberFormatException exc) {
      setErrorMessage("значение должно быть числом");
      isValid = false;
    }
    return isValid;
  }
}
