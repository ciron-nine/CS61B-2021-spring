package gitlet;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static gitlet.Utils.*;


/** Represents a gitlet repository.

 *  functions like name to do;
 *  does at a high level.
 *
 *  @author cirno-nine
 */
public class Repository {
    /**
     *  Some dirs: dir use
     *  STAGED_DIR: use to store STAGED "added" FILE
     *  REMOVAL_DIR: use to store STAGED "removed" FILE
     *  LOG_DIR: use to store commit file
     *  BLOP_DIR: use to store file
     *  BRANCH_DIR: use to store branch message
     *  Head_commit_pointer: like name, HEAD reference
     *  NOW_BRANCH: like name, show now branch name
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
    public static final File REMOTE_DIR = join(GITLET_DIR, "remote");
    public static final File BRANCH_DIR = join(GITLET_DIR,"branches");
    public static File Head_commit_pointer = new File(GITLET_DIR + "/" + "head");

    public static File current_branch = new File(GITLET_DIR + "/" + "branch");

    private static void check_gitlet() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
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
        REMOVAL_DIR.mkdir();
        REMOTE_DIR.mkdir();

        File master_branch = new File(BRANCH_DIR + "/" + "master");
        Date first_date = new Date(0);
        String mes = "initial commit";
        String file_name = sha1(mes+first_date.toString());
        Commit init = new Commit(mes,file_name,"","", first_date,null, null);
        File init_commit = new File(LOG_DIR + "/" + file_name);
        writeObject(init_commit, init);
        writeObject(Head_commit_pointer,init);
        writeObject(master_branch,init);
        writeContents(current_branch, "master");
    }

    public static void makeadd(String file_name) {
        check_gitlet();
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
        File remove_file = new File(REMOVAL_DIR + "/" + file_name);
        if(remove_file.exists()) {
            File cur_file = new File(CWD + "/" + file_name);
            writeContents(cur_file,readContentsAsString(remove_file));
            remove_file.delete();
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
        check_gitlet();
        if(message.compareTo("") == 0) {
            System.out.println("Please enter a commit message.");
        }
        Date date = new Date();
        List<String> dir_file = plainFilenamesIn(STAGED_DIR);
        List<String> removal_file = plainFilenamesIn(REMOVAL_DIR);
        if(dir_file.isEmpty() && removal_file.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        String commit_name = sha1(message + date);
        Commit parent_commit = readObject(Head_commit_pointer,Commit.class);
        Commit new_commit = new Commit(message,commit_name, parent_commit.sha_name, "", date,parent_commit, null);
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
        check_gitlet();

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
                File removal_file = new File(REMOVAL_DIR + "/" + file_name);
                if(!removal_file.exists()) {
                    Blop blop = head_commit.map.get(file_name);
                    writeContents(removal_file,blop.data);
                    if(work_remove_file.exists()) {
                        restrictedDelete(work_remove_file);
                    }
                }
            }
        }
    }

    public static void makelog() {
        check_gitlet();

        Commit cur_commit = readObject(Head_commit_pointer,Commit.class);
        cur_commit.print_log();
        cur_commit = cur_commit.parent;
        while(cur_commit != null) {
            cur_commit.print_log();
            cur_commit = cur_commit.parent;
        }
    }

    public static  void makeGlobalLog() {
        check_gitlet();

        List<String> commits = plainFilenamesIn(LOG_DIR);
        for(int i = 0; i < commits.size(); i ++) {
            String commit_name = commits.get(i);
            File commit_file = new File(LOG_DIR + "/" + commit_name);
            Commit cur_commit = readObject(commit_file, Commit.class);
            cur_commit.print_log();
        }
    }

    public static  void makefind(String commit_message) {
        check_gitlet();

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
        check_gitlet();

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
        check_gitlet();

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
        check_gitlet();

        File branch_file = new File(BRANCH_DIR + "/" + branch);
        if(!branch_file.exists()) {
            System.out.println("No such branch exists.");
        }
        else {
            String branch_now = readContentsAsString(current_branch);
            if(branch.compareTo(branch_now) == 0) {
                System.out.println("No need to checkout the current branch.");
            }
            else {
                Commit branch_commit = readObject(branch_file, Commit.class);
                Commit now_commit = readObject(Head_commit_pointer, Commit.class);
                List<String> file_name = plainFilenamesIn(CWD);
                for(int i = 0; i < file_name.size(); i ++) {
                    String now_file_name = file_name.get(i);
                    if(now_commit.map.get(now_file_name) == null && branch_commit.map.get(now_file_name) != null){
                        System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                        return;
                    }
                }
                for(int i = 0; i < file_name.size(); i ++) {
                    String now_file_name = file_name.get(i);
                    File cwd_file = new File(CWD + "/" +now_file_name);
                    if(branch_commit.map.get(now_file_name) == null && now_commit.map.get(now_file_name) !=null) {
                        restrictedDelete(cwd_file);
                    }
                }
                file_name = plainFilenamesIn(STAGED_DIR);
                for(int i = 0; i < file_name.size(); i ++) {
                    String now_file_name = file_name.get(i);
                    File stage_file = new File(STAGED_DIR + "/" +now_file_name);
                    stage_file.delete();
                }

                file_name = plainFilenamesIn(REMOVAL_DIR);
                for(int i = 0; i < file_name.size(); i ++) {
                    String now_file_name = file_name.get(i);
                    File removal_file = new File(REMOVAL_DIR + "/" +now_file_name);
                    removal_file.delete();
                }


                for (Map.Entry<String, Blop> entry : branch_commit.map.entrySet()) {
                    Blop now = entry.getValue();
                    String create_file_name = now.name;
                    File create_file = new File(CWD + "/" + create_file_name);
                    writeContents(create_file,now.data);
                }
                writeObject(Head_commit_pointer,branch_commit);
                writeContents(current_branch,branch);
            }
        }
    }

    public static void make_commitcheckout(String commit, String file_name) {
        check_gitlet();
        Commit cur_commit = null;
        boolean is_exist = false;
        List<String> file_list = plainFilenamesIn(LOG_DIR);
        for(int i = 0; i < file_list.size(); i ++) {
            String commit_name = file_list.get(i);
            String sub = commit_name.substring(0, commit.length());
            if(sub.compareTo(commit) ==  0) {
                File file = new File(LOG_DIR + "/" + commit_name);
                cur_commit = readObject(file, Commit.class);
                is_exist = true;
                break;
            }
        }
        if(!is_exist) {
            System.out.println("No commit with that id exists.");
        }
        else {
            if(cur_commit.map.get(file_name) == null) {
                System.out.println("File does not exist in that commit.");
            }
            else {
                File cwd_file = new File(CWD + "/" + file_name);
                writeContents(cwd_file, cur_commit.map.get(file_name).data);
            }
        }
    }

    public static void makebranch(String branch_name) {
        check_gitlet();

        File branch_file = new File(BRANCH_DIR + "/" + branch_name);
        if(branch_file.exists()) {
            System.out.println("A branch with that name already exists.");
        }
        else {
            Commit cur_commit = readObject(Head_commit_pointer, Commit.class);
            writeObject(branch_file, cur_commit);
        }
    }

    public static void makermbranch(String branch_name) {
        check_gitlet();

        File branch_file = new File(BRANCH_DIR + "/" + branch_name);
        if(!branch_file.exists()) {
            System.out.println("A branch with that name does not exist.");
        }
        else {
            String branch = readContentsAsString(current_branch);
            if(branch.compareTo(branch_name) == 0) {
                System.out.println("Cannot remove the current branch.");
            }
            else {
                branch_file.delete();
            }
        }
    }

    public static void makereset(String commit_id) {
        check_gitlet();

        File log_file = new File(LOG_DIR + "/" + commit_id);
        if(!log_file.exists()) {
            System.out.println("No commit with that id exists.");
        }
        else {
            Commit commit = readObject(log_file, Commit.class);
            String cur_branch = readContentsAsString(current_branch);
            File branch_file = new File(BRANCH_DIR + "/" + cur_branch);
            writeObject(branch_file, commit);
            writeContents(current_branch, "");

            make_branchcheckout(cur_branch);
        }
    }

    public static void makemerge(String other_branch) {
        File other_branch_file = new File(BRANCH_DIR + "/" + other_branch);
        String cur_branch = readContentsAsString(current_branch);
        List<String> stage = plainFilenamesIn(STAGED_DIR);
        List<String> removal = plainFilenamesIn(REMOVAL_DIR);
        if(!stage.isEmpty() || !removal.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        if(!other_branch_file.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if(cur_branch.compareTo(other_branch) == 0) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        Commit split_commit = Commit.lca(cur_branch,other_branch);


        File cur_file = new File(Repository.BRANCH_DIR + "/" +cur_branch);
        File other_file = new File(Repository.BRANCH_DIR + "/" + other_branch);
        Commit cur = readObject(cur_file, Commit.class);
        Commit other = readObject(other_file, Commit.class);
        if(split_commit.sha_name.compareTo(other.sha_name) == 0) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        if(split_commit.sha_name.compareTo(cur.sha_name) == 0) {
            make_branchcheckout(other_branch);
            System.out.println("Current branch fast-forwarded.");
            return;
        }

        List<String> file_list = plainFilenamesIn(CWD);
        for(int i = 0; i < file_list.size(); i ++) {
            String now_file_name = file_list.get(i);
            if(cur.map.get(now_file_name) == null && other.map.get(now_file_name) != null){
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }

        String message = "Merged " + other_branch + " into " + cur_branch + ".";
        Date date = new Date();
        Boolean is_conflict = false;
        String sha_name = sha1(message + date);
        Commit new_commit = new Commit(message, sha_name, cur.sha_name, other.sha_name, date, cur, other);
        for (Map.Entry<String, Blop> entry : split_commit.map.entrySet()) {
            Blop split_blop = entry.getValue();
            String key_name = entry.getKey();
            Blop cur_blop = cur.map.get(key_name);
            Blop other_blop = other.map.get(key_name);
            if(cur_blop == null && other_blop != null && other_blop.data.compareTo(split_blop.data) == 0) {
                continue;
            }
            if(cur_blop != null && cur_blop.data.compareTo(split_blop.data) == 0 && other_blop == null) {
                new_commit.map.remove(key_name);
                restrictedDelete(key_name);
                continue;
            }
            if(cur_blop == null && other_blop != null) {
                String replace_content = "<<<<<<< HEAD\n";
                replace_content += "=======\n";
                replace_content += other_blop.data;
                replace_content += ">>>>>>>\n";
                Blop new_blop = new Blop(key_name, replace_content);
                new_commit.map.put(key_name, new_blop);
                File cwd_file = new File(CWD + "/" + key_name);
                writeContents(cwd_file, replace_content);
                is_conflict = true;
                continue;
            }
            if(other_blop == null && cur_blop != null) {
                String replace_content = "<<<<<<< HEAD\n" + cur_blop.data;
                replace_content += "=======\n";
                replace_content += ">>>>>>>\n";
                Blop new_blop = new Blop(key_name, replace_content);
                new_commit.map.put(key_name, new_blop);
                File cwd_file = new File(CWD + "/" + key_name);
                writeContents(cwd_file, replace_content);
                is_conflict = true;
                continue;
            }
            if(cur_blop == null && other_blop == null) {
                continue;
            }
            if(split_blop.data.compareTo(cur_blop.data) == 0 && split_blop.data.compareTo(other_blop.data) != 0) {
                new_commit.map.put(key_name, other_blop);
                File cwd_file = new File(CWD + "/" + other_blop.name);
                writeContents(cwd_file, other_blop.data);
                continue;
            }
            if(split_blop.data.compareTo(cur_blop.data) != 0 && split_blop.data.compareTo(other_blop.data) == 0) {
                continue;
            }
            if(split_blop.data.compareTo(cur_blop.data) != 0 && split_blop.data.compareTo(other_blop.data) != 0 && cur_blop.data.compareTo(other_blop.data) == 0) {
                continue;
            }
            else {
                String replace_content = "<<<<<<< HEAD\n" + cur_blop.data;
                replace_content += "=======\n";
                replace_content += other_blop.data;
                replace_content += ">>>>>>>\n";
                Blop new_blop = new Blop(key_name, replace_content);
                new_commit.map.put(key_name, new_blop);
                File cwd_file = new File(CWD + "/" + key_name);
                writeContents(cwd_file, replace_content);
                is_conflict = true;
            }
        }
        for (Map.Entry<String, Blop> entry : other.map.entrySet()) {
            String key_name = entry.getKey();
            Blop split_blop = split_commit.map.get(key_name);
            if(split_blop != null) {
                continue;
            }
            else {
                Blop cur_blop = cur.map.get(key_name);
                Blop other_blop = entry.getValue();
                if(cur_blop == null) {
                    new_commit.map.put(key_name, other_blop);
                    File cwd_file = new File(CWD + "/" + other_blop.name);
                    writeContents(cwd_file, other_blop.data);
                    continue;
                }
                if(cur_blop.data.compareTo(other_blop.data) != 0) {
                    String replace_content = "<<<<<<< HEAD\n" + cur_blop.data;
                    replace_content += "=======\n";
                    replace_content += other_blop.data;
                    replace_content += ">>>>>>>\n";
                    Blop new_blop = new Blop(key_name, replace_content);
                    new_commit.map.put(key_name, new_blop);
                    File cwd_file = new File(CWD + "/" + key_name);
                    writeContents(cwd_file, replace_content);
                    is_conflict = true;
                }
            }
        }
        if(is_conflict) {
            System.out.println("Encountered a merge conflict.");
        }
        File commit_file = new File(LOG_DIR + "/" + sha_name);
        File branch_file = new File (BRANCH_DIR + "/" + cur_branch);
        writeObject(commit_file,new_commit);
        writeObject(Head_commit_pointer,new_commit);
        writeObject(branch_file,new_commit);
    }

    public static void makeremoteadd(String remote_name, String remote_dir) {
        File remote_file = new File(REMOTE_DIR + "/" + remote_name);
        if(remote_file.exists()) {
            System.out.println("A remote with that name already exists.");
            return;
        }
        writeContents(remote_file, remote_dir);
    }

    public static void makeremoterm(String remote_name) {
        File remote_file = new File(REMOTE_DIR + "/" + remote_name);
        if(!remote_file.exists()) {
            System.out.println("A remote with that name does not exist.");
            return;
        }
        remote_file.delete();
    }

    public static void makepush(String remote_name, String remote_branch) {
        File remote_file = new File(REMOTE_DIR + "/" + remote_name);
        String cur_to_remote_dir = readContentsAsString(remote_file);
        String remote_gitlet_dir = CWD + "/" +cur_to_remote_dir;
        File remote_gitlet = new File(remote_gitlet_dir);
        if(!remote_gitlet.exists()) {
            System.out.println("Remote directory not found.");
            return;
        }
        File remote_branch_file = new File(remote_gitlet + "/branches/" + remote_branch);
        Commit remote_head = null;
        File cur_commit = null;
        if(remote_branch_file.exists()) {
            remote_head = readObject(remote_branch_file, Commit.class);
            cur_commit = new File(LOG_DIR + "/" + remote_head.sha_name);
        }
        if(remote_branch_file.exists() && !cur_commit.exists()) {
            System.out.println("Please pull down remote changes before pushing.");
            return;
        }
        Commit cur = readObject(Head_commit_pointer, Commit.class);
        Commit now_head = readObject(Head_commit_pointer, Commit.class);
        File remote_log_dir = join(remote_gitlet, "log");
        File remote_branch_dir = join(remote_gitlet, "branches");
        while(now_head != null) {
            File now_commit_file = new File(remote_log_dir + "/" + now_head.sha_name);
            if(now_commit_file.exists()) {
                now_head = now_head.parent;
            }
            else {
                writeObject(now_commit_file, now_head);
                now_head = now_head.parent;
            }
        }
        writeObject(remote_branch_file, cur);
        File remote_head_pointer = new File(remote_gitlet + "/" + "head");
        writeObject(remote_head_pointer, cur);
        File remote_cur_branch = new File(remote_gitlet + "/" + "branch");
        writeContents(remote_cur_branch, remote_branch);
    }

    public static void makefetch(String remote_name, String remote_branch) {
        File remote_file = new File(REMOTE_DIR + "/" + remote_name);
        String cur_to_remote_dir = readContentsAsString(remote_file);
        String remote_gitlet_dir = CWD + "/" +cur_to_remote_dir;
        File remote_gitlet = new File(remote_gitlet_dir);
        if(!remote_gitlet.exists()) {
            System.out.println("Remote directory not found.");
            return;
        }
        File remote_branch_file = new File(remote_gitlet + "/branches/" + remote_branch);
        if(!remote_branch_file.exists()) {
            System.out.println("That remote does not have that branch.");
            return;
        }
        Commit remote_commit = readObject(remote_branch_file, Commit.class);
        File remote_branch_ = new File(BRANCH_DIR + "/" + remote_name);
        if(!remote_branch_.exists()) {
            remote_branch_.mkdir();
        }
        File cur_branch_file = new File(remote_branch_ + "/" + remote_branch);
        Commit cur = remote_commit;
        while(cur != null) {
            File log_file = new File(LOG_DIR + "/" + cur.sha_name);
            if(log_file.exists()) {
                cur = cur.parent;
            }
            else {
                writeObject(log_file, cur);
                cur = cur.parent;
            }
        }
        writeObject(cur_branch_file, remote_commit);

    }

    public static void makepull(String remote_name, String branch_name) {
        makefetch(remote_name, branch_name);
        makemerge(remote_name + "/" +branch_name);
    }
}
