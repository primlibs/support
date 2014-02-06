package com.prim.support.filterValidator.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.*;

/**
 * преобразовать строку к числу
 *
 * @author Pavel Rice
 */
public class InnValidator extends ValidatorAbstract {

  static final long serialVersionUID = 12345L;

  @Override
  public boolean execute() {
    boolean result = false;
    String str = data.toString();
    data = str.replaceAll("[^0-9]*", "");
    if (data.toString().length() == 10) {
      String[] multyArr = {"2", "4", "10", "3", "5", "9", "4", "6", "8"};
      BigInteger step2 = new BigInteger("0");
      String sres = "";
      //ИНН.10. 1)Находим произведения первых 9-ти цифр ИНН на спепиальные множители соотственно. 9 множителей ( 2 4 10 3 5 9 4 6 8 ). 
      //ИНН.10. 2) Складываем все 9-ть получившихся произведений. 
      for (Integer in = 0; in < 9; in++) {
        char[] cAr = {data.toString().charAt(in)};
        String stt = String.valueOf(cAr);
        BigInteger st1 = new BigInteger(stt);
        step2 = step2.add(st1.multiply(new BigInteger(multyArr[in])));
      }
      //ИНН.10. 3) Получившуюся сумму делим на число 11 и извлекаем целую часть частного от деления. 
      BigInteger step3 = step2.divide(new BigInteger("11"));

      //ИНН.10. 4) Умножаем получившееся число на 11.
      BigInteger step4 = step3.multiply(new BigInteger("11"));
      //ИНН.10. 5) Сравниваем числа получившиеся на шаге 2 и шаге 4, их разница,
      //и есть контрольное число, которое и должно равняться 10-й цифре в ИНН. 
      //(Если контрольное число получилось равным 10-ти, в этом случае принимаем контрольное 
      //число равным 0.) 
      BigInteger step5 = step4.subtract(step2).abs();
      char[] cAr = {data.toString().charAt(9)};
      String stt = String.valueOf(cAr);
      BigInteger val = new BigInteger(stt);
      if (val.equals(step5) || (step5.toString().equals("10") && val.toString().equals("0"))) {
        result = true;
      } else {
        result = false;
        addErrorMessage("Такого ИНН не существует, нарушены контрольные соотношения.");
      }

    } else if (data.toString().length() == 12) {
      String[] multyArr = {"7", "2", "4", "10", "3", "5", "9", "4", "6", "8"};
      BigInteger step2 = new BigInteger("0");
      //ИНН.12. 1)Находим произведения первых 10-ти цифр ИНН на спепиальные множители соотственно 
      //(10-ю цифру принимаем за 0). 10 множителей ( 7 2 4 10 3 5 9 4 6 8 ). 
      //ИНН.12. 2) Складываем все 10-ть получившихся произведений. 
      for (Integer in = 0; in < 10; in++) {
          char[] cAr = {data.toString().charAt(in)};
          String stt = String.valueOf(cAr);
          BigInteger st1 = new BigInteger(stt);
          step2 = step2.add(st1.multiply(new BigInteger(multyArr[in])));
      }
      //ИНН.12. 3) Получившуюся сумму делим на число 11 и извлекаем целую часть частного от деления. 
      BigInteger step3 = step2.divide(new BigInteger("11"));
      //ИНН.12. 4) Умножаем получившееся число на 11.
      BigInteger step4 = step3.multiply(new BigInteger("11"));
      //ИНН.12. 5) Сравниваем числа получившиеся на шаге 2 и шаге 4, их разница, и 
      //есть первое контрольное число, которое и должно равняться 11-й цифре в ИНН.
      //(Если контрольное число получилось равным 10-ти, в этом случае принимаем контрольное 
      //число равным 0.) Если получившееся число не не равно 11-ой цифре ИНН, значит ИНН не верный,
      //если же совпадает, тогда высчитываем следующее контрольное число, которое 
      //должно быть равным 12-ой цифре ИНН
      BigInteger step5 = step4.subtract(step2).abs();
      char[] cAr = {data.toString().charAt(10)};
      String stt = String.valueOf(cAr);
      BigInteger val11 = new BigInteger(stt);
      if (val11.equals(step5) || (val11.toString().equals("0") && step5.toString().equals("10"))) {
        //ИНН.12. 6)Находим произведения первых 11-ти цифр ИНН на спепиальные множители 
        //соотственно (10-ю цифру принимаем за 0). 11 множителей ( 3 7 2 4 10 3 5 9 4 6 8 ). 
        //ИНН.12. 7) Складываем все 11-ть получившихся произведений. 
        String[] multyArr2 = {"3", "7", "2", "4", "10", "3", "5", "9", "4", "6", "8"};
        BigInteger step7 = new BigInteger("0");
        for (Integer in = 0; in < 11; in++) {
            char[] cAr2 = {data.toString().charAt(in)};
            String stt1 = String.valueOf(cAr2);
            BigInteger st1 = new BigInteger(stt1);
            step7 = step7.add(st1.multiply(new BigInteger(multyArr2[in])));
        }
        //ИНН.12. 8) Получившуюся сумму делим на число 11 и извлекаем целую часть частного от деления. 
        BigInteger step8 = step7.divide(new BigInteger("11"));
        //ИНН.12. 9) Умножаем получившееся число на 11. 
        BigInteger step9 = step8.multiply(new BigInteger("11"));
        //ИНН.12. 10) Сравниваем числа получившиеся на шаге 7 и шаге 9, их разница, 
        //и есть контрольное число, которое и должно равняться 12-й цифре в ИНН. 
        //(Если контрольное число получилось равным 10-ти, в этом случае принимаем 
        //контрольное число равным 0.) Если высчитанное число равно 12-ой цифре ИНН, 
        //и на первом этапе все контрольное число совпало с 11-ой цифрой ИНН, следовательно
        //ИНН считается верным.
        BigInteger step10 = step9.subtract(step7).abs();
        char[] cAr2 = {data.toString().charAt(11)};
        String stt3 = String.valueOf(cAr2);
        BigInteger val12 = new BigInteger(stt3);
        if (val12.equals(step10) || (val12.toString().equals("0") && step10.toString().equals("10"))) {
          result = true;
        } else {
          result = false;
          addErrorMessage("Такого ИНН не существует, нарушены контрольные соотношения.");
        }
      } else {
        result = false;
        addErrorMessage("Такого ИНН не существует, нарушены контрольные соотношения.");
      }
    } else {
      addErrorMessage("ИНН должен быть равен 10 (юр лица) или 12 (ИП и физ лица) знакам.");
      result = false;
    }

    return result;
  }
}
