package com.github.lucasaquiles.domain;

import java.io.Serializable;
import java.util.Objects;

public class BasicInformation implements Serializable {

    private String name;
    private String email;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicInformation that = (BasicInformation) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }

    @Override
    public String toString() {
        return "BasicInformation{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
