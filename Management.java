import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Management implements ActionListener {
    JFrame frame;
    JButton addBtn, viewBtn, deleteBtn, updateBtn, exitBtn;
    JTextField rollField, nameField, ageField, marksField, deleteRollField;
    JTextArea outputArea;

    // JDBC credentials
    String url = "jdbc:mysql://localhost:3306/StudentDB?useSSL=false&serverTimezone=UTC";
    String user = "root";
    String pass = "Harish@1308";

    Management() {
        frame = new JFrame("Student Management System");
        frame.setSize(600, 700);
        frame.setLayout(null);

        // Input fields for add/update
        JLabel rollLabel = new JLabel("Roll No:");
        rollLabel.setBounds(20, 20, 80, 25);
        frame.add(rollLabel);

        rollField = new JTextField();
        rollField.setBounds(100, 20, 150, 25);
        frame.add(rollField);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 60, 80, 25);
        frame.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(100, 60, 150, 25);
        frame.add(nameField);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBounds(20, 100, 80, 25);
        frame.add(ageLabel);

        ageField = new JTextField();
        ageField.setBounds(100, 100, 150, 25);
        frame.add(ageField);

        JLabel marksLabel = new JLabel("Marks:");
        marksLabel.setBounds(20, 140, 80, 25);
        frame.add(marksLabel);

        marksField = new JTextField();
        marksField.setBounds(100, 140, 150, 25);
        frame.add(marksField);

        // Buttons
        addBtn = new JButton("Add Student");
        addBtn.setBounds(300, 20, 150, 30);
        addBtn.addActionListener(this);
        frame.add(addBtn);

        updateBtn = new JButton("Update Student");
        updateBtn.setBounds(300, 60, 150, 30);
        updateBtn.addActionListener(this);
        frame.add(updateBtn);

        viewBtn = new JButton("View All");
        viewBtn.setBounds(300, 100, 150, 30);
        viewBtn.addActionListener(this);
        frame.add(viewBtn);

        exitBtn = new JButton("Exit");
        exitBtn.setBounds(300, 140, 150, 30);
        exitBtn.addActionListener(this);
        frame.add(exitBtn);

        // Delete section
        JLabel deleteLabel = new JLabel("Roll No to Delete:");
        deleteLabel.setBounds(20, 190, 120, 25);
        frame.add(deleteLabel);

        deleteRollField = new JTextField();
        deleteRollField.setBounds(150, 190, 100, 25);
        frame.add(deleteRollField);

        deleteBtn = new JButton("Delete Student");
        deleteBtn.setBounds(270, 190, 180, 30);
        deleteBtn.addActionListener(this);
        frame.add(deleteBtn);

        // Output area
        outputArea = new JTextArea();
        outputArea.setBounds(20, 240, 540, 400);
        outputArea.setEditable(false);
        frame.add(outputArea);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            addStudent();
        } else if (e.getSource() == viewBtn) {
            viewStudents();
        } else if (e.getSource() == deleteBtn) {
            deleteStudent();
        } else if (e.getSource() == updateBtn) {
            updateStudent();
        } else if (e.getSource() == exitBtn) {
            System.exit(0);
        }
    }

    void addStudent() {
        String roll = rollField.getText();
        String name = nameField.getText();
        String age = ageField.getText();
        String marks = marksField.getText();

        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String sql = "INSERT INTO students (roll_no, name, age, marks) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, roll);
            pst.setString(2, name);
            pst.setInt(3, Integer.parseInt(age));
            pst.setInt(4, Integer.parseInt(marks));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Student added successfully.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    void updateStudent() {
        String roll = rollField.getText();
        String name = nameField.getText();
        String age = ageField.getText();
        String marks = marksField.getText();

        if (roll.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter Roll No to update.");
            return;
        }

        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String sql = "UPDATE students SET name = ?, age = ?, marks = ? WHERE roll_no = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setInt(2, Integer.parseInt(age));
            pst.setInt(3, Integer.parseInt(marks));
            pst.setString(4, roll);
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(frame, "Student updated successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Student not found.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    void viewStudents() {
        outputArea.setText("Roll No\tName\tAge\tMarks\n------------------------------------\n");
        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String sql = "SELECT * FROM students";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                outputArea.append(rs.getString("roll_no") + "\t"
                        + rs.getString("name") + "\t"
                        + rs.getInt("age") + "\t"
                        + rs.getInt("marks") + "\n");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    void deleteStudent() {
        String roll = deleteRollField.getText();
        if (roll.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Enter a roll number to delete.");
            return;
        }

        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String sql = "DELETE FROM students WHERE roll_no = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, roll);
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(frame, "Student deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "No student found with that Roll No.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Management();
    }
}
