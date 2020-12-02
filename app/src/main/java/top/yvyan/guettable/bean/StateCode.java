package top.yvyan.guettable.bean;

public enum StateCode {

    SUCCESS(0, "登录成功"),
    LOGIN_INVALID(1, "登录失效"),
    NOT_LOGIN(2, "未登录"),

    PASSWORD_ERROR(-1, "密码错误"),
    NETWORK_ERROR(-2, "网络错误"),
    CHECK_CODE_ERROR(-3, "验证码持续错误"),

    CLASS_OK(21, "理论课更新成功"),
    LAB_OK(22, "课内实验更新成功"),
    EXAM_OK(23, "考试安排更新成功");

    private Integer code;
    private String message;

    StateCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
