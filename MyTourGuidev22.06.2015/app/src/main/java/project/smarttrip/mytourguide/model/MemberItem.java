package project.smarttrip.mytourguide.model;

/**
 * Created by invite on 17/06/15.
 */
public class MemberItem {

    private String prenom;
    private String nom;
    private String birthday;
    private String id;

    public MemberItem() {
    }

    public MemberItem(String prenom, String nom, String birthday, String id) {
        this.prenom = prenom;
        this.nom = nom;
        this.birthday = birthday;
        this.id = id;
    }


    public String getNom() {
        return this.nom;
    }

    public String getPrenom() { return this.prenom; }

    public String getBirthday() {return this.birthday;}

    public String getId() {
        return this.id;
    }

    public void setNom(String nom) {
        this.nom=nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setId (String id) {this.id = id;}


}
