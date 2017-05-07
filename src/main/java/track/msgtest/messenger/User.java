package track.msgtest.messenger;

import java.util.Objects;

/**
 *
 */
public class User {
    private long id;
    private String name;
    private String pass;

    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    @Override
    public boolean equals(Object other) {
        User otherUser = (User) other;
        return this.name.equals(otherUser.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
