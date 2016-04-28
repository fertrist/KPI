package app.entity;

/**
 * Entity class for Family.
 */
public class Family {
    private int id;
    private String fName;
    private String fPassword;
    private String email;
    /* Secret question and its answer, in case when password was lost. Every user of a given
    family can restore a password this way. Password will be sent on user's email.*/
    private String question;
    private String answer;

    public Family() {
        this.id = 0;
        this.fName = null;
        this.fPassword = null;
        this.email = null;
        this.question = null;
        this.answer = null;
    }

    public Family(Family f) {
        this.id = f.getId();
        this.fName = f.getfName();
        this.fPassword = f.getfPassword();
        this.email = f.getEmail();
        this.question = f.getQuestion();
        this.answer = f.getAnswer();
    }

    public Family(String fName, String fPassword, String email, String question, String answer) {
        this.id = 0;
        this.fName = fName;
        this.fPassword = fPassword;
        this.email = email;
        this.question = question;
        this.answer = answer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfPassword() {
        return fPassword;
    }

    public void setfPassword(String fPassword) {
        this.fPassword = fPassword;
    }

    @Override
    public String toString() {
        return String.format("|%5s|%10s|%10s|10%s|%10s|%10s|", id, fName, fPassword, email,
            question, answer);
    }

    @Override
    public boolean equals(Object o) {
        Family f;
        if (o instanceof Family) {
            f = (Family) o;
            return f.getId() == this.id && f.getfName().equals(this.getfName())
                && f.getfPassword().equals(this.fPassword) && f.getAnswer().equals(this.answer)
                && f.getEmail().equals(this.email) && f.getQuestion().equals(this.question);
        } else return false;
    }
}
