package com.example.luca.planit;

/**
 * Created by diego on 22/07/2017.
 */

public class LoginData {
    private String email;
    private String password;
    private String username;

    private LoginData(String email, String username, String password) {
            this.email = email;
            this.username = username;
            this.password = password;

    }

    public static LoginData getLoginDataInstanceByUsername(String username, String password) {
        return new LoginData("", username, password);
    }

    public static LoginData getLoginDataInstanceByEmail(String email, String password) {
        return new LoginData(email, "", password);
    }

    public boolean isUsernameLoginData(){
        return this.email.isEmpty();
    }
    public boolean isMailLoginData(){
        return this.username.isEmpty();
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
