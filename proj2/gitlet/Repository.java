package gitlet;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static final File STAGED_DIR = join(GITLET_DIR,"staged");

    public static final File LOG_DIR = join(GITLET_DIR,"log");

    public static final File REMOVAL_DIR = join(GITLET_DIR, "removal");
    public static final File BLOP_DIR = join(GITLET_DIR,"blop");

    public static final File BRANCH_DIR = join(GITLET_DIR,"branches");
    public static File Head_commit_pointer = new File(GITLET_DIR + "/" + "head");

    public static File current_branch = new File(GITLET_DIR + "/" + "branch");
    public static void makeinit() {
        if(GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        GITLET_DIR.mkdir();
        STAGED_DIR.mkdir();
        BLOP_DIR.mkdir();
        LOG_DIR.mkdir();
        REMOVAL_DIR.mkdir();
        BRANCH_DIR.mkdir();

        File master_branch = new File(BRANCH_DIR + "/" + "master");
        Date first_date = new Date(0);
        String mes = "initial commit";
        String file_name = sha1(mes+first_date.toString());
        Commit init = new Commit(mes,file_name,"",first_date,null);
        File init_commit = new File(LOG_DIR + "/" + file_name);
        writeObject(init_commit, init);
        writeObject(Head_commit_pointer,init);
        writeObject(master_branch,init);
        writeContents(current_branch, "master");
    }

    public static void makeadd(String file_name) {
        List<String> dir_file = plainFilenamesIn(CWD);
        boolean is_have = false;
        for(int i = 0; i < dir_file.size(); i ++) {
            if(dir_file.get(i).compareTo(file_name) == 0) {
                is_have = true;
                break;
            }
        }
        if(!is_have) {
            System.out.println("File does not exist.");
            return;
        }
        File added_file = new File(CWD + "/" + file_name);
        Commit head = readObject(Head_commit_pointer,Commit.class);
        if(head.map.get(file_name) == null ) {
           File stage_file = new File(STAGED_DIR + "/" + file_name);
           writeContents(stage_file,readContentsAsString(added_file));
        }
        else {
           String data = readContentsAsString(added_file);
           Blop blop = new Blop(file_name,data);
           Blop head_blop = head.map.get(file_name);
           if(head_blop != null && head_blop.name.compareTo(blop.name) == 0 && head_blop.data.compareTo(blop.data) == 0) {
               File staged_file = new File(STAGED_DIR + "/" + file_name);
               if(staged_file.exists()) {
                   staged_file.delete();
               }
           }
           else {
               File stage_file = new File(STAGED_DIR + "/" + file_name);
               writeContents(stage_file,readContentsAsString(added_file));
           }
        }
    }

    public static void makecommit(String message) {
        Date date = new Date();
        List<String> dir_file = plainFilenamesIn(STAGED_DIR);
        List<String> removal_file = plainFilenamesIn(REMOVAL_DIR);
        if(dir_file.isEmpty() && removal_file.isEmpty()) {
            System.out.println("If no files have been staged, abort. Print the message No changes added to the commit.");
            System.exit(0);
        }
        String commit_name = sha1(message + date);
        Commit parent_commit = readObject(Head_commit_pointer,Commit.class);
        Commit new_commit = new Commit(message,commit_name, parent_commit.sha_name, date,parent_commit);
        for(int i = 0; i < dir_file.size(); i ++) {
            String file_name = dir_file.get(i);
            File file = new File(STAGED_DIR + "/" + file_name);
            String data = readContentsAsString(file);
            Blop blop = new Blop(file_name,data);
            String blop_name = sha1(file_name + data);
            File blop_file = new File(BLOP_DIR + "/" + blop_name);
            writeObject(blop_file,blop);
            new_commit.map.put(file_name, blop);
            file.delete();
        }
        dir_file = plainFilenamesIn(REMOVAL_DIR);
        for(int i = 0; i < dir_file.size(); i ++) {
            String file_name = dir_file.get(i);
            new_commit.map.remove(file_name);
            File file = new  File(REMOVAL_DIR + "/"+ file_name);
            file.delete();
        }
        File commit_file = new File(LOG_DIR + "/" + commit_name);
        String cur_branch = readContentsAsString(current_branch);
        File branch_file = new File(BRANCH_DIR + "/" + cur_branch);
        writeObject(commit_file,new_commit);
        writeObject(Head_commit_pointer,new_commit);
        writeObject(branch_file,new_commit);
    }

    public static void makerm(String file_name) {
        Commit head_commit = readObject(Head_commit_pointer, Commit.class);
        File stage_remove_file = new File(STAGED_DIR + "/" + file_name);
        File work_remove_file = new File(CWD + "/" + file_name);
        if(stage_remove_file.exists()) {
            stage_remove_file.delete();
        }
        else {
            if(head_commit.map.get(file_name) == null) {
                System.out.println("No reason to remove the file.");
            }
            else {
                if(work_remove_file.exists()) {
                    File removal_file = new File(REMOVAL_DIR + "/" + file_name);
                    writeContents(removal_file,readContentsAsString(work_remove_file));
                    restrictedDelete(work_remove_file);
                }
            }
        }
    }

    public static void makelog() {
        Commit cur_commit = readObject(Head_commit_pointer,Commit.class);
        cur_commit.print_log();
        cur_commit = cur_commit.parent;
        while(cur_commit != null) {
            cur_commit.print_log();
            cur_commit = cur_commit.parent;
        }
    }

    public static  void makeGlobalLog() {
        List<String> commits = plainFilenamesIn(LOG_DIR);
        for(int i = 0; i < commits.size(); i ++) {
            String commit_name = commits.get(i);
            File commit_file = new File(LOG_DIR + "/" + commit_name);
            Commit cur_commit = readObject(commit_file, Commit.class);
            cur_commit.print_log();
        }
    }

    public static  void makefind(String commit_message) {
        List<String> commits = plainFilenamesIn(LOG_DIR);
        boolean is_have = false;
        for(int i = 0; i < commits.size(); i ++) {
            String commit = commits.get(i);
            File commit_file = new File(LOG_DIR + "/" + commit);
            Commit cur = readObject(commit_file, Commit.class);
            if(cur.message.compareTo(commit_message) == 0) {
                is_have = true;
                System.out.println(cur.sha_name);
            }
        }
        if(!is_have) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void makestatus() {
        String cur_branch = readContentsAsString(current_branch);
        System.out.println("=== Branches ===");
        List<String> file_name = plainFilenamesIn(BRANCH_DIR);
        for(int i = 0; i < file_name.size(); i ++) {
            String now_name = file_name.get(i);
            if(now_name.compareTo(cur_branch) == 0) {
                System.out.print("*");
            }
            System.out.println(now_name);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        file_name = plainFilenamesIn(STAGED_DIR);
        for(int i = 0; i < file_name.size(); i ++) {
            String now_name = file_name.get(i);
            System.out.println(now_name);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        file_name = plainFilenamesIn(REMOVAL_DIR);
        for(int i = 0; i < file_name.size(); i ++) {
            String now_name = file_name.get(i);
            System.out.println(now_name);
        }
        System.out.println();

        Commit now_commit = readObject(Head_commit_pointer, Commit.class);
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (Map.Entry<String, Blop> entry : now_commit.map.entrySet()) {
            String map_file_name = entry.getKey();
            File cwd_file = new File(CWD + "/" + map_file_name);
            if(!cwd_file.exists()) {
                File removal_file = new File(REMOVAL_DIR + "/" + map_file_name);
                if(removal_file.exists()) {
                    continue;
                }
                else {
                    System.out.println(map_file_name + " (deleted)" );
                }
            }
            else {
                String data = readContentsAsString(cwd_file);
                if(data.compareTo(entry.getValue().data) == 0) {
                    continue;
                }
                else {
                    File added_file = new File(STAGED_DIR + "/" + map_file_name);
                    if(added_file.exists()) {
                        String staged_data = readContentsAsString(added_file);
                        if(staged_data.compareTo(entry.getValue().data) == 0) {
                            continue;
                        }
                        else  {
                            System.out.println(map_file_name+ " (modified)");
                        }
                    }
                    else {
                        System.out.println(map_file_name + " (modified)");
                    }
                }
            }
        }
        System.out.println();

        System.out.println("=== Untracked Files ===");
        file_name = plainFilenamesIn(CWD);
        for(int i = 0; i < file_name.size(); i ++) {
            String now_file_name = file_name.get(i);
            File staged_file = new File(STAGED_DIR + "/" + now_file_name);
            if(now_commit.map.get(now_file_name) == null && !staged_file.exists()){
                System.out.println(now_file_name);
            }
        }
        System.out.println();
    }

    public static void make_headcheckout(String file_name) {
        Commit cur_commit = readObject(Head_commit_pointer,Commit.class);
        if(cur_commit.map.get(file_name) == null) {
            System.out.println("File does not exist in that commit.");
        }
        else {
            Blop blop = cur_commit.map.get(file_name);
            File file = new File(CWD + "/" + blop.name);
            writeContents(file,blop.data);
        }
    }

    public static void make_branchcheckout(String branch) {

    }

    public static void make_commitcheckout(String commit, String file_name) {
        File commit_file = new File(LOG_DIR + "/" + commit);
        if(!commit_file.exists()) {
            System.out.println("No commit with that id exists.");
        }
        else {
            Commit cur_commit = readObject(commit_file, Commit.class);
            if(cur_commit.map.get(file_name) == null) {
                System.out.println("File does not exist in that commit.");
            }
            else {
                Blop blop = cur_commit.map.get(file_name);
                File file = new File(CWD + "/" + blop.name);
                writeContents(file,blop.data);
            }
        }
    }
    /* TODO: fill in the rest of this class. */
}
