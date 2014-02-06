package com.prim.support.filterValidator.entity;

import java.util.Iterator;

/**
 * преобразовать строку к числу и отформатировать в "денежном" формате
 *
 * @author Pavel Rice
 */
public class PhoneFilter extends ValidatorAbstract {

  static final long serialVersionUID = 12345L;
  private String firstDigit = "8";
  private String firstFourDigit = "8342";
  private String firstFiveDigit = "83422";
  
  @Override
  public boolean execute() {
    String str = data.toString();
    if(str.length()==10){
      str=firstDigit+str;
      data=str;
    }else if(str.length()==7){
      str=firstFourDigit+str;
      data=str;
    }else if(str.length()==6){
      str=firstFiveDigit+str;
      data=str;
    }
    return  true;
  }

  public String getFirstDigit() {
    return firstDigit;
  }

  public void setFirstDigit(Object firstDigit) {
    if(firstDigit!=null){
      this.firstDigit = firstDigit.toString();
    }
  }

  public String getFirstFourDigit() {
    return firstFourDigit;
  }

  public void setFirstFourDigit(Object firstFourDigit) {
    if(firstFourDigit!=null){
      this.firstFourDigit = firstFourDigit.toString();
    }
  }

  public String getFirstFiveDigit() {
    return firstFiveDigit;
  }

  public void setFirstFiveDigit(Object firstFiveDigit) {
    if(firstFiveDigit!=null){
      this.firstFiveDigit = firstFiveDigit.toString();
    }
  }
  
}
