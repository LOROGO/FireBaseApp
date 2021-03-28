package sk.spsepo.ban.fbapp;

public class Person {


        // Variable to store data corresponding
        // to firstname keyword in database
        private String fname;

        // Variable to store data corresponding
        // to lastname keyword in database
        private String status;

        // Variable to store data corresponding
        // to age keyword in database



        // Mandatory empty constructor
        // for use of FirebaseUI


    public Person() {
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

    }
