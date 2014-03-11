/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prim.support.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * типы затрат
 * 
 * @author Rice Pavel
 */
public enum ParameterContain {
  
  EXIST(1, "Содержит"), NOT_EXIST(2, "Не содержит"), NOT_IMPORTANT(1, "Неважно");
  
  final private Integer id;
  
  final private String name;
  
  public Integer getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  private ParameterContain(int id, String name) {
    this.id = id;
    this.name = name;
  }
  
  public static Map<String, Object> all() {
    Map<String, Object> map = new LinkedHashMap();
    for (ParameterContain type: ParameterContain.values()) {
      map.put(type.id.toString(), type.name);
    }
    return map;
  }
  
  public static String getNameById(Object typeId) {
    String name = "";
    if (typeId != null) {
      Map<String, Object> all = all();
      String typeIdString = typeId.toString();
      if (all.get(typeIdString) != null) {
        name = all.get(typeIdString).toString();
      }
    }
    return name;
  }
  
}
