package sk.spsepo.ban.fbapp;


public class Post {
    private String image;
    private String UID;
    private String description;
    private String datetime;

    public Post(String image, String UID, String description, String datetime) {
        this.image = image;
        this.UID = UID;
        this.description = description;
        this.datetime = datetime;
    }

    public Post() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}