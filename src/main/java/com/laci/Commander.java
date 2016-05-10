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

        String response = "{\"result\": " + result + ",\"command\": \"" + escape(command)
        + "\",\"output\": \"" + escape(output) + "\"}";

        return response;
    }

    public static Commander getInstance() {
        if (instance == null) {
            instance = new Commander();
        }
        return instance;
    }
    
    /**
	 * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters (U+0000 through U+001F).
	 * @param s
	 * @return
	 */
	public static String escape(String s){
		if(s==null)
			return null;
        StringBuffer sb = new StringBuffer();
        escape(s, sb);
        return sb.toString();
    }

    /**
     * @param s - Must not be null.
     * @param sb
     */
    static void escape(String s, StringBuffer sb) {
		for(int i=0;i<s.length();i++){
			char ch=s.charAt(i);
			switch(ch){
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
                //Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if((ch>='\u0000' && ch<='\u001F') || (ch>='\u007F' && ch<='\u009F') || (ch>='\u2000' && ch<='\u20FF')){
					String ss=Integer.toHexString(ch);
					sb.append("\\u");
					for(int k=0;k<4-ss.length();k++){
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				}
				else{
					sb.append(ch);
				}
			}
		}//for
	}
}