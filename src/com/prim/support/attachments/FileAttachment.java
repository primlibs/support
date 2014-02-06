package prim.libs.attachments;

import java.io.File;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

/**
 * вложение, которое представляет собой файл на диске
 * @author Rice Pavel
 */
public class FileAttachment implements Attachment {
  
  /**
   * путь к файлу
   */
  private String path = "";
  
  /**
   * название файла
   */
  private String name = "";
  
  /**
   * 
   * @param path путь к файлу
   * @param name новое название файла
   */
  public FileAttachment(String path, String name) {
    this.path = path;
    this.name = name;
  }
  
  /**
   * 
   * @param path путь к файлу
   */
  public FileAttachment(String path) {
    this.path = path;
    File file = new File(path);
    this.name = file.getName();
  }
  
  @Override
  public String getName() {
    return name;
  }
  
  @Override
  public DataSource getDataSource() {
    return new FileDataSource(path);
  }
  
}
