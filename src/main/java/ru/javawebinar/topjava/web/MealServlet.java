package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("do get meals");

        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("mealList", MealsUtil.getFiltered(MealsUtil.MEALS, 2000));
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            String id = request.getParameter("id");
            if (id == null) {
                throw new ServletException("id parameter required");
            }
            MealsUtil.delete(Integer.parseInt(id));
            response.sendRedirect("meals");
        } else {
            String id = request.getParameter("id");
            Meal meal = null;
            if (id != null) {
                meal = MealsUtil.get(Integer.parseInt(id));
            }

            if (meal == null)
                meal = new Meal(LocalDateTime.now(), "", 0);

            request.setAttribute("meal", MealsUtil.createTo(meal, false));
            request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
        }
//        request.getRequestDispatcher("/users.jsp").forward(request, response);
//        response.sendRedirect("mealList.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("UTF-8");
        log.debug("do post meals");

        LocalDateTime dateTime = TimeUtil.parseDateTime(request.getParameter("dateTime"));
        String id = request.getParameter("id");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        Meal meal = new Meal(dateTime, description, calories);
        if (id != null) {
            meal.setId(Integer.parseInt(id));
        }
        MealsUtil.save(meal);
        response.sendRedirect("meals");
    }
}
