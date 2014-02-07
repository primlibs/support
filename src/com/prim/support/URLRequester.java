/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prim.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import sun.misc.BASE64Encoder;

/**
 * класс для обработки html запросов по url
 * основная задача - получать код удаленной страницы
 * @author пользователь
 */
public final class URLRequester {

  private final String urlSrc;
  private final String encode;
  private final String login;
  private final String password;
  private List<String> errors = new ArrayList<String>();
  private String result = "";

  
  public String getResult() {
    return result;
  }

  /**
   * 
   * @param urlSrc адрес URL
   * @param encode кодировка
   * @param login логин
   * @param password  пароль
   */
  public URLRequester(String urlSrc, String encode, String login, String password) {
    this.urlSrc = urlSrc;
    this.encode = encode;
    this.login = login;
    this.password = password;
  }

  /**
   * 
   * @param urlSrc адрес URL
   * @param encode кодировка
   */
  public URLRequester(String urlSrc, String encode) {
    this.urlSrc = urlSrc;
    this.encode = urlSrc;
    this.login = null;
    this.password = null;
  }

  /*
   * создание запроса
   */
  final public boolean create() {
    Boolean res = false;
    //начало процедуры получения страницы
    BufferedReader reader = null;
    try {
      if (urlSrc != null) {
        URL url = new URL(urlSrc);
        URLConnection conn = url.openConnection();
        if (login != null && password != null) {
          String userPassword = login + ":" + password;
          String encoding = new BASE64Encoder().encode(userPassword.getBytes());
          conn.setRequestProperty("Authorization", "Basic " + encoding);
        }
        if (encode != null) {
          reader = new BufferedReader(
                  new InputStreamReader(conn.getInputStream(), encode));
        } else {
          reader = new BufferedReader(
                  new InputStreamReader(conn.getInputStream()));
        }

        //построчное чтение данных из потока
        String s;
        while ((s = reader.readLine()) != null) {
          result += s + "\r\n";
        }
        if (reader != null) {
          //закрыть входной поток
          reader.close();
        }
        res=true;
      } else {
        errors.add("Url is null");
      }
    } catch (Exception e) {
      try {
        if (reader != null) {
          //закрыть входной поток
          reader.close();
        }
      } catch (Exception ex) {
      }
      errors.add(MyString.getStackExeption(e));
    }
    
    //вернуть результат
    return res;
  }

  /**
   * получить ошибки
   * @return 
   */
  public final List<String> getErrors() {
    return errors;
  }
  
}
