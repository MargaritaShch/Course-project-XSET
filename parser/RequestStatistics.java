package parser;

//хранение и перебор
import java.util.HashMap;
import java.util.Map;

public class RequestStatistics {
    private int totalCalls;
    private Map<String, Integer> methodCounts;
    private String[] relevantMethods;

    //конструктор инициализации статистики запросов
    public RequestStatistics(String[] relevantMethods) {
        this.totalCalls = 0;
        this.methodCounts = new HashMap<>();
        this.relevantMethods = relevantMethods;
    }

    //добавление запроса
    public void addRequest(String methodPath) {
        methodCounts.put(methodPath, methodCounts.getOrDefault(methodPath, 0) + 1);
        totalCalls++;
    }

    //общее кол-во вызовов
    public int getTotalCalls() {
        return totalCalls;
    }

    //получение кол-ва вызовов для каждого метода
    public Map<String, Integer> getMethodCounts() {
        return methodCounts;
    }

    //список методов
    public String[] getRelevantMethods() {
        return relevantMethods;
    }

    //проверка, релевантен ли запрос
    public boolean isRelevantRequest(String methodPath) {
        for (String method : relevantMethods) {
            if (methodPath.equals(method)) {
                return true;
            }
        }
        return false;
    }

    //статистика запросов
    public void printStatistics() {
        System.out.println("Количество вызовов методов:");
        for (String method : relevantMethods) {
            int count = methodCounts.getOrDefault(method, 0);
            double percentage = (count / (double) totalCalls) * 100;
            System.out.printf("%s: %d (%.2f%%)\n", method, count, percentage);
        }
        System.out.println("Всего вызовов: " + totalCalls);
    }

    //расчет RPS
    public void printRPS() {
        final int SECONDS_IN_HOUR = 3600;
        double totalRPS = totalCalls / (double) SECONDS_IN_HOUR;

        System.out.println("\nРасчетная интенсивность (RPS):");
        for (String method : relevantMethods) {
            int count = methodCounts.getOrDefault(method, 0);
            double rps = totalRPS * (count / (double) totalCalls);
            System.out.printf("%s: %.2f RPS\n", method, rps);
        }
    }
}
