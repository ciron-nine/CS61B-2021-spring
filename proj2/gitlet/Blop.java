package gitlet;

import java.io.Serializable;

public class Blop implements Serializable {
    public String name;
    public String data;

    public Blop(String nam, String dat) {
        data = dat;
        name = nam;
    }
}
