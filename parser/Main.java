package parser;

public class Main {
    public static void main(String[] args) {
        String filePath = "logs/production_log.csv";

        //методы из сваггера
        String[] relevantMethods = {
            "POST /api/signDoc",
            "GET /api/sendMessage",
            "GET /api/getMessage",
            "POST /api/addDoc",
            "GET /api/getDocByName",
            "GET /api/stopLeak",
            "GET /api/stopCPULoad",
            "GET /api/startLeak",
            "GET /api/startCPULoad"
        };

        //инициализация парсера
        Parser parser = new Parser(relevantMethods);
        // разбор лог-файла
        parser.parseLogFile(filePath);

        //статистика
        RequestStatistics stats = parser.getStatistics();
        stats.printStatistics();
        stats.printRPS();
    }
}
