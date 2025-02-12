package htkc.ebaba.ecom.response;

public class UpdateCartResponse {
    private String success;
    private String msg;
    private boolean error;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public UpdateCartResponse(String success, String msg, boolean error) {
        this.success = success;
        this.msg = msg;
        this.error = error;
    }
}
