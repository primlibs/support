/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.prim.support.filterValidator.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.*;
/**
 *
 * @author Admin
 */
public class OgrnValidator extends ValidatorAbstract {

    static final long serialVersionUID = 12345L;
       
    @Override
    public boolean execute() {
        boolean result = false;
        BigInteger test = new BigInteger(data.toString());
        addErrorMessage(" 1. " + data.toString());
        data = data.toString().replaceAll("[^0-9]*", "");
        addErrorMessage(" 2. " + data.toString());
        BigInteger test2 = new BigInteger(data.toString());
        if (data.toString().length() == 13) {
            String step = "";
            step = data.toString().substring(0,12);
            addErrorMessage(" 3. " + step);
            BigInteger step1 = new BigInteger(step);
            char cs = data.toString().charAt(12);
            String coSu = String.valueOf(cs);
            BigInteger contSum = new BigInteger(coSu);
            BigInteger param = new BigInteger("11");
            BigInteger step2 = step1.mod(param);
            if (step2==contSum){
                result = true;
            }else{
                result = false;
                addErrorMessage("Такого ОГРН не существует, нарушены контрольные соотношения.");
            }
        } else if (data.toString().length() == 15){
            String step = "";
            step = data.toString().substring(0,14);
            BigInteger step1 = new BigInteger(step);
            char cs = data.toString().charAt(14);
            String coSu = String.valueOf(cs);
            BigInteger contSum = new BigInteger(coSu);
            BigInteger param = new BigInteger("13");
            BigInteger step2 = step1.mod(param);
            if (step2==contSum){
                result = true;
            } else {
                result = false;
                addErrorMessage("Такого ОГРН не существует, нарушены контрольные соотношения.");
            }
        } else {
          result = false;
          addErrorMessage("Такого ОГРН не существует, нарушены контрольные соотношения.");
        }
        return result;
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
