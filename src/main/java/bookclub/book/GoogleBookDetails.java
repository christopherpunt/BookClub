package bookclub.book;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class GoogleBookDetails {
    static String baseUrl = "https://www.googleapis.com/books/v1/volumes?q=";

    public static String executeGetRequest(String urlString){
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code: " + connection.getResponseCode());
            }

            Scanner scanner2 = new Scanner(connection.getInputStream());
            String response = scanner2.useDelimiter("\\A").next();
            scanner2.close();

            return response;

        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Book getBookDetailsFromIsbn(String isbn){
        String urlString = baseUrl + "isbn:" + isbn;

        String response = executeGetRequest(urlString);

        if (response == null){
            return null;
        }

        Book book = null;

        Gson gson = new Gson();
        JsonElement element = gson.fromJson(response, JsonElement.class);
        JsonObject object = element.getAsJsonObject();
        int totalItems = object.get("totalItems").getAsInt();

        if (totalItems == 1) {
            var jsonBook = object.getAsJsonArray("items").get(0).getAsJsonObject().getAsJsonObject("volumeInfo");
            return getBook(jsonBook);
        }
        
        System.out.println("No book found");
        return null;
    }

    public static List<Book> getBooksBasedOnTitle(String title){
        String urlString = null;
        try {
            urlString = baseUrl + URLEncoder.encode(title, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String response = executeGetRequest(urlString);

        if (response == null){
            return null;
        }
        
        LinkedList<Book> books = new LinkedList<Book>();

        Gson gson = new Gson();
        JsonElement element = gson.fromJson(response, JsonElement.class);
        JsonObject object = element.getAsJsonObject();
        JsonArray itemsArray = object.getAsJsonArray("items");
        int itemsCount = itemsArray.size();
        
        for (int i = 0; i < itemsCount; i++) {
            JsonObject jsonBook = itemsArray.get(i).getAsJsonObject().getAsJsonObject("volumeInfo");
            books.add(getBook(jsonBook));
        }
        return books;
    }

    private static Book getBook(JsonObject jsonBook) {
        Book book;
        book = new Book();
        String isbn10 = "";
        for (JsonElement id : jsonBook.getAsJsonArray("industryIdentifiers")) {
            JsonObject industryId = id.getAsJsonObject();
            if (industryId.get("type").getAsString().equals("ISBN_10")) {
                isbn10 = industryId.get("identifier").getAsString();
                break;
            }
        }
        book.setIsbn(isbn10);
        book.setTitle(jsonBook.get("title").getAsString());
        book.setDescription(jsonBook.get("description").getAsString());
        JsonArray authorsArray = jsonBook.getAsJsonArray("authors");
        StringBuilder authors = new StringBuilder();

        for (int i = 0; i < authorsArray.size(); i++){
            if (i == 0){
                authors.append(authorsArray.get(0).getAsString());
            } else {
                authors.append(", ").append(authorsArray.get(i).getAsString());
            }
        }

        book.setAuthor(authors.toString());
        return book;
    }

}
