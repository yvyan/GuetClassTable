package top.yvyan.guettable.Gson;

public class LabTableJWT {
    private Result result;
    private static class Result {
        public String token;
    }
    public String getToken(){
        return result.token;
    }
}
