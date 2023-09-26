package com.example.springbootarticles;

import com.example.springbootarticles.models.Article;
import com.example.springbootarticles.models.Comment;
import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.ArticleRepository;
import com.example.springbootarticles.repositories.CommentRepository;
import com.example.springbootarticles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class SpringBootArticlesApplication implements CommandLineRunner {

	@Autowired
	ArticleRepository articleRepo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	CommentRepository commentRepo;

	public static void main(String[] args) { SpringApplication.run(SpringBootArticlesApplication.class, args); }

//	public void showAllArticlesByCategory(String tag) {
//		articleRepo.findAll(tag).forEach(this::getArticleDetails);
//	}
//	public void showArticleById(String articleId) {
//		Article article = articleRepo.findArticleById(articleId);
//		if (article != null) {
//			System.out.println(
//					"\n--------- Article ---------" +
//						"\nTitle: " + article.getTitle() +
//						"\nContent: " + article.getContent() +
//						"\nLikes: " + article.getFavoriteCount() +
//						"\nTags: " + article.getTags() +
//						"-----------------------"
//			);
//		} else {
//			System.out.println("Article not found with ID: " + articleId);
//		}
//	}
//	public void getArticleDetails(Article article) {
//		System.out.println(
//				"---------------------" +
//					"\nArticle title: " + article.getTitle() +
//					",\nArticle Body: " + article.getContent() +
//					",\nLikes: " + article.getFavoriteCount() +
//					",\nTags: " + article.getTags()
//		);
//	}
	public void showUserById(String userId) {
		User user = userRepo.findUserById(userId);
		if (user != null){
			System.out.println(
					"\n--------- User ---------" +
						"\nName: " + user.getName() +
						"\nEmail: " + user.getEmail() +
						"\nSubscription: " + user.getSubscription() +
						"\n-----------------------"
			);
		} else {
			System.out.println("User not found with ID: " + userId);
		}
	}

	public User getUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

	public User getUserByUsernameAndPassword(String username, String password) {
        return userRepo.findUserByUsernameAndPassword(username, password);
	}

	@Override
	public void run(String... args) throws Exception {
//		showAllArticlesByCategory("Glassfish");
//		showArticleById("64feeb3b62070be77772b96c");
//		showUserById("64fef86a34cd3b4f73c5a13b");
//		showCommentById("65081aace6b5b60cd9f5e784");
	}
}