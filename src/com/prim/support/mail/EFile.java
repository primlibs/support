/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prim.support.mail;

import java.io.IOException;

/**
 * файл, прикрепленный к письму
 * @author Кот
 */
public class EFile {

  public EFile() {
  }

  /**
   * 
   * @param fileName 
   * @param mimeType 
   */
  public EFile(String fileName, String mimeType) {
    this.fileName = fileName;
    this.mimeType = mimeType;
  }

  public EFile(String fileName) {
    this.fileName = fileName;
  }
  private String fileName;
  private byte[] content = null;
  private String mimeType;

  /**
   * Устанавливает содержание файла
   *
   * @param content - содержание файла
   */
  public void setContent(byte[] content) {
    this.content = content;
  }

  /**
   * Возвращает содержание файла
   *
   * @return - содержание файла
   * @throws IOException
   */
  public byte[] getContent() throws IOException {
    return content;
  }

  /**
   * Устанавливает Mime тип файла
   *
   * @param mimeType - Mime тип файла
   */
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * Возвращает Mime тип файла
   *
   * @return - Mime тип файла
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * Устанавливает название файла
   *
   * @param fileName - название файла
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Возвращает название файла
   *
   * @return - название файла
   */
  public String getFileName() {
    return fileName;
  }
}
