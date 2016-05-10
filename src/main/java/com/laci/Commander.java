package com.laci;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;



public class Commander {

    private static Commander instance = null;

    protected Commander() {
        // Exists only to defeat instantiation.
    }

    public String exec(String command) {
        String output = "";
        boolean result = true;
        try {
//        	// init shell
//        	ProcessBuilder builder = new ProcessBuilder("/bin/bash");
//        	Process p = null;
//            p = builder.start();
//            BufferedWriter p_stdin = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
//
//            p_stdin.write(command);
//            p_stdin.newLine();
//            p_stdin.flush();
//            // downloadyoutube script don't need a final exit command, and don't wait for result
//            if (command.contains("downloadyoutube.sh")) {
//                output += "start downloading from youtube";
//            } else {
//                p_stdin.write("exit");
//                p_stdin.newLine();
//                p_stdin.flush();
//                // write stdout of shell (=output of all commands)
//                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                String line = null;
//                while ((line = in.readLine()) != null) {
//                    output += line + "\n";
//                    System.out.println(line);
//                }
//            }
        	/*Process proc = Runtime.getRuntime().exec(command);
            java.io.InputStream is = proc.getInputStream();
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            while (s.hasNext()) {
            	output += s.next();
            }
            s.close();
            is.close();*/
        	ProcessBuilder pb=new ProcessBuilder(command);
        	pb.redirectErrorStream(true);
        	Process process=pb.start();
        	
//        	BufferedWriter p_stdin = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
//        	//
//            p_stdin.write(command);
//            p_stdin.newLine();
//            p_stdin.flush();
        	
//        	BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        	String line = null;
//        	while ((line = in.readLine()) != null) {
//              output += line + "\n";
//              System.out.println(line);
//        	}
//            java.io.InputStream is = process.getInputStream();
//            @SuppressWarnings("resource")
//			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
//            while (s.hasNext()) {
//            	output += s.next();
//            }
//            s.close();
//            is.close();
            BufferedReader reader=new BufferedReader( new InputStreamReader(process.getInputStream()));
            String s; 
            while ((s = reader.readLine()) != null){
                System.out.println(s);
                output += s;
            }  

        } catch (Exception e) {
            output += "Execution faiulre:" + e.toString();
            System.out.println(e);
            result = false;
        }

        String response = "{\"result\": " + result + ",\"command\": " + quote(command)
                + ",\"output\": " + quote(output) + "}";

        return response;
    }

    public static Commander getInstance() {
        if (instance == null) {
            instance = new Commander();
        }
        return instance;
    }
    
    public static String quote(String string) {
        if (string == null || string.length() == 0) {
            return "\"\"";
        }

        char         c = 0;
        int          i;
        int          len = string.length();
        StringBuilder sb = new StringBuilder(len + 4);
        String       t;

        sb.append('"');
        for (i = 0; i < len; i += 1) {
            c = string.charAt(i);
            switch (c) {
            case '\\':
            case '"':
                sb.append('\\');
                sb.append(c);
                break;
            case '/':
//                if (b == '<') {
                    sb.append('\\');
//                }
                sb.append(c);
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\r':
               sb.append("\\r");
               break;
            default:
                if (c < ' ') {
                    t = "000" + Integer.toHexString(c);
                    sb.append("\\u" + t.substring(t.length() - 4));
                } else {
                    sb.append(c);
                }
            }
        }
        sb.append('"');
        return sb.toString();
    }
}