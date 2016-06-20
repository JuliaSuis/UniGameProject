package julia.connectivity.serialization;

/**
 * Created by julia on 18.06.16.
 */
class Meta {
    private String classInfo;
    private String data;

    Meta() {

    }

    Meta(String classInfo, String data) {
        this.classInfo = classInfo;
        this.data = data;
    }

    public String getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(String classInfo) {
        this.classInfo = classInfo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
