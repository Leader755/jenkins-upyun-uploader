package com.huangmb.jenkins.upyun;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import main.java.com.UpYun;
import main.java.com.upyun.UpException;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class UpYunUploader extends Notifier {
    private String account;
    private String bucketName;
    private String remotePath;
    private String filePath;

    @DataBoundConstructor
    public UpYunUploader(String account, String bucketName, String remotePath, String filePath) {
        this.account = account;
        this.bucketName = bucketName;
        this.remotePath = remotePath;
        this.filePath = filePath;
    }

    public String getAccount() {
        return account;
    }

    @DataBoundSetter
    public void setAccount(String account) {
        this.account = account;
    }

    public String getBucketName() {
        return bucketName;
    }

    @DataBoundSetter
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getRemotePath() {
        return remotePath;
    }

    @DataBoundSetter
    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getFilePath() {
        return filePath;
    }

    @DataBoundSetter
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public UpYunUploadDescriptor getDescriptor() {
        return (UpYunUploadDescriptor) super.getDescriptor();
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        PrintStream logger = listener.getLogger();
        if (build.getResult() != Result.SUCCESS) {
            logger.println("构建不成功，跳过upyun上传");
            return true;
        }
        EnvVars env = build.getEnvironment(listener);
        String remote = env.expand(remotePath);
        String uploadFilePath = env.expand(filePath);
        FilePath path = build.getWorkspace().child(uploadFilePath);
        if (!path.exists()) {
            logger.println("上传文件不存在");
            return false;
        }
        logger.println("上传的文件路径为：" + path.getRemote());
        File uploadFile = new File(path.getRemote());
        User user = findUser(account);
        if (user == null) {
            logger.println("用户" + account + "不存在");
            return false;
        }
        UpYun upYun = new UpYunWithMd5Pwd(bucketName, user.getName(), user.getPassword());
        upYun.setTimeout(60 * 3);
        try {
            boolean result = upYun.writeFile(remote, uploadFile, true);
            if (result) {
                logger.println("文件上传成功");
            } else {
                logger.println("文件上传失败");
            }
        } catch (Exception e) {
            logger.println("文件上传失败");
            logger.println(e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    private User findUser(String account) {
        List<User> users = UpYunConfiguration.get().getUsers();
        for (User user : users) {
            if (StringUtils.equals(account, user.getName())) {
                return user;
            }
        }
        return null;
    }
}
