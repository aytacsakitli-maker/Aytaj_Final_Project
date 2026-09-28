package FinalProject;

public class User {
    private String username;
    private String fullName;
    private String password;
    private String subscriptionType;

    public User(String username, String fullName, String password, String subscriptionType) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.subscriptionType = subscriptionType;
    }

    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getPassword() { return password; }
    public String getSubscriptionType() { return subscriptionType; }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
}
