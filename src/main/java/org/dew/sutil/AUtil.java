package org.dew.sutil;

/**
 * A collection of byte arrays utilities.
 */
public class AUtil {
  
  public static 
  int byteArrayIndexOf(byte[] source, byte[] target) 
  {
    return byteArrayIndexOf(source, 0, source.length, target, 0, target.length, 0);
  }
  
  public static 
  int byteArrayIndexOf(byte[] source, byte[] target, int fromIndex) 
  {
    return byteArrayIndexOf(source, 0, source.length, target, 0, target.length, fromIndex);
  }
  
  public static 
  int byteArrayIndexOf(byte[] source, byte target) 
  {
    for(int i = 0; i < source.length; i++) {
      if(source[i] == target) return i;
    }
    return -1;
  }
  
  public static 
  int byteArrayIndexOf(byte[] source, byte target, int fromIndex) 
  {
    if(fromIndex < 0 || fromIndex >= source.length) {
      return -1;
    }
    for(int i = fromIndex; i < source.length; i++) {
      if(source[i] == target) return i;
    }
    return -1;
  }
  
  // Preso dalla classe String e riadattato (char[] -> byte[])
  public static 
  int byteArrayIndexOf(byte[] source, int sourceOffset, int sourceCount, byte[] target, int targetOffset, int targetCount, int fromIndex)
  {
    if (fromIndex >= sourceCount) {
      return (targetCount == 0 ? sourceCount : -1);
    }
    if (fromIndex < 0) {
      fromIndex = 0;
    }
    if (targetCount == 0) {
      return fromIndex;
    }
    
    byte first = target[targetOffset];
    int max = sourceOffset + (sourceCount - targetCount);
    
    for (int i = sourceOffset + fromIndex; i <= max; i++) {
      /* Look for first character. */
      if (source[i] != first) {
        while (++i <= max && source[i] != first);
      }
      
      /* Found first character, now look at the rest of v2 */
      if (i <= max) {
        int j = i + 1;
        int end = j + targetCount - 1;
        for (int k = targetOffset + 1; j < end && source[j] == target[k]; j++, k++);
        
        if (j == end) {
          /* Found whole string. */
          return i - sourceOffset;
        }
      }
    }
    return -1;
  }
  
  public static
  int getLast(byte[] request, byte[] target)
  {
    if(target  == null || target.length == 0) return -1;
    if(request == null || request.length < target.length) return -1;
    for(int i = target.length; i <= request.length; i++) {
      boolean match = true;
      for(int j = 0; j < target.length; j++) {
        int x = request.length - i + j;
        if(request[x] != target[j]) {
          match = false;
          break;
        }
      }
      if(match) return request.length - i;
    }
    return -1;
  }
}
