package ru.nsu.controller;

import ru.nsu.IdName;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class VoiceController {
    private static volatile HashMap<Integer, String> voiceNames = null;
    private static volatile Connection connection = null;

    public static void init(Connection connection) throws Exception{
        VoiceController.connection = connection;
        // load <id, name> pairs from database
        Statement stmt = connection.createStatement();

        ResultSet res = stmt.executeQuery("select * from voices;");
        voiceNames = new HashMap<>();

        while (res.next()){
            int id = res.getInt("id");
            String name = res.getString("name");

            voiceNames.put(id, name);
        }
    }

    public static String getVoiceNameFromId(int id){
        String res = "";
        synchronized (voiceNames){
            res = voiceNames.get(id);
        }

        return res;
    }

    public static ArrayList<IdName> getIdNameList(){
        ArrayList<IdName> res = new ArrayList<>();
        synchronized (voiceNames){
            for (Integer x : voiceNames.keySet()) {
                res.add(new IdName(x, voiceNames.get(x)));
            }
        }

        return res;
    }
}
