package org.duyi;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.print.attribute.IntegerSyntax;
import java.io.*;
import java.util.*;


public class App
{
    public static void main( String[] args )
    {

      int[] filters = {1, 10, 20, 50};
      String projJsonFile = "proj_all_json.json";

      new App().interByKeys_v3(ParseUtils.path+"/"+projJsonFile);
      new App().genKeyDiversityByYear_v3(500);

    }

  /**
   * calculate interdisciplinarity of different key terms of different years
   * @param count
   */
  private void genKeyDiversityByYear(int count) {
      try{
        StringBuffer sb = new StringBuffer("");
        for(int i = 1999; i < 2020; i ++){
          JSONArray jaKeysOld = new JSONArray(FileUtils.readFileToString(
            new File(ParseUtils.path+"/"+"key_dist_filter_json_"+count+"_" + i + ".json"), "utf-8"));
          for(int j = 0; j < jaKeysOld.length(); j ++) {
            JSONObject oneKey = jaKeysOld.getJSONObject(j);
            sb.append(i + "\t");
            sb.append(oneKey.getString("key")+"\t");
            sb.append(oneKey.getDouble("diversity1")+"\t");
            int maxValue = oneKey.getInt("A");
            String maxCode = "A";
            for(char k = 'A'; k <='H'; k ++){
              if(maxValue < oneKey.getInt(String.valueOf(k))){
                maxValue = oneKey.getInt(String.valueOf(k));
                maxCode = String.valueOf(k);
              }
            }
            sb.append(maxCode+"\t");
            sb.append(maxValue+"\t\n");
          }
        }
        FileUtils.writeStringToFile(
          new File(ParseUtils.path+"/keyDiversity_"+count+".csv"),
          sb.toString(),"utf-8",false);
      }catch (Exception e){
        e.printStackTrace();
      }
  }

  /**
   * calculate interdisciplinarity of different key terms of different years, with similarity
   * @param count
   */
  private void genKeyDiversityByYear_v2(int count) {
    try{

      StringBuffer sb = new StringBuffer("");
      for(int i = 1999; i < 2020; i ++){
        JSONArray jaKeysOld = new JSONArray(FileUtils.readFileToString(
          new File(ParseUtils.path+"/key_dist_filter_v2/"+"key_dist_filter_json_"+count+"_" + i + ".json"),
          "utf-8"));
        for(int j = 0; j < jaKeysOld.length(); j ++) {
          JSONObject oneKey = jaKeysOld.getJSONObject(j);
          sb.append(i + "\t");
          sb.append(oneKey.getString("key")+"\t");
          sb.append(oneKey.getDouble("diversity1")+"\t");
          int maxValue = oneKey.getInt("A");
          String maxCode = "A";
          for(char k = 'A'; k <='H'; k ++){
            if(maxValue < oneKey.getInt(String.valueOf(k))){
              maxValue = oneKey.getInt(String.valueOf(k));
              maxCode = String.valueOf(k);
            }
          }
          sb.append(maxCode+"\t");
          sb.append(maxValue+"\t\n");
        }
      }
      FileUtils.writeStringToFile(
        new File(ParseUtils.path+"/keyDiversity_sim_"+count+".csv"),
        sb.toString(),"utf-8",false);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  /**
   * calculate interdisciplinarity of different key terms of different years, with similarity
   * @param count
   */
  private void genKeyDiversityByYear_v3(int count) {
    try{

      StringBuffer sb = new StringBuffer("");
      for(int i = 1999; i < 2020; i ++){
        JSONArray jaKeysOld = new JSONArray(FileUtils.readFileToString(
          new File(ParseUtils.path+"/key_dist_filter_v3/"+"key_dist_filter_json_"+count+"_" + i + ".json"),
          "utf-8"));
        for(int j = 0; j < jaKeysOld.length(); j ++) {
          JSONObject oneKey = jaKeysOld.getJSONObject(j);
          sb.append(i + "\t");
          sb.append(oneKey.getString("key")+"\t");
          sb.append(oneKey.getDouble("diversity1")+"\t");
          int maxValue = oneKey.getInt("A");
          String maxCode = "A";
          for(char k = 'A'; k <='H'; k ++){
            if(maxValue < oneKey.getInt(String.valueOf(k))){
              maxValue = oneKey.getInt(String.valueOf(k));
              maxCode = String.valueOf(k);
            }
          }
          sb.append(maxCode+"\t");
          sb.append(maxValue+"\t\n");
        }
      }
      FileUtils.writeStringToFile(
        new File(ParseUtils.path+"/keyDiversity_sim_v3_"+count+".csv"),
        sb.toString(),"utf-8",false);
    }catch (Exception e){
      e.printStackTrace();
    }
  }


  /**
   * extract key trems from abstract and title, count is the count of key terms
   * @param count
   */
  private void reparseKeyFromAbstATitle(String projJsonFile, int count) {
      try{
        JSONArray jaKeysOld = new JSONArray(FileUtils.readFileToString(new File(ParseUtils.path+"/"+"key_dist_filter_json_"+count+"_2019.json"), "utf-8"));
        JSONArray jaKeys = new JSONArray();

        for(int i = 0; i < jaKeysOld.length(); i ++){
          if(jaKeysOld.getJSONObject(i).getString("key").length() >= 3)
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
        FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+"keywords_"+count+"_sort.csv"), sb.toString(), "utf-8", false);
        System.out.println("key sort completed.");

        JSONArray jaProj = new JSONArray(FileUtils.readFileToString(new File(projJsonFile), "utf-8"));
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
          oneProj.append("title_key", titleKeys);
        }
        FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+"proj_addtitleKeys_filter"+count+".json"), jaProj.toString(), "utf-8", false);

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
          oneProj.append("abs_key", titleKeys);
          if(countParsed ++ % 1000 == 0){
            System.out.println("count parsed:" + countParsed);
          }
        }
        System.out.println("title abs complete");
        FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+"proj_addkeys.json_filter"+count+".json"), jaProj.toString(), "utf-8", false);

        }catch (Exception e){
        e.printStackTrace();
      }



    }

  public void analyzeKeys(String[] list){
      try {
        JSONArray result = new JSONArray();

        for(String key : list) {
          for (int year = 1999; year < 2020; year++) {
            String s = FileUtils.readFileToString(new File(ParseUtils.path+"/"+"key_dist_filter_json_50_" + year + ".json"), "utf-8");
            JSONArray ja = new JSONArray(s);
            for(int i = 0; i < ja.length(); i ++){
              if(ja.getJSONObject(i).getString("key").equals(key)){
                String oneS = "";
                oneS += key + "\t" + year + "\t" + "A" + "\t" +ja.getJSONObject(i).getInt("A") + "\t" + ja.getJSONObject(i).getDouble("diversity1") + "\n";
                oneS += key + "\t" + year + "\t" + "B" + "\t" +ja.getJSONObject(i).getInt("B") + "\t" + ja.getJSONObject(i).getDouble("diversity1") + "\n";
                oneS += key + "\t" + year + "\t" + "C" + "\t" +ja.getJSONObject(i).getInt("C") + "\t" + ja.getJSONObject(i).getDouble("diversity1") + "\n";
                oneS += key + "\t" + year + "\t" + "D" + "\t" +ja.getJSONObject(i).getInt("D") + "\t" + ja.getJSONObject(i).getDouble("diversity1") + "\n";
                oneS += key + "\t" + year + "\t" + "E" + "\t" +ja.getJSONObject(i).getInt("E") + "\t" + ja.getJSONObject(i).getDouble("diversity1") + "\n";
                oneS += key + "\t" + year + "\t" + "F" + "\t" +ja.getJSONObject(i).getInt("F") + "\t" + ja.getJSONObject(i).getDouble("diversity1") + "\n";
                oneS += key + "\t" + year + "\t" + "G" + "\t" +ja.getJSONObject(i).getInt("G") + "\t" + ja.getJSONObject(i).getDouble("diversity1") + "\n";
                oneS += key + "\t" + year + "\t" + "H" + "\t" +ja.getJSONObject(i).getInt("H") + "\t" + ja.getJSONObject(i).getDouble("diversity1") + "\n";
                FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+"casestudy.csv"), oneS, "utf-8", true);
                FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+"casestudySum.csv"),
                  (key+"\t"+year+"\t"+ja.getJSONObject(i).getDouble("diversity1")+"\n"), "utf-8", true);
              }
            }
          }
        }
      }catch (Exception e){
        e.printStackTrace();
      }
    }

  /**
   * distribution
   */
  private void outputDistibution() {
      try {
        JSONArray jaProj = new JSONArray(FileUtils.readFileToString(new File(ParseUtils.path+"/"+"proj_diversity.json"), "utf-8"));
        StringBuffer sb = new StringBuffer("");
        for(int i = 0; i < jaProj.length(); i ++){
          String s = "";
          s += jaProj.getJSONObject(i).getInt("year") + "\t";
          s += jaProj.getJSONObject(i).getString("code1") + "\t";
          s += jaProj.getJSONObject(i).getString("code1").substring(0,1) + "\t";
          s += jaProj.getJSONObject(i).getString("code1").substring(0,3) + "\t";
          s += jaProj.getJSONObject(i).getDouble("diversityProj") + "\t";
          s += Math.round(jaProj.getJSONObject(i).getDouble("diversityProj")*100)/100.0 + "\n";
          sb.append(s);
        }
        FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+"distribution.csv"), sb.toString(), "utf-8", false);
      }catch (Exception e){
        e.printStackTrace();
      }
    }


  /**
   * calculate interdisciplinarity by key_dist_filter, and save to proj_diversity.json
   * @param projJsonFile
   * @param filter key terms need to filter
   */
  private void deptInterdisciplinary(String projJsonFile, int filter) {
      try{
        JSONArray jaProj = new JSONArray(FileUtils.readFileToString(new File(ParseUtils.path+"/"+projJsonFile), "utf-8"));

        for (int year = 1999; year < 2020; year++) {

          String s = FileUtils.readFileToString(new File(ParseUtils.path+"/"+ "key_dist_filter_json_"+filter+"_" + year+".json"), "utf-8");
          JSONArray ja = new JSONArray(s);
          HashMap<String, Double> diversityMap = new HashMap<>();
          for(int i = 0; i < ja.length(); i ++){
            diversityMap.put(ja.getJSONObject(i).getString("key"), ja.getJSONObject(i).getDouble("diversity1"));
          }

          System.out.println("year:"+year);
          for(int i = 0; i < jaProj.length(); i ++) {
            JSONObject oneProj = jaProj.getJSONObject(i);
            if (oneProj.getInt("year") != year) {
              continue;
            }

            double diversityProj = 0.0;
            {
              int cal = 0;
              JSONArray keysArray = oneProj.getJSONArray("keys");
              for(int j = 0; j < keysArray.length(); j ++){
                if(!diversityMap.containsKey(keysArray.getString(j))){
                  continue;
                }
                cal ++;
                diversityProj += diversityMap.get(keysArray.getString(j));
              }
              if(cal == 0){
                diversityProj = -1;
              }else {
                diversityProj = diversityProj / cal;
              }
              oneProj.put("inCountKeys", cal);
            }
            oneProj.put("diversityProj", diversityProj);
            oneProj.remove("sum");
          }
        }
        FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+ "proj_diversity"+filter+".json"), jaProj.toString(), "utf-8", false);

      }catch (Exception e){
        e.printStackTrace();
      }
    }


  /**
   * @param projJsonFile
   */
  private void interByKeys(String projJsonFile) {
    HashMap<String, HashMap<String, Integer>> keySumm = new HashMap<>();
    try {

      JSONArray all = new JSONArray(FileUtils.readFileToString(new File("proj_1999_2020_0131"+"/"+"sim.json"), "utf-8"));
      HashMap<String, Double> allHash = new HashMap<String, Double>();
      for(int i = 0; i < allHash.size(); i ++){
        allHash.put(all.getJSONObject(i).getString("key"), all.getJSONObject(i).getDouble("value"));
      }


      HashMap<String, Integer> overallCount = new HashMap<>();
      {
        String s = FileUtils.readFileToString(new File(projJsonFile), "utf-8");
        JSONArray ja = new JSONArray(s);
        for (int i = 0; i < ja.length(); i++) {
          JSONObject jo = ja.getJSONObject(i);
          JSONArray keys = jo.getJSONArray("keys");
          for (int j = 0; j < keys.length(); j++) {
            String key = keys.getString(j);
            if (!overallCount.containsKey(key)) {
              overallCount.put(key, 1);
            }else{
              overallCount.put(key, overallCount.get(key) + 1 );
            }
          }
        }
      }
      StringBuffer sb = new StringBuffer("");
      for(String key: overallCount.keySet()){
        sb.append(key+"\t"+overallCount.get(key)+"\n");
      }

      FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+"key_count_sum"), sb.toString(), "utf-8", false);

      for (int year = 1999; year < 2020; year++) {
        String s = FileUtils.readFileToString(new File(projJsonFile), "utf-8");
        JSONArray ja = new JSONArray(s);
        int count = 0;
        int count2 = 0;
        for (int i = 0; i < ja.length(); i++) {
          JSONObject jo = ja.getJSONObject(i);
          if(jo.getInt("year") != year)
            continue;
          JSONArray keys = jo.getJSONArray("keys");
          for (int j = 0; j < keys.length(); j++) {
            String key = keys.getString(j);
            if (keySumm.get(key) == null || keySumm.get(key).isEmpty()) {
              HashMap<String, Integer> map = new HashMap<>();
              map.put("A", 0);
              map.put("B", 0);
              map.put("C", 0);
              map.put("D", 0);
              map.put("E", 0);
              map.put("F", 0);
              map.put("G", 0);
              map.put("H", 0);
              map.put(jo.getString("code1").substring(0, 1), map.get(jo.getString("code1").substring(0, 1)) + 1);
              keySumm.put(key, map);
            } else {
              HashMap<String, Integer> map = keySumm.get(key);
              map.put(jo.getString("code1").substring(0, 1), map.get(jo.getString("code1").substring(0, 1)) + 1);
            }
          }
        }


        {
          //
          JSONArray filteredArrayl = new JSONArray();
          Iterator it2 = keySumm.entrySet().iterator();
          while (it2.hasNext()) {
            Map.Entry pairs = (Map.Entry) it2.next();
            if(overallCount.get(pairs.getKey().toString()) == 1){
              continue;
            }
            JSONObject oneKey = new JSONObject();
            oneKey.put("key", pairs.getKey());
            HashMap<String, Integer> dist = (HashMap<String, Integer>) pairs.getValue();
            int sum = 0;
            for (String k : dist.keySet()) {
              oneKey.put(k, dist.get(k));
              sum += dist.get(k);
            }
            oneKey.put("all", sum);
            oneKey.put("diversity1", calDiversity(dist));
            filteredArrayl.put(oneKey);

          }
          FileUtils.writeStringToFile(new File(ParseUtils.path + "/"+"key_dist_filter"+"/"+"key_dist_filter_json_1_" + year+".json"),
            filteredArrayl.toString(4), "utf-8", false);
        }

        {
          JSONArray filteredArray10 = new JSONArray();
          Iterator it3 = keySumm.entrySet().iterator();
          while (it3.hasNext()) {
            Map.Entry pairs = (Map.Entry) it3.next();
            if(overallCount.get(pairs.getKey().toString()) < 10){
              continue;
            }

            JSONObject oneKey = new JSONObject();
            oneKey.put("key", pairs.getKey());
            HashMap<String,Integer> dist = (HashMap<String,Integer>)pairs.getValue();
            int sum = 0;
            for(String k : dist.keySet()) {
              oneKey.put(k, dist.get(k));
              sum += dist.get(k);
            }
            oneKey.put("all", sum);
            oneKey.put("diversity1", calDiversity(dist));
            filteredArray10.put(oneKey);

          }

          FileUtils.writeStringToFile(new File(ParseUtils.path + "/"+"key_dist_filter"+"/"+"key_dist_filter_json_10_"+year+".json"),
            filteredArray10.toString(4), "utf-8",false);
        }

        {
          JSONArray filteredArray20 = new JSONArray();
          Iterator it3 = keySumm.entrySet().iterator();
          while (it3.hasNext()) {
            Map.Entry pairs = (Map.Entry) it3.next();
            if(overallCount.get(pairs.getKey().toString()) < 20){
              continue;
            }

            JSONObject oneKey = new JSONObject();
            oneKey.put("key", pairs.getKey());
            HashMap<String,Integer> dist = (HashMap<String,Integer>)pairs.getValue();
            int sum = 0;
            for(String k : dist.keySet()) {
              oneKey.put(k, dist.get(k));
              sum += dist.get(k);
            }
            oneKey.put("all", sum);
            oneKey.put("diversity1", calDiversity(dist));
            filteredArray20.put(oneKey);

          }
          FileUtils.writeStringToFile(new File(ParseUtils.path + "/"+ "key_dist_filter"+"/"+"key_dist_filter_json_20_"+year+".json"),
            filteredArray20.toString(4), "utf-8",false);
        }

        {
          JSONArray filteredArray50 = new JSONArray();
          Iterator it3 = keySumm.entrySet().iterator();
          while (it3.hasNext()) {
            Map.Entry pairs = (Map.Entry) it3.next();
            if(overallCount.get(pairs.getKey().toString()) < 50){
              continue;
            }

            JSONObject oneKey = new JSONObject();
            oneKey.put("key", pairs.getKey());
            HashMap<String,Integer> dist = (HashMap<String,Integer>)pairs.getValue();
            int sum = 0;
            for(String k : dist.keySet()) {
              oneKey.put(k, dist.get(k));
              sum += dist.get(k);
            }
            oneKey.put("all", sum);
            oneKey.put("diversity1", calDiversity(dist));
            filteredArray50.put(oneKey);

          }
          FileUtils.writeStringToFile(new File(ParseUtils.path + "/"+ "key_dist_filter"+"/"+"key_dist_filter_json_50_"+year+".json"),
            filteredArray50.toString(4), "utf-8",false);
        }

      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  /**
   * @param projJsonFile
   */
  private void interByKeys_v2(String projJsonFile) {
    HashMap<String, HashMap<String, Integer>> keySumm = new HashMap<>();
    try {

      JSONArray all = new JSONArray(FileUtils.readFileToString(new File("proj_1999_2020_0131"+"/"+"sim.json"), "utf-8"));
      HashMap<String, Double> allHash = new HashMap<String, Double>();
      for(int i = 0; i < all.length(); i ++){
        allHash.put(all.getJSONObject(i).getString("key"), all.getJSONObject(i).getDouble("value"));
      }

      HashMap<String, Integer> overallCount = new HashMap<>();
      {
        String s = FileUtils.readFileToString(new File(projJsonFile), "utf-8");
        JSONArray ja = new JSONArray(s);
        for (int i = 0; i < ja.length(); i++) {
          JSONObject jo = ja.getJSONObject(i);
          JSONArray keys = jo.getJSONArray("keys");
          for (int j = 0; j < keys.length(); j++) {
            String key = keys.getString(j);
            if (!overallCount.containsKey(key)) {
              overallCount.put(key, 1);
            }else{
              overallCount.put(key, overallCount.get(key) + 1 );
            }
          }
        }
      }
      StringBuffer sb = new StringBuffer("");
      for(String key: overallCount.keySet()){
        sb.append(key+"\t"+overallCount.get(key)+"\n");
      }
      FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+"key_count_sum"), sb.toString(), "utf-8", false);

      for (int year = 1999; year < 2020; year++) {
        String s = FileUtils.readFileToString(new File(projJsonFile), "utf-8");
        JSONArray ja = new JSONArray(s);
        int count = 0;
        int count2 = 0;
        for (int i = 0; i < ja.length(); i++) {
          JSONObject jo = ja.getJSONObject(i);
          if(jo.getInt("year") != year)
            continue;
          JSONArray keys = jo.getJSONArray("keys");
          for (int j = 0; j < keys.length(); j++) {
            String key = keys.getString(j);
            if (keySumm.get(key) == null || keySumm.get(key).isEmpty()) {
              HashMap<String, Integer> map = new HashMap<>();
              map.put("A", 0);
              map.put("B", 0);
              map.put("C", 0);
              map.put("D", 0);
              map.put("E", 0);
              map.put("F", 0);
              map.put("G", 0);
              map.put("H", 0);
              map.put(jo.getString("code1").substring(0, 1), map.get(jo.getString("code1").substring(0, 1)) + 1);
              keySumm.put(key, map);
            } else {
              HashMap<String, Integer> map = keySumm.get(key);
              map.put(jo.getString("code1").substring(0, 1), map.get(jo.getString("code1").substring(0, 1)) + 1);
            }
          }
        }

        {
          JSONArray filteredArray500 = new JSONArray();
          Iterator it3 = keySumm.entrySet().iterator();
          while (it3.hasNext()) {
            Map.Entry pairs = (Map.Entry) it3.next();
            if(overallCount.get(pairs.getKey().toString()) < 500){
              continue;
            }

            JSONObject oneKey = new JSONObject();
            oneKey.put("key", pairs.getKey());
            HashMap<String,Integer> dist = (HashMap<String,Integer>)pairs.getValue();
            int sum = 0;
            for(String k : dist.keySet()) {
              oneKey.put(k, dist.get(k));
              sum += dist.get(k);
            }
            oneKey.put("all", sum);
            oneKey.put("diversity1", calDiversity_v2(year, dist,allHash));
            filteredArray500.put(oneKey);

          }

          FileUtils.writeStringToFile(new File(ParseUtils.path + "/"+ "key_dist_filter_v2"+"/"+"key_dist_filter_json_500_"+year+".json"),
            filteredArray500.toString(4), "utf-8",false);
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  //recalculate interdisciplinarity, former algorithm forgot calculate year by year
  private void interByKeys_v3(String projJsonFile) {

    try {

      JSONArray all = new JSONArray(FileUtils.readFileToString(new File("proj_1999_2020_0131"+"/"+"sim.json"), "utf-8"));
      HashMap<String, Double> allHash = new HashMap<String, Double>();
      for(int i = 0; i < all.length(); i ++){
        allHash.put(all.getJSONObject(i).getString("key"), all.getJSONObject(i).getDouble("value"));
      }


      HashMap<String, Integer> overallCount = new HashMap<>();
      {
        String s = FileUtils.readFileToString(new File(projJsonFile), "utf-8");
        JSONArray ja = new JSONArray(s);
        for (int i = 0; i < ja.length(); i++) {
          JSONObject jo = ja.getJSONObject(i);
          JSONArray keys = jo.getJSONArray("keys");
          for (int j = 0; j < keys.length(); j++) {
            String key = keys.getString(j);
            if (!overallCount.containsKey(key)) {
              overallCount.put(key, 1);
            }else{
              overallCount.put(key, overallCount.get(key) + 1 );
            }
          }
        }
      }
      StringBuffer sb = new StringBuffer("");
      for(String key: overallCount.keySet()){
        sb.append(key+"\t"+overallCount.get(key)+"\n");
      }

      FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+"key_count_sum"), sb.toString(), "utf-8", false);

      for (int year = 1999; year < 2020; year++) {
        HashMap<String, HashMap<String, Integer>> keySumm = new HashMap<>();

        String s = FileUtils.readFileToString(new File(projJsonFile), "utf-8");
        JSONArray ja = new JSONArray(s);
        int count = 0;
        int count2 = 0;
        for (int i = 0; i < ja.length(); i++) {
          JSONObject jo = ja.getJSONObject(i);
          if(jo.getInt("year") != year)
            continue;
          JSONArray keys = jo.getJSONArray("keys");
          for (int j = 0; j < keys.length(); j++) {
            String key = keys.getString(j);
            if (keySumm.get(key) == null || keySumm.get(key).isEmpty()) {
              HashMap<String, Integer> map = new HashMap<>();
              map.put("A", 0);
              map.put("B", 0);
              map.put("C", 0);
              map.put("D", 0);
              map.put("E", 0);
              map.put("F", 0);
              map.put("G", 0);
              map.put("H", 0);
              map.put(jo.getString("code1").substring(0, 1), map.get(jo.getString("code1").substring(0, 1)) + 1);
              keySumm.put(key, map);
            } else {
              HashMap<String, Integer> map = keySumm.get(key);
              map.put(jo.getString("code1").substring(0, 1), map.get(jo.getString("code1").substring(0, 1)) + 1);
            }
          }
        }


        {
          JSONArray filteredArray50 = new JSONArray();
          Iterator it3 = keySumm.entrySet().iterator();
          while (it3.hasNext()) {
            Map.Entry pairs = (Map.Entry) it3.next();
            if(overallCount.get(pairs.getKey().toString()) < 50){
              continue;
            }

            JSONObject oneKey = new JSONObject();
            oneKey.put("key", pairs.getKey());
            HashMap<String,Integer> dist = (HashMap<String,Integer>)pairs.getValue();
            int sum = 0;
            for(String k : dist.keySet()) {
              oneKey.put(k, dist.get(k));
              sum += dist.get(k);
            }
            oneKey.put("all", sum);
            oneKey.put("diversity1", calDiversity_v2(year, dist,allHash));
            filteredArray50.put(oneKey);

          }
          FileUtils.writeStringToFile(new File(ParseUtils.path + "/"+ "key_dist_filter_v3"+"/"+"key_dist_filter_json_50_"+year+".json"),
            filteredArray50.toString(4), "utf-8",false);
        }

        {
          JSONArray filteredArray100 = new JSONArray();
          Iterator it3 = keySumm.entrySet().iterator();
          while (it3.hasNext()) {
            Map.Entry pairs = (Map.Entry) it3.next();
            if(overallCount.get(pairs.getKey().toString()) < 100){
              continue;
            }

            JSONObject oneKey = new JSONObject();
            oneKey.put("key", pairs.getKey());
            HashMap<String,Integer> dist = (HashMap<String,Integer>)pairs.getValue();
            int sum = 0;
            for(String k : dist.keySet()) {
              oneKey.put(k, dist.get(k));
              sum += dist.get(k);
            }
            oneKey.put("all", sum);
            oneKey.put("diversity1", calDiversity_v2(year, dist,allHash));
            filteredArray100.put(oneKey);

          }
          FileUtils.writeStringToFile(new File(ParseUtils.path + "/"+ "key_dist_filter_v3"+"/"+"key_dist_filter_json_100_"+year+".json"),
            filteredArray100.toString(4), "utf-8",false);
        }

        {
          JSONArray filteredArray500 = new JSONArray();
          Iterator it3 = keySumm.entrySet().iterator();
          while (it3.hasNext()) {
            Map.Entry pairs = (Map.Entry) it3.next();
            if(overallCount.get(pairs.getKey().toString()) < 500){
              continue;
            }

            JSONObject oneKey = new JSONObject();
            oneKey.put("key", pairs.getKey());
            HashMap<String,Integer> dist = (HashMap<String,Integer>)pairs.getValue();
            int sum = 0;
            for(String k : dist.keySet()) {
              oneKey.put(k, dist.get(k));
              sum += dist.get(k);
            }
            oneKey.put("all", sum);
            oneKey.put("diversity1", calDiversity_v2(year, dist,allHash));
            filteredArray500.put(oneKey);

          }
          FileUtils.writeStringToFile(new File(ParseUtils.path + "/"+ "key_dist_filter_v3"+"/"+"key_dist_filter_json_500_"+year+".json"),
            filteredArray500.toString(4), "utf-8",false);
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  private double calDiversity(HashMap<String, Integer> dist) {
      double diversity = 0.0;
      int sum = 0;
    for(String s1 : dist.keySet()){
      sum += dist.get(s1);
    }
      for(String s1 : dist.keySet()){
        for(String s2 : dist.keySet()){
          if(s1.equals(s2))
            continue;
          diversity += ((dist.get(s1)/(1.0*sum)) * (dist.get(s2)/(1.0*sum)));
        }
      }
      return diversity;
  }

  private double calDiversity_v2(int year, HashMap<String, Integer> dist, HashMap<String, Double> sim) {
    double diversity = 0.0;
    int sum = 0;
    for(String s1 : dist.keySet()){
      sum += dist.get(s1);
    }
    for(String s1 : dist.keySet()){
      for(String s2 : dist.keySet()){
        if(s1.equals(s2))
          continue;
        diversity += (1- sim.get(s1+s2+year)) * ((dist.get(s1)/(1.0*sum)) * (dist.get(s2)/(1.0*sum)));
      }
    }
    return diversity;
  }

  /**
   * print
   * @param projJsonFile
   */
  public void genKeys(String projJsonFile){
      try {
        String s = FileUtils.readFileToString(new File(ParseUtils.path+"/"+projJsonFile), "utf-8");
        JSONArray jo = new JSONArray(s);
        for(char l = 'A'; l <= 'H'; l ++){
          System.out.println(l);
        for (int j = 1999; j < 2020; j++) {
            int[] count = new int[5];
            for (int k = 0; k < count.length; k++) count[k] = 0;

            for (int i = 0; i < jo.length(); i++) {
              if (jo.getJSONObject(i).getInt("year") == j && jo.getJSONObject(i).getString("code1").charAt(0) == l) {
                String code1 = jo.getJSONObject(i).getString("code1");
                String code2 = jo.getJSONObject(i).getString("code2");

                count[calInterValue(code1, code2)]++;
              }
            }
            System.out.print(j + "\t");
            int sum = 0;
            for (int k = 0; k < count.length; k++) {
              sum += count[k];
            }
            for (int k = 0; k < count.length; k++) {
              System.out.print(count[k]/(1.0*sum) + "\t");
            }
            System.out.println();
        }
      }
      }catch (Exception e){
        e.printStackTrace();
      }
    }

    //double check
    int calInterValue(String code1, String code2){
    if(code2.equals("") || code2.isEmpty())
      return 0;
    if(code1.charAt(0) != code2.charAt(0))
      return 4;
    if(!code1.substring(0,3).equals(code2.substring(0,3)))
        return 3;
    if(code1.length()==3 || code2.length() == 3)
      return 2;
    if(!code1.substring(0,5).equals(code2.substring(0,5)))
      return 2;
    if(code1.length()==5 || code2.length() == 5)
      return 1;
    else {
      return 1;
    }
    }



  private void genCodeInterdis() {
    try {
      String s = FileUtils.readFileToString(new File("proj_json"), "utf-8");
      JSONArray jo = new JSONArray(s);
      for(int i = 0; i < jo.length(); i ++){
        JSONObject oneProj = jo.getJSONObject(i);
        String r = "";
        r += oneProj.getInt("year") + "\t";
        r += oneProj.getString("code1") + "\t";
        r += oneProj.getString("code1").substring(0,1) + "\t";
        r += oneProj.getString("code1").substring(0,3) + "\t" + "1\t";
        r += calInterValue(oneProj.getString("code1"), oneProj.getString("code2")) + "\n";
        FileUtils.writeStringToFile(new File("code_inter.csv"), r,"utf-8", true);
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }







    class KeyDiversity{
      HashMap<String,Integer> map;
      double diversity1;
      double diversity2;
      void keyDiversity(){
        map = new HashMap<>();
        map.put("A", 0);
        map.put("B", 0);
        map.put("C", 0);
        map.put("D", 0);
        map.put("E", 0);
        map.put("F", 0);
        map.put("G", 0);
        map.put("H", 0);
        diversity1 = 0.0;
        diversity2 = 0.0;
      }

      public HashMap<String, Integer> getMap() {
        return map;
      }

      public void setMap(HashMap<String, Integer> map) {
        this.map = map;
      }

      public double getDiversity1() {
        return diversity1;
      }

      public void setDiversity1(double diversity1) {
        this.diversity1 = diversity1;
      }

      public double getDiversity2() {
        return diversity2;
      }

      public void setDiversity2(double diversity2) {
        this.diversity2 = diversity2;
      }
    }
}
