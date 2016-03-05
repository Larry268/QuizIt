package User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by scottparsons on 2/27/16.
 */
@WebServlet(name = "NewAccountServlet")
public class NewAccountServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IUserRepository userRepo = (UserRepository) request.getSession().getAttribute("user");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String adminCheckbox = request.getParameter("adminCheckbox");

        try
        {
            if(userRepo.createNewUser(username, password, adminCheckbox != null)) {
                request.getSession().setAttribute("createInfo",new String(""));
                RequestDispatcher dispatch = request.getRequestDispatcher("userHome.jsp");
                dispatch.forward(request, response);
            }
            else {
                request.getSession().setAttribute("createInfo",new String("Username already taken. Please try another."));
                RequestDispatcher dispatch = request.getRequestDispatcher("createAccount.jsp");
                dispatch.forward(request, response);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
