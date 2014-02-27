/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prim.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import sun.misc.BASE64Encoder;

/**
 * класс для обработки html запросов по url основная задача - получать код
 * удаленной страницы
 *
 * @author пользователь
 */
public final class URLRequester {

  private final String urlSrc;
  private final String encode;
  private final String login;
  private final String password;
  private List<String> errors = new ArrayList<String>();
  private String result = "";
  private String postParameters = "";
  boolean post = false;
  String urlEncoding = "UTF-8";

  /**
   *
   * @param urlSrc адрес URL
   * @param encode кодировка
   * @param login логин
   * @param password пароль
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
    this.encode = encode;
    this.login = null;
    this.password = null;
  }

  /**
   * установить строку параметров для запроса POST в формате
   * name=value&name2=value2
   *
   * @param postParameters
   */
  public void addPostParameter(Object name, Object value)
          throws UnsupportedEncodingException {
    if (!postParameters.isEmpty()) {
      postParameters += "&";
    }
    postParameters += URLEncoder.encode(name.toString(), urlEncoding);
    postParameters += "=";
    postParameters += URLEncoder.encode(value.toString(), urlEncoding);
  }

  /**
   * установить, что запрос является запросом POST
   */
  public void setPostMethod() {
    this.post = true;
  }

  /**
   * устанавливает кодировку для URL. По умолчанию UTF-8
   *
   * @param urlEncoding
   */
  public void setUrlEncoding(String urlEncoding) {
    this.urlEncoding = urlEncoding;
  }

  public String getResult() {
    return result;
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

        if (post) {
          // если это POST запрос, то отправляем данные на сервер
          conn.setDoOutput(true);
          OutputStreamWriter out;
          if (encode != null) {
            out = new OutputStreamWriter(conn.getOutputStream(), encode);
          } else {
            out = new OutputStreamWriter(conn.getOutputStream());
          }
          out.write(postParameters);
          out.write("\r\n");
          out.flush();
          out.close();
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
        res = true;
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
   *
   * @return
   */
  public final List<String> getErrors() {
    return errors;
  }
}
