import org.json.JSONObject;
import org.json.JSONArray;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LotteryHtmlGenerator {
    public static void main(String[] args) {
        try {
            // Read the template
            String template = new String(Files.readAllBytes(Paths.get("result-template.html")));
            
            // Get all JSON files in the current directory
            File[] jsonFiles = new File(".").listFiles((dir, name) -> name.endsWith(".json"));
            
            if (jsonFiles != null) {
                for (File jsonFile : jsonFiles) {
                    try {
                        // Read JSON data
                        String jsonContent = new String(Files.readAllBytes(jsonFile.toPath()));
                        JSONObject data = new JSONObject(jsonContent);
                        
                        // Generate HTML filename
                        String htmlFilename = jsonFile.getName().replace(".json", ".html");
                        
                        // Replace placeholders in template
                        String htmlContent = template
                            .replace("{{LOTTERY_NAME}}", data.optString("lottery_name", ""))
                            .replace("{{DRAW_NUMBER}}", data.optString("draw_number", ""))
                            .replace("{{JSON_FILENAME}}", jsonFile.getName());
                        
                        // Write HTML file
                        Files.write(Paths.get(htmlFilename), htmlContent.getBytes());
                        System.out.println("Generated: " + htmlFilename);
                        
                    } catch (Exception e) {
                        System.err.println("Error processing " + jsonFile.getName() + ": " + e.getMessage());
                    }
                }
            }
            
            // Also generate index.html
            generateIndexHtml();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void generateIndexHtml() {
        try {
            // Read the template
            String template = new String(Files.readAllBytes(Paths.get("result-template.html")));
            
            // Get all HTML files except index.html and template files
            File[] htmlFiles = new File(".").listFiles((dir, name) -> 
                name.endsWith(".html") && !name.equals("index.html") && !name.contains("template"));
            
            StringBuilder cards = new StringBuilder();
            
            if (htmlFiles != null) {
                for (File htmlFile : htmlFiles) {
                    try {
                        // Derive JSON filename from HTML filename
                        String jsonFilename = htmlFile.getName().replace(".html", ".json");
                        File jsonFile = new File(jsonFilename);
                        
                        if (jsonFile.exists()) {
                            // Read JSON data
                            String jsonContent = new String(Files.readAllBytes(jsonFile.toPath()));
                            JSONObject data = new JSONObject(jsonContent);
                            
                            String code = data.optString("draw_number", "");
                            String name = data.optString("lottery_name", "");
                            String date = data.optString("draw_date", "");
                            
                            cards.append(String.format(
                                "<a href=\"%s\" class=\"card\">" +
                                "<div class=\"code\">%s</div>" +
                                "<div class=\"name\">%s</div>" +
                                "<div class=\"date\">%s</div>" +
                                "</a>",
                                htmlFile.getName(), code, name, date
                            ));
                        }
                    } catch (Exception e) {
                        System.err.println("Error processing " + htmlFile.getName() + ": " + e.getMessage());
                    }
                }
            }
            
            // Generate index.html content (simplified version)
            String indexContent = generateIndexTemplate(cards.toString());
            Files.write(Paths.get("index.html"), indexContent.getBytes());
            System.out.println("Generated: index.html");
            
        } catch (Exception e) {
            System.err.println("Error generating index: " + e.getMessage());
        }
    }
    
    private static String generateIndexTemplate(String cards) {
        return "<!DOCTYPE html>\n" +
               "<html lang=\"en\">\n" +
               "<head>\n" +
               "  <meta charset=\"UTF-8\">\n" +
               "  <title>Kerala Lottery Results</title>\n" +
               "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "  <style>\n" +
               "    body { font-family: Arial, sans-serif; margin: 20px; }\n" +
               "    .cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; }\n" +
               "    .card { padding: 15px; border: 1px solid #ddd; border-radius: 8px; text-decoration: none; color: inherit; }\n" +
               "    .card:hover { box-shadow: 0 2px 8px rgba(0,0,0,0.1); }\n" +
               "    .code { font-weight: bold; color: #1976d2; }\n" +
               "    .name { margin: 5px 0; }\n" +
               "    .date { color: #666; }\n" +
               "  </style>\n" +
               "</head>\n" +
               "<body>\n" +
               "  <h1>Kerala Lottery Results</h1>\n" +
               "  <div class=\"cards\">\n" + cards + "\n" +
               "  </div>\n" +
               "</body>\n" +
               "</html>";
    }
} 