package elderlycare.DAO.Entities;

public enum TodoStatus {
    PENDING("PENDING"),
    COMPLETED("COMPLETED"),
    IN_PROGRESS("IN_PROGRESS");

    private final String value;

    TodoStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}