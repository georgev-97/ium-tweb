/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author george
 */
public class Main extends HttpServlet {
    
    Model model;
    ServletContext context;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession s = request.getSession();
        this.context = request.getServletContext();
        String action = (String) request.getParameter("action");
        switch (action) {
            case "login":
                loginHandler(request, response);
                break;
            default:
                context.log("There was some problem");
                response.getWriter().print("Error, unrecognized input");
                break;
            case "checkUser":
                checkUser(request, response);
                break;
            case "getSession":
                response.getWriter().print(new JSONObject().put("user", s.getAttribute("user")));
                System.out.println(s.getAttribute("user")+ "AAA");
                break;
        }
    }
    
    private boolean loginHandler(HttpServletRequest request, HttpServletResponse response) {
        String account = request.getParameter("user");
        String password = request.getParameter("password");
        try {
            if (model.checkLogin(account, password)) {
                request.getSession().setAttribute("user", account);
                response.getWriter().print(new JSONObject().put("error", ""));
                return true;
            } else {
                response.getWriter().print(new JSONObject().put("error", "Wrong password"));
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        if (config != null) {
            String url = config.getInitParameter("dbUrl");
            String account = config.getInitParameter("account");
            String password = config.getInitParameter("password");
            this.model = new Model(url, account, password);
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private boolean checkUser(HttpServletRequest request, HttpServletResponse response) {
        String account = request.getParameter("user");
        context.log(account);
        if (account != null) {
            try {
                JSONObject obj = new JSONObject();
                if (model.checkAccountExistance(account)) {
                    obj.put("response", "true");
                    response.getWriter().print(obj);
                    return true;
                } else {
                    obj.put("response", "false");
                    response.getWriter().print(obj);
                    return false;
                }
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
}
