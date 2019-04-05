
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lollo
 */
public class DBDaemon implements ServletContextListener {
    
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    public void contextInitialized(ServletContextEvent sce) {
        //start Daemon task at init
        sce.getServletContext().log("COUNER DI PROVA ");
        Daemon d = new Daemon(sce.getServletContext());
        d.setDaemon(true);
        d.start();
        
        //start Daemon task every day at 24:00
        Long midnight = LocalDateTime.now().until(LocalDate.now().plusDays(1)
                .atStartOfDay(), ChronoUnit.MILLIS);
        scheduler.scheduleAtFixedRate(new Daemon(sce.getServletContext()), midnight,
                TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        scheduler.shutdownNow();
    }
}

class Daemon extends Thread {

    ServletContext context;
    DataBase dB;

    public Daemon(ServletContext c) {
        context = c;
        dB = new DataBase(c.getInitParameter("dbUrl"),
                 c.getInitParameter("account"), c.getInitParameter("password"));
    }

    @Override
    public void run() {
        context.log("MESSAGE : DBDaemon is performing task");
        try {
            dB.update(getQuery());
        } catch (SQLException ex) {
            context.log("ERROR DBDaemon : " + ex);
        }
    }

    private String getQuery() {
        return "update reservation r\n"
                + "set state = 'expried'\n"
                + "from  slot s \n"
                + "where  r.slot = s.id and\n"
                + "	state != 'expried' and s.day < " + (getDay() + 1) + ";\n"
                + "\n"
                + "update reservation r\n"
                + "set state = 'free'\n"
                + "from  slot s \n"
                + "where  r.slot = s.id and s.day > " + getDay() + " and state = 'expried';\n"
                + "  \n"
                + "DO\n"
                + "$do$\n"
                + "\n"
                + "DECLARE r_u_id reservation_user.id%type;\n"
                + "\n"
                + "BEGIN\n"
                + "	FOR r_u_id IN \n"
                + "		select r_u.id\n"
                + "		from reservation_user r_u join reservation r \n"
                + "			on r_u.reservation = r.id\n"
                + "			join slot s on s.id = r.slot\n"
                + "		where s.day < " + getDay() + "	LOOP\n"
                + "		\n"
                + "		delete from reservation_user ru\n"
                + "		where ru.id = r_u_id;\n"
                + "		\n"
                + "	END LOOP;\n"
                + "END;\n"
                + "$do$";
    }

    private int getDay() {
        //return translate(LocalDate.now().getDayOfWeek().name());
        return 1;
    }

    private int translate(String in) {
        in = in.toLowerCase();
        switch (in) {
            case "monday":
                return 0;
            case "tuesday":
                return 1;
            case "wednesday":
                return 2;
            case "thursday":
                return 3;
            case "friday":
                return 4;
            case "saturday":
                return -1;
            case "sunday":
                return -1;
        }
        return -1;
    }

}
