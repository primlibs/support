/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prim.support.mail;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import org.apache.commons.io.IOUtils;

/**
 * электронное письмо
 *
 * @author Кот
 */
public class Email {

  public Email() {
    attachedFile = new ArrayList<EFile>();
  }
  private String content;
  private List<EFile> attachedFile;
  private EMailType emailType;
  private List<String> from;
  private List<String> to;
  private String subject;
  private Date sendDate;
  private String messageId;
  private String references;
  private String replyContent;
  private EFile replyFile;
  private Boolean isValid;
  private String xMailer;

  /**
   * Устанавливает флаг письма
   *
   * @param emailType - флаг письма
   */
  public void setEmailType(EMailType emailType) {
    this.emailType = emailType;
  }

  public void setXMailer(String xMailer) {
    this.xMailer = xMailer;
  }

  /**
   * Возвращает флаг письма
   *
   * @return - флаг письма
   */
  public EMailType getEmailType() {
    return emailType;
  }

  /**
   * Устанавливает список прикрепленных файлов
   *
   * @param attachedFile - список прикрепленных файлов
   */
  public void setAttachedFile(List<EFile> attachedFile) {
    this.attachedFile = attachedFile;
  }

  /**
   * Возвращает список прикрепленных файлов
   *
   * @return - список прикрепленных файлов
   */
  public List<EFile> getAttachedFile() {
    return attachedFile;
  }

  /**
   * Устанавливает список отправителей
   *
   * @param from - список отправителей
   */
  public void setFrom(List<String> from) {
    this.from = from;
  }

  /**
   * Возвращает список отправителей
   *
   * @return - список отправителей
   */
  public List<String> getFrom() {
    return this.from;
  }

  /**
   * Устанавливает список получателей
   *
   * @param to - список получателей
   */
  public void setTo(List<String> to) {
    this.to = to;
  }

  /**
   * Возвращает список получателей
   *
   * @return - список получателей
   */
  public List<String> getTo() {
    return to;
  }

  /**
   * Устанавливает тему письма
   *
   * @param subject - тема письма
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * Возвращает тему письма
   *
   * @return - тема письма
   */
  public String getSubject() {
    return subject;
  }

  /**
   * Устанавливает дату отправки письма
   *
   * @param sendDate - дата отправки письма
   */
  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
  }

  /**
   * Возвращает дату отправки письма
   *
   * @return - дата отправки письма
   */
  public Date getSendDate() {
    return sendDate;
  }

  /**
   * Устанавливает содержание письма
   *
   * @param content - содержание письма
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Возвращает содержание письма
   *
   * @return - содержание письма
   */
  public String getContent() {
    return content;
  }

  /**
   * Устанавливает идентификатор письма
   *
   * @param messageId - идентификатор письма
   */
  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  /**
   * Возвращает идентификатор письма
   *
   * @return - идентификатор письма
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Устанавливает ссылки на связанные письма
   *
   * @param references - ссылки на связанные письма
   */
  public void setReferences(String references) {
    this.references = references;
  }

  /**
   * Возвращает ссылки на связанные письма
   *
   * @return - ссылки на связанные письма
   */
  public String getReferences() {
    return references;
  }

  /**
   * Обработка части письма
   *   
* @param email - объект EMail, который заполняеться из письма
   * @param m - Часть письма
   * @param level - Уровень структуры письма
   * @throws MessagingException
   * @throws IOException
   */
  public static void ProcessingPart(Email email, Part m, Integer level)
          throws MessagingException, IOException {

    if (m instanceof Message) {
      ProcessingInfoPart(email, (Message) m);
    }

    String filename = m.getFileName();

    if ((filename == null || "".equals(filename)) && (!m.isMimeType("multipart/*") && !m.isMimeType("message/rfc822"))) {
// Текст сообщения
      email.setContent((String) m.getContent());
    } else if (m.isMimeType("multipart/*")) {
// Рекурсивный разбор иерархии
      Multipart mp = (Multipart) m.getContent();
      level++;
      int count = mp.getCount();
      for (int i = 0; i < count; i++) {
        ProcessingPart(email, mp.getBodyPart(i), level);
      }
    } else if (m.isMimeType("message/rfc822")) {
// Вложенное сообщение
      level++;
      ProcessingPart(email, (Part) m.getContent(), level);
      level--;
    }

    if (level != 0 && !m.isMimeType("multipart/*") && filename != null) {
// Сохранения атачей
      String disp = m.getDisposition();
// many mailers don't include a Content-Disposition
      if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {

        List<EFile> efiles = email.getAttachedFile();
        InputStream in = null;
        try {
          in = ((MimeBodyPart) m).getInputStream();
          byte[] content = IOUtils.toByteArray(in);

          EFile efile = new EFile();
          efile.setContent(content);
          efile.setMimeType(((MimeBodyPart) m).getContentType());
          efile.setFileName(MimeUtility.decodeText(filename));
          efiles.add(efile);
        } finally {
          try {
            if (in != null) {
              in.close();
            }
          } catch (IOException e) {
          }
        }
      }
    }
  }

  /**
   * Обработка информационной части письма
   *   
* @param email - объект для сохранения email
   * @param m - входящий объект письма
   * @throws MessagingException
   * @throws UnsupportedEncodingException
   */
  private static void ProcessingInfoPart(Email email, Message m)
          throws MessagingException, UnsupportedEncodingException {

// Обработка основных параметров письма
    AddAddress(m.getFrom(), email, false, true);
    AddAddress(m.getRecipients(Message.RecipientType.TO), email, true,
            false);

    email.setSubject(m.getSubject());

    Date d = (m.getSentDate());
    if (d != null) {
      email.setSendDate(d);
    }

    String msgId = GetElementOrNull(m.getHeader("Message-Id"));
    if (msgId != null) {
      email.setMessageId(msgId);
    }

    /*
     String refs = GetElementOrNull(m.getHeader("References"));
     if (refs == null) {
     refs = GetElementOrNull(m.getHeader("In-Reply-To"));
     }
     if (msgId != null) {
     if (refs != null) {
     refs = MimeUtility.unfold(refs) + " " + msgId;
     } else {
     refs = msgId;
     }
     }
     if (refs != null) {
     email.setReferences(MimeUtility.fold(12, refs));
     }
     */
// Обработка флагов сообщения
    Flags flags = m.getFlags();
    Flags.Flag[] sf = flags.getSystemFlags();
    for (int i = 0; i < sf.length; i++) {
      Flags.Flag f = sf[i];
      if (f == Flags.Flag.ANSWERED) {
        email.setEmailType(EMailType.Answered);
      } else if (f == Flags.Flag.DELETED) {
        email.setEmailType(EMailType.Deleted);
      } else if (f == Flags.Flag.DRAFT) {
        email.setEmailType(EMailType.Draft);
      } else if (f == Flags.Flag.FLAGGED) {
        email.setEmailType(EMailType.Flagged);
      } else if (f == Flags.Flag.RECENT) {
        email.setEmailType(EMailType.Recent);
      } else if (f == Flags.Flag.SEEN) {
        email.setEmailType(EMailType.Seen);
      } else {
        continue;
      }
    }


// x-mail
    String[] hdrs = m.getHeader("X-Mailer");
    if (hdrs != null) {
      email.setXMailer(hdrs[0]);
    } else {
      email.setXMailer("");
    }
  }

  /**
   * Добавляет адрес получателя/отправителя
   *   
* @throws UnsupportedEncodingException
   */
  private static void AddAddress(Address[] address, Email email,
          Boolean addTo, Boolean addFrom) throws UnsupportedEncodingException {
    if ((!addTo && !addFrom) || (addTo && addFrom)) {
      throw new IllegalArgumentException(
              "Не установлен не один из флагов addTo, addFrom или оба установлены!");
    }

    List<String> result = new ArrayList<String>();
    if (address == null || address.length == 0) {
      return;
    }
    for (int i = 0; i < address.length; i++) {
      result.add(MimeUtility.decodeText(address[i].toString()));
    }
    if (addTo) {
      email.setTo(result);
    }
    if (addFrom) {
      email.setFrom(result);
    }
  }

  public static ArrayList<String> parseFromTo(String fromTo) {
    ArrayList<String> reslist = new ArrayList<String>();
    if (fromTo != null) {
      Boolean start = false;
      String res = "";
      for (char c : fromTo.toCharArray()) {
        String ch = "" + c;
        if (ch.equals(">")) {
          start = false;
          reslist.add(res);
        }
        if (start == true) {
          res += c;
        }
        if (ch.equals("<")) {
          start = true;
        }
      }
    }
    return reslist;
  }

  /**
   * Возвращает первый не null элемент массива и если таких элементов нет, то
   * null
   *   
* @param mas - массив
   * @return Первый не null элемент массива и если таких элементов нет, то null
   */
  private static String GetElementOrNull(String[] mas) {
    if (mas != null && mas.length > 0) {
      for (int i = 0; i < mas.length; i++) {
        if (mas[i] != null && !"".equals(mas[i])) {
          return mas[i];
        }
      }
    }
    return null;
  }
}
