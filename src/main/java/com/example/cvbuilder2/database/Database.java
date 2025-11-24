package com.example.cvbuilder2.database;

import com.example.cvbuilder2.model.cvmodel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Database {

    private static final String URL = "jdbc:sqlite:cvbuilder.db";
    // Executor for DB operations
    private static final ExecutorService DB_EXEC = Executors.newFixedThreadPool(2);


    public static final String SAMPLE_JSON_PATH = "/mnt/data/Untitled document.pdf";


    public static Connection connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC"); // ⭐ REQUIRED ⭐
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return DriverManager.getConnection("jdbc:sqlite:cvbuilder.db");
    }






    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS cvdata (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT," +
                "phone TEXT," +
                "address TEXT," +
                "education TEXT," +
                "skills TEXT," +
                "experience TEXT," +
                "projects TEXT" +
                ");";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static long saveCV(cvmodel cv) throws SQLException {
        String sql = "INSERT INTO cvdata(name, email, phone, address, education, skills, experience, projects) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cv.getName());
            ps.setString(2, cv.getEmail());
            ps.setString(3, cv.getPhone());
            ps.setString(4, cv.getAddress());
            ps.setString(5, cv.getEducation());
            ps.setString(6, cv.getSkills());
            ps.setString(7, cv.getExperience());
            ps.setString(8, cv.getProjects());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return -1;
    }


    public static boolean updateCV(cvmodel cv) throws SQLException {
        String sql = "UPDATE cvdata SET name=?, email=?, phone=?, address=?, education=?, skills=?, experience=?, projects=? WHERE id=?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cv.getName());
            ps.setString(2, cv.getEmail());
            ps.setString(3, cv.getPhone());
            ps.setString(4, cv.getAddress());
            ps.setString(5, cv.getEducation());
            ps.setString(6, cv.getSkills());
            ps.setString(7, cv.getExperience());
            ps.setString(8, cv.getProjects());
            ps.setLong(9, cv.getId());
            int updated = ps.executeUpdate();
            return updated > 0;
        }
    }


    public static boolean deleteCV(long id) throws SQLException {
        String sql = "DELETE FROM cvdata WHERE id=?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int deleted = ps.executeUpdate();
            return deleted > 0;
        }
    }


    public static List<cvmodel> fetchAll() throws SQLException {
        List<cvmodel> list = new ArrayList<>();
        String sql = "SELECT id, name, email, phone, address, education, skills, experience, projects FROM cvdata ORDER BY id DESC";
        try (Connection conn = connect(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                cvmodel cv = new cvmodel();
                cv.setId(rs.getLong("id"));
                cv.setName(rs.getString("name"));
                cv.setEmail(rs.getString("email"));
                cv.setPhone(rs.getString("phone"));
                cv.setAddress(rs.getString("address"));
                cv.setEducation(rs.getString("education"));
                cv.setSkills(rs.getString("skills"));
                cv.setExperience(rs.getString("experience"));
                cv.setProjects(rs.getString("projects"));
                list.add(cv);
            }
        }
        return list;
    }


    public static cvmodel fetchById(long id) throws SQLException {
        String sql = "SELECT id, name, email, phone, address, education, skills, experience, projects FROM cvdata WHERE id=?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cvmodel cv = new cvmodel();
                    cv.setId(rs.getLong("id"));
                    cv.setName(rs.getString("name"));
                    cv.setEmail(rs.getString("email"));
                    cv.setPhone(rs.getString("phone"));
                    cv.setAddress(rs.getString("address"));
                    cv.setEducation(rs.getString("education"));
                    cv.setSkills(rs.getString("skills"));
                    cv.setExperience(rs.getString("experience"));
                    cv.setProjects(rs.getString("projects"));
                    return cv;
                }
            }
        }
        return null;
    }


    public static CompletableFuture<Long> saveCVAsync(cvmodel cv) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return saveCV(cv);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        }, DB_EXEC);
    }

    public static CompletableFuture<Boolean> updateCVAsync(cvmodel cv) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return updateCV(cv);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        }, DB_EXEC);
    }

    public static CompletableFuture<Boolean> deleteCVAsync(long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return deleteCV(id);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        }, DB_EXEC);
    }

    public static CompletableFuture<List<cvmodel>> fetchAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return fetchAll();
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        }, DB_EXEC);
    }

    public static CompletableFuture<cvmodel> fetchByIdAsync(long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return fetchById(id);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        }, DB_EXEC);
    }


    public static void exportToJson(File file, List<cvmodel> data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
    }

    public static List<cvmodel> importFromJson(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        cvmodel[] arr = mapper.readValue(file, cvmodel[].class);
        List<cvmodel> list = new ArrayList<>();
        for (cvmodel c : arr) list.add(c);
        return list;
    }


    public static void shutdownExecutor() {
        DB_EXEC.shutdown();
    }
}
