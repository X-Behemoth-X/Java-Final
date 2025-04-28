import java.io.Serializable;

public class Task implements Serializable {
    private String name;
    private String priority;
    private String dueDate;

    public Task(String name, String priority, String dueDate) {
        this.name = name;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public String getPriority() {
        return priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String toString() {
        return "Task: " + name + " | Priority: " + priority + (dueDate.isEmpty() ? "" : " | Due: " + dueDate);
    }
}