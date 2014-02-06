package com.prim.support.filterValidator.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * преобразовать строку к числу
 *
 * @author Pavel Rice
 */
public class DigitsFilter extends ValidatorAbstract {

  static final long serialVersionUID = 12345L;

  public boolean execute() {
    String str = data.toString();
    data = str.replaceAll("[^0-9]*", "");
    return true;
  }
}
