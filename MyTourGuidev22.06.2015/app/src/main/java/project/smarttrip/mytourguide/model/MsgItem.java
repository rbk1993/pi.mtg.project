package project.smarttrip.mytourguide.model;

/**
 * Created by invite on 17/06/15.
 */
public class MsgItem {

    private String login;
    private String msg;
    private String timestamp;

    public MsgItem() {
    }

    public MsgItem(String login, String msg, String timestamp) {
        this.login = login;
        this.msg = msg;
        this.timestamp = timestamp;
    }


    public String getLogin() {
        return this.login;
    }

    public String getMsg() { return this.msg ;}

    public String getTimestamp() {return this.timestamp;}

    public void setLogin(String login) {
        this.login = login;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
