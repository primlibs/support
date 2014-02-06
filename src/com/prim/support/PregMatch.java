/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prim.libs;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * класс для работы с регулярными выражениями
 * @author obydennaya_office
 */
public class PregMatch {
  private PregMatch(){
  }
  
  /**
   * возвращает массив совпадений, найденных в строке
   * @param sQuery строка для разбора
   * @param pattern регулярное выражение, с которым надо найти совпадения
   * @return 
   */
  static public ArrayList<String> getMap(String sQuery, String pattern) {
    ArrayList<String> result = new ArrayList<String>();
    Matcher matcher = Pattern.compile(pattern).matcher(sQuery);
    while (matcher.find()) {
      if (matcher.groupCount()>1) {
        result.add(matcher.group(1));
      }
    }
    return result;
  }
}
