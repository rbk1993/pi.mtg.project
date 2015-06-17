package project.smarttrip.mytourguide.model;

/**
 * Created by invite on 17/06/15.
 */
public class GroupItem {

    private String name;
    private String date;
    private String duree;
    private String horaire;
    private String admin;
    private String id;
    private String membresmax;
    private String membresinscrits;
    private String url;

    public GroupItem() {
    }

    public GroupItem(String name, String date, String duree,
                     String admin, String id, String horaire, String membresmax,
                     String membresinscrits, String url) {
        this.name = name;
        this.date = date;
        this.duree = duree;
        this.admin = admin;
        this.id = id;
        this.membresmax = membresmax;
        this.membresinscrits = membresinscrits;
        this.url = url;
        this.horaire = horaire;
    }


    public String getName() {
        return this.name;
    }

    public String getDate() {
        return this.date;
    }

    public String getDuree() {
        return this.duree;
    }

    public String getAdmin() {
        return this.admin;
    }

    public String getId() {
        return this.id;
    }

    public String getMembresmax() {
        return this.membresmax;
    }

    public String getMembresinscrits() { return this.membresinscrits; }

    public String getUrl() { return this.url; }

    public String getHoraire() { return this.horaire; }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {this.date = date; }

    public void setDuree(String duree) {this.duree = duree;}

    public void setAdmin (String admin) {this.admin = admin;}

    public void setId (String id) {this.id = id;}

    public void setMembresmax (String membresmax) {this.membresmax = membresmax;}

    public void setMembresinscrits (String membresinscrits) {this.membresinscrits = membresinscrits;}

    public void setUrl (String url) {this.url = url;}

    public void setHoraire (String horaire) {this.horaire = horaire;}
}
