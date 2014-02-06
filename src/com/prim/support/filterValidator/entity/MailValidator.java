package com.prim.support.filterValidator.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * преобразовать строку к числу
 *
 * @author Pavel Rice
 */
public class MailValidator extends ValidatorAbstract {

  static final long serialVersionUID = 12345L;

  @Override
  public boolean execute() {
    Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
    Matcher m = p.matcher(data.toString());
    if(m.matches()){
      return true;
    }else{  
      addErrorMessage("Не соответсвует шаблону   name@host");
      return false;
    }
  }
}
