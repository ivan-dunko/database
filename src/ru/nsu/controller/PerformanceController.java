package ru.nsu.controller;

import ru.nsu.IdName;
import ru.nsu.entity.Actor;
import ru.nsu.entity.Employee;
import ru.nsu.entity.Performance;
import ru.nsu.entity.Role;
import ru.nsu.table.PerformanceTable;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class PerformanceController {
    private static volatile Connection connection = null;
    private static volatile JTable perfTable = null;

    public static void init(Connection connection, JTable employeeTable){
        PerformanceController.connection = connection;
        PerformanceController.perfTable = employeeTable;
        updatePerformanceTable();
    }

    public static ArrayList<Performance> getAllPerformances(){
        ArrayList<Performance> perfs = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String query = "select * from performances";
            ResultSet res = stmt.executeQuery(query);

            while (res.next()) {
                perfs.add(new Performance(res));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return perfs;
    }


    public static void updatePerformanceTable(){
        new SwingWorker<ArrayList<Performance>, Void>() {
            @Override
            protected ArrayList<Performance> doInBackground() throws Exception {
                ArrayList<Performance> perfs = new ArrayList<>();
                try {
                    Statement stmt = connection.createStatement();
                    String query = "select * from performances";
                    ResultSet res = stmt.executeQuery(query);

                    while (res.next()) {
                        perfs.add(new Performance(res));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return perfs;
            }

            @Override
            protected void done() {
                try {
                    perfTable.setModel(new PerformanceTable(get()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public static void addPerformance(
            int playId,
            Timestamp start,
            Timestamp end,
            int stageDirId,
            int prodDirId,
            ArrayList<Role> roles,
            ArrayList<Actor> actors,
            ArrayList<Actor> stunts){

        try {
            // 1. Add to `Performances` table
            StringBuilder query = new StringBuilder("insert into performances" +
                    " (stage_director_id, prod_director_id, play_id, start_time, end_time) " +
                    "values (?, ?, ?, ?, ?);\n");
            // 2. Add roles to `Performance-Role` table

            for (int i = 0; i < roles.size(); ++i)
                query.append("insert into performance_role " +
                        "(performance_id, role_id, actor_id, stunt_id) " +
                        "values (LAST_INSERT_ID(), ?, ?, ?);\n");


            System.out.println(query);
            PreparedStatement stmt = connection.prepareStatement(query.toString());
            stmt.setInt(1, stageDirId);
            stmt.setInt(2, prodDirId);
            stmt.setInt(3, playId);
            stmt.setTimestamp(4, start);
            stmt.setTimestamp(5, end);

            for (int i = 0; i < roles.size(); ++i){
                stmt.setInt(6 + i * 3, roles.get(i).getId());
                stmt.setInt(6 + i * 3 + 1, actors.get(i).getId());
                stmt.setInt(6 + i * 3 + 2, stunts.get(i).getId());
            }



            stmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void searchPerformances(PerformanceController.SearchContext context){
        // CONSTRUCTING QUERY
        StringBuilder query = new StringBuilder("select * from performances where " +
                "join plays on performances.play_id = plays.id");

        //select dates
        query.append("performances.start_time >= ").
                append(context.getStart().toString()).
                append(" AND performances.end_time <= ").
                append(context.getEnd().toString()).
                append(" ");

        //genres
        query.append("AND (0 OR ");
        boolean isAny = false;
        if (context.isVaudeville()) {
            query.append("genre_id = 1 OR ");
            isAny = true;
        }
        if (context.isComedy()) {
            query.append("genre_id = 2 OR ");
            isAny = true;
        }
        if (context.isDrama()) {
            query.append("genre_id = 3 OR ");
            isAny = true;
        }
        if (context.isMelodrama()) {
            query.append("genre_id = 4 OR ");
            isAny = true;
        }
        if (context.isTragedy()) {
            query.append("genre_id = 5 OR ");
            isAny = true;
        }
        if (context.isMusical()) {
            query.append("genre_id = 6 OR ");
            isAny = true;
        }
        if (isAny)
            query.append("1) ");
        else
            query.append("0) ");

        //authors
        if (context.getAuthIds().size() != 0){
            query.append("AND (0 OR ");
            for (IdName x : context.getAuthIds()){
                query.append("author_id = ").
                        append(x.getId()).
                        append(" OR ");
            }
            query.append("0) ");
        }

        //centuries
        isAny = false;
        query.append("AND (0 OR ");
        if (context.isCent16()){
            isAny = true;
            query.append("year >= 1500 AND year < 1600 OR ");
        }
        if (context.isCent17()){
            isAny = true;
            query.append("year >= 1600 AND year < 1700 OR ");
        }
        if (context.isCent18()){
            isAny = true;
            query.append("year >= 1700 AND year < 1800 OR ");
        }
        if (context.isCent19()){
            isAny = true;
            query.append("year >= 1800 AND year < 1900 OR ");
        }
        if (context.isCent20()){
            isAny = true;
            query.append("year >= 1900 AND year < 2000 OR ");
        }
        if (isAny)
            query.append("0) ");
        else
            query.append("1) ");

        //only first performance in given range
        System.out.println(query.toString());

        String finalQuery = query.toString();
        new SwingWorker<ArrayList<Performance>, Void>() {
            String query = finalQuery;
            @Override
            protected ArrayList<Performance> doInBackground() throws Exception {
                ArrayList<Performance> perfs = new ArrayList<>();
                Statement stmt = connection.createStatement();
                ResultSet res = stmt.executeQuery(query);

                while (res.next()) {
                    perfs.add(new Performance(res));
                }

                return perfs;
            }

            @Override
            protected void done() {
                try {
                    perfTable.setModel(new PerformanceTable(get()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public static boolean doIntersect(Performance a, Performance b){
        Date aStart = a.getStartTime();
        Date aEnd = a.getEndTime();
        Date bStart = b.getStartTime();
        Date bEnd = b.getEndTime();

        return !(aEnd.compareTo(bStart) < 0 || aStart.compareTo(bEnd) > 0);
    }

    public static class SearchContext{
        private final Timestamp start;
        private final Timestamp end;
        private final boolean vaudeville;
        private final boolean comedy;
        private final boolean drama;
        private final boolean melodrama;
        private final boolean tragedy;
        private final boolean musical;
        private final boolean first;
        private final boolean cent16;
        private final boolean cent17;
        private final boolean cent18;
        private final boolean cent19;
        private final boolean cent20;
        private final ArrayList<IdName> authIds;


        public SearchContext(Timestamp start, Timestamp end,
                             boolean vaudeville, boolean comedy,
                             boolean drama, boolean melodrama, boolean musical,
                             boolean tragedy, boolean first,
                             boolean cent16, boolean cent17,
                             boolean cent18, boolean cent19, boolean cent20,
                             ArrayList<IdName> authIds) {
            this.start = start;
            this.end = end;
            this.vaudeville = vaudeville;
            this.comedy = comedy;
            this.drama = drama;
            this.melodrama = melodrama;
            this.tragedy = tragedy;
            this.musical = musical;
            this.first = first;
            this.cent16 = cent16;
            this.cent17 = cent17;
            this.cent18 = cent18;
            this.cent19 = cent19;
            this.cent20 = cent20;
            this.authIds = authIds;
        }

        public Timestamp getStart() {
            return start;
        }

        public Timestamp getEnd() {
            return end;
        }

        public boolean isVaudeville() {
            return vaudeville;
        }

        public boolean isComedy() {
            return comedy;
        }

        public boolean isDrama() {
            return drama;
        }

        public boolean isMelodrama() {
            return melodrama;
        }

        public boolean isTragedy() {
            return tragedy;
        }

        public boolean isMusical(){
            return musical;
        }

        public boolean isFirst() {
            return first;
        }

        public boolean isCent16() {
            return cent16;
        }

        public boolean isCent17() {
            return cent17;
        }

        public boolean isCent18() {
            return cent18;
        }

        public boolean isCent19() {
            return cent19;
        }

        public boolean isCent20(){
            return cent20;
        }

        public ArrayList<IdName> getAuthIds() {
            return authIds;
        }
    }
}
