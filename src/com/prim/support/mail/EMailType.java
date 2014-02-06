/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prim.support.mail;

/**
 * типы сообщений
 * @author Кот
 */
public enum EMailType {

  /**
   * Ответ
   */
  Answered,
  /**
   * Удален
   */
  Deleted,
  /**
   * Черновик
   */
  Draft,
  /**
   * Помечен
   */
  Flagged,
  /**
   * Новый
   */
  Recent,
  /**
   * Просмотренный
   */
  Seen
}
