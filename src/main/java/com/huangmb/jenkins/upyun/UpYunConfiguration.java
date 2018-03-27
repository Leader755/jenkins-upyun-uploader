package com.huangmb.jenkins.upyun;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import java.util.ArrayList;
import java.util.List;

@Extension
public class UpYunConfiguration extends GlobalConfiguration {
    private List<User> users = new ArrayList<>();
    public static UpYunConfiguration get() {
        return GlobalConfiguration.all().get(UpYunConfiguration.class);
    }

    public UpYunConfiguration() {
        load();
    }

    public List<User> getUsers() {
        return users;
    }

    @DataBoundSetter
    public void setUsers(List<User> users) {
        this.users = users;
        save();
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this,json);
        save();
        return super.configure(req, json);
    }

    public FormValidation doCheckName(@QueryParameter String name) {
        if (StringUtils.isBlank(name)) {
            return FormValidation.error("用户名不能为空");
        }
        return FormValidation.ok();
    }
    public FormValidation doCheckPassword(@QueryParameter String password) {
        if (StringUtils.isBlank(password)) {
            return FormValidation.error("密码不能为空");
        }
        return FormValidation.ok();
    }
}
