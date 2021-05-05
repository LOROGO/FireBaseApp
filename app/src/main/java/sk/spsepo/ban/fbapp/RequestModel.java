package sk.spsepo.ban.fbapp;

public class RequestModel {
    String fname;
    String image;

    public RequestModel(String fname, String image) {
        this.fname = fname;
        this.image = image;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public RequestModel() {

    }

}
