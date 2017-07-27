package com.example.luca.planit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by diego on 22/07/2017.
 */

public class AccountImpl implements Account {
    private final String email;
    private final String password;
    private final String username;
    private final String name;
    private final String surname;
    private final String id;
    private final String borndate;

    public AccountImpl(String email, String password, String username, String name, String surname, String id,
                       String borndate) {
        super();
        this.email = email;
        this.password = password;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.borndate = borndate;
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

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getId() {
        return id;
    }

    public String getBornDate(DateFormatType formatType) {
        if(formatType == null){
            switch (formatType) {

                case DD_MM_YYYY_BACKSLASH:
                    SimpleDateFormat fromFormatter = new SimpleDateFormat(DateFormatType.YYYY_MM_DD_DASH.getFormat(), Locale.US);
                    SimpleDateFormat toFormatter  = new SimpleDateFormat(DateFormatType.DD_MM_YYYY_BACKSLASH.getFormat(), Locale.US);

                    String formattedDate = borndate;

                    try {
                        Date toFormatDate = fromFormatter.parse(formattedDate);
                        formattedDate = toFormatter.format(toFormatDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    return formattedDate;

                default:
                    return borndate;
            }
        }else {
            return borndate;
        }


    }


    @Override
    public String toString() {
        return "AccountImpl [email=" + email + ", password=" + password + ", username=" + username + ", name=" + name
                + ", surname=" + surname + ", id=" + id + ", borndate=" + borndate + "]";
    }


    public static class Builder {
        private String email;
        private String password;
        private String username;
        private String name;
        private String surname;
        private String id;
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

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setBorndate(String borndate) {
            this.borndate = borndate;
            return this;
        }

        public Account build() {
            return new AccountImpl(Objects.requireNonNull(email), Objects.requireNonNull(password),
                    Objects.requireNonNull(username), Objects.requireNonNull(name), Objects.requireNonNull(surname),
                    Objects.requireNonNull(id), Objects.requireNonNull(borndate));
        }

    }
}