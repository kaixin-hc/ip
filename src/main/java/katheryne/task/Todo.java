package katheryne.task;

public class Todo extends Task {
    
    // dummy constructor for Jackson
    public Todo() {
        super();
    }

    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[Todo]     " + super.toString();
    }
}