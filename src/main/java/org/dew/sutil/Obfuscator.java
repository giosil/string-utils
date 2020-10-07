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
 * Utility for encrypt and decrypt a text.
 *
 * @author <a href="mailto:giorgio.silvestris@gmail.com">Giorgio Silvestris</a>
 * @version 1.0
 */
public
class Obfuscator
{
  /**
   * Encrypt a text.
   *
   * @param sText String
   * @return String
   */
  public static
  String encrypt(String sText)
  {
    if(sText == null) {
      return null;
    }
    
    // The key can contain characters that belong to the set
    // [32 (space) - 95 (_)]
    String sKey = "@C=:F?['X;F)<=B<C?@):D';=@B<?C.)H:'%=?J)W<=(;?@>:Y";
    
    int k = 0;
    StringBuffer sb = new StringBuffer(sText.length());
    for(int i = 0; i < sText.length(); i++) {
      if(k >= sKey.length() - 1) {
        k = 0;
      }
      else {
        k++;
      }
      
      int c = sText.charAt(i);
      int d = sKey.charAt(k);
      
      int r = c;
      if(c >= 32 && c <= 126) {
        r = r - d;
        if(r < 32) {
          r = 127 + r - 32;
        }
      }
      
      sb.append((char) r);
    }
    
    return sb.toString();
  }

  /**
   * Dencrypt a text.
   *
   * @param sText String
   * @return String
   */
  public static
  String decrypt(String sText)
  {
    if(sText == null) {
      return null;
    }
    
    // The key can contain characters that belong to the set
    // [32 (space) - 95 (_)]
    String sKey = "@C=:F?['X;F)<=B<C?@):D';=@B<?C.)H:'%=?J)W<=(;?@>:Y";
    
    int k = 0;
    StringBuffer sb = new StringBuffer(sText.length());
    for(int i = 0; i < sText.length(); i++) {
      if(k >= sKey.length() - 1) {
        k = 0;
      }
      else {
        k++;
      }
      
      int c = sText.charAt(i);
      int d = sKey.charAt(k);
      
      int r = c;
      if(c >= 32 && c <= 126) {
        r = r + d;
        if(r > 126) {
          r = 31 + r - 126;
        }
      }
      
      sb.append((char) r);
    }
    
    return sb.toString();
  }
}
