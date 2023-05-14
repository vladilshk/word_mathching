package ru.vovai;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static final String inputFile = "input.txt";
    private static final String outputFile = "output.txt";

    public static void main(String[] args) {
        try {
            Path path = Paths.get(inputFile);
            Scanner scanner = new Scanner(path);
            List<String> list1 = readWordsSetFromFile(scanner);
            List<String> list2 = readWordsSetFromFile(scanner);

            Map<String, String> resultMap = findBestMatch(list1, list2);

            writeDataInFile(resultMap, list1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDataInFile(Map<String, String> resultMap, List<String > list1) throws IOException {
        FileWriter writer = new FileWriter(outputFile);
        //here we write pairs with worlds from thirst wordsSet
        for (String s1 : list1){
            String s2 = resultMap.get(s1);
            writer.write(s1 + ":" + s2 + "\n");
            resultMap.remove(s1);
        }
        //if second wordSet would be bigger when thirst here we will write pairs: worldFromSecondSet:?
        for (String s1 : resultMap.keySet()){
            String s2 = resultMap.get(s1);
            writer.write(s1 + ":" + s2 + "\n");
        }
        writer.close();
    }


    public static Map<String, String> findBestMatch(List<String> worldList1, List<String> worldList2) {
        List<String> list1;
        List<String> list2;
        boolean correctOrder;
        if (worldList1.size() <= worldList2.size()){
            list1 = worldList1;
            list2 = worldList2;
            correctOrder = true;
        } else {
            list1 = worldList2;
            list2 = worldList1;
            correctOrder = false;
        }

        Map<String, String> resultMap = new HashMap<>();
        Set<String> selected = new HashSet<>();
        for (String s1 : list1) {
            String bestMatch = "";
            int bestMatchLength = 0;
            for (String s2 : list2) {
                if (!selected.contains(s2)) {
                    int l = longestCommonSubstring(s1, s2);
                    if (l > bestMatchLength) {
                        bestMatch = s2;
                        bestMatchLength = l;
                    }
                }
            }
            if (correctOrder){
                resultMap.put(s1, bestMatch);
            } else {
                resultMap.put(bestMatch, s1);
            }
            selected.add(bestMatch);
        }
        for (String s2 : list2) {
            if (!selected.contains(s2)) {
                resultMap.put(s2, "?");
            }
        }

        return resultMap;
    }

    public static int longestCommonSubstring(String s1, String s2) {
        int[][] table = new int[s1.length() + 1][s2.length() + 1];
        int longest = 0;
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    table[i][j] = table[i - 1][j - 1] + 1;
                    if (table[i][j] > longest) {
                        longest = table[i][j];
                    }
                }
            }
        }
        return longest;
    }

    public static List<String> readWordsSetFromFile(Scanner scanner) throws IOException {
        if (!scanner.hasNextInt()){
            return new ArrayList<>();
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(scanner.nextLine());
        }
        return list;
    }
}