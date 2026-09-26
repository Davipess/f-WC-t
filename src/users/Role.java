package users;

/**
 * User roles in the system.
 */
public enum Role {
    STUDENT("Student"),
    ADMIN("Administrator");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    /**
     * Gets the description of the role.
     * @return role description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Parses a role from string.
     * @param text input string
     * @return matching Role, or STUDENT by default
     */
    public static Role fromString(String text) {
        if (text == null) {
            return STUDENT;
        }
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(text.trim()) || r.description.equalsIgnoreCase(text.trim())) {
                return r;
            }
        }
        return STUDENT;
    }
}
