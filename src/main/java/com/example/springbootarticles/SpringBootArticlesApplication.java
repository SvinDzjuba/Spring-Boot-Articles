package com.example.springbootarticles;

import com.example.springbootarticles.models.Article;
import com.example.springbootarticles.repositories.ArticleRepository;
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

	public static void main(String[] args) { SpringApplication.run(SpringBootArticlesApplication.class, args); }

	public void showAllArticlesByCategory(String tag) {
		articleRepo.findAll(tag).forEach(article -> System.out.println(getArticleDetails(article)));
	}
	public void showArticleById(String articleId) {
		Article article = articleRepo.findArticleById(articleId);
		if (article != null) {
			System.out.println(
					"\n--------- Article ---------" +
						"\nTitle: " + article.getTitle() +
						"\nContent: " + article.getContent() +
						"\nLikes: " + article.getFavoriteCount() +
						"\nTags: " + article.getTags()
			);
		} else {
			System.out.println("Article not found with ID: " + articleId);
		}
	}
	public String getArticleDetails(Article article) {
		System.out.println(
				"---------------------" +
					"\nArticle title: " + article.getTitle() +
					",\nArticle Body: " + article.getContent() +
					",\nLikes: " + article.getFavoriteCount() +
					",\nTags: " + article.getTags() +
					"-----------------------"
		);

		return "";
	}


	@Override
	public void run(String... args) throws Exception {
		showAllArticlesByCategory("Glassfish");
//		showArticleById("64feeb3b62070be77772b96c");
	}
}