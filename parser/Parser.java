package parser;

//чтение файла
import java.io.FileReader;
//буферное чтение
import java.io.BufferedReader;
//исключение
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Parser {
    private RequestStatistics stats;
    private SimpleDateFormat dateFormat;

    //конструктор инициализации парсера
    public Parser(String[] relevantMethods) {
        this.stats = new RequestStatistics(relevantMethods);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    }

    public void parseLogFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            //хранение прочитанной строки
            String line;
            // построчное чтение до конца
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
                        //удалить начиная с ? из пути
                        path = path.split("\\?")[0];

                        //для /api/sendMessage часть до первого /
                        if (path.startsWith("/api/sendMessage/")) {
                            path = "/api/sendMessage";
                        }

                        //фильтрация по нужным запросам
                        String methodPath = method + " " + path;
                        if (stats.isRelevantRequest(methodPath)) {
                            String timestamp = parts[0].trim();
                            String hour = extractHour(timestamp);
                            if (hour != null) {
                                stats.addRequest(methodPath, hour);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //извлечение часа из метки времени
    private String extractHour(String timestamp) {
        try {
            Date date = dateFormat.parse(timestamp);
            SimpleDateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH");
            return hourFormat.format(date);
        } catch (ParseException e) {
            System.err.println("Ошибка парсинга метки времени: " + timestamp);
            return null;
        }
    }

    //статистика
    public RequestStatistics getStatistics() {
        return stats;
    }
}
