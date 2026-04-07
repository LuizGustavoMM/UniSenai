package aulabaseweb;

public class AulaBaseWeb {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String mode = args[0].toLowerCase();
        switch (mode) {
            case "server":
                WebServerSocket.main(new String[0]);
                break;
            case "client":
                WebClientSocket.main(new String[0]);
                break;
            default:
                printUsage();
                break;
        }
    }

    private static void printUsage() {
        System.out.println("Uso:");
        System.out.println("  java ... aulabaseweb.AulaBaseWeb server");
        System.out.println("  java ... aulabaseweb.AulaBaseWeb client");
    }
    
}
