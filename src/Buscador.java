import buscador.models.AddressNotFoundException;
import buscador.models.Cep;
import buscador.models.InvalidCepException;
import buscador.models.ViaCEP;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Buscador {
    public static void main (String [] args) throws IOException, InterruptedException {
        String search = "";
        Scanner sc = new Scanner(System.in);
        System.out.println("Seja bem vindo!");
        char exit;
        FileWriter jsonLog = new FileWriter("jsonLog.json");
        do {
            System.out.println( "Digite o número CEP (sem hifen) que deseja pesquisar para encontrar o endereço:");
            try {
                search = sc.nextLine();
                if (search.length() != 8) {
                    throw new InvalidCepException();
                }
                for (char c : search.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        throw new InvalidCepException();
                    }
                }
                String address = "https://viacep.com.br/ws/" + search + "/json/";
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(address))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String json = response.body();
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();
                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                if (jsonObject.has("erro")) {
                    throw new AddressNotFoundException("Desculpe! Endereço não encontrado.");
                }
                ViaCEP searchResult = gson.fromJson(json, ViaCEP.class);
                Cep result = new Cep(searchResult);
                System.out.println(result);
                jsonLog.write(gson.toJson(result));                
            } catch (StringIndexOutOfBoundsException | InvalidCepException e) {
                System.out.println("Número invalido! Digite novamente, lembrando que o número do CEP deve ter exatamente 8 digitos numéricos, e deverá ser digitado sem hifen.");
            } catch (AddressNotFoundException e) {
                System.out.println(e.getMessage());
            }
            exit = ' ';
            while (exit != 's' && exit != 'n') {
                System.out.println("Deseja efetuar uma nova pesquisa? s/n");
                exit = sc.nextLine().charAt(0);
            }
        } while (exit == 's');
        jsonLog.close();
    }
}
