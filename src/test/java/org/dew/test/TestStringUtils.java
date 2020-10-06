package org.dew.test;

import org.dew.sutil.SUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestStringUtils extends TestCase {
  
  public TestStringUtils(String testName) {
    super(testName);
  }
  
  public static Test suite() {
    return new TestSuite(TestStringUtils.class);
  }
  
  public void testApp() {
    String s1 = "change";
    String s2 = "charge";
    
    int hd = SUtil.getHammingDistance(s1, s2);
    System.out.println("SUtil.getHammingDistance(\"" + s1 + "\", \"" + s2 + "\") -> " + hd);
    
    int ldr = SUtil.getLevenshteinDistance(s1, s2);
    System.out.println("SUtil.getLevenshteinDistance(\"" + s1 + "\", \"" + s2 + "\") -> " + ldr);
    
    int ldd = SUtil.getLevenshteinDistanceDyn(s1, s2);
    System.out.println("SUtil.getLevenshteinDistanceDyn(\"" + s1 + "\", \"" + s2 + "\") -> " + ldd);
    
    String xml = "<?xml version=\"1.0\"?>";
    xml += "<d:person xmlns:d=\"demo\">";
    xml += "<d:firstName d:checked=\"true\">Clark</d:firstName>";
    xml += "<!-- <d:lastName d:checked=\"true\">Fake</d:lastName> -->";
    xml += "<d:lastName d:checked=\"true\"><![CDATA[Kent]]></d:lastName>";
    xml += "</d:person>";
    String tag = "lastName";
    String def = "";
    
    System.out.println("xml = " + xml);
    
    String lastName = SUtil.getXmlVal(xml, tag, false, "");
    System.out.println("SUtil.getXmlVal(xml, \"" + tag + "\", false, \"" + def + "\") -> " + lastName);
    
    int ot = SUtil.indexOfOpenXmlTag(xml, tag, false);
    int ct = SUtil.indexOfCloseXmlTag(xml, tag, false);
    
    System.out.println("ot = " + ot + ", ct = " + ct);
  }
  
}
