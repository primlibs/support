package com.prim.support.filterValidator.entity;

import java.util.regex.*;

/**
 * проверяет, содержит ли строка только русские символы
 *
 * @author Pavel Rice
 */
public class StringRusValidator extends ValidatorAbstract {

  /**
   * разрешены ли цифры
   */
  protected boolean allowDigits = true;
  /**
   * разршены ли спецсимволы
   */
  protected boolean allowSpecChars = true;
  /**
   * разрешены ли пробелы
   */
  protected boolean allowSpace = true;
  static final long serialVersionUID = 12345L;

  public boolean execute() {
    String patternString = "[а-яА-Я";
    if (allowDigits) {
      patternString += "1-9";
    }
    if (allowSpace) {
      patternString += "\\s";
    }
    if (allowSpecChars) {
      patternString += "!.,-/@";
    }
    patternString += "]*";

    Pattern p = Pattern.compile(patternString);
    Matcher m = p.matcher(data.toString());
    boolean isValid = m.matches();
    if (!isValid) {
      setErrorMessage("поле должно содержать только русские буквы");
    }
    return isValid;
  }

  public boolean getAllowDigits() {
    return allowDigits;
  }

  public void setAllowDigits(Object allowDigits) {
    this.allowDigits = Boolean.parseBoolean(allowDigits.toString());
  }

  public boolean getAllowSpecChars() {
    return allowSpecChars;
  }

  public void setAllowSpecChars(Object allowSpecChars) {
    this.allowSpecChars = Boolean.parseBoolean(allowSpecChars.toString());
  }

  public boolean getAllowSpace() {
    return allowSpace;
  }

  public void setAllowSpace(Object allowSpace) {
    this.allowSpace = Boolean.parseBoolean(allowSpace.toString());
  }
}
