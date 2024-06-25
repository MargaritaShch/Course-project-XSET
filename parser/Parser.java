package parser;

//чтение файла
import java.io.FileReader;
//буферное чтение
import java.io.BufferedReader;
//исключение
import java.io.IOException;

public class Parser {
    private RequestStatistics stats;

    //конструктор  инициализации парсера
    public Parser(String[] relevantMethods) {
        this.stats = new RequestStatistics(relevantMethods);
    }

    public void parseLogFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            //хранение прочитанной строки
            String line;
            //построчное чтение до конца
            while ((line = br.readLine()) != null) {
                //разделение строки по "~"
                String[] parts = line.split("~");
                if (parts.length > 1) {
                    //получение http метода и пути
                    String requestPart = parts[1].trim();
                    //разделение на метод и путь
                    String[] requestParts = requestPart.split(" ");
                    if (requestParts.length > 1) {
                        //методы и пути
                        String method = requestParts[0].replaceAll("\"", "").trim();
                        String path = requestParts[1].replaceAll("\"", "").trim();

                        //для /api/sendMessage часть до первого /
                        if (path.startsWith("/api/sendMessage/")) {
                            path = "/api/sendMessage";
                        }

                        //фильтрация по нужным запросам
                        String methodPath = method + " " + path;
                        if (stats.isRelevantRequest(methodPath)) {
                            stats.addRequest(methodPath);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //получение статистики
    public RequestStatistics getStatistics() {
        return stats;
    }
}
