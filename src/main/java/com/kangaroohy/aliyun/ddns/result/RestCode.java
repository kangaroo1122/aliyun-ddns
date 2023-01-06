package com.kangaroohy.aliyun.ddns.result;

/**
 * @author kangaroo hy
 * @version 0.0.1
 * @since 2020/3/13
 */
public enum RestCode {
    //运行时错误
    RC_1(-1, "未知错误，请联系管理员"),
    //操作成功
    RC200(200, "操作成功"),
    //创建成功
    RC201(201, "创建成功"),
    //请求参数错误、不合法
    RC400(400, "请求参数错误"),
    //需要登录才能访问
    RC401(401, "请登录后再访问"),
    //没有访问权限，禁止访问
    RC403(403, "禁止访问"),
    //界面不存在
    RC404(404, "未知请求"),
    //不支持此方法
    RC500(500, "请求方法错误");

    private Integer code;
    private String msg;

    RestCode(){
    }

    RestCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
