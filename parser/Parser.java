package parser;
// чтение файла
import java.io.FileReader;
// буферное чтение
import java.io.BufferedReader;
//хранение и перебор
import java.util.HashMap;
import java.util.Map;
// обработка исключения
import java.io.IOException;

public class Parser {
    public static void main(String[] args) {
        String filePath = "parser/logs/production_log.csv";

        //общее кол-во вызовов
        int totalCalls = 0;

        //хранение кол-ва вызовов каждого метода
        Map<String, Integer> methodCounts = new HashMap<>();

        //методы из сваггера
        String[] relevantMethods = {
            "POST /api/signDoc",
            "GET /api/sendMessage",
            "GET /api/getMessage",
            "POST /api/addDoc",
            "GET /api/getDocByName"
        };

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            //хранение прочитанной строки
            String line;
            //построчное чтение до конца
            while ((line = br.readLine()) != null) {
                //разделение строки по  "~""
                String[] parts = line.split("~"); 
                if (parts.length > 1) {
                    //получение HTTP метода и пути
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

                        //фильтрация по интересующим запросам
                        String methodPath = method + " " + path;
                        if (isRelevantRequest(methodPath, relevantMethods)) {
                            methodCounts.put(methodPath, methodCounts.getOrDefault(methodPath, 0) + 1);
                            totalCalls++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //принт результатов
        System.out.println("Количество вызовов методов:");
        for (String method : relevantMethods) {
            int count = methodCounts.getOrDefault(method, 0);
            double percentage = (count / (double) totalCalls) * 100;
            System.out.printf("%s: %d (%.2f%%)\n", method, count, percentage);
        }
        //общее кол-во вызовов
        System.out.println("Всего вызовов: " + totalCalls);
    }

    //проверка нужных запрсоов
    private static boolean isRelevantRequest(String methodPath, String[] relevantMethods) {
        for (String method : relevantMethods) {
            if (methodPath.equals(method)) {
                return true;
            }
        }
        return false;
    }
}
