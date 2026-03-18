/*
 * ArkIVv1 - A simple To-Do List application with GUI (Swing)
 *
 * Salient Features:
 *
 * 1. **Task Management**
 *    - Allows users to create, edit, delete, and mark tasks as done/undone.
 *    - Supports both main tasks and subtasks (visually indented with a different background).
 *
 * 2. **Task Persistence**
 *    - Tasks are automatically saved to a local file (`data_1.txt`).
 *    - On restart, saved tasks are loaded back into the application.
 *
 * 3. **User-Friendly Interface**
 *    - Clean layout using `BorderLayout` and `BoxLayout`.
 *    - Scrollable panel for handling many tasks.
 *    - Input field at the bottom for quick task entry (press **Enter** to add).
 *    - Larger font size for readability.
 *
 * 4. **Task Controls**
 *    - Checkbox to mark tasks as done (strikethrough + gray text applied).
 *    - Edit button to update task text.
 *    - Delete button to remove tasks.
 *    - Create Subtask button (only for main tasks) to add subtasks directly.
 *
 * 5. **Consistent Design**
 *    - Each task has a fixed size for neat appearance.
 *    - Main tasks have white background, subtasks are shaded gray with indentation.
 *    - Uses borders for separation and hierarchy visualization.
 *
 * 6. **Error Handling**
 *    - Graceful handling of file read/write errors with dialog messages.
 *
 * 7. **Data Storage Format** (inside `data_1.txt`)
 *    Each task is stored on a new line in the format:
 *
 *        ID|DONE|TEXT|ISSUBTASK
 *
 *    - **ID** → Unique integer ID of the task.
 *    - **DONE** → "1" if the task is marked done, "0" otherwise.
 *    - **TEXT** → The actual task description.
 *    - **ISSUBTASK** → "true" if it’s a subtask, "false" if it’s a main task.
 *
 *    Example:
 *        1|0|Buy groceries|false
 *        2|1|Milk|true
 *
 * Overall:
 * This version establishes the foundation for a structured, persistent, and visually organized task manager application.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ArkIVv1 {
    private JFrame frame;
    private JPanel taskPanel;
    private JTextField inputField;
    private int taskCounter = 1;
    private final String FILE_NAME = "data_1.txt";

    public ArkIVv1() {
        frame = new JFrame("To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(870, 670);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 20)); // Increase the font size
        inputField.setPreferredSize(new Dimension(600, 40)); // Increase the width and height

        inputField.addActionListener(e -> addTaskFromInput());

        // Add the inputField to the bottom of the window
        frame.add(inputField, BorderLayout.SOUTH);

        loadTasks();

        frame.setVisible(true);
    }

    private void addTaskFromInput() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            TaskItem task = new TaskItem(taskCounter++, text, false, false);
            taskPanel.add(task);
            taskPanel.revalidate();
            inputField.setText("");
            saveTasks();
        }
    }

    private void loadTasks() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|", 4);
                    if (parts.length == 4) {
                        int id = Integer.parseInt(parts[0]);
                        boolean done = parts[1].equals("1");
                        String text = parts[2];
                        boolean isSubtask = Boolean.parseBoolean(parts[3]);
                        taskCounter = Math.max(taskCounter, id + 1);
                        TaskItem task = new TaskItem(id, text, done, isSubtask);
                        taskPanel.add(task);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error loading tasks");
            }
        }
    }

    private void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Component comp : taskPanel.getComponents()) {
                if (comp instanceof TaskItem) {
                    TaskItem task = (TaskItem) comp;
                    writer.println(task.getId() + "|" + (task.isDone() ? "1" : "0") + "|" + task.getText() + "|" + task.isSubtask());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving tasks");
        }
    }

    private class TaskItem extends JPanel {
        private int id;
        private boolean isSubtask;
        private JCheckBox checkBox;
        private JLabel textLabel;
        private JButton editButton;
        private JButton deleteButton;
        private JButton createSubtaskButton;

        public TaskItem(int id, String text, boolean done, boolean isSubtask) {
            this.id = id;
            this.isSubtask = isSubtask;
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(760, 60));
            setMaximumSize(new Dimension(760, 60));
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            setBackground(isSubtask ? new Color(240, 240, 240) : Color.WHITE);  // Subtasks have a different color

            checkBox = new JCheckBox();
            checkBox.setPreferredSize(new Dimension(30, 30));
            checkBox.setSelected(done);
            checkBox.addActionListener(e -> toggleDone());

            textLabel = new JLabel(text);
            textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            if (done) {
                textLabel.setForeground(Color.GRAY);
                textLabel.setText("<html><strike>" + text + "</strike></html>");
            }

            if (isSubtask) {
                //textLabel.setText("  " + text);  // Add hyphen for subtasks
                setBorder(BorderFactory.createMatteBorder(5, 20, 1, 0, Color.LIGHT_GRAY)); // Indent subtasks
            }

            JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            centerPanel.setOpaque(false);
            centerPanel.add(checkBox);
            centerPanel.add(textLabel);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setOpaque(false);
            editButton = new JButton("Edit");
            deleteButton = new JButton("Delete");

            editButton.addActionListener(e -> editTask());
            deleteButton.addActionListener(e -> deleteTask());

            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);

            add(centerPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.EAST);

            // If it's a main task, add the "Create Subtask" button
            if (!isSubtask) {
                createSubtaskButton = new JButton("Create Subtask");
                createSubtaskButton.addActionListener(e -> createSubtask());
                buttonPanel.add(createSubtaskButton);
            }
        }

        public int getId() { return id; }
        public boolean isDone() { return checkBox.isSelected(); }
        public String getText() {
            return textLabel.getText().replaceAll("<[^>]*>", "");
        }

        public boolean isSubtask() { return isSubtask; }

        private void toggleDone() {
            String plainText = getText();
            if (checkBox.isSelected()) {
                textLabel.setText("<html><strike>" + plainText + "</strike></html>");
                textLabel.setForeground(Color.GRAY);
            } else {
                textLabel.setText(plainText);
                textLabel.setForeground(Color.BLACK);
            }
            saveTasks();
        }

        private void editTask() {
            String newText = JOptionPane.showInputDialog(frame, "Edit Task", getText());
            if (newText != null && !newText.trim().isEmpty()) {
                textLabel.setText(checkBox.isSelected() ? "<html><strike>" + newText.trim() + "</strike></html>" : newText.trim());
                saveTasks();
            }
        }

        private void deleteTask() {
            taskPanel.remove(this);
            taskPanel.revalidate();
            taskPanel.repaint();
            saveTasks();
        }

        private void createSubtask() {
            String subtaskText = JOptionPane.showInputDialog(frame, "Enter Subtask Text");
            if (subtaskText != null && !subtaskText.trim().isEmpty()) {
                TaskItem subtask = new TaskItem(taskCounter++, subtaskText.trim(), false, true);
                taskPanel.add(subtask);
                taskPanel.revalidate();
                saveTasks();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ArkIVv1::new);
    }
}


