package org.duyi;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class CalCase {
  public static String path = "proj_38";

  public static void main(String[] args) {
//    new CalCase().run();
    new CalCase().caseDistribution("石墨烯");
  }

  /**
   * @param selectedCase
   */
  private void caseDistribution(String selectedCase){
    try{
      StringBuffer sb = new StringBuffer("");
      JSONArray[] yearly = new JSONArray[21];
      for(int i = 1999; i <= 2019; i ++){
        yearly[i-1999] = new JSONArray(FileUtils.readFileToString(
          new File("proj_1999_2020_0131" + "/key_dist_filter_v3" + "/key_dist_filter_json_500_"+i +".json"), "utf-8"));

        boolean found = false;
        for(int j = 0; j < yearly[i-1999].length(); j ++){
          if(yearly[i-1999].getJSONObject(j).getString("key").equals(selectedCase)){
            found = true;
            for(char k = 'A'; k <= 'H'; k ++) {
              sb.append(selectedCase+"\t"+ i + "\t" + k + "\t");
              sb.append(yearly[i-1999].getJSONObject(j).getInt(String.valueOf(k)) + "\t\n");
            }
            break;
          }
        }
      }

      FileUtils.writeStringToFile(new File("proj_1999_2020_0131" + "/" + "casedistribution.csv"), sb.toString(), "utf-8", false);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void run() {
    /**
     * step 1: read json
     *
     * step n: output projectNo. diversity1； diversity key1-key5; diversity overall
     */

    try {
      //year-> key -> String
      HashMap<Integer, HashMap<String, String>> keySumms = new HashMap<Integer, HashMap<String, String>>();
      {
        for (int year = 1999; year < 2020; year++) {
          HashMap<String, String> keySumm = new HashMap<>();
          JSONArray jaYear = new JSONArray(FileUtils.readFileToString(
            new File("proj_1999_2020_0131" + "/key_dist_filter/" + "key_dist_filter_json_1_"+year+".json"), "utf-8"));

          int count = 0;
          int count2 = 0;
          for (int i = 0; i < jaYear.length(); i++) {
            JSONObject jo = jaYear.getJSONObject(i);
            keySumm.put(jo.getString("key"), jo.toString());
          }
          keySumms.put(year, keySumm);
        }
      }

      JSONArray jaa = new JSONArray(FileUtils.readFileToString(new File(path + "/" + "proj.json"), "utf-8"));
      StringBuffer sbOut = new StringBuffer();
      for(int projCount = 0; projCount < jaa.length(); projCount ++) {
        JSONObject joo = jaa.getJSONObject(projCount);
        String code1 = joo.getString("code1");
        String code2 = joo.getString("code2");

        sbOut.append(joo.getInt("prj_code")+"\t");
        sbOut.append(code1+"\t"+code2+"\t");

        double diversity1 = calDiversity1(joo.getString("code1"), joo.getString("code2"));
        sbOut.append(diversity1+"\t");
        int years = joo.getInt("year");
        sbOut.append(years+"\t");
        String title = joo.getString("title");
        JSONArray keyss = joo.getJSONArray("keys");

        double divOverall = 0;
        for (int i = 0; i < 5; i++) {
          if(i >= keyss.length()){
            sbOut.append("placeholder" + "\t");
            sbOut.append("placeholder" + "\t");
            sbOut.append("placeholder_diversity" + "\t");
          }else {
            if(keySumms.get(years).get(keyss.getString(i)) == null){
              sbOut.append(keySumms.get(years).get(keyss.getString(i)) + "\t");
              sbOut.append("placeholder" + "\t");
              sbOut.append("placeholder_diversity" + "\t");
            }else {
              sbOut.append(keySumms.get(years).get(keyss.getString(i)) + "\t");
              sbOut.append(new JSONObject(keySumms.get(years).get(keyss.getString(i))).getString("key") + "\t");
              sbOut.append(new JSONObject(keySumms.get(years).get(keyss.getString(i))).getDouble("diversity1") + "\t");
              divOverall += new JSONObject(keySumms.get(years).get(keyss.getString(i))).getDouble("diversity1");
            }
          }
        }
        if(keyss.length() == 0)
          sbOut.append(0 + "\t");
        else
          sbOut.append(divOverall / keyss.length() + "\t");
        sbOut.append(joo.getString("title")+"\t\n");
      }
      FileUtils.writeStringToFile(new File(path + "/" + "diversity.csv"), sbOut.toString(),"utf-8", false);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private double calDiversity1(String code1, String code2) {
    if (code2 == null || code2.isEmpty()) {
      return 0;
    } else if (code2.length() == 7) {
      if (code1.length() >= 5 && code1.substring(0, 5).equals(code2.substring(0, 5))) {
        return 0.25;
      } else if (code1.length() >= 3 && code1.substring(0, 3).equals(code2.substring(0, 3))) {
        return 0.5;
      } else if (code1.length() >= 1 && code1.substring(0, 1).equals(code2.substring(0, 1))) {
        return 0.75;
      } else
        return 1;
    } else if (code2.length() == 5) {
      if (code1.length() >= 5 && code1.substring(0, 5).equals(code2.substring(0, 5))) {
        return 0.25;
      } else if (code1.length() >= 3 && code1.substring(0, 3).equals(code2.substring(0, 3))) {
        return 0.5;
      } else if (code1.length() >= 1 && code1.substring(0, 1).equals(code2.substring(0, 1))) {
        return 0.75;
      } else
        return 1;
    } else if (code2.length() == 3) {
      if (code1.length() >= 3 && code1.substring(0, 3).equals(code2.substring(0, 3))) {
        return 0.5;
      } else if (code1.length() >= 1 && code1.substring(0, 1).equals(code2.substring(0, 1))) {
        return 0.75;
      } else
        return 1;
    } else if (code2.length() == 1) {
      if (code1.substring(0, 1).equals(code2.substring(0, 1))) {
        return 0.75;
      } else
        return 1;
    } else {
      return 1;
    }
  }

}
