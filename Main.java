import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private int taskId;
    private String taskName;
    private String description;
    private String deadline;

    public Task(int taskId, String taskName, String description, String deadline) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.deadline = deadline;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", deadline='" + deadline + '\'' +
                '}';
    }
}

class TaskManager {
    private ArrayList<Task> tasks = new ArrayList<>();
    private final String FILE_PATH = "tasks.txt";

    public void addTask(Task task) {
        tasks.add(task);
        saveTasksToFile();
    }

    public void removeTask(int index) {
        tasks.remove(index);
        saveTasksToFile();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void viewTasks() {
        JFrame frame = new JFrame("Tasks");
        frame.setSize(600, 300);
        frame.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Task Name", "Description", "Deadline"};
        Object[][] data = new Object[tasks.size()][4];

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            data[i][0] = task.getTaskId();
            data[i][1] = task.getTaskName();
            data[i][2] = task.getDescription();
            data[i][3] = task.getDeadline();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.setBackground(new Color(235, 240, 245));
        table.setSelectionBackground(new Color(51, 153, 255));
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);

        JButton removeButton = new JButton("Remove Task");
        removeButton.setBackground(new Color(255, 51, 51));
        removeButton.setForeground(Color.WHITE);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    removeTask(selectedRow);
                    model.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a task to remove.");
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(235, 240, 245));
        buttonPanel.add(removeButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void saveTasksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadTasksFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            tasks = (ArrayList<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    private static TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        taskManager.loadTasksFromFile();
        setLookAndFeel();
        createGUI();
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("OptionPane.background", new Color(235, 240, 245));
            UIManager.put("Panel.background", new Color(235, 240, 245));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createGUI() {
        JFrame frame = new JFrame("Task Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 0, 10));
        buttonPanel.setBackground(new Color(235, 240, 245));

        JButton addButton = new JButton("Add Task");
        addButton.setBackground(new Color(102, 204, 255));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });
        buttonPanel.add(addButton);

        JButton viewButton = new JButton("View Tasks");
        viewButton.setBackground(new Color(102, 204, 255));
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskManager.viewTasks();
            }
        });
        buttonPanel.add(viewButton);

        JButton removeButton = new JButton("Remove Task");
        removeButton.setBackground(new Color(255, 51, 51));
        removeButton.setForeground(Color.WHITE);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Please view tasks and select a task to remove.");
            }
        });
        buttonPanel.add(removeButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(255, 102, 102));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskManager.saveTasksToFile();
                System.exit(0);
            }
        });
        buttonPanel.add(exitButton);

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void addTask() {
        JFrame addFrame = new JFrame("Add Task");
        addFrame.setSize(300, 200);
        addFrame.setLayout(new GridLayout(4, 2));
        addFrame.getContentPane().setBackground(new Color(235, 240, 245));

        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField deadlineField = new JTextField();
        JButton addButton = new JButton("Add");

        addFrame.add(new JLabel("Task Name:"));
        addFrame.add(nameField);
        addFrame.add(new JLabel("Description:"));
        addFrame.add(descriptionField);
        addFrame.add(new JLabel("Deadline:"));
        addFrame.add(deadlineField);
        addFrame.add(new JLabel()); // Empty label for spacing
        addFrame.add(addButton);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String description = descriptionField.getText();
                String deadline = deadlineField.getText();
    
                Task task = new Task(taskManager.getTasks().size() + 1, name, description, deadline);
                taskManager.addTask(task);
                addFrame.dispose();
            }
        });
    
        addFrame.setVisible(true);
    }
}    