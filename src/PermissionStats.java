/**
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author aditinarendran
 */
public class PermissionStats {

    final static String filePath = "listoffiles.txt";

    public static HashMap<String, List<String>> HashmapTextFile() {

        HashMap<String, List<String>> map = new HashMap<>();

        BufferedReader buff = null;

        try {
            File file = new File(filePath);

            buff = new BufferedReader(new FileReader(file));

            String line;
            while ((line = buff.readLine()) != null) {

                String[] parts = line.split(":");

                String appname = parts[0];
                if (!appname.equals("") && parts[1] != null) {
                    if (map.containsKey(appname)) {
                        map.get(appname).add(parts[1]);
                    } else {
                        map.put(appname, new ArrayList<String>());
                        map.get(appname).add(parts[1]);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (buff != null) {
                try {
                    buff.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    public static HashMap<String, Integer> topTen(HashMap<String, Integer> map) {

        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    }

    static void mostPermsApps() {

        HashMap<String, List<String>> mapfile = HashmapTextFile();
        HashMap<String, Integer> mostAppPerms = new HashMap<>();

        for (HashMap.Entry<String, List<String>> entry : mapfile.entrySet()) {

            mostAppPerms.put(entry.getKey(), entry.getValue().size());

        }

        HashMap<String, Integer> top10 = topTen(mostAppPerms);

        System.out.println("The top 10 applications with most permissions:\n");
        for (String var : top10.keySet()) {
            System.out.println("APK: " + var + " \t Permissions: " + top10.get(var));
        }

    }

    static void mostFreqPerms() {

        HashMap<String, List<String>> mapfile = HashmapTextFile();
        // System.out.println("Number of APK files:" + mapfile.size());
        HashMap<String, Integer> mostPerms = new HashMap<String, Integer>();

        List<String> list = new ArrayList<>();
        for (HashMap.Entry<String, List<String>> entry : mapfile.entrySet()) {
            // System.out.println(entry.getValue());

            for (String s : entry.getValue()) {
                if (!list.contains(s)) {
                    list.add(s);
                    mostPerms.put(s, 1);
                } else {
                    mostPerms.put(s, mostPerms.get(s) + 1);
                }

            }
        }

        HashMap<String, Integer> top10 = topTen(mostPerms);

        System.out.println("The top 10 most frequent permissions:\n");
        for (String var : top10.keySet()) {
            System.out.println("Permission: " + var + " \t Frequency: " + top10.get(var));
        }
    }

    public static void main(String[] args) {

        System.out.println("Number of APKs:" + HashmapTextFile().size());
        mostPermsApps();
        mostFreqPerms();

    }

}

/*
//Reverse the hashmap for top 10

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
  for (HashMap.Entry<String, Integer> entry : mostAppPerms.entrySet()) {
  list.add(entry.getValue()); } Collections.sort(list);
  Collections.reverse(list); for (int num : list) { for (HashMap.Entry<String,
  Integer> entry : mostAppPerms.entrySet()) { if (entry.getValue().equals(num))
  { sortedMap.put(entry.getKey(), num); } } }
  for (int i =0; i < 10; i++) { System.out.println(sortedMap.); }
*/
