package com.prim.support;

import java.io.*;
import java.util.ArrayList;

/**
 * класс для работы с файлами
 *
 * @author Pavel Rice
 */
public class FileExecutor {

  /**
   * объект файла
   */
  private File file;
  /**
   * массив ошибок
   */
  private ArrayList<String> errors = new ArrayList<String>();
  /**
   * путь к директории файла
   */
  private String path;
  /**
   * название файла
   */
  private String name;

  /**
   *
   * @param path - путь до директории, где находитс файл
   * @param fileName - имя файла
   */
  public FileExecutor(String path, String fileName) {
    this.path = path;
    this.name = fileName;
    file = new File(path, fileName);
  }

  /**
   *
   * @param fullName - полный путь к файлу
   */
  public FileExecutor(String fullName) {
    file = new File(fullName);
    this.path = file.getParent();
    this.name = file.getName();
  }

  /**
   *
   * @param file - объект файла
   */
  public FileExecutor(File file) {
    this.file = file;
    path = file.getParent();
    name = file.getName();
  }

  /**
   * существует ли файл
   *
   * @return
   */
  public boolean exists() {
    boolean exists = false;
    if (file.exists() & file.isFile()) {
      exists = true;
    }
    return exists;
  }

  /**
   * возвращает массив ошибок
   *
   * @return
   */
  public ArrayList<String> getErrors() {
    return errors;
  }

  /**
   * возвращает путь каталога, в котором содержится файл
   *
   * @return
   */
  public String getPath() {
    return path;
  }

  /**
   * возвращает имя файла
   *
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * возвращает содержимое файла в виде строки. Может работать некорректно из-за
   * проблем с кодировкой.
   *
   * TODO узнавать кодировку файла и читать в зависимости от кодировки. <br/>
   * можно использовать эту библиотеку: http://jchardet.sourceforge.net/ либо
   * библиотеки от Apache
   *
   * @return
   */
  public String readString() {
    /*
     String result;
     int i;
     result = "";
     try {
     char[] ch = new char[(int) file.length()];

     // если нужно читать файл в кодировке UTF-8, то можно делать так:
     // Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");

     FileReader reader = new FileReader(file);

     // возможно, длина массива ch больше, чем количество символов в файле
     // поэтому получаем количество считанных символов
     int count = reader.read(ch);
     result = String.valueOf(ch, 0, count);
     reader.close();
     } catch (IOException exc) {
     errors.add("ошибка при чтении файла: файла не существует");
     }
     return result;
     */
    return readString(null);
  }

  /**
   * возвращает содержимое файла в виде строки. Если charset == null, то читает
   * содержимое файла без учета кодировки. В этом случае может работать
   * некорректно.
   *
   * @param charset кодировка файла
   * @return
   */
  public String readString(String charset) {
    String result;
    int i;
    result = "";
    try {
      char[] ch = new char[(int) file.length()];

      Reader reader;
      if (charset != null) {
        reader = new InputStreamReader(new FileInputStream(file), charset);
      } else {
        reader = new FileReader(file);
      }

      // возможно, длина массива ch больше, чем количество символов в файле
      // поэтому получаем количество считанных символов
      int count = reader.read(ch);
      result = String.valueOf(ch, 0, count);
      reader.close();
    } catch (IOException exc) {
      errors.add("ошибка при чтении файла: файла не существует");
    }
    return result;
  }

  /**
   * записать строку в файл. Перед записью содержимое файла стирается <br/> если
   * файла не существует - он будет создан
   *
   * @param string - строка для записи
   * @return - успешна ли запись
   */
  public boolean writeString(String string) {
    return writeOrAppend(string, false);
  }

  /**
   * добавить строку в файл. Содержимое файла не стирается, строка записываетс в
   * конец файла<br/> Если файла не существует - он будет создан
   *
   * @param string
   * @return
   */
  public boolean appendString(String string) {
    return writeOrAppend(string, true);
  }

  /**
   * удалить файл
   *
   * @return - удалился ли файл
   */
  public boolean delete() {
    return file.delete();
  }

  /**
   * чтение байтов из файла
   *
   * @return массив быйтов
   */
  public byte[] readBytes() {
    byte[] bytes = new byte[(int) file.length()];
    try {
      FileInputStream input = new FileInputStream(file);
      input.read(bytes);
      input.close();
    } catch (IOException e) {
      errors.add("ошибка при чтении файла: файла не существует");
    }
    return bytes;
  }

  /**
   * запись в файл массива байтов
   *
   * @param bytes - массив байтов
   * @return - удалась ли запись
   */
  public boolean writeBytes(byte[] bytes) {
    boolean done = false;
    try {
      FileOutputStream output = new FileOutputStream(file);
      output.write(bytes);
      output.close();
      done = true;
    } catch (IOException e) {
      errors.add("ошибка при записи в файл: файла не существует");
    }
    return done;
  }

  /**
   * копировать файл в новую директорию. Если в новой директории уже есть файл с
   * таким именем, то он стирается
   *
   * @param newPath - имя новой директории
   * @return
   */
  public boolean copy(String newPath) {
    return copyOrMove(newPath, false);
  }

  /**
   * переместить файл в новую директкторию. Если в новой директории уже есть
   * файл с таким именем, то он стирается
   *
   * @param newPath - имя новой директории
   * @return
   */
  public boolean move(String newPath) {
    return copyOrMove(newPath, true);
  }

  /**
   * переименовать файл
   *
   * @param newName - новое имя файла
   * @return - переименовался ли файл
   */
  public boolean rename(String newName) {
    File newFile = new File(path, newName);
    boolean done = file.renameTo(newFile);
    if (done) {
      this.name = newName;
      this.file = newFile;
    }
    return done;
  }

  private boolean writeOrAppend(String string, boolean append) {
    boolean done = false;
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(file, append));
      writer.write(string);
      done = true;
      writer.close();
    } catch (IOException exc) {
      errors.add("ошибка при записи в файл");
    }
    return done;
  }

  /**
   * копировать либо переместить файл в новую директорию. если в новой
   * директории уже есть файл с таким именем, то он стирается
   *
   * @param dir - имя новой директории
   * @param del - удалять ли старый файл
   * @return
   */
  private boolean copyOrMove(String newPath, boolean delete) {
    boolean done = false;
    if (this.exists()) {
      File dir = new File(newPath);
      // если нет такой директории, то создать
      if (!dir.isDirectory()) {
        dir.mkdirs();
      }
      File newFile = new File(newPath, name);
      byte[] bytes = this.readBytes();
      FileExecutor newFileExecutor = new FileExecutor(newFile);
      done = newFileExecutor.writeBytes(bytes);
      if (done) {
        if (delete) {
          file.delete();
        }
        file = newFile;
        path = newPath;
      }
    }
    if (!done) {
      errors.add("ошибка при копировании файла");
    }
    return done;
  }
}
