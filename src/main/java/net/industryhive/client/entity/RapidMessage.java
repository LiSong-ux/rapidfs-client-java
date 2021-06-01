package net.industryhive.client.entity;

/**
 * @Author 未央
 * @Create 2020-11-01 18:46
 */
public class RapidMessage {
    private int status;
    private String result;

    public RapidMessage(int status, String result) {
        this.status = status;
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
