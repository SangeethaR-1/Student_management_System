/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.student.db;


import com.student.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class StudentDAO {
   
    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students(name, email, phone) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, student.getName());
            pst.setString(2, student.getEmail());
            pst.setString(3, student.getPhone());
            pst.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Student s = new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone")
                );
                list.add(s);
            }
        }
        return list;
    }
    
    
    public void updateStudent(Student student) throws SQLException {
    String sql = "UPDATE students SET name=?, email=?, phone=? WHERE id=?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, student.getName());
        stmt.setString(2, student.getEmail());
        stmt.setString(3, student.getPhone());
        stmt.setInt(4, student.getId());
        stmt.executeUpdate();
    }
}

public void deleteStudent(int id) throws SQLException {
    String sql = "DELETE FROM students WHERE id=?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}

public List<Student> searchStudents(String keyword) throws SQLException {
    List<Student> students = new ArrayList<>();
    String sql = "SELECT * FROM students WHERE name LIKE ? OR id LIKE ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, "%" + keyword + "%");
        stmt.setString(2, "%" + keyword + "%");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            students.add(new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone")
            ));
        }
    }
    return students;
}

}


