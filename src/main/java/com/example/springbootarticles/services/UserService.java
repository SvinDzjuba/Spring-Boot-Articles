package com.example.springbootarticles.services;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.ArticleResponse;
import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ArticleService articleService;

    private Date newMonthDate;
    private Date newYearDate;

    public void updateUserHandler(String id, User user) throws NotFoundException {
        Optional<User> userData = userRepo.findById(id);
        if (userData.isPresent()){
            User userToUpdate = userData.get();
            userToUpdate.setName(user.getName() != null ? user.getName() : userToUpdate.getName());
            userToUpdate.setUsername(user.getUsername() != null ? user.getUsername() : userToUpdate.getUsername());
            userToUpdate.setRole(user.getRole() != null ? user.getRole() : userToUpdate.getRole());
            userToUpdate.setEmail(user.getEmail() != null ? user.getEmail() : userToUpdate.getEmail());
            userToUpdate.setPassword(user.getPassword() != null ? passwordEncoder.encode(user.getPassword()) : userToUpdate.getPassword());
            userToUpdate.setUpdated_at(new Date());
            userToUpdate.setSubscription(user.getSubscription() != null ? user.getSubscription() : userToUpdate.getSubscription());
            userRepo.save(userToUpdate);
        } else {
            throw new NotFoundException("User not found!");
        }
    }

    public ArticleResponse checkUserSubscriptionAvailability(Authentication authentication, String articleId) throws ParseException {
        // Check if user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userRepo.findByUsername(username);
            Date currentDate = new Date();
            if (user.getSubscription().getResets_at().after(currentDate)) {
                // Subscription is active
                if (user.getSubscription().getRemains() > 0) {
                    user.getSubscription().setRemains(user.getSubscription().getRemains() - 1);
                    userRepo.save(user);
                    return articleService.getArticleWithDetails(articleId, "ORIGINAL");
                }
                return articleService.getArticleWithDetails(articleId, "DEMO");
            }
        }
        // Non authenticated user - demo article
        return articleService.getArticleWithDetails(articleId, "DEMO");
    }

    public void checkAndUpdateUserSubscription(String username) {
        User user = userRepo.findByUsername(username);
        Date now = new Date();
        if (user.getSubscription().getResets_at().before(now)) {
            // Subscription is need to be reset
            String currentPlan = user.getSubscription().getPlan();
            switch (currentPlan) {
                case "Free":
                    user.getSubscription().setRemains(5);
                    newMonthDate = addMonthToCurrentDate();
                    user.getSubscription().setExpires_at(newMonthDate);
                    user.getSubscription().setResets_at(newMonthDate);
                    userRepo.save(user);
                    break;
                case "Silver Month":
                    user.getSubscription().setRemains(10);
                    newMonthDate = addMonthToCurrentDate();
                    user.getSubscription().setExpires_at(newMonthDate);
                    user.getSubscription().setResets_at(newMonthDate);
                    userRepo.save(user);
                    break;
                case "Silver Year":
                    user.getSubscription().setRemains(10);
                    newYearDate = addYearToCurrentDate();
                    newMonthDate = addMonthToCurrentDate();
                    user.getSubscription().setExpires_at(newYearDate);
                    user.getSubscription().setResets_at(newMonthDate);
                    userRepo.save(user);
                    break;
                case "Gold Month":
                    user.getSubscription().setRemains(20);
                    newMonthDate = addMonthToCurrentDate();
                    user.getSubscription().setExpires_at(newMonthDate);
                    user.getSubscription().setResets_at(newMonthDate);
                    userRepo.save(user);
                    break;
                case "Gold Year":
                    user.getSubscription().setRemains(20);
                    newYearDate = addYearToCurrentDate();
                    newMonthDate = addMonthToCurrentDate();
                    user.getSubscription().setExpires_at(newYearDate);
                    user.getSubscription().setResets_at(newMonthDate);
                    userRepo.save(user);
                    break;
            }
        }
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
}
