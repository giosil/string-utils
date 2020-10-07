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

/**
 * A collection of string utilities:
 */
public
class SUtil
{
  /**
   * Left padding.
   * 
   * @param text string
   * @param c character
   * @param length of result
   * @return left padding
   */
  public static
  String lpad(String text, char c, int length)
  {
    if(text == null) text = "";
    int iTextLength = text.length();
    if(iTextLength >= length) return text;
    int diff = length - iTextLength;
    StringBuffer sb = new StringBuffer();
    for(int i = 0; i < diff; i++) sb.append(c);
    sb.append(text);
    return sb.toString();
  }
  
  /**
   * Right padding.
   * 
   * @param text string
   * @param c character
   * @param length of result
   * @return right padding
   */
  public static
  String rpad(String text, char c, int length)
  {
    if(text == null) text = "";
    int iTextLength = text.length();
    if(iTextLength >= length) return text;
    int diff = length - iTextLength;
    StringBuffer sb = new StringBuffer();
    sb.append(text);
    for(int i = 0; i < diff; i++) sb.append(c);
    return sb.toString();
  }
  
  /**
   * Calculate Hamming distance.
   * 
   * @param s1 String
   * @param s2 String
   * @return Hamming distance: different character count.
   */
  public static 
  int getHammingDist(String s1, String s2) 
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
  int getLevenshteinDist(String s1, String s2) 
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
    
    int sub = getLevenshteinDist(s1.substring(1), s2.substring(1)) + costOfSubstitution;
    int ins = getLevenshteinDist(s1, s2.substring(1)) + 1;
    int del = getLevenshteinDist(s1.substring(1), s2) + 1;
    
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
  int getLevenshteinDistDyn(String s1, String s2) 
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
  
  public static
  int indexOfOpenXmlTag(String xml, String tag, boolean ignoreCase) 
  {
    return indexOfOpenXmlTag(xml, tag, ignoreCase, 0);
  }
  
  public static
  int indexOfOpenXmlTag(String xml, String tag, boolean ignoreCase, int start) 
  {
    if(xml == null || xml.length() == 0) {
      return -1;
    }
    if(tag == null || tag.length() == 0) {
      return -1;
    }
    int length = xml.length();
    if(start < 0) start = 0;
    if(length <= start) return -1;
    
    if(ignoreCase) tag = tag.toLowerCase();
    
    boolean isTag     = false;
    boolean isComment = false;
    int     beginTag  = 0;
    StringBuilder sbTag = new StringBuilder();
    for(int i = start; i < length; i++) {
      char c = xml.charAt(i);
      
      if(isComment) {
        if(c == '>' && xml.substring(i-2, i).equals("--")) {
          isComment = false;
        }
        continue;
      }
      else if(c == '-') {
        if(i > 2 && xml.substring(i-3, i).equals("<!-")) {
          isComment = true;
        }
      }
      
      if(c == '<') {
        isTag = true;
        sbTag.setLength(0);
        beginTag = i;
      }
      else if(c == '>') {
        isTag = false;
        
        String  tagName  = ignoreCase ? sbTag.toString().toLowerCase().trim() : sbTag.toString().trim();
        boolean closeTag = tagName.startsWith("/");
        int sepAttribs = tagName.indexOf(' ');
        if(sepAttribs > 0) {
          tagName = tagName.substring(0, sepAttribs);
        }
        int sepNamespace = tagName.indexOf(':');
        if(sepNamespace >= 0) {
          tagName = tagName.substring(sepNamespace + 1);
        }
        
        if(tagName.equals(tag)) {
          if(!closeTag) {
            return beginTag;
          }
        }
      }
      else if(isTag) {
        sbTag.append(c);
      }
    }
    return -1;
  }
  
  public static
  int indexOfCloseXmlTag(String xml, String tag, boolean ignoreCase) 
  {
    return indexOfCloseXmlTag(xml, tag, ignoreCase, 0);
  }
  
  public static
  int indexOfCloseXmlTag(String xml, String tag, boolean ignoreCase, int start) 
  {
    if(xml == null || xml.length() == 0) {
      return -1;
    }
    if(tag == null || tag.length() == 0) {
      return -1;
    }
    int length = xml.length();
    if(start < 0) start = 0;
    if(length <= start) return -1;
    
    if(ignoreCase) tag = tag.toLowerCase();
    
    boolean isTag     = false;
    boolean isComment = false;
    int     beginTag  = 0;
    StringBuilder sbTag = new StringBuilder();
    for(int i = 0; i < length; i++) {
      char c = xml.charAt(i);
      
      if(isComment) {
        if(c == '>' && xml.substring(i-2, i).equals("--")) {
          isComment = false;
        }
        continue;
      }
      else if(c == '-') {
        if(i > 2 && xml.substring(i-3, i).equals("<!-")) {
          isComment = true;
        }
      }
      
      if(c == '<') {
        isTag = true;
        sbTag.setLength(0);
        beginTag = i;
      }
      else if(c == '>') {
        isTag = false;
        
        String  tagName  = ignoreCase ? sbTag.toString().toLowerCase().trim() : sbTag.toString().trim();
        boolean closeTag = tagName.startsWith("/");
        int sepAttribs = tagName.indexOf(' ');
        if(sepAttribs > 0) {
          tagName = tagName.substring(0, sepAttribs);
        }
        int sepNamespace = tagName.indexOf(':');
        if(sepNamespace >= 0) {
          tagName = tagName.substring(sepNamespace + 1);
        }
        
        if(tagName.equals(tag)) {
          if(closeTag) {
            return beginTag;
          }
        }
      }
      else if(isTag) {
        sbTag.append(c);
      }
    }
    return -1;
  }
  
  public static
  String getXmlVal(String xml, String tag) 
  {
    return getXmlVal(xml, tag, false, 0, null);
  }
  
  public static
  String getXmlVal(String xml, String tag, String defaultValue) 
  {
    return getXmlVal(xml, tag, false, 0, defaultValue);
  }
  
  public static
  String getXmlVal(String xml, String tag, boolean ignoreCase, String defaultValue) 
  {
    return getXmlVal(xml, tag, ignoreCase, 0, defaultValue);
  }
  
  public static
  String getXmlVal(String xml, String tag, boolean ignoreCase, int start, String defaultValue) 
  {
    if(xml == null || xml.length() == 0) {
      return defaultValue;
    }
    if(tag == null || tag.length() == 0) {
      return defaultValue;
    }
    int length = xml.length();
    if(start < 0) start = 0;
    if(length <= start) {
      return defaultValue;
    }
    
    if(ignoreCase) tag = tag.toLowerCase();
    
    boolean isTag     = false;
    boolean isComment = false;
    boolean tagFound  = false;
    int endOpenTag    = -1;
    int begCloseTag   = -1;
    StringBuilder sbTag = new StringBuilder();
    for(int i = 0; i < length; i++) {
      char c = xml.charAt(i);
      
      if(isComment) {
        if(c == '>' && xml.substring(i-2, i).equals("--")) {
          isComment = false;
        }
        continue;
      }
      else if(c == '-') {
        if(i > 2 && xml.substring(i-3, i).equals("<!-")) {
          isComment = true;
        }
      }
      
      if(c == '<') {
        isTag = true;
        sbTag.setLength(0);
        begCloseTag = i;
      }
      else if(c == '>') {
        isTag = false;
        
        String  tagName  = ignoreCase ? sbTag.toString().toLowerCase().trim() : sbTag.toString().trim();
        boolean closeTag = tagName.startsWith("/");
        int sepAttribs = tagName.indexOf(' ');
        if(sepAttribs > 0) {
          tagName = tagName.substring(0, sepAttribs);
        }
        int sepNamespace = tagName.indexOf(':');
        if(sepNamespace >= 0) {
          tagName = tagName.substring(sepNamespace + 1);
        }
        
        if(tagName.equals(tag)) {
          if(closeTag) {
            break;
          }
          else {
            tagFound    = true;
            endOpenTag  = i;
            begCloseTag = -1;
          }
        }
      }
      else if(isTag) {
        sbTag.append(c);
      }
    }
    
    if(!tagFound) return defaultValue;
    if(begCloseTag < endOpenTag) {
      begCloseTag = length;
    }
    
    String value = xml.substring(endOpenTag + 1, begCloseTag);
    if(value.startsWith("<![CDATA[") && value.endsWith("]]>")) {
      return value.substring(9, value.length()-3);
    }
    
    int iInnerOpenTag = value.indexOf('<');
    if(iInnerOpenTag >= 0) {
      int iInnerCloseTag = value.lastIndexOf('>');
      if(iInnerCloseTag > iInnerOpenTag) {
        String s1 = value.substring(0, iInnerOpenTag);
        String s2 = value.substring(iInnerCloseTag + 1);
        value = s1 + s2;
      }
    }
    return value.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&").replace("&quot;", "\"").replace("&apos;", "'");
  }
  
  public static
  String getXmlAttribVal(String xml, String tag, String attribName, String defaultValue) 
  {
    return getXmlAttribVal(xml, tag, attribName, false, 0, defaultValue);
  }
  
  public static
  String getXmlAttribVal(String xml, String tag, String attribName, boolean ignoreCase, String defaultValue) 
  {
    return getXmlAttribVal(xml, tag, attribName, ignoreCase, 0, defaultValue);
  }
  
  public static
  String getXmlAttribVal(String xml, String tag, String attribName, boolean ignoreCase, int start, String defaultValue) 
  {
    if(xml == null || xml.length() == 0) {
      return defaultValue;
    }
    if(tag == null || tag.length() == 0) {
      return defaultValue;
    }
    if(attribName == null || attribName.length() == 0) {
      return defaultValue;
    }
    int length = xml.length();
    if(start < 0) start = 0;
    if(length <= start) {
      return defaultValue;
    }
    
    if(ignoreCase) tag = tag.toLowerCase();
    if(ignoreCase) attribName = attribName.toLowerCase();
    
    boolean isTag     = false;
    boolean isComment = false;
    StringBuilder sbTag = new StringBuilder();
    for(int i = 0; i < length; i++) {
      char c = xml.charAt(i);
      
      if(isComment) {
        if(c == '>' && xml.substring(i-2, i).equals("--")) {
          isComment = false;
        }
        continue;
      }
      else if(c == '-') {
        if(i > 2 && xml.substring(i-3, i).equals("<!-")) {
          isComment = true;
        }
      }
      
      if(c == '<') {
        isTag = true;
        sbTag.setLength(0);
      }
      else if(c == '>') {
        isTag = false;
        
        String  tagName  = ignoreCase ? sbTag.toString().toLowerCase().trim() : sbTag.toString().trim();
        boolean closeTag = tagName.startsWith("/");
        int sepAttribs = tagName.indexOf(' ');
        if(sepAttribs > 0) {
          tagName = tagName.substring(0, sepAttribs);
        }
        int sepNamespace = tagName.indexOf(':');
        if(sepNamespace >= 0) {
          tagName = tagName.substring(sepNamespace + 1);
        }
        
        if(tagName.equals(tag)) {
          if(!closeTag) {
            String tagWithAttribs = ignoreCase ? sbTag.toString().toLowerCase().trim() : sbTag.toString().trim();
            int[] valueBounds = getValueBoundsFromKeyValuePair(tagWithAttribs, attribName, '=');
            if(valueBounds != null && valueBounds.length > 1 && valueBounds[0] >= 0) {
              return sbTag.toString().trim().substring(valueBounds[0], valueBounds[1]);
            }
          }
        }
      }
      else if(isTag) {
        sbTag.append(c);
      }
    }
    
    return defaultValue;
  }
  
  public static
  String getValueFromKeyValuePair(String text, String key, char sep, String defaultValue)
  {
    int[] valueBounds = getValueBoundsFromKeyValuePair(text, key, sep);
    if(valueBounds == null || valueBounds.length < 2 || valueBounds[0] < 0) {
      return defaultValue;
    }
    return text.substring(valueBounds[0], valueBounds[1]);
  }
  
  public static
  int[] getValueBoundsFromKeyValuePair(String text, String key, char sep)
  {
    if(text == null || text.length() == 0) {
      return new int[] {-1, -1};
    }
    if(key == null || key.length() == 0) {
      return new int[] {-1, -1};
    }
    
    int keyLength  = key.length();
    int textLength = text.length();
    int begValue   = -1;
    int endValue   = -1;
    int currIdx    = 0;
    while(true) {
      currIdx = text.indexOf(key, currIdx);
      if(currIdx < 0) break;
      
      if(currIdx > 0) {
        char c_1 = text.charAt(currIdx - 1);
        if(c_1 > 32) {
          currIdx += keyLength;
          if(currIdx >= textLength) break;
          continue;
        }
      }
      
      int s = currIdx + keyLength;
      if(s < textLength) {
        
        // Find sep
        int idxSep = -1;
        for(int j = s; j < textLength; j++) {
          char c = text.charAt(j);
          if(c == sep) {
            idxSep = j;
            break;
          }
          if(c < 33) continue;
          break;
        }
        
        if(idxSep < 0) {
          currIdx += keyLength;
          if(currIdx >= textLength) break;
          continue;
        }
        
        // Find value
        char wrapp = ' ';
        boolean isValue = false;
        for(int j = idxSep + 1; j < textLength; j++) {
          char c = text.charAt(j);
          if(c < 33 && !isValue) continue;
          if(!isValue) {
            isValue = true;
            if(c == '"' || c == '\'') {
              wrapp = c;
              begValue = j + 1;
            }
            else {
              begValue = j;
            }
          }
          else if(c == wrapp) {
            if(text.charAt(j - 1) == '\\') {
              continue;
            }
            endValue = j;
            break;
          }
        }
        
        if(begValue > 0 && endValue >= begValue) {
          break;
        }
        currIdx += keyLength;
        if(currIdx >= textLength) break;
      }
    }
    if(begValue >= 0 && endValue >= begValue) {
      return new int[] {begValue, endValue};
    }
    return new int[] {-1, -1};
  }
  
  public static
  String toEscapedText(String text, String sDefault)
  {
    if(text == null) return sDefault;
    int iLength = text.length();
    if(iLength == 0) return "";
    StringBuilder sb = new StringBuilder(iLength);
    for(int i = 0; i < iLength; i++) {
      char c = text.charAt(i);
      if(c == '<')  sb.append("&lt;");   else
      if(c == '>')  sb.append("&gt;");   else
      if(c == '&')  sb.append("&amp;");  else
      if(c == '"')  sb.append("&quot;"); else
      if(c == '\'') sb.append("&apos;"); else
      if(c > 127) {
        int code = (int) c;
        sb.append("&#" + code + ";");
      }
      else {
        sb.append(c);
      }
    }
    return sb.toString();
  }
  
  public static
  String toHTMLText(String text, String sDefault)
  {
    if(text == null) return sDefault;
    int iLength = text.length();
    if(iLength == 0) return "";
    StringBuilder sb = new StringBuilder(iLength);
    for(int i = 0; i < iLength; i++) {
      char c = text.charAt(i);
      switch (c) {
        case    '<':   sb.append("&lt;");     break;
        case    '>':   sb.append("&gt;");     break;
        case    '&':   sb.append("&amp;");    break;
        case '\300':   sb.append("&Agrave;"); break;
        case '\310':   sb.append("&Egrave;"); break;
        case '\314':   sb.append("&Igrave;"); break;
        case '\322':   sb.append("&Ograve;"); break;
        case '\331':   sb.append("&Ugrave;"); break;
        case '\301':   sb.append("&Aacute;"); break;
        case '\311':   sb.append("&Eacute;"); break;
        case '\315':   sb.append("&Iacute;"); break;
        case '\323':   sb.append("&Oacute;"); break;
        case '\332':   sb.append("&Uacute;"); break;
        case '\340':   sb.append("&agrave;"); break;
        case '\350':   sb.append("&egrave;"); break;
        case '\354':   sb.append("&igrave;"); break;
        case '\362':   sb.append("&ograve;"); break;
        case '\371':   sb.append("&ugrave;"); break;
        case '\341':   sb.append("&aacute;"); break;
        case '\351':   sb.append("&eacute;"); break;
        case '\355':   sb.append("&iacute;"); break;
        case '\363':   sb.append("&oacute;"); break;
        case '\372':   sb.append("&uacute;"); break;
        case '\347':   sb.append("&ccedil;"); break;
        case '\307':   sb.append("&Ccedil;"); break;
        case '\252':   sb.append("&ordf;");   break;
        case '\260':   sb.append("&deg;");    break;
        case '\u20ac': sb.append("&euro;");   break;
        default: {
          if(c < 128) {
            sb.append(c);
          }
          else {
            int code = (int) c;
            sb.append("&#" + code + ";");
          }
        }
      }
    }
    return sb.toString();
  }
  
  public static
  String removeHtmlTags(String sText)
  {
    StringBuilder sbResult = new StringBuilder();
    if(sText == null || sText.length() == 0) {
      return sbResult.toString();
    }
    boolean boIsTag = false;
    int iTextLength = sText.length();
    int iStartTag = -1;
    for(int i = 0; i < iTextLength; i++) {
      char c = sText.charAt(i);
      if(c == '<' && i < iTextLength - 1) {
        char c1 = sText.charAt(i + 1);
        boIsTag = (Character.isLetter(c1) || c1 == '/') && sText.indexOf('>', i) > 0;
        if(boIsTag) iStartTag = i;
      }
      if(c == '>') {
        boIsTag = false;
        if(iStartTag >= 0) {
          String sTag = sText.substring(iStartTag, i + 1);
          if(sTag.startsWith("<br") || sTag.startsWith("<BR")) sbResult.append('\n'); else
          if(sTag.equalsIgnoreCase("</P>")) sbResult.append('\n');
        }
      }
      if(!boIsTag && c != '>') {
        if(c == '&') {
          int iEnd = sText.indexOf(';', i);
          if(iEnd > 0 && iEnd - i <= 8) {
            String sSeq = sText.substring(i, iEnd + 1);
            if(sSeq.equalsIgnoreCase("&agrave;"))      sbResult.append('\340');
            else if(sSeq.equalsIgnoreCase("&egrave;")) sbResult.append('\350');
            else if(sSeq.equalsIgnoreCase("&igrave;")) sbResult.append('\354');
            else if(sSeq.equalsIgnoreCase("&ograve;")) sbResult.append('\362');
            else if(sSeq.equalsIgnoreCase("&ugrave;")) sbResult.append('\371');
            else if(sSeq.equalsIgnoreCase("&aacute;")) sbResult.append('\341');
            else if(sSeq.equalsIgnoreCase("&eacute;")) sbResult.append('\351');
            else if(sSeq.equalsIgnoreCase("&iacute;")) sbResult.append('\355');
            else if(sSeq.equalsIgnoreCase("&oacute;")) sbResult.append('\363');
            else if(sSeq.equalsIgnoreCase("&uacute;")) sbResult.append('\372');
            else if(sSeq.equalsIgnoreCase("&ccedil;")) sbResult.append('\347');
            else if(sSeq.equalsIgnoreCase("&Ccedil;")) sbResult.append('\307');
            else if(sSeq.equalsIgnoreCase("&ordf;"))   sbResult.append('\252');
            else if(sSeq.equalsIgnoreCase("&deg;"))    sbResult.append('\260');
            else if(sSeq.equalsIgnoreCase("&euro;"))   sbResult.append('\u20ac');
            else if(sSeq.equalsIgnoreCase("&gt;"))     sbResult.append('>');
            else if(sSeq.equalsIgnoreCase("&lt;"))     sbResult.append('<');
            i = iEnd;
          }
          else {
            sbResult.append(c);
          }
        }
        else {
          sbResult.append(c);
        }
      }
    }
    return sbResult.toString();
  }
  
  public static
  String denormalizeText(String sText)
  {
    if(sText == null || sText.length() <= 1) return sText;
    // i < sText.length() - 1 because String s = sText.substring(i, i + 2);
    boolean boReplaced = false;
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < sText.length() - 1; i++) {
      char c = sText.charAt(i);
      String s = sText.substring(i, i + 2);
      boReplaced = true;
      if(s.equals("a'")) sb.append('\340');
      else if(s.equals("e'")) sb.append('\350');
      else if(s.equals("i'")) sb.append('\354');
      else if(s.equals("o'")) {
        if(i > 1 && !sText.substring(i-2,i).equalsIgnoreCase(" p") && !sText.substring(i-2,i).equalsIgnoreCase("\np")) {
          sb.append('\362');
        }
        else {
          sb.append("o'");
        }
      }
      else if(s.equals("u'")) sb.append('\371');
      else if(s.equals("a`")) sb.append('\341');
      else if(s.equals("e`")) sb.append('\351');
      else if(s.equals("i`")) sb.append('\355');
      else if(s.equals("o`")) sb.append('\363');
      else if(s.equals("u`")) sb.append('\372');
      else if(s.equals("c,")) sb.append('\347');
      else if(s.equals("A'")) sb.append('\300');
      else if(s.equals("E'")) sb.append('\310');
      else if(s.equals("I'")) sb.append('\314');
      else if(s.equals("O'")) {
        if(i > 1 && !sText.substring(i-2,i).equalsIgnoreCase(" p") && !sText.substring(i-2,i).equalsIgnoreCase("\np")) {
          sb.append('\322');
        }
        else {
          sb.append("O'");
        }
      }
      else if(s.equals("U'")) sb.append('\331');
      else if(s.equals("C,")) sb.append('\307');
      else if(s.equals("A`")) sb.append('\301');
      else if(s.equals("E`")) sb.append('\311');
      else if(s.equals("I`")) sb.append('\315');
      else if(s.equals("O`")) sb.append('\323');
      else if(s.equals("U`")) sb.append('\332'); {
        sb.append(c);
        boReplaced = false;
      }
      if(boReplaced) i++;
    }
    char cLast = sText.charAt(sText.length() - 1);
    if(cLast != '\'' && cLast != '`') sb.append(cLast);
    return sb.toString();
  }
  
  public static
  String normalizeText(String sText)
  {
    if(sText == null || sText.length() == 0) return sText;
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < sText.length(); i++) {
      char c = sText.charAt(i);
      if(c == '\340') sb.append("a'");
      else if(c == '\350') sb.append("e'");
      else if(c == '\354') sb.append("i'");
      else if(c == '\362') sb.append("o'");
      else if(c == '\371') sb.append("u'");
      else if(c == '\341') sb.append("a`");
      else if(c == '\351') sb.append("e`");
      else if(c == '\355') sb.append("i`");
      else if(c == '\363') sb.append("o`");
      else if(c == '\372') sb.append("u`");
      else if(c == '\347') sb.append("c,");
      else if(c == '\300') sb.append("A'");
      else if(c == '\310') sb.append("E'");
      else if(c == '\314') sb.append("I'");
      else if(c == '\322') sb.append("O'");
      else if(c == '\331') sb.append("U'");
      else if(c == '\301') sb.append("A`");
      else if(c == '\311') sb.append("E`");
      else if(c == '\315') sb.append("I`");
      else if(c == '\323') sb.append("O`");
      else if(c == '\332') sb.append("U`");
      else if(c == '\332') sb.append("U`");
      else if(c == '\307') sb.append("C,");
      else if(c > 127) sb.append(" ");
      else sb.append(c);
    }
    return sb.toString();
  }
  
  public static
  String wrap(String text, int cols)
  {
    if(text == null || text.length() == 0) return text;
    StringBuilder sb = new StringBuilder();
    int iLastBreak = 0;
    int iTextLenth = text.length();
    for(int i = 0; i < iTextLenth; i++) {
      char c = text.charAt(i);
      if(c == ' ' || c == '.' || c == ',' || c == ';' || c == ':') {
        sb.append(c);
        if(iTextLenth > i + 1) {
          char cNext = text.charAt(i + 1);
          if(cNext == ' ') {
            sb.append(' ');
            i++;
          }
        }
        if(i - iLastBreak > cols) {
          int iNextNewLine = text.indexOf('\n', i);
          if(iNextNewLine < 0 || iNextNewLine - i > 3) {
            sb.append('\n');
            iLastBreak = i;
          }
        }
        continue;
      }
      sb.append(c);
      if(c == '\n') {
        iLastBreak = i;
      }
    }
    return sb.toString();
  }
  
  public static
  String getQueryStringParValue(String url, String param)
  {
    if(url == null || url.length() == 0) return null;
    int iStartPar = url.indexOf(param + "=");
    if(iStartPar < 0) return null;
    int iEndPar = url.indexOf('&', iStartPar);
    if(iEndPar < 0) iEndPar = url.length();
    int iStartValue = iStartPar + (param + "=").length();
    return url.substring(iStartValue, iEndPar);
  }
  
  public static
  String getJSONVal(String jsonObject, String key, String defaultValue)
  {
    if(jsonObject == null || jsonObject.length() == 0 || jsonObject.indexOf('{') < 0) {
      return defaultValue;
    }
    if(key == null || key.length() == 0) {
      return defaultValue;
    }
    // Find key
    int k = jsonObject.indexOf("\"" + key + "\"");
    if(k < 0) {
      return defaultValue;
    }
    // Separator key-value
    int s = jsonObject.indexOf(':', k + key.length() + 2);
    if(s < 0) {
      return defaultValue;
    }
    // Find end value
    int  e = -1;
    // Previous char
    char cp = '\0';
    // String delimiter char
    char cs = '\0';
    // Is string?
    boolean jsonString = false;
    for(int i = s; i < jsonObject.length() - 1; i++) {
      char c = jsonObject.charAt(i);
      if(c == '"' && cp != '\\') {
        if(jsonString) {
          if(cs == '"') {
            // End string
            jsonString = false;
          }
        }
        else {
          // Start string
          jsonString = true;
          cs = '"';
        }
      }
      else if(c == '\'' && cp != '\\') {
        if(jsonString) {
          if(cs == '\'') {
            // End string
            jsonString = false;
          }
        }
        else {
          // Start string
          jsonString = true;
          cs = '\'';
        }
      }
      else if(c == ',' || c == '}') {
        if(!jsonString) {
          // End value found
          e = i;
          break;
        }
      }
      // Set previous char
      cp = c;
    }
    if(e < 0) {
      e = jsonObject.length();
    }
    String value = jsonObject.substring(s + 1, e).trim();
    if(value.equals("null")) {
      return defaultValue;
    }
    // Strip string
    if(value.startsWith("\"") && value.endsWith("\"")) {
      value = value.substring(1, value.length() - 1);
    }
    else if(value.startsWith("'") && value.endsWith("'")) {
      value = value.substring(1, value.length() - 1);
    }
    // Replace escape characters
    return value.replace("\\n", "\n").replace("\\t", "\t");
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