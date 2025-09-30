import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ToDoApp extends JFrame {
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskField;
    private JButton addButton, deleteButton, doneButton;
    private File saveFile = new File("tasks.txt"); // File to store tasks

    public ToDoApp() {
        setTitle("To-Do List App");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Task input field
        taskField = new JTextField();
        add(taskField, BorderLayout.NORTH);

        // Task list
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        add(new JScrollPane(taskList), BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Task");
        deleteButton = new JButton("Delete Task");
        doneButton = new JButton("Mark as Done");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(doneButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load tasks from file at startup
        loadTasks();

        // Add task action
        addButton.addActionListener(e -> {
            String task = taskField.getText().trim();
            if (!task.isEmpty()) {
                taskListModel.addElement(task);
                taskField.setText("");
                saveTasks();
            } else {
                JOptionPane.showMessageDialog(null, "Enter a task!");
            }
        });

        // Delete task action
        deleteButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                taskListModel.remove(selectedIndex);
                saveTasks();
            } else {
                JOptionPane.showMessageDialog(null, "Select a task to delete!");
            }
        });

        // Mark as Done action (add [Done] prefix)
        doneButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                String task = taskListModel.getElementAt(selectedIndex);
                if (!task.startsWith("[Done] ")) {
                    taskListModel.set(selectedIndex, "[Done] " + task);
                    saveTasks();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Select a task to mark as done!");
            }
        });

        setVisible(true);
    }

    // Save tasks to file
    private void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            for (int i = 0; i < taskListModel.size(); i++) {
                writer.write(taskListModel.getElementAt(i));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load tasks from file
    private void loadTasks() {
        if (saveFile.exists()) {
            try (Scanner scanner = new Scanner(saveFile)) {
                while (scanner.hasNextLine()) {
                    taskListModel.addElement(scanner.nextLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoApp::new);
    }
}