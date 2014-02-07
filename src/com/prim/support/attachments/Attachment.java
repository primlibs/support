
package com.prim.support.attachments;

import javax.activation.DataSource;

/**
 * объект класса, реализующего этот интерфейс, представляет собой файл вложения, который может быть прикреплен к сообщению
 * @author Rice Pavel
 */
public interface Attachment {
  
  /**
   * возвращает объект DataSource
   * @return 
   */
  public DataSource getDataSource();
  
  /**
   * возвращает название вложения
   * @return 
   */
  public String getName();
  
}
