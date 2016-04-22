package com.mymusic;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloServlet
 */
public class Service extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */

    public void init() throws ServletException {

    }
    public Service() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Hello friend! Not a GET method!");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        IdleThread idleThread = IdleThread.getInsatnce();
        idleThread.resetRmeainingTime();

        String command = request.getParameter("command");

        String result;
        if (command == null) {
            result = "no command";
        } else {
            Commander commander = Commander.getInstance();
            result = commander.exec(command);
        }
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(result);

    }

}
