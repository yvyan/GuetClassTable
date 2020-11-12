package top.yvyan.guettable.bean;

/**
 * @clear
 */
public class LoginResponse {
    private boolean success;
    private String msg;
    private String data;

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public String getData() {
        return data;
    }
}
