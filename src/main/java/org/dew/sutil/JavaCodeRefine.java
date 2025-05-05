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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;

/**
 * Java code beautifier.
 */
public 
class JavaCodeRefine 
{
  private static String license = "";
  static {
//    license += "/**\n";
//    license += " * Licensed under the Apache License, Version 2.0 (the \"License\");\n";
//    license += " * you may not use this file except in compliance with the License.\n";
//    license += " * You may obtain a copy of the License at\n";
//    license += " * \n";
//    license += " *   http://www.apache.org/licenses/LICENSE-2.0\n";
//    license += " * \n";
//    license += " * Unless required by applicable law or agreed to in writing, software\n";
//    license += " * distributed under the License is distributed on an \"AS IS\" BASIS,\n";
//    license += " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n";
//    license += " * See the License for the specific language governing permissions and\n";
//    license += " * limitations under the License.\n";
//    license += " */\n";
  }

  private final static String sSRC_FOLDER = null;

  public static 
  String getLicence() 
  {
    return license;
  }
  
  public static 
  void main(String[] args) 
  {
    String sFileOrFolder = "";
    if(args != null && args.length > 0) {
      sFileOrFolder = args[0];
    }
    if(sFileOrFolder == null || sFileOrFolder.length() < 2) {
      sFileOrFolder = sSRC_FOLDER;
    }
    if(sFileOrFolder == null || sFileOrFolder.length() == 0) {
      System.out.println("Missing source folder or file.");
      return;
    }
    refine(new File(sFileOrFolder));
  }
  
  public static 
  boolean refine(File fileOrFolder)
  {
    try {
      if(fileOrFolder == null)   return false;
      if(!fileOrFolder.exists()) {
        System.out.println(fileOrFolder.getAbsolutePath() + " NOT found");
        return false;
      }
      if(fileOrFolder.isFile()) {
        String sFileName = fileOrFolder.getName();
        if(sFileName.endsWith(".java")) {
          refineFile(fileOrFolder);
          return true;
        }
        return false;
      }
      if(!fileOrFolder.isDirectory()) {
        return false;
      }
      File[] files = fileOrFolder.listFiles();
      if(files == null || files.length == 0) return false;
      for(int i = 0; i < files.length; i++) {
        File file = files[i];
        if(file.isDirectory()) {
          refine(file);
          continue;
        }
        String sFileName = file.getName();
        if(sFileName.endsWith(".java")) {
          refineFile(file);
        }
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
      return false;
    }
    return true;
  }
  
  public static 
  boolean refineFile(File file) 
  {
    int    iLine  = 0;
    String sLine  = null;
    File fileTemp = null;
    BufferedReader br = null;
    PrintStream    ps = null;
    try {
      fileTemp = new File(file.getAbsolutePath() + ".tmp");
                  
      int iCurrIndent = 0;
      int iPrevIndent = 0;
      boolean boReplaceFileHeader  = true;
      boolean boSingleLineComment  = false;
      boolean boMultiLineComment   = false;
      boolean boStringLiteral      = false;
      boolean boBeginCode          = false;
      boolean boCurrLineIsEmpty    = false;
      boolean boPrevLineIsEmpty    = false;
      boolean boPrevLineIncomplete = false;
      boolean boJavaDocIndent      = false;
      
      char o = '\0'; // prev prev
      char p = '\0'; // prev
      char n = '\0'; // next
      char cStartLiteral = '\0';
      
      ps = new PrintStream(fileTemp);
      if(license != null && license.length() > 0) {
        ps.print(license);
      }
      else {
        boReplaceFileHeader = false;
      }
      
      br = new BufferedReader(new FileReader(file));
      while((sLine = br.readLine()) != null) {
        if(boReplaceFileHeader) {
          if(sLine.trim().length() == 0) continue;
          if(sLine.startsWith("/*")) continue;
          if(sLine.startsWith(" *")) continue;
          if(sLine.startsWith("*"))  continue;
          boReplaceFileHeader = false;
        }
        boBeginCode         = false;
        boSingleLineComment = false;
        boJavaDocIndent     = false;
        boStringLiteral     = false;
        cStartLiteral       = '\0';
        
        iLine++;
        int iLineLength = sLine.length();
        StringBuffer sbCode = new StringBuffer(iLineLength);
        for(int i = 0; i < iLineLength; i++) {
          char c = sLine.charAt(i);
          if(i < iLineLength - 1) {
            n = sLine.charAt(i + 1);
          }
          else {
            n = '\0';
          }
          if(!boBeginCode && (c == ' ' || c == '\t')) {
            o = p;
            p = c;
            continue;
          }
          boBeginCode = true;
          if(c == '"' || c == '\'') {
            if(!boSingleLineComment && !boMultiLineComment) {
              if(boStringLiteral && p == '\\' &&  o != '\\') {
                boStringLiteral = true;
              }
              else 
              if(!boStringLiteral || c == cStartLiteral) {
                if(boStringLiteral) {
                  boStringLiteral = false;
                  cStartLiteral   = '\0';
                }
                else {
                  boStringLiteral = true;
                  cStartLiteral   = c;
                }
              }
            }
          }
          else if(c == '/') {
            if(p == '/') { // "//" -> boSingleLineComment = true
              if(!boStringLiteral && !boSingleLineComment && !boMultiLineComment) {
                boSingleLineComment = true;
              }
            }
            else if(p == '*') {  // "*/" -> boMultiLineComment=false
              if(!boStringLiteral && !boSingleLineComment) {
                boMultiLineComment = false;
              }
            }
          }
          else if(c == '*') {
            if(p == '/') { // "/*" -> boMultiLineComment = true
              if(!boStringLiteral && !boSingleLineComment && !boMultiLineComment) {
                boMultiLineComment = true;
              }
            }
            else if(p == ' ' || p == '\t') { // " *" -> boJavaDocIndent=true
              boJavaDocIndent = true;
            }
          }
          else if(c == '{') {
            if(!boStringLiteral && !boSingleLineComment && !boMultiLineComment) {
              iCurrIndent++;
              if(p == ')') sbCode.append(' '); // if(x > 0){ -> if(x > 0) {
              if(p == 'e') sbCode.append(' '); // else{ -> else {
              if(p == '=') sbCode.append(' '); // int[] x ={}; -> int[] x = {};
            }
          }
          else if(c == '}') {
            if(!boStringLiteral && !boSingleLineComment && !boMultiLineComment) {
              iCurrIndent--;
              iPrevIndent = iCurrIndent;
            }
          }
          else if(c == ' ') {
            if(!boStringLiteral && !boSingleLineComment && !boMultiLineComment) {
              if(n == '(' && p != '=' && p != '>' && p != '<' && p != 'n') {
                o = p;
                p = c;
                continue; // if ( -> if( , try ( -> try(
              }
              if(n == ';') {
                o = p;
                p = c;
                continue; // return o ; -> return o;
              }
            }
          }
          else if(c == '(') {
            if(!boStringLiteral && !boSingleLineComment && !boMultiLineComment) {
              if(p == '=') sbCode.append(' '); // =( -> = (
              if(p == '>') sbCode.append(' '); // >( -> > (
              if(p == '<') sbCode.append(' '); // <( -> < (
              if(sbCode.toString().endsWith("return")) {
                sbCode.append(' '); // return(String) o; -> < return (String) o;  
              }
            }
          }
          else if(c > 127) {
            if(boSingleLineComment || boMultiLineComment) {
              if(c == '\340') sbCode.append("a'");
              else if(c == '\350') sbCode.append("e'");
              else if(c == '\354') sbCode.append("i'");
              else if(c == '\362') sbCode.append("o'");
              else if(c == '\371') sbCode.append("u'");
              else if(c == '\341') sbCode.append("a'");
              else if(c == '\351') sbCode.append("e'");
              else if(c == '\355') sbCode.append("i'");
              else if(c == '\363') sbCode.append("o'");
              else if(c == '\372') sbCode.append("u'");
              else if(c == '\300') sbCode.append("A'");
              else if(c == '\310') sbCode.append("E'");
              else if(c == '\314') sbCode.append("I'");
              else if(c == '\322') sbCode.append("O'");
              else if(c == '\331') sbCode.append("U'");
              else if(c == '\301') sbCode.append("A'");
              else if(c == '\311') sbCode.append("E'");
              else if(c == '\315') sbCode.append("I'");
              else if(c == '\323') sbCode.append("O'");
              else if(c == '\332') sbCode.append("U'"); 
              else sbCode.append(c);
              o = p;
              p = c;
              continue;
            }
            else if(boStringLiteral) {
              String sOctal = Integer.toString((int) c, 8);
              sbCode.append("\\" + sOctal);
              o = p;
              p = c;
              continue;
            }
          }
          o = p;
          p = c;
          sbCode.append(c);
        }
        String sCode = sbCode.toString().trim();
        StringBuffer sbFormattedLine = new StringBuffer(sCode.length() + 5);
        if(boJavaDocIndent) {
          sbFormattedLine.insert(0, ' ');
        }
        sbFormattedLine.append(sCode);
        
        if(iCurrIndent < 0) iCurrIndent = 0;
        if(iPrevIndent < 0) iPrevIndent = 0;
        
        if(iPrevIndent > 0) {
          char[] acIndent = null;
          if(boPrevLineIncomplete || sCode.startsWith("throws ")) {
            acIndent = new char[iPrevIndent + 1];
          }
          else {
            acIndent = new char[iPrevIndent];
          }
          for(int i = 0; i < acIndent.length; i++) {
            acIndent[i] = '\t';
          }
          sbFormattedLine.insert(0, acIndent);
        }
        
        boCurrLineIsEmpty = sLine.length() == 0;
        if(boPrevLineIsEmpty && boCurrLineIsEmpty) continue;
        ps.println(sbFormattedLine);
        boPrevLineIsEmpty    = boCurrLineIsEmpty;
        if(!boSingleLineComment && !boMultiLineComment)  {
          boPrevLineIncomplete = sLine.endsWith(",") || sLine.endsWith("|") || sLine.endsWith("&") || sLine.endsWith("?") ||
            sLine.endsWith("+") || sLine.endsWith("-") || sLine.endsWith("*") ||
            sLine.endsWith("<") || sLine.endsWith(">") || sLine.endsWith("=") || sLine.endsWith("(");
        }
        else {
          boPrevLineIncomplete = false;
        }
        iPrevIndent = iCurrIndent;
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
      return false;
    }
    finally {
      if(ps != null) try{ ps.close(); } catch(Exception ex) {}
      if(br != null) try{ br.close(); } catch(Exception ex) {}
    }
    if(file.delete()) {
      fileTemp.renameTo(file);
      System.out.println(file.getAbsolutePath() + " (" + iLine + " lines) refined");
    }
    else {
      fileTemp.delete();
      System.out.println(file.getAbsolutePath() + " NOT refined");
      return false;
    }
    return true;
  }
}
