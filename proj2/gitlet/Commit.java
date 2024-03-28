package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.join;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author cirno-nine
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */

    public Commit parent;
    public String message;
    private Date timestamp;

    public String sha_name;

    public String parent_sha_name;
    public static final File STAGED_DIR = join(".gitlet","staged");
    public static final File Blop_Dir = join(".gitlet","blop");
    public Map<String,Blop> map;
    /* TODO: fill in the rest of this class. */
    public Commit(String mes,String sha,String parent_sha,Date time, Commit par) {
        timestamp = time;
        message = mes;
        parent = par;
        parent_sha_name  = parent_sha;
        sha_name = sha;
        if(parent != null) {
            map = new HashMap<String,Blop>(parent.map);
        }
        else {
            map = new HashMap<String,Blop >();

        }
    }
    public void print_log() {
        SimpleDateFormat ocean_time = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z",Locale.US);
        ocean_time.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        System.out.println("===");
        System.out.println("commit " + sha_name);
        System.out.println("Date: " + ocean_time.format(timestamp));
        System.out.println(message);
        System.out.println();
    }
}
