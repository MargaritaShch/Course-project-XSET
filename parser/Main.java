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
            "GET /api/getDocByName"
        };

        //инициализация парсера
        Parser parser = new Parser(relevantMethods);
        parser.parseLogFile(filePath);

        //статистика
        RequestStatistics stats = parser.getStatistics();
        stats.printStatistics();
        stats.printRPS();

        //расчет пикового часа
        String peakHour = stats.getPeakHour();
        if (peakHour != null) {
            System.out.println("\nРасчет для пикового часа:");
            stats.printPeakHourStatistics(peakHour);
        }
    }
}
