package com.prim.support.filterValidator;

import com.prim.support.filterValidator.entity.ValidatorAbstract;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Set;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import com.prim.support.MyString;

/**
 * класс представляет собой цепочку валидаторов и фильтров
 *
 * @author Pavel Rice
 */
public class ChainValidator {

  /**
   * массив, в котором содержатся объекты - фильтры и валидаторы
   */
  private List<ValidatorAbstract> objects = new ArrayList<ValidatorAbstract>();
  /**
   * массив шибок
   */
  private List<String> errors = new ArrayList<String>();
  /**
   * значение после фильтрации
   */
  private Object data;
  /**
   * сообщение о системной ошибке.
   */
  private final String systemError = "Системная ошибка.";

  public static ChainValidator getInstance(String... validatorName){
    ChainValidator result= new ChainValidator();
    for(String name:validatorName){
      if(MyString.NotNull(name)){
        result.addChain(name);
      }
    }
    return result;
  }
  
  public static ChainValidator getInstance(Map<String, ValidatorAbstract> validatorsMap){
    return new ChainValidator(validatorsMap);
  }
  
  
  public ChainValidator() {
  }
  /**
   *
   * @param validatorsMap массив валидаторов
   */
  private  ChainValidator(Map<String, ValidatorAbstract> validatorsMap) {
    for (String validatorName : validatorsMap.keySet()) {
      ValidatorAbstract validator = validatorsMap.get(validatorName);
      objects.add(validator);
    }
  }

  /**
   *
   * @param list массив валидаторов
   */
  public ChainValidator(List<ValidatorAbstract> list) {
    objects = list;
  }

  /**
   * добавить в цепочку фильтр или валидатор
   *
   * @param objectName - название класса фильтра или валидатора
   * @param params - массив параметров
   * @param terminate - прерывать ли цепочку валидации
   * @param errorMessage - сообщение об ошибке
   */
  public void addChain(String objectName, Map<String, Object> params, boolean terminate, String errorMessage, String classPath) {
    try {
      // создать объект валидатора
      ValidatorAbstract object = ValidatorAbstract.getValidator(objectName);

      // инициализировать объект параметрами
      Set<String> keys = params.keySet();
      for (String name : keys) {
        Object value = params.get(name);
        try {
          object.setParameter(name, value);
        } catch (Exception e) {
          errors.add(MyString.getStackExeption(e));
        }
      }

      object.setTerminate(terminate);
      object.setCustomErrorMessage(errorMessage);
      objects.add(object);
    } catch (Exception exc) {
      errors.add(systemError + MyString.getStackExeption(exc));
    }
  }

  /**
   * добавить в цепочку фильтр или валидатор
   *
   * @param objectName - название класса фильтра или валидатора
   * @param params - массив параметров
   * @param terminate - прерывать ли цепочку валидации
   */
  public void addChain(String objectName, Map<String, Object> params, boolean terminate) {
    addChain(objectName, params, terminate, null, null);
  }

  /**
   * добавить в цепочку фильтр или валидатор
   *
   * @param objectName - название класса фильтра или валидатора
   * @param params - массив параметров
   */
  public void addChain(String objectName, Map<String, Object> params) {
    addChain(objectName, params, false, null, null);
  }

  /**
   * добавить в цепочку фильтр или валидатор
   *
   * @param objectName - название класса фильтра или валидатора
   */
  public void addChain(String objectName) {
    HashMap<String, Object> params = new HashMap<String, Object>();
    addChain(objectName, params, false, null, null);
  }

  /**
   * добавить в цепочку фильтр или валидатор
   *
   * @param object - фильтр или валидатор
   */
  public void addChain(ValidatorAbstract object) {
    objects.add(object);
  }

  /**
   * выполнить фильтрацию и валидацию
   *
   * @param value
   * @return
   */
  public boolean execute(Object value) {
    boolean result = true;
    data = value;


    try {
      for (ValidatorAbstract object : objects) {
        object.setData(data);
        boolean isValid = object.execute();

        if (isValid) {
          data = object.getData();
        } else {
          result = false;
          // записываем сообщение об ошибке
          writeErrorMessage(object);
          if (object.isTerminate()) {
            break;
          }
        }
      }
    } catch (Exception exc) {
      result = false;
      errors.add(systemError + MyString.getStackExeption(exc));
      System.out.println(exc);
    }

    return result;
  }

  /**
   * записывает сообщение об ошибке
   *
   * @param object - фильтр или валидатор
   */
  private void writeErrorMessage(ValidatorAbstract object) {
    String message = object.getCustomErrorMessage();
    if (message == null) {
      message = object.getErrorMessage();
    }
    errors.add(message);
  }

  /**
   *
   * @return данные после фильтрации и валидации
   */
  public Object getData() {
    return data;
  }

  /**
   *
   * @return ошибки
   */
  public List<String> getErrors() {
    return errors;
  }
}
