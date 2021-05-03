package sk.spsepo.ban.fbapp;

public class Users {
    public String fname;
    public String status;
    public String image;

    public Users(String fname, String status, String image) {
        this.fname = fname;
        this.status = status;
        this.image = image;
    }

    public Users() {
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
