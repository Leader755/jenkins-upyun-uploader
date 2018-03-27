package com.huangmb.jenkins.upyun;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.util.ListBoxModel;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import java.util.List;

@Extension
public class UpYunUploadDescriptor extends BuildStepDescriptor<Publisher> {
    public UpYunUploadDescriptor() {
        super(UpYunUploader.class);
        load();
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        save();
        return super.configure(req, json);
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        return true;
    }

    @Override
    public String getDisplayName() {
        return "又拍云上传";
    }

    public ListBoxModel doFillAccountItems() {
        ListBoxModel list = new ListBoxModel();
        list.add("请选择", "-1");
        List<User> users = UpYunConfiguration.get().getUsers();
        for (User user : users) {
            list.add(user.getName());
        }

        return list;
    }
}
