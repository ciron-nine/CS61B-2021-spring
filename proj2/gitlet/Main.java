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
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                if(args.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makeinit();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                if(args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                Repository.makeadd(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                if(args.length == 1) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
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
            default:
                System.out.println("No command with that name exists.");
                break;
        }
    }


}
