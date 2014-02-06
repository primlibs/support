/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prim.support;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author User
 */
public interface ToXml {
  
  public void getSelfInXml(Document doc, Element root)throws Exception;
  
}
