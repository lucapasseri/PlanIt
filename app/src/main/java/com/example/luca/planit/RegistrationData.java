package com.example.luca.planit;

import java.util.Objects;

/**
 * Created by diego on 22/07/2017.
 */

public class RegistrationData {
    private String email;
    private String password;
    private String username;
    private String name;
    private String surname;
    private String borndate;


    private RegistrationData(String email, String password, String username, String name, String surname, String borndate) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.borndate = borndate;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getBorndate() {
        return borndate;
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


    public static class Builder {
        private String email;
        private String password;
        private String username;
        private String name;
        private String surname;
        private String borndate;

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder setBorndate(String borndate) {
            this.borndate = borndate;
            return this;
        }

        public RegistrationData build() {
            return new RegistrationData(Objects.requireNonNull(email), Objects.requireNonNull(password),
                    Objects.requireNonNull(username), Objects.requireNonNull(name), Objects.requireNonNull(surname), Objects.requireNonNull(borndate));
        }

    }
}
