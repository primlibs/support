package com.prim.support.sender;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.internet.MimeMessage;
import com.prim.support.MyString;
import com.prim.support.attachments.Attachment;


/**
 * Класс, который отправляет сообщения
 * @author Rice Pavel
 */
public abstract class Sender {

  /**
   * настройки
   */
  protected Properties props = new Properties();
  
  /**
   * ошибки
   */
  protected List<String> errors = new ArrayList();
  
  protected Sender(){
    
  }

  /**
   * фабричный метод - возвращает конкретный экземпляр Sender
   * @param name - название класса
   * @return
   * @throws Exception 
   */
  public static Sender getInstance(Object name) throws Exception {

    if (name == null) {
      throw new Exception("name is null");
    } else {
      Sender sender;
      name = MyString.ucFirst(name.toString());
      try {
        sender = (Sender) Class.forName("com.prim.support.sender." + name).newInstance();
      } catch (Exception eq) {
        throw new Exception("Sender not found " + "prim.libs.sender." + name + " ");
      }
      return sender;
    }

  }

  /**
   * возвращает ошибки
   * @return 
   */
  public List<String> getErrors() {
    return errors;
  }

  /**
   * установка настроек в виде Properties
   * @param props 
   */
  public void setProps(Properties props) {
    this.props = props;
  }

  /**
   * отправить сообщение 
   * @param subject заголовок
   * @param content текст сообщения
   * @param to адреса получателей
   * @return 
   */
  public boolean send(String subject, String content, List<Object> to) {
    Message mess = new Message();
    mess.setSubject(subject);
    mess.setMessage(content);
    mess.setTo(to);
    return send(mess);
  }
  
  /**
   * отправить сообщение
   * @param subject заголовок
   * @param content текст сообщения
   * @param to адреса получателей
   * @param files файлы вложений
   * @return 
   */
  public boolean send(String subject, String content, List<Object> to, List<Attachment> files) {
    Message mess = new Message();
    mess.setSubject(subject);
    mess.setMessage(content);
    mess.setTo(to);
    mess.setFiles(files);
    return send(mess);
  }
  
  public javax.mail.Message getMailMessage() {
    throw new UnsupportedOperationException("Операция не поддерживается");
  }
  
  /**
   * отправить сообщение
   * @param message сообщение
   * @return 
   */
  public abstract boolean send(Message message);

  /**
   * валидировать сообщение
   * @param message
   * @return 
   */
  protected abstract boolean validateMessage(Message message);

  /**
   * получить одно свойство из Properties
   * @param name название свойства
   * @return 
   */
  public abstract Object getProp(Object name);
}
