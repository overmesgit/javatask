package firsttask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by overmes on 21.11.14.
 */
public class UserStats {
    public Map<Integer, Integer> userStats = new HashMap<>();
    public Map<Integer, Integer> userLoginTimes = new HashMap<>();

    public static void main (String[] args) throws IOException {
        UserStats userStats1 = new UserStats();
        userStats1.readFile(args[0]);
        userStats1.printStats();
    }

    public void readFile (String filename) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            processLine(line);

            while (line != null) {
                line = br.readLine();
                processLine(line);
            }
        }
    }

    public void processLine(String line) {
        if (line == null) {
            return;
        }

        String[] split = line.split(", ");
        Integer userId = Integer.parseInt(split[1]);
        Integer time = Integer.parseInt(split[0]);

        if (split[2].equals("login")) {
            userLoginTimes.put(userId, time);
            if (!userStats.containsKey(userId)) {
                userStats.put(userId, 0);
            }
        } else {
            Integer loginTime = userLoginTimes.remove(userId);
            userStats.put(userId, userStats.get(userId) + time - loginTime);
        }
    }

    public void printStats() {
        List<Map.Entry<Integer,Integer>> userIdFullTimeEntities = new ArrayList<>();
        userIdFullTimeEntities.addAll(userStats.entrySet());
        Collections.sort(userIdFullTimeEntities, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> integerIntegerEntry, Map.Entry<Integer, Integer> integerIntegerEntry2) {
                return -Integer.compare(integerIntegerEntry.getValue(), integerIntegerEntry2.getValue());
            }
        });

        for (Map.Entry<Integer, Integer> entry:userIdFullTimeEntities) {
            Integer userId = entry.getKey();
            Integer totalTime = entry.getValue();
            int day = (int) TimeUnit.SECONDS.toDays(totalTime);
            long hours = TimeUnit.SECONDS.toHours(totalTime) - (day *24);
            long minute = TimeUnit.SECONDS.toMinutes(totalTime) - (TimeUnit.SECONDS.toHours(totalTime)* 60);
            long second = TimeUnit.SECONDS.toSeconds(totalTime) - (TimeUnit.SECONDS.toMinutes(totalTime) *60);
            System.out.println(String.format("User id: %s Total online time: %s days %s hours %s minuts %s seconds", userId, day, hours, minute, second));
        }
    }
}
