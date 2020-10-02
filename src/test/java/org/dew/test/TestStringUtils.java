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
    String df = "   -  ";
    String s2 = "charge";
    
    System.out.println("s1 = " + s1);
    System.out.println("     " + df);
    System.out.println("s2 = " + s2);
      
    int hammingDistance = SUtil.getHammingDistance(s1, s2);
    System.out.println("hammingDistance = " + hammingDistance);
    
    int levenshteinDistance = SUtil.getLevenshteinDistance(s1, s2);
    System.out.println("levenshteinDistance = " + levenshteinDistance);
    
    int levenshteinDistanceDyn = SUtil.getLevenshteinDistanceDyn(s1, s2);
    System.out.println("levenshteinDistanceDyn = " + levenshteinDistanceDyn);
  }
  
}
