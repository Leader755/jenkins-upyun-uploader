package com.huangmb.jenkins.upyun;

import org.kohsuke.stapler.DataBoundConstructor;

public class User {
    private String name;
    private String password;

    @DataBoundConstructor
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
