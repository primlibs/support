package com.prim.support.attachments;

import com.prim.support.sender.ByteArrayDataSource;
import javax.activation.DataSource;


/**
 * вложение, которое представляет собой контент в виде массива байтов
 * @author Rice Pavel
 */
public class ByteArrayAttachment implements Attachment {
  
  private byte[] content;
  
  private String name;
  
  /**
   * 
   * @param content контент вложения
   * @param name название вложения
   */
  public ByteArrayAttachment(byte[] content, String name) {
    this.name = name;
    this.content = content;
  }
  
  @Override
  public String getName() {
    return name;
  }

  @Override
  public DataSource getDataSource() {
    return new ByteArrayDataSource(content);
  }
  
 
  
}
