package com.example.springbootarticles.services;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.ArticleResponse;
import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.RouteMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.swing.*;
import java.text.ParseException;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ArticleService articleService;

    private final Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
    private Date newMonthDate;

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
            String[] planMonthArr = currentPlan.split(" ");
            String plan;
            if (planMonthArr.length == 1) {
                plan = planMonthArr[0];
            } else {
                plan = planMonthArr[1];
            }
            newMonthDate = addMonthToCurrentDate();
            switch (plan) {
                case "Free":
                    user.getSubscription().setRemains(5);
                    user.getSubscription().setExpires_at(newMonthDate);
                    user.getSubscription().setResets_at(newMonthDate);
                    userRepo.save(user);
                    break;
                case "Month":
                    // Silver/Gold Month is expired - downgrade to Free
                    downgradeUserSubscriptionToFree(user);
                    break;
                case "Year":
                    if (user.getSubscription().getExpires_at().before(now)) {
                        // Silver/Gold Year is expired - downgrade to Free
                        downgradeUserSubscriptionToFree(user);
                    } else {
                        // Silver/Gold Year is active - renew the reset date
                        if (planMonthArr[0].equals("Silver")) {
                            // Silver Year
                            user.getSubscription().setRemains(10);
                        } else {
                            // Gold Year
                            user.getSubscription().setRemains(20);
                        }
                        user.getSubscription().setResets_at(newMonthDate);
                        userRepo.save(user);
                    }
                    break;
            }
        }
    }

    public void followToUser(String id) {
        // Get the user to follow
        Optional<User> userToFollowOptional = userRepo.findById(id);
        if (!userToFollowOptional.isPresent()) {
            throw new NotFoundException("User not found!");
        }
        User userToFollow = userToFollowOptional.get();

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepo.findByUsername(currentUsername);

        String[] userFollowing = currentUser.getFollowing();
        String[] userFollowers = userToFollow.getFollowers();

        int initialFollowingLength = userFollowing.length;
        int initialFollowersLength = userFollowers.length;
        String[] following = Arrays.copyOf(userFollowing, initialFollowingLength + 1);
        String[] followers = Arrays.copyOf(userFollowers, initialFollowersLength + 1);
        following[initialFollowingLength] = userToFollow.getId();
        followers[initialFollowersLength] = currentUser.getId();

        currentUser.setFollowing(following);
        userToFollow.setFollowers(followers);

        // Save the updated current user and user to follow
        userRepo.save(currentUser);
        userRepo.save(userToFollow);
    }

    public void unfollowUser(String id) {
        // Get the user to unfollow
        Optional<User> userToUnfollowOptional = userRepo.findById(id);
        if (!userToUnfollowOptional.isPresent()) {
            throw new NotFoundException("User not found!");
        }
        User userToUnfollow = userToUnfollowOptional.get();

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepo.findByUsername(currentUsername);

        String[] userFollowing = currentUser.getFollowing();
        String[] userFollowers = userToUnfollow.getFollowers();
        List<String> followersList = new ArrayList<>(Arrays.asList(userFollowers));
        List<String> followingList = new ArrayList<>(Arrays.asList(userFollowing));
        followingList.remove(userToUnfollow.getId());
        followersList.remove(currentUser.getId());
        String[] followers = followersList.toArray(new String[0]);
        String[] following = followingList.toArray(new String[0]);
        currentUser.setFollowing(following);
        userToUnfollow.setFollowers(followers);

        // Save the updated current user and user to follow
        userRepo.save(currentUser);
        userRepo.save(userToUnfollow);
    }

    public boolean checkUserDuplicate(String username, String email) {
        User user = userRepo.findByUsername(username);
        User userByEmail = userRepo.findByEmail(email);
        return user != null || userByEmail != null;
    }

    public void downgradeUserSubscriptionToFree(User user) {
        user.getSubscription().setPlan("Free");
        user.getSubscription().setRemains(5);
        newMonthDate = addMonthToCurrentDate();
        user.getSubscription().setExpires_at(newMonthDate);
        user.getSubscription().setResets_at(newMonthDate);
        userRepo.save(user);
    }

    public void upgradeUserSubscription(String plan, User user) {
        user.getSubscription().setPlan(plan);
        Date newYearDate = addYearToCurrentDate();
        newMonthDate = addMonthToCurrentDate();
        switch (plan) {
            case "Silver Month":
                user.getSubscription().setRemains(10);
                user.getSubscription().setExpires_at(newMonthDate);
                user.getSubscription().setResets_at(newMonthDate);
                break;
            case "Gold Month":
                user.getSubscription().setRemains(20);
                user.getSubscription().setExpires_at(newMonthDate);
                user.getSubscription().setResets_at(newMonthDate);
                break;
            case "Silver Year":
                user.getSubscription().setRemains(10);
                user.getSubscription().setExpires_at(newYearDate);
                user.getSubscription().setResets_at(newMonthDate);
                break;
            case "Gold Year":
                user.getSubscription().setRemains(20);
                user.getSubscription().setExpires_at(newYearDate);
                user.getSubscription().setResets_at(newMonthDate);
                break;
        }
        userRepo.save(user);
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
