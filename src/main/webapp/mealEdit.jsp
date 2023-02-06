<%--
  Created by IntelliJ IDEA.
  User: a1234
  Date: 06.02.2023
  Time: 09:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meal</title>
</head>
<body>
<section>
    <h2><a href="index.html">Home</a></h2>
    <h3>Edit meal</h3>
    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo" scope="request"/>
    <form method="post" action="meals">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt>Description:</dt>
            <dd><input type="text" name = "description" value="${meal.description}"></dd>
        </dl>
        <dl>
            <dt>Calories:</dt>
            <dd><input type="number" name = "calories" value="${meal.calories}"></dd>
        </dl>
        <button type="submit">Save</button>
        <button onclick="window.history.back()">Cancel</button>
    </form>

</section>

</body>
</html>
