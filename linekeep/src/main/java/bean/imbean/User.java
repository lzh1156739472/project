package bean.imbean;


import cn.bmob.v3.BmobUser;
import db.NewFriend;

/**
 * @author :smile
 * @project:User
 * @date :2016-01-22-18:11
 */
public class User extends BmobUser {
    private double  longitude;//经度
    private double   latitude;//纬度


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private String avatar;

    private String job;
    private Boolean LoginState;
    private Boolean isFromTuiChu;
    private String Pic;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public Boolean getFromTuiChu() {
        return isFromTuiChu;
    }

    public void setFromTuiChu(Boolean fromTuiChu) {
        isFromTuiChu = fromTuiChu;
    }

    public Boolean getLoginState() {
        return LoginState;
    }

    public void setLoginState(Boolean loginState) {
        LoginState = loginState;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }


    public User(){}

    public User(NewFriend friend){
        setObjectId(friend.getUid());
        setUsername(friend.getName());
        setAvatar(friend.getAvatar());
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
