package itesm.mx.organizatec;

import java.util.ArrayList;

public class Material {

    private long id;
    private String type;
    private String name;
    private String topic;
    private String partial;
    private String date;
    private String content;
    private ArrayList<byte[]> images;

    public Material() {}

    public Material(long id, String type, String name, String topic, String partial, String date, String content) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.topic = topic;
        this.partial = partial;
        this.date = date;
        this.content = content;
        this.images = null;
    }

    public Material(long id, String type, String name, String topic, String partial, String date, String content, ArrayList<byte[]> images) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.topic = topic;
        this.partial = partial;
        this.date = date;
        this.content = content;
        this.images = images;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPartial() {
        return partial;
    }

    public void setPartial(String partial) {
        this.partial = partial;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<byte[]> getImages() {
        return images;
    }

    public void setImages(ArrayList<byte[]> images) {
        this.images = images;
    }
}
