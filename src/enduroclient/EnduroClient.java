package enduroclient;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class EnduroClient {
    private String serverURL;

    public EnduroClient(String serverURL) {
        this.serverURL = serverURL;
    }

    private HttpURLConnection getConnection(String URL, String method) throws MalformedURLException, IOException {
        URL url = new URL(serverURL + URL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        if (method.equals("POST") || method.equals("PUT")) {
            connection.setDoOutput(true);
        }

        connection.setRequestMethod(method);

        System.out.println(method + ": " + serverURL + URL);

        return connection;
    }

    public ArrayList<String> getRacerTimes(String id, String label) throws MalformedURLException, IOException {
        HttpURLConnection connection = getConnection("/" + label + "/" + id, "GET");
        ArrayList<String> response = new ArrayList<>();

        Scanner reader = new Scanner(connection.getInputStream());

        while (reader.hasNext()) {
            response.add(reader.nextLine());
        }

        connection.disconnect();

        return response;
    }

    public void storeRacerTime(String id, String label, String time) throws MalformedURLException, IOException {
        HttpURLConnection connection = getConnection("/" + label + "/" + id + "/" + time, "PUT");

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

        writer.write(time);
        writer.close();

        Scanner reader = new Scanner(connection.getInputStream());

        while (reader.hasNext()) {
            System.out.println(reader.nextLine());
        }

        connection.disconnect();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            EnduroClient client = new EnduroClient("http://localhost/projects/EnduroServer");

            client.storeRacerTime("1", "start", "12.34.56");

            for (String s : client.getRacerTimes("1", "start")) {
                System.out.println(s);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
