import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

class ToDoListApp {
    private static DefaultListModel<String> listModel;
    private static JList<String> taskList;
    private static JTextField taskField;
    private static final String FILE_PATH = "tasks.txt";

    public static void main(String[] args) {
        // Create the main frame (window)
        JFrame frame = new JFrame("To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // Create a panel for the input field and button
        JPanel panel = new JPanel();
        taskField = new JTextField(15);  // Input field for new tasks
        JButton addButton = new JButton("Add Task");  // Button to add a new task
        panel.add(taskField);
        panel.add(addButton);

        // Create the list model and the task list
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(taskList);  // Make the task list scrollable

        // Create buttons for additional functionalities
        JButton removeButton = new JButton("Remove Selected Task");  // Button to remove selected task
        JButton markCompletedButton = new JButton("Mark as Completed");  // Button to mark task as completed
        JButton clearAllButton = new JButton("Clear All Tasks");  // Button to clear all tasks

        // Add components to the frame
        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(removeButton);
        buttonPanel.add(markCompletedButton);
        buttonPanel.add(clearAllButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Load tasks from file when the application starts
        loadTasksFromFile();

        // Add button action listener for adding a task
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String task = taskField.getText();
                if (!task.isEmpty()) {
                    listModel.addElement(task);  // Add the new task to the list
                    taskField.setText("");  // Clear the input field
                    saveTasksToFile();  // Save tasks to file
                }
            }
        });

        // Remove button action listener for removing the selected task
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    listModel.remove(selectedIndex);  // Remove the selected task
                    saveTasksToFile();  // Save tasks to file
                }
            }
        });

        // Mark as completed button action listener
        markCompletedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String task = listModel.getElementAt(selectedIndex);
                    listModel.set(selectedIndex, "<html><strike>" + task + "</strike></html>");  // Mark the task as completed
                    saveTasksToFile();  // Save tasks to file
                }
            }
        });

        // Clear all button action listener
        clearAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listModel.clear();  // Clear all tasks
                saveTasksToFile();  // Save tasks to file
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    // Save tasks to file
    private static void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (int i = 0; i < listModel.size(); i++) {
                writer.write(listModel.getElementAt(i));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load tasks from file
    private static void loadTasksFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    listModel.addElement(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
