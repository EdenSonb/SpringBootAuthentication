package authentication.server.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ReadWriteToJson {

    public static boolean writeToJson(String fileName, Map<String,String> userMap){
        try(FileWriter fileWriter = new FileWriter(fileName)){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(userMap,fileWriter);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static Map<String,String> readFromJson(String fileName) throws FileNotFoundException {
        try(FileReader fileReader = new FileReader(fileName)){
            return new Gson().fromJson(fileReader,Map.class);
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }
}
