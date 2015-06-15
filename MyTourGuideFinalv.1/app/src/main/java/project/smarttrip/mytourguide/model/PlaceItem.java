package project.smarttrip.mytourguide.model;

/**
 * Created by invite on 14/06/15.
 */
public class PlaceItem {

    private String title;
    private String description;
    private String open;
    private String longitude;
    private String latitude;
    private String id;

    public PlaceItem() {
    }

    public PlaceItem(String title, String description, String open,
                     String longitude, String latitude, String id) {
        this.title = title;
        this.description = description;
        this.open = open;
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = id;
    }


    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getOpen() {
        return this.open;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {this.description = description; }

    public void setOpen(String open) {this.open = open;}

    public void setLongitude (String longitude) {this.longitude = longitude;}

    public void setLatitude (String latitude) {this.latitude = latitude;}

    public void setId (String id) {this.id = id;}

}
