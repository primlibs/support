/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.prim.support;

/**
 *
 * @author bezdatiuzer
 */
public class ExistValue {
    
    public static int EXIST=1;
    public static int NOT_EXIST=2;
    public static int NULL=0;
    
    private final int status;
    
    public static ExistValue  getInstance(Object ob){
        return new ExistValue(ob);
    }
    
    private ExistValue(Object ob){
        if(ob==null){
           status= ExistValue.NULL;
        }else if(MyString.NotNull(ob)){
            status= ExistValue.EXIST;
        }else{
            status= ExistValue.NOT_EXIST;
        }
    }
    
    public int getStatus(){
        return status;
    }
        
}
