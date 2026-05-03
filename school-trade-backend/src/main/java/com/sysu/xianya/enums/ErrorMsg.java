package com.sysu.xianya.enums;

/**
 * 全局错误信息枚举
 *
 * 统一管理项目中的所有业务错误提示信息。
 */
public enum ErrorMsg {

    // --- 用户与认证模块 ---
    ACCOUNT_EXIT("用户已存在"),
    ACCOUNT_Ban("账号已被封禁"),
    ACCOUNT_NOT_EXIT("用户不存在"),
    PASSWORD_IS_NOT_SAME("密码不一致"),
    PASSWORD_RESET_ERROR("修改密码失败"),
    EMAIL_SEND_ERROR("邮件发送失败 请重试"),
    EMAIL_LOGIN_ERROR("登录失败 账号或密码错误"),
    LOGIN_ERROR("登录状态异常，请重新登录"), // <<<--- 在这里添加了新的错误信息
    COOKIE_ERROR("Cookie异常，请重新登录"), // 原有的COOKIE_ERROR提示信息不够清晰，可以保留或用LOGIN_ERROR替代
    REGISTER_ERROR("注册失败"),

    // --- 文件处理模块 ---
    FILE_TYPE_ERROR("文件类型错误 请选择.jpg或.png"),
    FILE_UPLOAD_ERROR("文件上传失败"),
    FILE_NOT_EXIT("文件不存在"),
    FILE_DOWNLOAD_ERROR("文件下载异常"),
    FILE_SIZE_ERROR("文件过大"),

    // --- 通用系统与请求模块 ---
    PARAM_ERROR("参数错误"),
    MISSING_PARAMETER("缺少参数"),
    SYSTEM_ERROR("系统错误，请稍后重试"),
    OPERAT_FREQUENCY("操作频繁 稍后重试"),
    JSON_READ_ERROR("json参数解析错误"),
    FORM_NUMBER_ERROR("表单id错误"),
    REPEAT_COMMIT_ERROR("请勿重复提交"),
    COMMIT_FAIL_ERROR("提交失败"),

    // --- 业务逻辑模块 ---
    FAVORITE_EXIT("您已收藏过该物品");


    private String msg;

    ErrorMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}