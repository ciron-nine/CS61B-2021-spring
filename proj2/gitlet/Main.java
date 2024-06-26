package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author cirno-nine
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                if(args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makeinit();
                break;
            case "add":
                if(args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makeadd(args[1]);
                break;
            case "commit":
                if(args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makecommit(args[1]);
                break;
            case "rm":
                if(args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makerm(args[1]);
                break;
            case "log":
                if(args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makelog();
                break;
            case "global-log":
                if(args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makeGlobalLog();
                break;
            case "find":
                if(args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makefind(args[1]);
                break;
            case "status":
                if(args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makestatus();
                break;
            case "checkout":
                if(args.length == 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                else if(args.length == 3 && args[1].compareTo("--") != 0){
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                else if(args.length == 4 && args[2].compareTo("--") != 0){
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                else if(args.length > 4){
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                if(args.length == 2) {
                    Repository.make_branchcheckout(args[1]);
                }
                else if(args.length == 3) {
                    Repository.make_headcheckout(args[2]);
                }
                else {
                    Repository.make_commitcheckout(args[1],args[3]);
                }
                break;
            case "branch":
                if(args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makebranch(args[1]);
                break;
            case "rm-branch":
                if(args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makermbranch(args[1]);
                break;
            case "reset":
                if(args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makereset(args[1]);
                break;
            case "merge":
                if(args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makemerge(args[1]);
                break;
            case "add-remote":
                if(args.length != 3) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makeremoteadd(args[1], args[2]);
                break;
            case "rm-remote":
                if(args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makeremoterm(args[1]);
                break;
            case "push":
                if(args.length != 3) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makepush(args[1], args[2]);
                break;
            case "fetch":
                if(args.length != 3) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makefetch(args[1], args[2]);
                break;
            case "pull":
                if(args.length != 3) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makepull(args[1], args[2]);
                break;
            default:
                System.out.println("No command with that name exists.");
                break;
        }
    }


}
