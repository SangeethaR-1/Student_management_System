/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.student.ui;

/**
 *
 * @author Lenovo
 */

import com.student.db.StudentDAO;
import com.student.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class StudentManagementUI extends JFrame {

    private JTextField nameField, emailField, phoneField, searchField;
    private JButton saveButton, loadButton, updateButton, deleteButton, searchButton;
    private JTable studentTable;
    private int selectedStudentId = -1;

    public StudentManagementUI() {
        setTitle("Student Management System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        saveButton = new JButton("Save");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        formPanel.add(saveButton);
        formPanel.add(updateButton);
        formPanel.add(deleteButton);

        add(formPanel, BorderLayout.NORTH);

        // Table panel
        studentTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        loadButton = new JButton("Load Students");
        buttonPanel.add(loadButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        searchButton = new JButton("Go");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.WEST);

        // Action listeners
        saveButton.addActionListener(e -> saveStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        loadButton.addActionListener(e -> loadStudents());
        searchButton.addActionListener(e -> searchStudents());

        studentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedStudentId = (int) studentTable.getValueAt(selectedRow, 0);
                    nameField.setText((String) studentTable.getValueAt(selectedRow, 1));
                    emailField.setText((String) studentTable.getValueAt(selectedRow, 2));
                    phoneField.setText((String) studentTable.getValueAt(selectedRow, 3));
                }
            }
        });
    }

    private void saveStudent() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (!validateInput(name, email, phone)) return;

        try {
            Student student = new Student(name, email, phone);
            new StudentDAO().addStudent(student);
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            clearForm();
            loadStudents();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving student.");
        }
    }

    private void updateStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to update.");
            return;
        }

        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (!validateInput(name, email, phone)) return;

        try {
            Student student = new Student(selectedStudentId, name, email, phone);
            new StudentDAO().updateStudent(student);
            JOptionPane.showMessageDialog(this, "Student updated successfully!");
            clearForm();
            loadStudents();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating student.");
        }
    }

    private void deleteStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?");
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            new StudentDAO().deleteStudent(selectedStudentId);
            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            clearForm();
            loadStudents();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting student.");
        }
    }

    private void loadStudents() {
        try {
            List<Student> students = new StudentDAO().getAllStudents();
            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone"}, 0);
            for (Student s : students) {
                model.addRow(new Object[]{s.getId(), s.getName(), s.getEmail(), s.getPhone()});
            }
            studentTable.setModel(model);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading students.");
        }
    }

    private void searchStudents() {
        String keyword = searchField.getText();
        try {
            List<Student> students = new StudentDAO().searchStudents(keyword);
            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone"}, 0);
            for (Student s : students) {
                model.addRow(new Object[]{s.getId(), s.getName(), s.getEmail(), s.getPhone()});
            }
            studentTable.setModel(model);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching students.");
        }
    }

    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        selectedStudentId = -1;
    }

    private boolean validateInput(String name, String email, String phone) {
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return false;
        }

        if (!email.matches("^\\S+@\\S+\\.\\S+$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return false;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone must be 10 digits.");
            return false;
        }

        return true;
    }
}
