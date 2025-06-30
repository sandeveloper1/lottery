package src;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LotteryHtmlGenerator {
    public static void main(String[] args) throws Exception {
        // Find all JSON files in the current directory (except index.html and template.html)
        File dir = new File(".");
        File[] jsonFiles = dir.listFiles((d, name) -> name.endsWith(".json") && !name.equals("index.html") && !name.equals("template.html"));
        if (jsonFiles == null || jsonFiles.length == 0) {
            System.out.println("No JSON files found.");
            return;
        }
        // Sort files by date in filename (descending)
        Arrays.sort(jsonFiles, (a, b) -> b.getName().compareTo(a.getName()));

        // Read all JSON entries
        List<JSONObject> entries = new ArrayList<>();
        for (File file : jsonFiles) {
            String content = new String(Files.readAllBytes(file.toPath()));
            try {
                JSONObject obj = new JSONObject(content);
                entries.add(obj);
            } catch (Exception e) {
                // Try as array
                JSONArray arr = new JSONArray(content);
                for (int i = 0; i < arr.length(); i++) {
                    entries.add(arr.getJSONObject(i));
                }
            }
        }

        // Generate HTML
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n<title>Lottery Cards</title>\n<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n<style>\n");
        html.append("body{font-family:'Segoe UI',Arial,sans-serif;background:#e9ecf3;margin:0;}\n.header{background:#1a3c7c;color:#fff;padding:24px 0;text-align:center;font-size:2em;}\n.welcome{background:linear-gradient(90deg,#a1c4fd,#c2e9fb);color:#333;text-align:center;padding:12px 0;font-size:1.2em;}\n.cards{display:flex;flex-wrap:wrap;gap:20px;justify-content:center;padding:30px 0;}\n.card{background:#2155cd;color:#fff;border-radius:16px;width:220px;min-height:120px;box-shadow:0 4px 16px rgba(0,0,0,0.08);display:flex;flex-direction:column;align-items:flex-start;margin:0 8px 24px 8px;position:relative;overflow:hidden;}\n.card .badge{position:absolute;top:0;left:0;background:#ff3b3b;color:#fff;font-size:0.9em;padding:4px 12px;border-bottom-right-radius:12px;font-weight:bold;}\n.card .code{position:absolute;top:12px;right:12px;background:#3b5998;color:#fff;border-radius:8px;padding:2px 10px;font-size:0.95em;font-weight:bold;}\n.card .name{margin:36px 0 0 16px;font-size:1.3em;font-weight:bold;letter-spacing:1px;}\n.card .date{margin:18px 0 0 16px;background:#fff;color:#2155cd;border-radius:8px;padding:4px 12px;font-size:1.1em;font-weight:bold;align-self:flex-start;}\n@media (max-width:700px){.cards{flex-direction:column;align-items:center;}.card{width:90vw;}}\n");
        html.append("</style>\n</head>\n<body>\n<div class=\"header\">Ponkudam</div>\n<div class=\"welcome\">welcome</div>\n<div class=\"cards\">\n");
        for (int i = 0; i < entries.size(); i++) {
            JSONObject item = entries.get(i);
            html.append("<div class='card'>");
            if (i == 0) html.append("<div class='badge'>NEW</div>");
            html.append("<div class='code'>" + item.optString("code") + "</div>");
            html.append("<div class='name'>" + item.optString("name") + "</div>");
            html.append("<div class='date'>" + item.optString("date") + "</div>");
            html.append("</div>\n");
        }
        html.append("</div>\n</body>\n</html>");
        Files.write(Paths.get("index.html"), html.toString().getBytes());
        System.out.println("index.html generated with " + entries.size() + " cards.");
    }
} 