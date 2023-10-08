package com.example.springbootarticles.services;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.*;
import com.example.springbootarticles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CustomService customService;

    private Date newMonthDate;

    public void updateUserHandler(UserRequest user) throws NotFoundException {
        User userToUpdate = customService.getAuthenticatedUser();
        if (userToUpdate != null) {
            userToUpdate.setName(user.getName() != null ? user.getName() : userToUpdate.getName());
            userToUpdate.setUsername(user.getUsername() != null ? user.getUsername() : userToUpdate.getUsername());
            userToUpdate.setBio(user.getBio() != null ? user.getBio() : userToUpdate.getBio());
            userToUpdate.setEmail(user.getEmail() != null ? user.getEmail() : userToUpdate.getEmail());
            userToUpdate.setPassword(user.getPassword() != null ? passwordEncoder.encode(user.getPassword()) : userToUpdate.getPassword());
            userToUpdate.setUpdated_at(new Date());
            userRepo.save(userToUpdate);
        } else {
            throw new NotFoundException("User not found!");
        }
    }
    public void deleteUserHandler() {
        User userToDelete = customService.getAuthenticatedUser();
        if (userToDelete != null) {
            // Delete all user articles and comments
            List<Article> articles = articleService.getArticlesByAuthorId(userToDelete.getId());
            List<Comment> comments = commentService.getCommentsByAuthorId(userToDelete.getId());
            for (Article article : articles) {
                articleService.deleteArticleHandler(article.getId());
                for (Comment comment : comments) {
                    commentService.deleteCommentHandler(comment.getId(), article.getSlug());
                }
            }
            // Delete user
            userRepo.delete(userToDelete);
        } else {
            throw new NotFoundException("User not found!");
        }
    }
    public ArticleResponse checkUserSubscriptionAvailability(String articleId) {
        // Check if user is authenticated
        User currentUser = customService.getAuthenticatedUser();;
        if (currentUser != null) {
            Date currentDate = new Date();
            if (currentUser.getSubscription().getResets_at().after(currentDate)) {
                // Subscription is active
                if (currentUser.getSubscription().getRemains() > 0) {
                    currentUser.getSubscription().setRemains(currentUser.getSubscription().getRemains() - 1);
                    userRepo.save(currentUser);
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
            newMonthDate = customService.addMonthToCurrentDate();
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
    public FollowersFollowing showFollowersOrFollowing(String action) {
        User currentUser = customService.getAuthenticatedUser();
        String[] actionList;
        List<String> emptyActionList = new ArrayList<>();
        if (Objects.equals(action, "followers")) {
            actionList = currentUser.getFollowers();
        } else {
            actionList = currentUser.getFollowing();
        }
        for (String followerId : actionList) {
            User follower = userRepo.findById(followerId).orElse(null);
            String followerName = follower != null ? follower.getName() : "Unknown User";
            emptyActionList.add(followerName);
        }
        return new FollowersFollowing(
                emptyActionList.size(),
                emptyActionList.toArray(new String[0])
        );
    }

    public void followOrUnfollowUser(String username, String action) {
        // Get the user to unfollow
        User userToUnfollow = userRepo.findByUsername(username);
        if (userToUnfollow == null) {
            throw new NotFoundException("User not found!");
        }
        User currentUser = customService.getAuthenticatedUser();;
        boolean isFollowing = Arrays.asList(currentUser.getFollowing()).contains(userToUnfollow.getId());

        String[] following;
        String[] followers;

        if (Objects.equals(action, "follow")) {
            if (isFollowing) {
                throw new NotFoundException("User already followed!");
            }
            following = customService.addOrRemoveStringFromArray(currentUser.getFollowing(), userToUnfollow.getId(), "add");
            followers = customService.addOrRemoveStringFromArray(userToUnfollow.getFollowers(), currentUser.getId(), "add");
        } else {
            if (!isFollowing) {
                throw new NotFoundException("User not followed!");
            }
            following = customService.addOrRemoveStringFromArray(currentUser.getFollowing(), userToUnfollow.getId(), "remove");
            followers = customService.addOrRemoveStringFromArray(userToUnfollow.getFollowers(), currentUser.getId(), "remove");
        }

        currentUser.setFollowing(following);
        userToUnfollow.setFollowers(followers);

        // Save the updated current user and user to follow
        userRepo.save(currentUser);
        userRepo.save(userToUnfollow);
    }

    public Profile showUserProfile(String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new NotFoundException("User not found!");
        }
        // Check if authorized user is following the user
        User currentUser = customService.getAuthenticatedUser();
        if (currentUser != null) {
            String[] following = currentUser.getFollowing();
            boolean isFollowing = Arrays.asList(following).contains(user.getId());
            return new Profile(
                    user.getName(),
                    user.getUsername(),
                    user.getBio(),
                    isFollowing
            );
        } else {
            return new Profile(
                    user.getName(),
                    user.getUsername(),
                    user.getBio(),
                    false
            );
        }
    }

    public boolean checkUserDuplicate(String username, String email) {
        User user = userRepo.findByUsername(username);
        User userByEmail = userRepo.findByEmail(email);
        return user != null || userByEmail != null;
    }

    public void downgradeUserSubscriptionToFree(User user) {
        user.getSubscription().setPlan("Free");
        user.getSubscription().setRemains(5);
        newMonthDate = customService.addMonthToCurrentDate();
        user.getSubscription().setExpires_at(newMonthDate);
        user.getSubscription().setResets_at(newMonthDate);
        userRepo.save(user);
    }

    public void upgradeUserSubscription(String plan, User user) {
        user.getSubscription().setPlan(plan);
        Date newYearDate = customService.addYearToCurrentDate();
        newMonthDate = customService.addMonthToCurrentDate();
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
}
