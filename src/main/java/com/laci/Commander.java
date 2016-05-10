package com.laci;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;


public class Commander {

    private static Commander instance = null;

    protected Commander() {
        // Exists only to defeat instantiation.
    }

    public String exec(String command) {
        String output = "";
        boolean result = true;
        try {
        	// init shell
        	ProcessBuilder builder = new ProcessBuilder(command);
        	Process p = null;
            p = builder.start();
//            BufferedWriter p_stdin = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
//
//            p_stdin.write(command);
//            p_stdin.newLine();
//            p_stdin.flush();
            final int processStatus = p.waitFor();
            // downloadyoutube script don't need a final exit command, and don't wait for result
            if (command.contains("downloadyoutube.sh")) {
                output += "start downloading from youtube";
            } else {
//                p_stdin.write("exit");
//                p_stdin.newLine();
//                p_stdin.flush();
                // write stdout of shell (=output of all commands)
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    output += line + "\n";
                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            output += "Execution faiulre:" + e.toString();
            System.out.println(e);
            result = false;
        }

        String response = "{\"result\": " + result + ",\"command\": \"" + JSONObject.escape(command)
                + "\",\"output\": \"" + JSONObject.escape(output) + "\"}";

        return response;
    }

    public static Commander getInstance() {
        if (instance == null) {
            instance = new Commander();
        }
        return instance;
    }
}