package com.huangmb.jenkins.upyun;

import main.java.com.UpYun;

public class UpYunWithMd5Pwd extends UpYun {

    /**
     * 初始化 UpYun 存储接口
     *
     * @param bucketName 空间名称
     * @param userName   操作员名称
     * @param password   密码
     * @return UpYun object
     */
    public UpYunWithMd5Pwd(String bucketName, String userName, String password) {
        super(bucketName, userName, "");
        this.password = password;
    }
}
