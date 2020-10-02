/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dew.sutil;

public
class SUtil
{
  /**
   * Calculate Hamming distance.
   * 
   * @param s1 String
   * @param s2 String
   * @return Hamming distance: different character count.
   */
  public static 
  int getHammingDistance(String s1, String s2) 
  {
    if(s1 == null && s2 == null) {
      return 0;
    }
    if(s1 == null || s1.length() == 0) {
      return s2.length();
    }
    if(s2 == null || s2.length() == 0) {
      return s1.length();
    }
    
    int len1 = s1.length();
    int len2 = s2.length();
    int minLength = len1 < len2 ? len1 : len2;
    int maxLength = len1 > len2 ? len1 : len2;
    
    int result = 0;
    for(int i = 0; i < minLength; i++) {
      if (s1.charAt(i) != s2.charAt(i)) result++;
    }
    return result + (maxLength - minLength); 
  }
  
  /**
   * Recursive implementation of Levenshtein Distance.
   * 
   * @param s1 String
   * @param s2 String
   * @return Levenshtein Distance
   */
  public static 
  int getLevenshteinDistance(String s1, String s2) 
  {
    if(s1 == null && s2 == null) {
      return 0;
    }
    if(s1 == null || s1.length() == 0) {
      return s2.length();
    }
    if(s2 == null || s2.length() == 0) {
      return s1.length();
    }
    
    int costOfSubstitution = s1.charAt(0) == s2.charAt(0) ? 0 : 1;
    
    int sub = getLevenshteinDistance(s1.substring(1), s2.substring(1)) + costOfSubstitution;
    int ins = getLevenshteinDistance(s1, s2.substring(1)) + 1;
    int del = getLevenshteinDistance(s1.substring(1), s2) + 1;
    
    return min(sub, ins, del);
  }
  
  /**
   * Dynamic programming solution of Levenshtein Distance.
   * 
   * @param s1 String
   * @param s2 String
   * @return Levenshtein Distance
   */
  public static 
  int getLevenshteinDistanceDyn(String s1, String s2) 
  {
    if(s1 == null && s2 == null) {
      return 0;
    }
    if(s1 == null || s1.length() == 0) {
      return s2.length();
    }
    if(s2 == null || s2.length() == 0) {
      return s1.length();
    }
    
    // dynamic programming solution by memoization (which is not memorization)
    int[][] dp = new int[s1.length() + 1][s2.length() + 1];
    
    for (int i = 0; i <= s1.length(); i++) {
      for (int j = 0; j <= s2.length(); j++) {
        if (i == 0) {
          dp[i][j] = j;
        }
        else if (j == 0) {
          dp[i][j] = i;
        }
        else {
          int costOfSubstitution = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
          
          int sub = dp[i - 1][j - 1]  + costOfSubstitution;
          int ins = dp[i - 1][j] + 1;
          int del = dp[i][j - 1] + 1;
          
          dp[i][j] = min(sub, ins, del);
        }
      }
    }
    
    return dp[s1.length()][s2.length()];
  }
  
  private static
  int min(int a, int b, int c) 
  {
    if(a <= b && a <= c) return a;
    if(b <= a && b <= c) return b;
    if(c <= a && c <= b) return c;
    return a;
  }
}