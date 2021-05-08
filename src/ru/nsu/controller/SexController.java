package ru.nsu.controller;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import ru.nsu.IdName;

public class SexController {
    private static volatile HashMap<Integer, String> sexNames = null;
    private static volatile Connection connection = null;

    public static void init(Connection connection) throws Exception{
        SexController.connection = connection;
        // load <id, name> pairs from database
        Statement stmt = connection.createStatement();

        ResultSet res = stmt.executeQuery("select * from sexes;");
        sexNames = new HashMap<>();

        while (res.next()){
            int id = res.getInt("id");
            String name = res.getString("name");

            sexNames.put(id, name);
        }
    }

    public static String getSexNameFromId(int id){
        String res = "";
        synchronized (sexNames){
            res = sexNames.get(id);
        }

        return res;
    }

    public static ArrayList<IdName> getIdNameList(){
        ArrayList<IdName> res = new ArrayList<>();
        synchronized (sexNames){
            for (Integer x : sexNames.keySet()) {
                res.add(new IdName(x, sexNames.get(x)));
            }
        }

        return res;
    }
}
