package org.duyi;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 */
public class ProjectToJson_01 {


  static String projFile = "proj_old_45";

  static String[] ignoredKeys = {};

  public static void main(String[] args) {

    String projJsonFile = projFile+".json";
    new ProjectToJson_01().projectToJson(projFile);


  }

  /**
   * parse data to json and split by year
   */
  private void splitJsonByYear() {
    try{
      JSONArray js = new JSONArray(FileUtils.readFileToString(
        new File(ParseUtils.path+"/"+projFile + "_json.json"), "utf-8"));

      for(int i = 1999; i < 2020; i ++){
        JSONArray oneYear = new JSONArray();
        for(int j = 0; j < js.length(); j ++){
          JSONObject oneProj = js.getJSONObject(j);
          if(oneProj.getInt("year") == i){
            oneYear.put(oneProj);
          }
        }
        FileUtils.writeStringToFile(new File(ParseUtils.path+"/splitProj"+"/"+projFile + i + "_json.json"), oneYear.toString(4), "utf-8", false);
      }


    }catch (Exception e){
      e.printStackTrace();
    }
  }


  /**
   * @param projectFile
   * parse file to json
   */
  public void projectToJson(String projectFile) {
    try {

      try {
        FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+"keytemp"), "", "utf-8", false);
      } catch (Exception e) {
        e.printStackTrace();
      }

      BufferedReader bis = new BufferedReader(new FileReader(projectFile));
      String line = bis.readLine();
      int count = 0;

      JSONArray ja = new JSONArray();
      while (line != null) {
        JSONObject oneProj = new JSONObject();

        StringTokenizer st = new StringTokenizer(line, "\t");
        int year = (int) Double.parseDouble(st.nextToken());
        int prj_code = Integer.parseInt(st.nextToken());
        String title = st.nextToken();
        String code1 = st.nextToken();
        String temp = st.nextToken();
        String code2 = temp.equals("NULL") ? "" : temp;
        String keys = st.nextToken();
        String[] keyParsed = parseKeys(keys, year);
        String summ = st.nextToken().trim();

        oneProj.put("year", year);
        oneProj.put("prj_code", prj_code);
        oneProj.put("title", title);
        oneProj.put("code1", code1);
        oneProj.put("code2", code2);
        oneProj.put("keys", new JSONArray(keyParsed));
        oneProj.put("sum", summ);
        ja.put(oneProj);
        line = bis.readLine();

        if (count % 1000 == 0)
          System.out.println(count);
        count++;
      }
      FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+projectFile + "_json.json"), ja.toString(4), "utf-8", false);
      System.out.println("count:" + count);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param key
   * @return
   */
  private String[] parseKeys(String key, int year) {
    key = ParseUtils.ToDBC(key);
    //parse:类似"模型的构造..精确解.边界效应"
    if(key.contains("..")) {
      key = key.replace("..", ".").replace("..",".");
    }
    if(key.endsWith(".")){
      //parse:可生物降解；形状记忆；药物缓释；生物材料；智能器件.
      key = key.substring(0,key.length()-1);
    }

    String temp = year + ":";
    ArrayList<String> r = new ArrayList<String>();
    key = key.toLowerCase();//parse大小写 MicroRNA microRNA
    if(year < 2005) {
    StringTokenizer st = new StringTokenizer(key, ";；。.");
      while (st.hasMoreTokens()) {
        String s = st.nextToken();
        if (!s.equals("") && s.trim().length() > 1) {// s.length() > 1去掉 1个字符的关键词
          r.add(s);
          temp += (s + ":");
        }
      }
    }else {// year 2005 and after
      StringTokenizer st = new StringTokenizer(key, ";；。");
      while (st.hasMoreTokens()) {
        String s = st.nextToken();
        if (!s.equals("") &&  s.trim().length() > 1) {
          if(s.startsWith(".")){//parse：.非线性系统辩识法	非线性系统辩识法
            s = s.substring(1);
          }
          r.add(s);
          temp += (s + ":");
        }
      }
    }
    try {
      FileUtils.writeStringToFile(new File(ParseUtils.path+"/"+"keytemp"), temp + "\n", "utf-8", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String[] arr = new String[r.size()];
    return r.toArray(arr);

  }

  /**
   *
   * @param key
   * @return
   */
  private String validKey(String key){
    return "";

  }

}
