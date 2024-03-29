package gitlet;


import java.io.File;
import java.io.Serializable;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.join;
import static gitlet.Utils.readObject;

/** Represents a gitlet commit object.
 *  parent:  Commit parent  reference
 *  message: commit message
 *  sha_name: use message and timestamp sha cur-commit name
 *  parent_sha_name: as write
 *  map: cast to Blop
 *  instructor: instruct a commit
 *  printlog: format print log
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

    public Commit second_par;
    public String sha_name;

    public String parent_sha_name;
    public static final File STAGED_DIR = join(".gitlet","staged");
    public static final File Blop_Dir = join(".gitlet","blop");
    public Map<String,Blop> map;
    /* TODO: fill in the rest of this class. */
    public Commit(String mes,String sha,String parent_sha,Date time, Commit par, Commit second) {
        timestamp = time;
        message = mes;
        parent = par;
        parent_sha_name  = parent_sha;
        second_par = second;
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

    public static Commit lca(String cur_branch, String other_branch) {
        File cur_file = new File(Repository.BRANCH_DIR + "/" +cur_branch);
        File other_file = new File(Repository.BRANCH_DIR + "/" + other_branch);
        Commit cur = readObject(cur_file, Commit.class);
        Commit other = readObject(other_file, Commit.class);
        while (cur != null && other != null) {
            cur = cur.parent;
            other = other.parent;
        }
        int cur_count = 0;
        int other_count = 0;
        if(cur != null) {
            while(cur != null) {
                cur_count ++;
                cur = cur.parent;
            }
        }
        else {
            while(other != null) {
                other_count ++;
                other = other.parent;
            }
        }
        cur = readObject(cur_file, Commit.class);
        other = readObject(other_file, Commit.class);
        while (cur_count != 0) {
            cur_count --;
            cur = cur.parent;
        }
        while (other_count != 0) {
            other_count --;
            other = other.parent;
        }
        while (other != cur) {
            other = other.parent;
            cur = cur.parent;
        }
        return other;
    }
}
