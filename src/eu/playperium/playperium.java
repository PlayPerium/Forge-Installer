package eu.playperium;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class playperium {

    public static void main(String[] args) throws Exception {
        System.out.println("Searching for Forge...");
        setupForgeServer(getForgeVersion(args));
    }

    private static void setupForgeServer(String url) {
        if (url == null) {
            System.out.println("Exiting...");
            System.exit(0);
        } else {
            System.out.println("Downloading Forge... This can take a while.");
            System.out.println(url);
            downloadFile(url, "forge_setup.jar");

            System.out.println("Try to Install Forge Server...");
            ProcessBuilder pb;

            try {
                pb = new ProcessBuilder("java", "-jar", "forge_setup.jar", "--installServer", "1");
                pb.inheritIO();
                pb.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getForgeVersion(String[] args) {
        String version = null;
        String argument = null;

        for(int i = 0; i < args.length; i++) {
            if(args[i].contains("--forge=")) {
                argument = args[i];
                break;
            }
        }

        if (argument == null) {
            System.out.println("Forge Version not found!");
        } else {
            version = argument.replace("--forge=","");
        }

        return version;
    }

    private static void downloadFile(String url, String filename) {
        try {
            URL fileurl = new URL(url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) fileurl.openConnection();
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                ReadableByteChannel rbc = Channels.newChannel(fileurl.openStream());
                FileOutputStream fos = new FileOutputStream(filename);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
