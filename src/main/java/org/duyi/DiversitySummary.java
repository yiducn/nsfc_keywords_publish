package org.duyi;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public class DiversitySummary {
  static String projFile = "proj_all";

  public static void main(String[] args) {

    new DiversitySummary().run();

  }

  /**
   */
  private void run(){
    try{
      JSONArray accumulated = new JSONArray(FileUtils.readFileToString(
        new File("proj_1999_2020_0131" + "/key_dist_filter_v2" + "/key_dist_filter_json_50_2019.json"), "utf-8"));

      JSONArray[] yearly = new JSONArray[21];
      for(int i = 1999; i <= 2019; i ++){
        yearly[i-1999] = new JSONArray(FileUtils.readFileToString(
          new File("proj_1999_2020_0131" + "/key_dist_filter_v3" + "/key_dist_filter_json_50_"+i +".json"), "utf-8"));

      }

      StringBuffer sb = new StringBuffer("");
      for(int i = 0 ; i < accumulated.length(); i ++){
        JSONObject accOne = accumulated.getJSONObject(i);
        String key = accOne.getString("key");
        sb.append(key +"\t");
        int sum = accOne.getInt("all");
        sb.append(sum +"\t");
        String div = "";
        for(int j = 0; j <21; j ++){
          JSONObject one = parseJSONObject(yearly[j], key);
          if(one == null){
            div += " \t";
          }else {
            div += (one.getDouble("diversity1") + "\t");
          }
        }

        for(int j = 0; j <21; j ++){
          JSONObject one = parseJSONObject(yearly[j], key);
          if(one == null){
            div += " \t";
          }else {
            div += (one.getInt("all") + "\t");
          }
        }

        sb.append(div +"\t");

        String dist = "";
        for(char a = 'A'; a <= 'H'; a ++){
          dist += (accOne.getInt(String.valueOf(a)) + "\t");
        }
        sb.append(dist +"\n");
      }

      FileUtils.writeStringToFile(new File("proj_1999_2020_0131" + "/" + "sum_50.csv"), sb.toString(), "utf-8", false);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  private JSONObject parseJSONObject(JSONArray array, String key){
    for(int i = 0; i < array.length(); i ++){
      if(array.getJSONObject(i).getString("key").equals(key)){
        return array.getJSONObject(i);
      }
    }
    return null;
  }

}
