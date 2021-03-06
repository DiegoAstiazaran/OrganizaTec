package itesm.mx.organizatec;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Material implements Serializable {

    private long id;
    private String materialType;
    private String contentType;
    private String name;
    private String topic;
    private String partial;
    private String date;
    private String content;
    private ArrayList<String> images;

    public Material() {}

    public Material(long id, String materialType, String contentType, String name, String topic, String partial, String date, String content) {
        this.id = id;
        this.materialType = materialType;
        this.contentType = contentType;
        this.name = name;
        this.topic = topic;
        this.partial = partial;
        this.date = date;
        this.content = content;
        this.images = null;
    }

    public Material(long id, String materialType, String contentType, String name, String topic, String partial, String date, String content, ArrayList<String> images) {
        this.id = id;
        this.materialType = materialType;
        this.contentType = contentType;
        this.name = name;
        this.topic = topic;
        this.partial = partial;
        this.date = date;
        this.content = content;
        this.images = images;
    }

    public Material(String materialType, String contentType, String name, String topic, String partial, String date, String content) {
        this.materialType = materialType;
        this.contentType = contentType;
        this.name = name;
        this.topic = topic;
        this.partial = partial;
        this.date = date;
        this.content = content;
        this.images = null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public static Comparator<Material> nameComparator = new Comparator<Material>() {

        public int compare(Material m1, Material m2) {
            String name1 = m1.getName().toUpperCase();
            String name2 = m2.getName().toUpperCase();

            return name1.compareTo(name2);

        }};

    public static Comparator<Material> dateComparator = new Comparator<Material>() {

        public int compare(Material s1, Material s2) {
            String myFormat = "EEEE, MMMM dd yyyy";
            DateFormat dateFormat = new SimpleDateFormat(myFormat);

            Date date1 = new Date();
            Date date2 = new Date();

            try {
                date1 = dateFormat.parse(s1.getDate());
                date2 = dateFormat.parse(s2.getDate());
            } catch (Exception e) {

            }

            return date1.compareTo(date2);
        }
    };

}
