package com.example.springbootarticles.services;

import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.*;

@Service
public class CustomService {

    @Autowired
    private UserRepository userRepo;

    private static final int MAX_SLUG_LENGTH = 256;

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

    public String slugify(String s) {
        final String intermediateResult = Normalizer
                .normalize(s, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^-_a-zA-Z0-9]", "-").replaceAll("\\s+", "-")
                .replaceAll("[-]+", "-").replaceAll("^-", "")
                .replaceAll("-$", "").toLowerCase();
        return intermediateResult.substring(0,
                Math.min(MAX_SLUG_LENGTH, intermediateResult.length()));
    }
}
