package hello;

import org.json.JSONArray;

public class PlacesSelection {

    private final long id;
    private final String content;

    public PlacesSelection(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

}
