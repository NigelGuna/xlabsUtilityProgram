import java.io.*;

public class ColorTagIdentifier {
    public static String consoleOutput = null;

    public static void searchFile(File file) {
        int fileCounter = 0;

        //CREATE STREAMS TO CAPTURE SYSTEM CONSOLE LOG
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        //BACKUP OLD System.out
        PrintStream old = System.out;
        System.setOut(ps);

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {

                if((files[fileCounter].getName().equals(".git")) || (files[fileCounter].getName().equals("node_modules")) || (files[fileCounter].getName().equals("dist"))){
                    fileCounter++;
                    continue;
                }
                else {
                    fileCounter++;
                }

                searchFile(f);

                if (f.getName().equals("variables.scss")){
                    continue;
                }

                if (f.getName().endsWith(".scss") || f.getName().endsWith(".css")){
                    System.out.println("Current file path is: " + f.getAbsolutePath());

                    try {
                        BufferedReader br = new BufferedReader(new FileReader(f));
                        int lineCounter = 0;
                        String line = null;
                        while((line = br.readLine()) != null){
                            lineCounter++;
                            if(line.contains("rgb") || line.contains("rgba") || line.contains("#")){
                                if((line.contains("{")) || line.contains("//")){
                                    continue;
                                }
                                System.out.println("Violation found on line " + lineCounter + ": " + line);
                            }
                        }
                    }
                    catch (Exception ex){
                        System.out.println(ex);
                    }
                    System.out.println("-----------------------------------------------------");
                    continue;
                }
            }
        }
        System.out.flush();
        System.setOut(old);
        System.out.println(baos.toString());
        consoleOutput = baos.toString();
    }

    public static void checkCssFiles(File file){
        consoleOutput = null;
        //CREATE STREAMS TO CAPTURE SYSTEM CONSOLE LOG
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        //BACKUP OLD System.out
        PrintStream old = System.out;
        System.setOut(ps);

        System.out.println("======================");
        System.out.println("|                                                    |");
        System.out.println("|             CSS FILE CHECK             |");
        System.out.println("|                                                    |");
        System.out.println("======================");

        int fileCounter = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {

                if((files[fileCounter].getName().equals(".git")) || (files[fileCounter].getName().equals("node_modules")) || (files[fileCounter].getName().equals("dist"))){
                    fileCounter++;
                    continue;
                }
                else {
                    fileCounter++;
                }

                if (f.getName().endsWith(".css")){
                    System.out.println("CSS file found at: " + f.getAbsolutePath());
                    continue;
                }
            }
        }
        System.out.flush();
        System.setOut(old);
        System.out.println(baos.toString());
        consoleOutput = baos.toString();
    }

}

