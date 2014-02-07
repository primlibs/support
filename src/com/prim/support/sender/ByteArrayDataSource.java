/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prim.support.sender;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;

/**
 * класс DataSource для массива байт <br/><br/>
 * системный класс, для использования внутри пакета, не предназначен для внешнего использования
 * @author User
 */
public class ByteArrayDataSource implements DataSource {

  private byte[] content;

  /**
   * 
   * @param content массив байтов
   */
  public ByteArrayDataSource(byte[] content) {
    this.content = content;
  }
  
  @Override
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(content);
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    throw new IOException("Cannot write to this read-only resource");
  }

  @Override
  public String getContentType() {
    return "application/octet-stream";
  }

  @Override
  public String getName() {
    return "";
  }
}
