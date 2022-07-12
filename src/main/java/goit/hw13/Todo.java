package goit.hw13;

public class Todo {
    private long userId;
    private long id;
    private String title;
    private boolean completed;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return String .format ( "%s Todo [%d] of user [%d] / %s%n",completed ? "+" : "!", id, userId, title, completed );
    }
}
