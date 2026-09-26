package users;

/**
 * User implementation.
 */
public class UserClass implements User {

    private final String id;
    private final String name;
    private final Role role;
    private String password;
    private int reviewCount;
    private int alertCount;

    /**
     * Constructs a new User instance with password.
     *
     * @param id       the unique identifier for the user
     * @param name     the display name of the user
     * @param role     the user's role in the system
     * @param password the password for authentication
     */
    public UserClass(String id, String name, Role role, String password) {
        if (id != null) {
            this.id = id.trim().toLowerCase();
        } else {
            this.id = "";
        }
        if (name != null) {
            this.name = name.trim();
        } else {
            this.name = "";
        }
        if (role != null) {
            this.role = role;
        } else {
            this.role = Role.STUDENT;
        }
        if (password != null && !password.trim().isEmpty()) {
            this.password = password.trim();
        } else {
            this.password = "1234";
        }
        this.reviewCount = 0;
        this.alertCount = 0;
    }

    /**
     * Constructs a new User instance with default password "1234".
     *
     * @param id   the unique identifier for the user
     * @param name the display name of the user
     * @param role the user's role in the system
     */
    public UserClass(String id, String name, Role role) {
        this(id, name, role, "1234");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public boolean validatePassword(String candidate) {
        if (candidate == null) {
            return false;
        }
        return this.password.equals(candidate.trim());
    }

    @Override
    public void setPassword(String newPassword) {
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            this.password = newPassword.trim();
        }
    }

    @Override
    public int getReviewCount() {
        return reviewCount;
    }

    @Override
    public int getAlertCount() {
        return alertCount;
    }

    @Override
    public void incrementReviewCount() {
        this.reviewCount++;
    }

    @Override
    public void incrementAlertCount() {
        this.alertCount++;
    }
}
