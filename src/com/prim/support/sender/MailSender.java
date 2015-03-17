package com.prim.support.sender;

import java.util.List;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import com.prim.support.MyString;
import com.prim.support.attachments.Attachment;
import javax.mail.internet.MimeUtility;

/**
 * 
 * класс, который отправляет сообщения по электронной почте <br/><br/>
 *
 * необходимо установить файлы настроек Properties следующего формата, например:  <br/><br/>
 *  
 *   Properties mailProps = new Properties(); <br/>
 *   mailProps.put("mail.transport.protocol", "smtp");<br/>
 *   mailProps.put("mail.smtp.host", host);<br/>
 *   mailProps.put("mail.smtp.auth", "true");<br/>
 *   mailProps.put("mail.smtp.port", port);<br/>
 *   mailProps.put("mail.smtp.starttls.enable", "true");<br/><br/>
 *   
 *   mailProps.put("sender.mail.login", mailLogin);<br/>
 *   mailProps.put("sender.mail.password", mailPassword);<br/><br/>
 * 
 * также можно установить следующие настройки:<br/>
 * sender.mail.subjectEncoding - кодировка темы, по умолчанию "utf-8" <br/>
 * sender.mail.contentType - MIME-type контента и кодировка, по умолчанию "text/html; charset=utf-8" <br/>
 * sender.mail.contentEncoding - кодировка контента, по умолчанию "8bit" <br/>
 * sender.mail.messageEncoding - кодировка сообщения, по умолчанию "base64" <br/>
 * 
 * @author Rice Pavel
 * 
 */
public class MailSender extends Sender {

  private Session session;

  private final String SUBJECT_ENCODING = "utf-8";
  private final String CONTENT_TYPE = "text/html; charset=utf-8";
  private final String CONTENT_ENCODING = "8bit";
  private final String MESSAGE_ENCODING = "base64";
  private MimeMessage mimeMessage;
  
  protected MailSender() {
    super();
  }
    
  @Override
  public boolean send(Message mail) {
    boolean ok = false;
    if (getProp("login") != null && getProp("password") != null) {
      session = getSession();
      ok = validateMessage(mail);

      if (ok) {
        try {
          String subject = mail.getSubject();
          String msg = mail.getMessage();
          List<Object> to = mail.getTo();
          MimeMessage message = new MimeMessage(session);
          mimeMessage = message;

          Address[] addrs = new Address[to.size()];
          for (Object t : to) {
            addrs[to.indexOf(t)] = new InternetAddress(t.toString());
          }

          message.setRecipients(javax.mail.Message.RecipientType.TO, addrs);
          String subjectEncoding = (getProp("subjectEncoding") != null ? getProp("subjectEncoding") : SUBJECT_ENCODING);
          message.setSubject(subject + "\n", subjectEncoding);

          if (mail.getFrom() != null) {
            message.setFrom(new InternetAddress(mail.getFrom().toString()));
          } else {
            message.setFrom(new InternetAddress(getProp("login")));
          }

          MimeBodyPart messageBodyPart =
                  new MimeBodyPart();
          String contentType = (getProp("contentType") != null ? getProp("contentType") : CONTENT_TYPE);
          messageBodyPart.setContent(msg, contentType);
          String contentEncoding = (getProp("contentEncoding") != null ? getProp("contentEncoding") : CONTENT_ENCODING);
          messageBodyPart.addHeader("Content-Transfer-Encoding", contentEncoding);

          Multipart multipart = new MimeMultipart();
          multipart.addBodyPart(messageBodyPart);

          for (Attachment file : mail.getFiles()) {

            DataSource source = file.getDataSource();
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(
                    new DataHandler(source));

            messageBodyPart.setFileName(MyString.transliterate(file.getName()));
            multipart.addBodyPart(messageBodyPart);
          }

          String messageEncoding = (getProp("messageEncoding") != null ? getProp("messageEncoding") : MESSAGE_ENCODING);
          message.addHeader("Content-Transfer-Encoding", messageEncoding);
          message.setContent(multipart);

          Transport.send(message);
        } catch (Exception e) {
          ok = false;
          errors.add(MyString.getStackExeption(e));
        }
      }
    } else {
      if (getProp("login") == null) {
        errors.add("не передан логин");
      }
      if (getProp("password") == null) {
        errors.add("не передан пароль");
      }
      ok = false;
    }
    return ok;
  }
  
  
  
 @Override
 public javax.mail.Message getMailMessage() {
    return mimeMessage;
  }
  

  @Override
  protected boolean validateMessage(Message mess) {
    boolean ok = true;
    // проверка адреса получателя
    if (mess != null) {
      try {
        for (Object adr : mess.getTo()) {
          // создать адрес и сразу проверить
          InternetAddress toAdress = new InternetAddress(adr.toString(), true);
        }
        if (mess.getFrom() != null) {
          InternetAddress fromAdress = new InternetAddress(mess.getFrom().toString(), true);
        }
      } catch (Exception e) {
        ok = false;
        errors.add(MyString.getStackExeption(e));
      }
    } else {
      ok = false;
      errors.add("message is null");
    }
    return ok;
  }

  private Session getSession() {
    Session session = Session.getInstance(props, new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return (new PasswordAuthentication(getProp("login"), getProp("password")));
      }
    });
    return session;
  }

  @Override
  public String getProp(Object name) {
    if (name != null) {
      return props.getProperty("sender.mail." + name.toString());
    }
    return null;
  }

  
  
}
