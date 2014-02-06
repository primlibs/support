package com.prim.support.filterValidator.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * проверка, является ли значение числом
 *
 * @author Pavel Rice
 */
public class DecimalValidator extends ValidatorAbstract {

  /**
   * разрешены ли отрицательные значения
   */
  private boolean allowMinus = true;
  static final long serialVersionUID = -2721530704452019754L;

  public boolean execute() {
    boolean isValid = true;
    try {
      double d = Double.parseDouble(data.toString());
      if (!allowMinus) {
        if (d < 0) {
          isValid = false;
          setErrorMessage("значение должно быть больше ноля");
        }
      }
    } catch (NumberFormatException e) {
      isValid = false;
      setErrorMessage("значение должно быть числом");
    }
    return isValid;
  }

  public boolean isAllowMinus() {
    return allowMinus;
  }

  public void setAllowMinus(Object allowMinus) {
    this.allowMinus = Boolean.parseBoolean(allowMinus.toString());
  }
}
