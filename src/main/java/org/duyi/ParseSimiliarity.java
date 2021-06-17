package org.duyi;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public class ParseSimiliarity {
  static String projFile = "proj_all";

  public static void main(String[] args) {

    new ParseSimiliarity().run();

  }

  /**
   * parse similarity
   */
  private void run(){
    try{

      HashMap<Integer, String> all = new HashMap<Integer, String>();
      HashMap<String, String> all2 = new HashMap<String, String>();
      JSONArray output = new JSONArray();
      for(char a = 'A'; a <= 'H'; a ++) {
        for (char b = 'A'; b <= 'H'; b++) {
          all2.put(new StringBuffer().append(a).append(b).toString(), "");
        }
      }
      for(int i = 1999; i < 2020; i ++){
        all.put(i, "");
        JSONArray oneYear = new JSONArray(FileUtils.readFileToString(
          new File("proj_1999_2020_0131"+"/splitProj"+"/"+projFile + i + "_json.json"), "utf-8"));
        HashMap<Character, HashSet<String>> keyset = new HashMap<Character, HashSet<String>>();
        for(char a = 'A'; a <= 'H'; a ++){
          keyset.put(a, new HashSet<String>());
        }

        for(int j = 0; j < oneYear.length(); j ++){
          char code  = oneYear.getJSONObject(j).getString("code1").charAt(0);
          JSONArray keys = oneYear.getJSONObject(j).getJSONArray("keys");

          for(int k = 0; k < keys.length(); k ++){
            keyset.get(code).add(keys.getString(k));
          }
        }

        for(char a = 'A'; a <= 'H'; a ++){
          for(char b = 'A'; b <= 'H'; b ++){
            String sim = String.format("%.3f", calSimiliarity(keyset.get(a), keyset.get(b)));
            all.put(i, all.get(i)+"\n"+a+"\t"+b+"\t"+sim);
            String keyTemp = new StringBuffer().append(a).append(b).toString();
            all2.put(keyTemp, all2.get(keyTemp)+"\n"+a+"\t"+b+"\t"+i+"\t"+sim);
            output.put(new JSONObject().put("key", String.valueOf(a)+String.valueOf(b)+i).put("value", Double.parseDouble(sim)));
          }
        }
        for(char c : keyset.keySet()){
          System.out.print(keyset.get(c).size()+"\t");
        }
        System.out.println();
        }

      for(char a = 'A'; a <= 'H'; a ++){
        for(char b = 'A'; b <= 'H'; b ++) {
          String keyTemp = new StringBuffer().append(a).append(b).toString();
        }}


      FileUtils.writeStringToFile(new File("proj_1999_2020_0131"+"/"+"sim.json"), output.toString(2), "utf-8",false);


    }catch (Exception e){
      e.printStackTrace();
    }
  }


  static double calSimiliarity(HashSet<String> a , HashSet<String> b){
    HashSet<String> c = new HashSet<String>();
    c.addAll(a);
    c.retainAll(b);
    return c.size()/((a.size()+b.size())/2.0);
  }
}
