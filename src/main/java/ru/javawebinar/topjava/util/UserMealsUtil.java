package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        boolean excess = false;
        int caloriesSum = 0;
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            caloriesSum += meal.getCalories();
            if (caloriesSum > caloriesPerDay) {
                excess = true;
            }

            LocalTime mealTime = meal.getDateTime().toLocalTime();
            if (mealTime.isAfter(startTime.minusSeconds(1))
                    && mealTime.isBefore(endTime.plusSeconds(1))) {
                result.add(new UserMealWithExcess(meal, excess));
            }
        }

        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        AtomicBoolean excess = new AtomicBoolean(false);
        AtomicInteger caloriesSum = new AtomicInteger();

        return meals.stream()
                .map(meal -> {
                    if (caloriesSum.addAndGet(meal.getCalories()) > caloriesPerDay) {
                        excess.set(true);
                    }
                    return new UserMealWithExcess(meal, excess.get());
                    })
                .filter(meal -> {
                    LocalTime mealTime = meal.getDateTime().toLocalTime();
                    return mealTime.isAfter(startTime) && mealTime.isBefore(endTime);
                })
                .collect(Collectors.toList());
    }
}
