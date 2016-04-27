package com.laci;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloServlet
 */
public class InitPlay extends HttpServlet {
       
    private static final long serialVersionUID = 1L;

    public void init() throws ServletException {

        IdleThread idleThread = IdleThread.getInsatnce();
        idleThread.resetRmeainingTime();

        String command = "mpc clear && mpc add http://stream.radio88.hu:8000 && mpc play";
        Commander commander = Commander.getInstance();
        commander.exec(command);
    }
    public InitPlay() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Hello friend!");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // String type = request.getParameter("type");
        String filename = request.getParameter("filename");
        String command = "";
        // if (type.equals("radio")) {
        // command += "mpc clear && mpc load " + filename + " && mpc play";
        // } else {
        command += "mpc clear && mpc add " + filename + " && mpc play";
        // }
        Commander commander = Commander.getInstance();
        String result = commander.exec(command);
        response.getWriter().println(result);

    }

}
