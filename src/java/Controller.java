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
public class Controller extends HttpServlet {
    
    DataBase dB;
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
        String command= (String) request.getParameter("command");
        if(command!=null){
            switch (command) {
            case "login":
                loginHandler(request, response);
                break;
            case "checkUser":
                checkUser(request, response);
                break;
            case "getCourse":
                getCourse(request, response);
                break;
            case "addCourse":
                addCourse(request, response);
                break;
            case "addProfessor":
                addProfessor(request, response);
                break;
            case "getSession":
                response.getWriter().print(new JSONObject().put("user", s.getAttribute("user")));
                System.out.println(s.getAttribute("user")+ "AAA");
                break;
            default:
                context.log("ERROR : Recived invalid action!");
                response.getWriter().print(new JSONObject().put("ERROR", "unrecognized input"));
            }
        }else{
            context.log("ERROR : Recived NULL action!");
        }
    }
    
    private boolean loginHandler(HttpServletRequest request, HttpServletResponse response) {
        String account = request.getParameter("user");
        String password = request.getParameter("password");
        try {
            if (new LoginModel(dB).checkLogin(account, password)) {
                request.getSession().setAttribute("user", account);
                response.getWriter().print(new JSONObject().put("error", ""));
                return true;
            } else {
                response.getWriter().print(new JSONObject().put("error", "Wrong password"));
                try {
                    request.getRequestDispatcher("home.html").forward(request, response);
                } catch (ServletException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    private boolean checkUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String account = request.getParameter("user");
        context.log(account);
        if (account != null) {
            try {
                JSONObject res = new JSONObject();
                if (new LoginModel(dB).checkAccountExistance(account)) {
                    res.put("response", "true");
                    response.getWriter().print(res);
                    return true;
                } else {
                    res.put("response", "false");
                    response.getWriter().print(res);
                    return false;
                }
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }                    
                return true;
        } else {

        }
        return false;
    }
    private void getCourse(HttpServletRequest request, HttpServletResponse response) throws IOException{
      //wrap the arrayList in a Json, and return it as response
        response.getWriter().print(new JSONObject().put("courseList",new ReservationModel(dB).getCourse()));
    }
    private void addCourse(HttpServletRequest request, HttpServletResponse response) throws IOException{
        if(!new AdminModel(dB).addCourse(request.getParameter("course"))){
            //show error message on the web page
        }
    }
    private void addProfessor(HttpServletRequest request, HttpServletResponse response) throws IOException{
        if(!new AdminModel(dB).addProfessor(request.getParameter("professor"))){
            //show error message on the web page
        }
    }
    
    
    
    @Override
    public void init(ServletConfig config) throws ServletException  {
        if (config != null) {
            String url = config.getInitParameter("dbUrl");
            String account = config.getInitParameter("account");
            String password = config.getInitParameter("password");
            dB = new DataBase(url, account, password);
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

    
}
