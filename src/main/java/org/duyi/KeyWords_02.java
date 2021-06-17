package org.duyi;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * case study
 *
 */
public class KeyWords_02
{

    public static void main( String[] args )
    {

      int[] filters = {1};
      String projJsonFile = "proj_all_json.json";

      for(int i = filters.length - 1; i >=0;i --) {
        new KeyWords_02().reparseKeyFromAbstATitle(projJsonFile, filters[i]);
      }
    }

  /**
   * @param count
   */
  private void reparseKeyFromAbstATitle(String projJsonFile, int count) {
      try{
        //TODO
        JSONArray jaKeysOld = new JSONArray(FileUtils.readFileToString(new File(ParseUtils.path+"/"+"key_dist_filter_json_"+count+"_2019.json"), "utf-8"));
        JSONArray jaKeys = new JSONArray();

        for(int i = 0; i < jaKeysOld.length(); i ++){
          if(jaKeysOld.getJSONObject(i).getString("key").length() >= 2)
            jaKeys.put(jaKeysOld.getJSONObject(i));
        }

        String[] keys = new String[jaKeys.length()];
        for(int i = 0; i < jaKeys.length(); i ++){
          keys[i] = jaKeys.getJSONObject(i).getString("key");
        }
        Arrays.sort(keys, new Comparator<String>() {
          @Override
          public int compare(String o1, String o2) {
            if (o1.length() == o2.length()) {
              return o1.compareTo(o2);
            } else {
              if (o1.length() > o2.length()) {
                return -1;
              } else {
                return 1;
              }
            }
          }
        });

        StringBuffer sb = new StringBuffer("");
        for(int k = 0; k < keys.length; k ++){
          sb.append(keys[k]+"\n");
        }
        FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+"keys/"+"keywords_"+count+"_sort.csv"), sb.toString(), "utf-8", false);
        System.out.println("key sort completed.");

        JSONArray jaProj = new JSONArray(FileUtils.readFileToString(new File(ParseUtils.path+"/"+projJsonFile), "utf-8"));
        for(int i = 0; i < jaProj.length(); i ++){
          JSONObject oneProj = jaProj.getJSONObject(i);
          String title = oneProj.getString("title");
          JSONArray titleKeys = new JSONArray();
          for(int j = 0; j < title.length(); j ++){
            for(int k = 0; k < keys.length; k ++){
              if(title.substring(j).startsWith(keys[k])){
                j += keys[k].length();
                titleKeys.put(keys[k]);
                k = 0;
              }
            }
          }
          oneProj.remove("title");
          oneProj.put("title_key", titleKeys);
        }
        FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+"keys/"+"proj_addtitleKeys_filter"+count+".json"),
          jaProj.toString(4), "utf-8", false);

        System.out.println("title key complete");
        int countParsed = 0;
        for(int i = 0; i < jaProj.length(); i ++){
          JSONObject oneProj = jaProj.getJSONObject(i);
          String sum = oneProj.getString("sum");
          JSONArray titleKeys = new JSONArray();
          for(int j = 0; j < sum.length(); j ++){
            for(int k = 0; k < keys.length; k ++){
              if(sum.substring(j).startsWith(keys[k])){
                j += keys[k].length();
                titleKeys.put(keys[k]);
                k = 0;
              }
            }
          }
          oneProj.remove("sum");
          oneProj.put("abs_key", titleKeys);
          if(countParsed ++ % 1000 == 0){
            System.out.println("count parsed:" + countParsed);
          }
        }
        System.out.println("title abs complete");
        FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+"keys/"+"proj_addkeys_json_filter"+count+".json"),
          jaProj.toString(4), "utf-8", false);

        }catch (Exception e){
        e.printStackTrace();
      }

    }

  /**
   *
   * @param list
   * @deprecated
   */
  public void analyzeKeys(String[] list){
      try {
        FileUtils.writeStringToFile(new File("casestudy.csv"), "", "utf-8", false);
        FileUtils.writeStringToFile(new File("casestudySum.csv"),"", "utf-8", false);

        JSONArray result = new JSONArray();

        for(String key : list) {
          for (int year = 1999; year < 2020; year++) {
            String s = FileUtils.readFileToString(new File("key_dist_filter_json_1_" + year + ".json"), "utf-8");
            JSONArray ja = new JSONArray(s);
            for(int i = 0; i < ja.length(); i ++){
              if(ja.getJSONObject(i).getString("key").equals(key)){
                String oneS = "";
                oneS += key + "\t" + year + "\t" + "A" + "\t" +ja.getJSONObject(i).getInt("A") + "\n";
                oneS += key + "\t" + year + "\t" + "B" + "\t" +ja.getJSONObject(i).getInt("B") + "\n";
                oneS += key + "\t" + year + "\t" + "C" + "\t" +ja.getJSONObject(i).getInt("C") + "\n";
                oneS += key + "\t" + year + "\t" + "D" + "\t" +ja.getJSONObject(i).getInt("D") + "\n";
                oneS += key + "\t" + year + "\t" + "E" + "\t" +ja.getJSONObject(i).getInt("E") + "\n";
                oneS += key + "\t" + year + "\t" + "F" + "\t" +ja.getJSONObject(i).getInt("F") + "\n";
                oneS += key + "\t" + year + "\t" + "G" + "\t" +ja.getJSONObject(i).getInt("G") + "\n";
                oneS += key + "\t" + year + "\t" + "H" + "\t" +ja.getJSONObject(i).getInt("H") + "\n";
                FileUtils.writeStringToFile(new File("casestudy.csv"), oneS, "utf-8", true);
                FileUtils.writeStringToFile(new File("casestudySum.csv"),
                  (key+"\t"+year+"\t"+ja.getJSONObject(i).getDouble("diversity1")+"\n"), "utf-8", true);
              }
            }
          }
        }
      }catch (Exception e){
        e.printStackTrace();
      }
    }

}
