package bean;

/**
 * Created by Administrator on 2017/9/24.
 */

public class QuanZiBean {
    private String title;
    private  String newirong;

    public QuanZiBean(String title, String newirong) {
        this.title = title;
        this.newirong = newirong;
    }

    public QuanZiBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewirong() {
        return newirong;
    }

    public void setNewirong(String newirong) {
        this.newirong = newirong;
    }
}
