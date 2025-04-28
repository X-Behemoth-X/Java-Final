import java.util.*;
import java.io.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Task> pendingTasks = new ArrayList<>();
    static Task[] completedTasks = new Task[100];
    static int completedCount = 0;

    public static void main(String[] args) {
        loadData();
        menu();
        saveData();
    }

    public static void menu() {
        while (true) {
            System.out.println("\n--- Task Manager ---");
            System.out.println("1. View Pending Tasks");
            System.out.println("2. Add New Task");
            System.out.println("3. Mark Task as Completed");
            System.out.println("4. Delete a Task");
            System.out.println("5. View Completed Tasks");
            System.out.println("6. Save and Exit");

            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1 -> viewPendingTasks();
                case 2 -> addTask();
                case 3 -> completeTask();
                case 4 -> deleteTask();
                case 5 -> viewCompletedTasks();
                case 6 -> {
                    return; 
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public static void viewPendingTasks() {
        if (pendingTasks.isEmpty()) {
            System.out.println("No pending tasks!");
            return;
        }

        pendingTasks.sort(Comparator.comparing(Task::getPriority));
        int index = 1;
        for (Task task : pendingTasks) {
            System.out.println(index++ + ". " + task);
        }
    }

    public static void addTask() {
        System.out.print("Enter task name: ");
        String name = scanner.nextLine();

        String priority = getPriorityInput();

        System.out.print("Enter due date (MM/DD/YYYY) or press Enter to skip: ");
        String dueDate = scanner.nextLine();

        pendingTasks.add(new Task(name, priority, dueDate));
        System.out.println("Task added!");
    }

    public static void completeTask() {
        if (pendingTasks.isEmpty()) {
            System.out.println("No tasks to complete.");
            return;
        }
        viewPendingTasks();
        int index = getIntInput("Enter the number of the task to mark as completed: ") - 1;

        try {
            Task completed = pendingTasks.remove(index);
            if (completedCount < completedTasks.length) {
                completedTasks[completedCount++] = completed;
                System.out.println("Task marked as completed!");
            } else {
                System.out.println("Completed tasks list is full.");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid task number. Try again.");
        }
    }

    public static void deleteTask() {
        if (pendingTasks.isEmpty()) {
            System.out.println("No tasks to delete.");
            return;
        }
        viewPendingTasks();
        int index = getIntInput("Enter the number of the task to delete: ") - 1;

        try {
            Task removed = pendingTasks.remove(index);
            System.out.println("Deleted task: " + removed.getName());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid task number. Try again.");
        }
    }

    public static void viewCompletedTasks() {
        if (completedCount == 0) {
            System.out.println("No completed tasks!");
            return;
        }

        for (int i = 0; i < completedCount; i++) {
            System.out.println((i + 1) + ". " + completedTasks[i]);
        }
    }

    public static int getIntInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter an integer.");
            return getIntInput(prompt);
        }
    }

    public static String getPriorityInput() {
        System.out.print("Enter priority (Low, Medium, High): ");
        String priority = scanner.nextLine().trim().toLowerCase();
        if (priority.equals("low") || priority.equals("medium") || priority.equals("high")) {
            return priority.substring(0, 1).toUpperCase() + priority.substring(1);
        } else {
            System.out.println("Invalid priority. Try again.");
            return getPriorityInput();
        }
    }

    public static void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("pending_tasks.dat"))) {
            out.writeObject(pendingTasks);
        } catch (IOException e) {
            System.out.println("Error saving pending tasks: " + e.getMessage());
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("completed_tasks.dat"))) {
            out.writeObject(Arrays.copyOf(completedTasks, completedCount));
        } catch (IOException e) {
            System.out.println("Error saving completed tasks: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("pending_tasks.dat"))) {
            pendingTasks = (ArrayList<Task>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No pending tasks found or error loading. Starting fresh.");
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("completed_tasks.dat"))) {
            Task[] loaded = (Task[]) in.readObject();
            System.arraycopy(loaded, 0, completedTasks, 0, loaded.length);
            completedCount = loaded.length;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No completed tasks found or error loading. Starting fresh.");
        }
    }
}