package top.yvyan.guettable.Http;

import java.net.HttpURLConnection;

public class HttpConnectionAndCode {
    public HttpURLConnection c;
    public int code;
    public String comment;
    public int resp_code;
    public String cookie;
    public Object obj;

    public HttpConnectionAndCode(int code_) {
        c = null;
        comment = null;
        code = code_;
        resp_code = 0;
        cookie = null;
        obj = null;
    }

    public HttpConnectionAndCode(HttpURLConnection cn, int code_, String comm) {
        c = cn;
        code = code_;
        comment = comm;
        resp_code = 0;
        cookie = null;
        obj = null;
    }

    public HttpConnectionAndCode(HttpURLConnection cn, int code_, String comm, String cookie_, int resp_code_) {
        c = cn;
        code = code_;
        comment = comm;
        resp_code = resp_code_;
        cookie = cookie_;
        obj = null;
    }
}
