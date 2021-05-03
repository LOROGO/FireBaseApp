package sk.spsepo.ban.fbapp;

public class Person {


        // Variable to store data corresponding
        // to firstname keyword in database
        private String fname;

        private String image;

        // Variable to store data corresponding
        // to lastname keyword in database
        private String status;

        // Variable to store data corresponding
        // to age keyword in database



        // Mandatory empty constructor
        // for use of FirebaseUI


    public Person() {
    }

    public Person(String fname, String image, String status) {
        this.fname = fname;
        this.image = image;
        this.status = status;
    }

    // Getter and setter method
        public String getFname()
        {
            return fname;
        }
        public void setFname(String fname)
        {
            this.fname = fname;
        }
        public String getStatus()
        {
            return status;
        }
        public void setStatus(String status)
        {
            this.status = status;
        }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
