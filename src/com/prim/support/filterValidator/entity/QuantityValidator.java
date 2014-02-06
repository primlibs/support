package com.prim.support.filterValidator.entity;

/**
 * проверка на на соответствие минимальному или максимальному значению
 *
 * @author Pavel Rice
 */
public class QuantityValidator extends ValidatorAbstract {

  private double min;
  private double max;
  private boolean setMin = false;
  private boolean setMax = false;
  private DecimalValidator valid = new DecimalValidator();
  static final long serialVersionUID = 2148380179510403911L;

  public boolean execute() {
    boolean isValid = true;
    // проверка, является ли значение числом
    valid = new DecimalValidator();
    valid.setAllowMinus(true);
    valid.setData(data);
    isValid = valid.execute();

    if (!isValid) {
      setErrorMessage(valid.getErrorMessage());
    } else {
      double digits = Double.parseDouble(data.toString());
      if (setMax & max <= digits) {
        addErrorMessage("значение должно быть менее, чем " + max+" передано "+digits);
        isValid = false;
      }
      if (setMin & digits < min) {
        addErrorMessage("значение должно быть более или равно, чем " + min+" передано "+digits);
        isValid = false;
      }
    }
    return isValid;
  }

  public double getMin() {
    return min;
  }

  public void setMin(Object min) {
    this.min = Double.parseDouble(min.toString());
    setMin = true;
  }

  public double getMax() {
    return max;
  }

  public void setMax(Object max) {
    this.max = Double.parseDouble(max.toString());
    setMax = true;
  }
}
