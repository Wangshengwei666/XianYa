package com.sysu.xianya.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sysu.xianya.enums.ErrorMsg;

/**
 * 全局统一响应对象 (Response VO)
 *
 * 用于包装所有Controller接口的返回数据，确保前后端交互的数据格式一致。
 * @param <T> data字段的泛型类型
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL) // JSON序列化时，忽略值为null的字段
public class R<T> {

    private Integer status_code; // 状态码 (1: 成功, 0: 失败)
    private String msg;          // 提示信息
    private T data;              // 响应数据

    // --- 成功的静态方法 ---

    /**
     * 返回一个不带数据的成功响应
     */
    public static R success() {
        R r = new R();
        r.setStatus_code(1);
        return r;
    }

    /**
     * 返回一个带数据的成功响应
     * @param data 响应数据
     */
    public static <T> R success(T data) {
        R<T> r = new R<>();
        r.setStatus_code(1);
        r.setData(data);
        return r;
    }

    /**
     * 【新增】返回一个同时带提示信息和数据的成功响应
     * @param msg  成功提示信息, 如 "发布成功"
     * @param data 响应数据
     */
    public static <T> R success(String msg, T data) { // <<<--- 在这里添加了新的success方法
        R<T> r = new R<>();
        r.setStatus_code(1);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    // --- 失败的静态方法 ---

    /**
     * 返回一个带错误信息的失败响应
     * @param errorMsg 错误枚举
     */
    public static R fail(ErrorMsg errorMsg) {
        R r = new R();
        r.setStatus_code(0);
        r.setMsg(errorMsg.getMsg());
        return r;
    }

    /**
     * 返回一个同时带错误信息和数据的失败响应 (例如，返回具体的校验失败字段)
     * @param errorMsg 错误枚举
     * @param data     附加的错误数据
     */
    public static <T> R fail(ErrorMsg errorMsg, T data) {
        R<T> r = new R<>();
        r.setStatus_code(0);
        r.setMsg(errorMsg.getMsg());
        r.setData(data);
        return r;
    }


    // --- Getter 和 Setter ---

    public Integer getStatus_code() {
        return status_code;
    }

    public void setStatus_code(Integer status_code) {
        this.status_code = status_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // --- 构造函数 ---

    public R(Integer status_code, String msg, T data) {
        this.status_code = status_code;
        this.msg = msg;
        this.data = data;
    }

    public R() {
    }
}