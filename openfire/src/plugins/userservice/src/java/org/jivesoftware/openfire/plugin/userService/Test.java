package org.jivesoftware.openfire.plugin.userService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {
public static void main(String[] args) {
    JSONArray jsonArray=new JSONArray();
    jsonArray.add("a");
    jsonArray.add("b");
    JSONArray jsonObjec=JSONArray.fromObject(jsonArray.toString());
    System.out.println(jsonObjec.toString());
}
}
