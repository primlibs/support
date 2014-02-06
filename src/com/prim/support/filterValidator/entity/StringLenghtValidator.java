package com.prim.support.filterValidator.entity;

/**
 * проверка длины строки
 *
 * @author Pavel Rice
 */
public class StringLenghtValidator extends ValidatorAbstract {

  private int min;
  private int max;
  private boolean setMax = false;
  private boolean setMin = false;
  static final long serialVersionUID = 998201847350957326L;

  public boolean execute() {
    boolean result = true;
    String str = data.toString();
    if (setMax & max < str.length()) {
      addErrorMessage("Длина строки должна быть менее, чем " + max + " символов, передано "+str.length()+".");
      result = false;
    }
    if (setMin & str.length() < min) {
      addErrorMessage("Длина строки должна быть более, чем " + min + " символов, передано "+str.length()+".");
      result = false;
    }
    return result;
  }

  public int getMin() {
    return min;
  }

  public void setMin(Object min) {
    this.min = Integer.parseInt(min.toString());
    setMin = true;
  }

  public int getMax() {
    return max;
  }

  public void setMax(Object max) {
    this.max = Integer.parseInt(max.toString());
    setMax = true;
  }
}
