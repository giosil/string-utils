package org.dew.sutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public 
class StringList 
{
  protected List<String> values;
  protected char separator = ',';
  
  public 
  StringList(String values)
  {
    this.values = stringToList(values);
    if(this.values == null) {
      this.values = new ArrayList<String>();
    }
  }
  
  public 
  StringList(String values, char separator)
  {
    this.values = stringToList(values);
    if(this.values == null) {
      this.values = new ArrayList<String>();
    }
    this.separator = separator;
  }
  
  public 
  StringList(List<String> values)
  {
    this.values = values;
    if(this.values == null) {
      this.values = new ArrayList<String>();
    }
  }
  
  public 
  StringList(List<String> values, char separator)
  {
    this.values = values;
    if(this.values == null) {
      this.values = new ArrayList<String>();
    }
    this.separator = separator;
  }
  
  public
  boolean add(String item)
  {
    if(item == null) {
      return false;
    }
    item = item.trim();
    if(item.length() == 0) {
      return false;
    }
    if(values .contains(item)) {
      return false;
    }
    values.add(item);
    return true;
  }
  
  public
  boolean remove(String... items)
  {
    if(items == null || items.length == 0) {
      return false;
    }
    boolean result = false;
    for(int i = 0; i < items.length; i++) {
      String item = items[i];
      if(item == null) continue;
      item = item.trim();
      if(item.length() == 0) continue;
      boolean res = values.remove(item);
      result = result || res;
    }
    return result;
  }
  
  public
  boolean contains(String item)
  {
    if(item == null) {
      return false;
    }
    item = item.trim();
    if(item.length() == 0) {
      return false;
    }
    return values.contains(item);
  }
  
  public
  int indexOf(String item)
  {
    if(item == null) {
      return -1;
    }
    item = item.trim();
    if(item.length() == 0) {
      return -1;
    }
    return values.indexOf(item);
  }
  
  public
  StringList sort()
  {
    Collections.sort(values);
    return this;
  }
  
  public
  StringList reverse()
  {
    Collections.sort(values, Collections.reverseOrder());
    return this;
  }
  
  public 
  String get(int index)
  {
    int idx = index;
    if(index < 0) {
      idx = values.size() + index;
    }
    if(idx < 0 || idx >= values.size()) {
      return null;
    }
    return values.get(idx);
  }
  
  public
  String first()
  {
    if(values.size() == 0) return null;
    String result = values.get(0);
    if(result != null) result = result.trim();
    return result;
  }
  
  public
  String last()
  {
    if(values.size() == 0) return null;
    String result = values.get(values.size() - 1);
    if(result != null) result = result.trim();
    return result;
  }
  
  public
  boolean isEmpty()
  {
    return values.isEmpty();
  }
  
  public
  int size()
  {
    return values.size();
  }
  
  public
  List<String> stringToList(String values)
  {
    if(values == null || values.length() == 0) {
      return new ArrayList<String>(0);
    }
    if(values.startsWith("[") && values.endsWith("]")) {
      values = values.substring(1, values.length()-1);
    }
    List<String> result = new ArrayList<String>();
    int idx = values.indexOf(separator);
    int beg = 0;
    while(idx >= 0) {
      String item = values.substring(beg, idx).trim();
      if(item.length() > 0) {
        result.add(item);
      }
      beg = idx + 1;
      idx = values.indexOf(separator, beg);
    }
    String lastItem = values.substring(beg).trim();
    if(lastItem.length() > 0) {
      result.add(lastItem);
    }
    return result;
  }
  
  @Override
  public boolean equals(Object object) {
    if(object instanceof StringList) {
      return toString().equals(object.toString());
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return toString().hashCode();
  }
  
  @Override
  public
  String toString()
  {
    if(values == null || values.size() == 0) {
      return "";
    }
    String result = "";
    for(int i = 0; i < values.size(); i++) {
      String s = values.get(i);
      if(s == null || s.length() == 0) continue;
      result += separator + s.trim();
    }
    if(result.length() > 0) {
      return result.substring(1);
    }
    return result;
  }
}
