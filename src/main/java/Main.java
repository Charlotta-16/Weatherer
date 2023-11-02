import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;

public class Main {
    public static JSONObject totalWeatherReport = new JSONObject();

    public void  run() throws IOException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();

        mainPanel.setPreferredSize(new Dimension(450, 300));

        JButton testing = new JButton("This is a test");
        JSONObject weather = getJson(new URL("https://api.weather.gov/points/37.11713922134418,-113.61091493319164"));
        getAllInformation(weather);
        System.out.println(totalWeatherReport);
        JTextArea string = new JTextArea();

        testing.addActionListener(e -> System.out.println(weather.getString(string.getText().toString())));
        mainPanel.add(string);
        mainPanel.add(testing);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public JSONObject getAllInformation(JSONObject initial) throws IOException {
        Iterator<?> keys = initial.keys();
        while(keys.hasNext()) {
            String key = (String)keys.next();
            try {
                if (initial.get(key) instanceof JSONObject || initial.get(key) instanceof JSONArray) {
                    getAllInformation((JSONObject) initial.get(key));
                } else if (((String) initial.get(key)).startsWith("https") || ((String) initial.get(key)).startsWith("http")) {
                    getJson(new URL((String) initial.get(key)));
                } else {
                    totalWeatherReport.put(key, initial.get(key));
                }
            } catch (ClassCastException e) {
                totalWeatherReport.put(key, initial.get(key));
            }
        }
        return totalWeatherReport;
    }

    public static JSONObject getJson(URL url) throws IOException {
        String json = IOUtils.toString(url, Charset.forName("UTF-8"));
        return new JSONObject(json);
    }

    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
