/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author george, lorenzo
 */
public class Controller extends HttpServlet {

    private String url;
    private String account;
    private String password;
    private ServletContext context;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession ses = request.getSession();
        this.context = request.getServletContext();
        DataBase dB = new DataBase(url, account, password);
        String command = (String) request.getParameter("command");
        //checking permission
        if (checkPermission(command, request, response)) {
            //checking permission

            if (command != null) {
                switch (command) {
                    case "login":
                        loginHandler(request, response, dB);
                        break;
                    case "register":
                        register(request, response, dB);
                    case "checkUser":
                        checkUser(request, response, dB);
                        break;
                    case "getCourse":
                        getCourse(request, response, dB);
                        break;
                    case "getProfessor":
                        getProfessor(request, response, dB);
                        break;
                    case "getFreeCourse":
                        getFreeCourse(request, response, dB);
                        break;
                    case "getCourseProfessor":
                        getCourseProfessor(request, response, dB);
                        break;
                    case "addCourse":
                        addCourse(request, response, dB);
                        break;
                    case "addProfessor":
                        addProfessor(request, response, dB);
                        break;
                    case "courseProfessor":
                        courseProfessor(request, response, dB);
                        break;
                    case "getReservation":
                        getReservation(request, response, dB);
                        break;
                    case "reserve":
                        reserve(request, response, dB);
                        break;
                    case "getUserReservation":
                        getUserReservation(request, response, dB);
                        break;
                    case "deleteReservation":
                        deleteReservation(request, response, dB);
                        break;
                    case "getAutSesData":
                        JSONObject gasd = new JSONObject();
                        
                        try {gasd.put("account", ses.getAttribute("account"));
                        } catch (Exception e) {gasd.put("account", "");}
                        try {gasd.put("id", ses.getAttribute("id"));
                        } catch (Exception e) {gasd.put("id", "");}
                        try {gasd.put("role", ses.getAttribute("role"));
                        } catch (Exception e) {gasd.put("role", "");}
                        //request.getRequestDispatcher("permissionDenied.html").forward(request, response);
                        response.getWriter().print(gasd);
                        break;
                        case "logout":
                            ses.invalidate();
                        break;
                    default:
                        context.log("ERROR : Received invalid action!");
                        response.getWriter().print(new JSONObject().put("error", "unrecognized input"));
                }
            } else {
                context.log("ERROR : Recived NULL command!");
            }
        } else {
            context.log("ERROR : " + command + " permission denied");
            response.getWriter().print(new JSONObject().put("error", "you don't have permission"));
        }
    }

    private boolean checkPermission(String command, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String id = (String) request.getSession().getAttribute("id");
        int role;
        if (request.getSession().getAttribute("role") != null) {
            role = ((Integer) request.getSession().getAttribute("role")).intValue();
        } else {
            role = -1;
        }
        String noPermission[] = {"checkUser", "register", "login", "getAutSesData"};
        String basePermission[] = {"logout","getCourse", "getProfessor", "getCourseProfessor", "getReservation", "reserve", "getUserReservation", "deleteReservation"};
        String adminPermission[] = {"logout","getCourse", "getProfessor", "addCourse", "addProfessor", "getFreeCourse", "courseProfessor"};

        if (Arrays.asList(noPermission).contains(command)) {
            return true;
        } else if (Arrays.asList(basePermission).contains(command) && id != null && role == 1) {
            return true;
        } else if (Arrays.asList(adminPermission).contains(command) && id != null && role == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response, DataBase dB) throws ServletException, IOException {
        String account = request.getParameter("account");
        String password = Hash.md5(request.getParameter("password"));
        try {
            new RegisterModel(dB).addUser(account, password);
            response.getWriter().print(new JSONObject().put("error",""));
        } catch (SQLException ex) {
            response.getWriter().print(new JSONObject().put("error", "errore creazione account"));

        }
    }

    private void loginHandler(HttpServletRequest request, HttpServletResponse response, DataBase dB) {
        String account = request.getParameter("account");
        String password = request.getParameter("password");

        JSONObject resp = new JSONObject();
        try {
            User res = new LoginModel(dB).login(account, password);
            if (res != null) {
                request.getSession().setAttribute("account", res.getAccount());
                request.getSession().setAttribute("id", res.getId());
                request.getSession().setAttribute("role", res.getRole());
                resp.put("account", account);
                resp.put("error", "");
                if (res.getRole() == 0) {
                    resp.put("role", "admin");
                } else {
                    resp.put("role", "basic");
                }
                response.getWriter().print(resp);
            } else {
                response.getWriter().print(new JSONObject().put("error", "Wrong password"));
            }
        } catch (IOException ex) {
            resp.put("error", "sql error");
        }
    }

    private boolean checkUser(HttpServletRequest request, HttpServletResponse response, DataBase dB) throws IOException {
        String account = request.getParameter("account");
        context.log("logged as: " + account);
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

    private void getCourse(HttpServletRequest request, HttpServletResponse response, DataBase dB) throws IOException {
        try {
            JSONObject res = new JSONObject().put("courseList", new AdminModel(dB).getCourse());
            res.put("error", "");
            response.getWriter().print(res);
        } catch (SQLException ex) {
            response.getWriter().print(new JSONObject().put("error", ex.getMessage()));
            context.log(ex.toString());
            context.log("getCourse : " + ex.toString());
            response.getWriter().print(new JSONObject().put("error", ex.getMessage()));
        }
    }

    private void getProfessor(HttpServletRequest request, HttpServletResponse response, DataBase dB) throws IOException {
        try {
            JSONObject res = new JSONObject().put("professorList", new AdminModel(dB).getProfessor());
            res.put("error", "");
            response.getWriter().print(res);
        } catch (SQLException ex) {
            response.getWriter().print(new JSONObject().put("error", ex.getMessage()));
            context.log(ex.toString());
            context.log("getProfessor : " + ex.toString());
            response.getWriter().print(new JSONObject().put("error", ex.getMessage()));
        }
    }

    private void getCourseProfessor(HttpServletRequest request, HttpServletResponse response, DataBase dB) throws IOException {
        try {
            JSONObject res = new JSONObject().put("professorList", new AdminModel(dB)
                    .getCourseProfessor(request.getParameter("course")));
            res.put("error", "");
            response.getWriter().print(res);
        } catch (SQLException ex) {
            context.log("getFreeProfessor : " + ex.toString());
            context.log("getCourseProfessor : " + ex.toString());
            response.getWriter().print(new JSONObject().put("error", "sql error"));
        }
    }

    private void getFreeCourse(HttpServletRequest request, HttpServletResponse response, DataBase dB) throws IOException {
        try {
            JSONArray ar = new AdminModel(dB).getFreeCourse(request.getParameter("professor"));
            JSONObject res = new JSONObject().put("courseList", ar);
            if (ar.isNull(0)) {
                res.put("error", "no element");
            } else {
                res.put("error", "");
            }

            response.getWriter().print(res);
        } catch (SQLException ex) {
            context.log("getFreeCourse : " + ex.toString());
            response.getWriter().print(new JSONObject().put("error", "sql error"));
        }
    }

    private void addProfessor(HttpServletRequest request, HttpServletResponse response, DataBase dB) throws IOException {
        try {
            new AdminModel(dB).addProfessor(request.getParameter("name"),
                    request.getParameter("username"), request.getParameter("email"));
            response.getWriter().print(new JSONObject().put("error", ""));

        } catch (SQLException ex) {
            response.getWriter().print(new JSONObject().put("error", "errore inserimento"));
        }
    }

    private void addCourse(HttpServletRequest request, HttpServletResponse response, DataBase dB) throws IOException {
        try {
            new AdminModel(dB).addCourse(request.getParameter("course"), request.getParameter("description"));
            response.getWriter().print(new JSONObject().put("error", ""));
        } catch (SQLException ex) {
            response.getWriter().print(new JSONObject().put("error", "errore inserimento"));
        }
    }

    private void courseProfessor(HttpServletRequest request, HttpServletResponse response, DataBase dB) throws IOException {
        try {
            new AdminModel(dB).courseProfessor(request.getParameter("course"), request.getParameter("professor"));
            response.getWriter().print(new JSONObject().put("error", ""));
        } catch (SQLException ex) {
            context.log("courseProfessor : " + ex.toString());
            response.getWriter().print(new JSONObject().put("error", "sql error"));
        }
    }

    private void getReservation(HttpServletRequest request, HttpServletResponse response, DataBase dB) throws IOException {
        try {
            JSONArray ar = new UserModel(dB).getReservation(request.getParameter("course"), request.getParameter("professor"));
            JSONObject res = new JSONObject().put("reservationMatrix", ar);
            if (ar.isNull(0)) {
                res.put("error", "no element");
            } else {
                res.put("error", "");
            }

            response.getWriter().print(res);
        } catch (SQLException ex) {
            context.log("getReservation : " + ex.toString());
            response.getWriter().print(new JSONObject().put("error", "sql error"));
        }
    }

    private void reserve(HttpServletRequest request, HttpServletResponse response, DataBase dB) throws IOException {
        try {
            new UserModel(dB).reserve(request.getParameter("slotId"), (String) request.getSession().getAttribute("id"));
            response.getWriter().print(new JSONObject().put("error", ""));
        } catch (SQLException ex) {
            context.log("reserve : " + ex.toString());
            response.getWriter().print(new JSONObject().put("error", "sql error"));
        }
    }

    private void getUserReservation(HttpServletRequest request, HttpServletResponse response, DataBase dB) throws IOException {
        try {
            JSONArray ar = new UserModel(dB).getReservation((String) request.getSession().getAttribute("id"));
            JSONObject re = new JSONObject();
            re.put("reservationList", ar);
            response.getWriter().print(re.put("error", ""));
        } catch (SQLException ex) {
            context.log("reserve : " + ex.toString());
            response.getWriter().print(new JSONObject().put("error", "sql error"));
        }
    }

    private void deleteReservation(HttpServletRequest request, HttpServletResponse response, DataBase dB) throws IOException {
        try {
            new UserModel(dB).deleteReservation(request.getParameter("reservationId"),
                    request.getParameter("reservationUserId"), (String) request.getSession().getAttribute("id"));
            response.getWriter().print(new JSONObject().put("error", ""));
        } catch (SQLException ex) {
            context.log("deleteReservation : " + ex.toString());
            response.getWriter().print(new JSONObject().put("error", "sql error"));
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        if (config != null) {
            url = config.getInitParameter("dbUrl");
            account = config.getInitParameter("account");
            password = config.getInitParameter("password");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
