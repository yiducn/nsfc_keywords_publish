package org.duyi;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public class CalDistribution {

  public static void main( String[] args )
  {
    new CalDistribution().run();
  }
  private void run(){
  try{
    String s = FileUtils.readFileToString(new File(ParseUtils.path+"/"+"proj_all_json.json"), "utf-8");
    JSONArray ja = new JSONArray(s);
    int A = 0, B = 0, C = 0, D = 0, E = 0, F = 0, G = 0, H = 0;
    HashSet<String> keyA = new HashSet<String>();
    HashSet<String> keyB = new HashSet<String>();
    HashSet<String> keyC = new HashSet<String>();
    HashSet<String> keyD = new HashSet<String>();
    HashSet<String> keyE = new HashSet<String>();
    HashSet<String> keyF = new HashSet<String>();
    HashSet<String> keyG = new HashSet<String>();
    HashSet<String> keyH = new HashSet<String>();

    for(int i = 0; i < ja.length(); i ++){
      JSONObject oneProj = ja.getJSONObject(i);
      JSONArray keys = oneProj.getJSONArray("keys");
      if(oneProj.getString("code1").startsWith("A")) {
        A++;
        for(int j = 0; j < keys.length(); j ++){
          keyA.add(keys.getString(j));
        }
      }
      else if(oneProj.getString("code1").startsWith("B")) {
        B++;
        for(int j = 0; j < keys.length(); j ++){
          keyB.add(keys.getString(j));
        }
      }
      else if(oneProj.getString("code1").startsWith("C")){
        C ++;
        for(int j = 0; j < keys.length(); j ++){
          keyC.add(keys.getString(j));
        }
      }
      else if(oneProj.getString("code1").startsWith("D")){
        D ++;
        for(int j = 0; j < keys.length(); j ++){
          keyD.add(keys.getString(j));
        }
      }
      else if(oneProj.getString("code1").startsWith("E")){
        E ++;
        for(int j = 0; j < keys.length(); j ++){
          keyE.add(keys.getString(j));
        }
      }
      else if(oneProj.getString("code1").startsWith("F")){
        F ++;
        for(int j = 0; j < keys.length(); j ++){
          keyF.add(keys.getString(j));
        }
      }
      else if(oneProj.getString("code1").startsWith("G")){
        G ++;
        for(int j = 0; j < keys.length(); j ++){
          keyG.add(keys.getString(j));
        }
      }
      else if(oneProj.getString("code1").startsWith("H")){
        H ++;
        for(int j = 0; j < keys.length(); j ++){
          keyH.add(keys.getString(j));
        }
      }
    }
    System.out.println(A +":"+B+":"+C+":"+D+":"+E+":"+F+":"+G+":"+H);
    System.out.println(keyA.size() +":"+keyB.size()+":"+keyC.size()+":"+keyD.size()+":"+keyE.size()+":"+keyF.size()+":"+keyG.size()+":"+keyH.size());

  }catch (Exception e){
    e.printStackTrace();
  }
  }
}
