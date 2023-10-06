package com.example.springbootarticles.services;

import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomService {

    @Autowired
    private UserRepository userRepo;

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepo.findByUsername(username);
    }
    public Date addMonthToCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }
    public Date addYearToCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        return calendar.getTime();
    }

    public String[] addOrRemoveStringFromArray(String[] array, String string, String action) {
        List<String> arrayList = new ArrayList<>(Arrays.asList(array));
        if (Objects.equals(action, "add")) {
            arrayList.add(string);
        } else {
            arrayList.remove(string);
        }
        return arrayList.toArray(new String[0]);
    }
}
