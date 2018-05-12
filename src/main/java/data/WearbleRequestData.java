package data;

public class WearbleRequestData {
    private String id;
    private String data;

    public WearbleRequestData() {
    }

    public WearbleRequestData(String id, String data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
