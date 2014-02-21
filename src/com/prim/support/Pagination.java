package com.prim.support;

/**
 * класс, в котором собраны статические методы для работы с пагинатором
 *
 * @author Rice Pavel
 */
public class Pagination {

  private Pagination() {
  }

  /**
   * возвращает номер текущей страницы
   *
   * Функция принимает параметр, пришедший из запроса, и возвращает значение
   * номера текуще страницы с типом int. Параметр, пришедший из запроса, может
   * быть равен null, или может иметь некорректное значение. В любом случае
   * функция возвращает корректное значение типа int
   *
   * @param pageObject параметр, который пришел из запроса, и который обозначает
   * номер текущей страницы
   * @return
   */
  public static int getPage(Object pageObject) {
    int page = 1;
    if (pageObject != null) {
      try {
        page = Integer.parseInt(pageObject.toString());
      } catch (Exception e) {
      }
    }
    if (page < 1) {
      page = 1;
    }
    return page;
  }

  /**
   * возвращает количество страниц
   *
   * @param count количество записей
   * @param recordsPerPage количество записей на одной странице
   * @return
   */
  public static int countPages(int count, int recordsPerPage) {
    double countDouble = count;
    double recordsOfPageDouble = recordsPerPage;
    double countPagesDouble = Math.ceil(countDouble / recordsOfPageDouble);
    int countPages = (int) countPagesDouble;
    return countPages;
  }

  /**
   * возвращает номер записи, с которой нужно начать выборку
   *
   * @param page текущая страница
   * @param recordsPerPage количество записей на страницу
   * @return
   */
  public static int getStart(int page, int recordsPerPage) {
    return (page - 1) * recordsPerPage;
  }
}
