package org.dew.test;

import java.util.List;

import org.dew.sutil.AES;
import org.dew.sutil.Obfuscator;
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
  
  public void testApp() throws Exception {
    String s1 = "change";
    String s2 = "charge";
    
    int hd = SUtil.getHammingDist(s1, s2);
    System.out.println("SUtil.getHammingDist(\"" + s1 + "\", \"" + s2 + "\") -> " + hd);
    
    int ldr = SUtil.getLevenshteinDist(s1, s2);
    System.out.println("SUtil.getLevenshteinDist(\"" + s1 + "\", \"" + s2 + "\") -> " + ldr);
    
    int ldd = SUtil.getLevenshteinDistDyn(s1, s2);
    System.out.println("SUtil.getLevenshteinDistDyn(\"" + s1 + "\", \"" + s2 + "\") -> " + ldd);
    
    String xml = "<?xml version=\"1.0\"?>";
    xml += "<d:person xmlns:d=\"demo\">";
    xml += "<d:firstName d:checked=\"true\">Clark</d:firstName>";
    xml += "<!-- <d:lastName d:checked=\"true\">Fake</d:lastName> -->";
    xml += "<d:lastName d:checked=\"true\"><![CDATA[Kent]]></d:lastName>";
    xml += "</d:person>";
    String tag = "lastName";
    String def = "";
    
    System.out.println("xml ----------------------------------------");
    System.out.println(xml);
    System.out.println("--------------------------------------------");
    
    String lastName = SUtil.getXmlVal(xml, tag, false, def);
    System.out.println("SUtil.getXmlVal(xml, \"" + tag + "\", false, \"" + def + "\") -> " + lastName);
    
    int ot = SUtil.indexOfOpenXmlTag(xml,  tag, false);
    int ct = SUtil.indexOfCloseXmlTag(xml, tag, false);
    
    System.out.println("indexOfOpenXmlTag = " + ot + ", indexOfCloseXmlTag = " + ct);
    
    xml =  "<?xml version=\"1.0\"?>";
    xml += "<d:person xmlns:d=\"demo\" firstName=\"Clark\" lastName  = \"Kent\">";
    xml += "</d:person>";
    
    System.out.println("xml ----------------------------------------");
    System.out.println(xml);
    System.out.println("--------------------------------------------");
    
    tag = "person";
    String att = "lastName";
    
    lastName = SUtil.getXmlAttribVal(xml, tag, att, def);
    System.out.println("SUtil.getXmlAttribVal(xml, \"" + tag + "\", \"" + att + "\", \"" + def + "\") -> " + lastName);
    
    String csvRow = " \"A\",  3, \",\", \"\", ";
    List<String> values = SUtil.parseCSVRow(csvRow);
    System.out.println("SUtil.parseCSVRow(\"" + csvRow + "\" -> " + values);
    
    // Obfuscator
    String text = "Clark Kent";
    String tenc = Obfuscator.encrypt(text);
    String tdec = Obfuscator.decrypt(tenc);
    System.out.println("Obfuscator.encrypt(\"" + text + "\") -> " + tenc);
    System.out.println("Obfuscator.decrypt(\"" + tenc + "\") -> " + tdec);
    
    // Advanced Encryption Standard (AES)
    String aenc = AES.encrypt(text);
    String adec = AES.decrypt(aenc);
    System.out.println("AES.encrypt(\"" + text + "\") -> " + aenc);
    System.out.println("AES.decrypt(\"" + aenc + "\") -> " + adec);
  }
  
}
