import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class UniqueMaker {
    public static void main(String[] args) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap();
        ArrayList<String> list = new ArrayList();
        String inputFile = args[0];
        String outputFile = args[1];
        String mode = args[2];
        String delim = "";
        int np = 0;
        int numArg = args.length;
        if (numArg != 3 && numArg != 4) {
            System.out.println("Example Usage--------> java -jar UniqueMaker <input_file> <output_file> <mode> (separator)");
            System.out.println("Output Mode----------> 1: A unique set of entries, 2: Frequency set, 3: Frequency list");
            System.out.println("Separator (optional)-> t: Tab, c: Comma, s: Semicolon, p: Space");
        }
        if (numArg == 4) {
            delim = args[3];
            switch (delim) {
                case "t":
                    delim = "\t";
                    break;
                case "c":
                    delim = ",";
                    break;
                case "s":
                    delim = ";";
                    break;
                case "p":
                    delim = " ";
            }
            np = check(inputFile, delim);
            if (np == -1) {
                System.out.println("Warning: Input file has an invalid format!");
                System.exit(1);
            }
        }

        if (!delim.isEmpty()) {
            try {
                Reader reader = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
                BufferedReader br = new BufferedReader(reader);
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    String[] parts = strLine.split(delim);
                    for (int i = 0; i < np; i++) {
                        String temp = "";
                        for (int j = 0; j < i + 1; j++) {
                            temp += parts[j] + delim;
                        }
                        temp = temp.substring(0, temp.length() - 1);
                        Integer freq = map.get(temp);
                        if (freq == null) {
                            freq = 0;
                        }
                        freq++;
                        map.put(temp, freq);
                        list.add(temp);
                    }
                }
                reader.close();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                System.exit(1);
            }

            try {
                Writer out[] = new Writer[np];
                for (int i = 0; i < np; i++) {
                    out[i] = new BufferedWriter(new OutputStreamWriter(new FileOutputStream((i + 1) + "_" + outputFile), "UTF-8"));
                }
                if (mode.equals("3")) {
                    for (String e : list) {
                        String[] parts = e.split(delim);
                        out[parts.length - 1].write(e + "\t" + map.get(e) + "\n");
                    }
                } else {
                    Set keys = map.keySet();
                    Iterator itr = keys.iterator();
                    while (itr.hasNext()) {
                        String key = (String) itr.next();
                        String[] parts = key.split(delim);
                        if (mode.equals("2")) {
                            out[parts.length - 1].write(key + "\t" + map.get(key) + "\n");
                        } else if (mode.equals("1")) {
                            out[parts.length - 1].write(key + "\n");
                        }
                    }
                }
                for (int i = 0; i < np; i++) {
                    out[i].close();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.exit(1);
            }
        } else {
            try {
                Reader reader = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
                BufferedReader br = new BufferedReader(reader);
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    Integer freq = map.get(strLine);
                    if (freq == null) {
                        freq = 0;
                    }
                    freq++;
                    map.put(strLine, freq);
                    list.add(strLine);
                }
                reader.close();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                System.exit(1);
            }

            try {
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
                if (mode.equals("3")) {
                    for (String e : list) {
                        out.write(e + "\t" + map.get(e) + "\n");
                    }
                } else {
                    Set keys = map.keySet();
                    Iterator itr = keys.iterator();
                    while (itr.hasNext()) {
                        String key = (String) itr.next();
                        if (mode.equals("2")) {
                            out.write(key + "\t" + map.get(key) + "\n");
                        } else if (mode.equals("1")) {
                            out.write(key + "\n");
                        }
                    }
                }
                out.close();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    private static int check(String fileName, String d) {
        int np;
        try {
            Reader reader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String strLine;
            int i = 0;
            np = 0;
            while ((strLine = br.readLine()) != null) {
                String parts[] = strLine.split(d);
                if (parts == null || (i > 0 && np != parts.length)) {
                    return -1;
                }
                np = parts.length;
                i++;
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return -1;
        }
        return np;
    }
}
