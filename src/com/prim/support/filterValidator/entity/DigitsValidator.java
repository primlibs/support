package com.prim.support.filterValidator.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * состоит ли поле только из цифр
 *
 * @author Pavel Rice
 */
public class DigitsValidator extends ValidatorAbstract {

  static final long serialVersionUID = -6229871206744510731L;

  public boolean execute() {
    // привести к строке 
    String str = data.toString();
    // проверить на соответствие регулярному выражению
    boolean isValid = str.matches("[0-9]+");
    if (!isValid) {
      setErrorMessage("поле должно состоять только из цифр");
    }
    return isValid;
  }
}
