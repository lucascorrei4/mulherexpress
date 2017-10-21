package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import models.Article;
import models.Parameter;
import models.TheSystem;
import play.mvc.Controller;
import play.vfs.VirtualFile;
import util.TypeAdsNewsEnum;
import util.Utils;

public class Application extends Controller {

	public static void index() {
		List<Article> highlightArticles = Article.find("highlight = true and isActive = true").fetch(2);
		List<Article> listArticles = Article.find("highlight = false and isActive = true order by postedAt desc").fetch();
		List<Article> sidebarRightNews = getArticlesSidebarRightNews(listArticles);
		List<Article> bottomNews = getArticlesBottomNews(listArticles, sidebarRightNews);
		/* Article Ads */
		Article articleTopAds = getArticleAdsTop(listArticles);
		List<Article> articleSidebarRightAds = getArticleAdsSidebarRight(listArticles);
		List<Article> articleBottomAds = getArticlesAdsBottom(listArticles);
		Parameter parameter = Parameter.all().first();
		List<TheSystem> listTheSystems = TheSystem.find("highlight = false and isActive = true order by postedAt desc")
				.fetch(6);
		TheSystem theSystem = new TheSystem();
		theSystem.setShowTopMenu(true);
		render(bottomNews, sidebarRightNews, highlightArticles, parameter, listTheSystems, theSystem, articleTopAds, articleSidebarRightAds, articleBottomAds);
	}
	
	public static void details(String id) {
		Article article = Article.findById(Long.valueOf(id));
		List<Article> listArticles = Article.find("highlight = false and isActive = true and id <>  " + Long.valueOf(id) + " order by postedAt desc").fetch();
		List<Article> sidebarRightNews = getArticlesSidebarRightNews(listArticles);
		List<Article> bottomNews = getArticlesBottomNews(listArticles, sidebarRightNews);
		/* Article Ads */
		Article articleTopAds = getArticleAdsTop(listArticles);
		List<Article> articleSidebarRightAds = getArticleAdsSidebarRight(listArticles);
		List<Article> articleBottomAds = getArticlesAdsBottom(listArticles);
		Parameter parameter = Parameter.all().first();
		List<TheSystem> listTheSystems = TheSystem.find("highlight = false and isActive = true order by postedAt desc")
				.fetch(6);
		TheSystem theSystem = new TheSystem();
		theSystem.setShowTopMenu(true);
		String title = Utils.removeHTML(article.getTitle());
		render(article, bottomNews, sidebarRightNews, parameter, listTheSystems, theSystem, title, articleTopAds, articleSidebarRightAds, articleBottomAds);
	}

	private static List<Article> getArticlesSidebarRightNews(List<Article> listArticles) {
		List<Article> listArticle = new ArrayList<Article>();
		for (Article article : listArticles) {
			if (article.getTypeAds() != null && TypeAdsNewsEnum.isDefaultArticle(article.getTypeAds().getValue())) {
				listArticle.add(article);
			}
			if (listArticle.size() == 2) {
				return listArticle;
			}
		}
		return listArticle;
	}

	private static List<Article> getArticlesBottomNews(List<Article> listArticles, List<Article> listArticlesSidebarRight) {
		List<Article> listArticle = new ArrayList<Article>();
		for (Article article : listArticles) {
			if (article.getTypeAds() != null && TypeAdsNewsEnum.isDefaultArticle(article.getTypeAds().getValue()) && !listArticlesSidebarRight.contains(article)) {
				listArticle.add(article);
			}
			if (listArticle.size() == 6) {
				return listArticle;
			}
		}
		return listArticle;
	}

	private static List<Article> getArticlesAdsBottom(List<Article> listArticles) {
		List<Article> listArticle = new ArrayList<Article>();
		for (Article article : listArticles) {
			if (article.getTypeAds() != null && TypeAdsNewsEnum.isAdsBottom(article.getTypeAds().getValue())) {
				listArticle.add(article);
			}
			if (listArticle.size() == 4) {
				return listArticle;
			}
		}
		return listArticle;
	}

	private static Article getArticleAdsTop(List<Article> listArticles) {
		for (Article article : listArticles) {
			if (article.getTypeAds() != null && TypeAdsNewsEnum.isAdsTop(article.getTypeAds().getValue())) {
				return article;
			}
		}
		return null;
	}

	private static List<Article> getArticleAdsSidebarRight(List<Article> listArticles) {
		List<Article> listArticle = new ArrayList<Article>();
		for (Article article : listArticles) {
			if (article.getTypeAds() != null && TypeAdsNewsEnum.isAdsSidebarRight(article.getTypeAds().getValue())) {
				listArticle.add(article);
			}
			if (listArticle.size() == 2) {
				return listArticle;
			}
		}
		return listArticle;
	}

	public static void getImage(long id, String index) {
		final Article article = Article.findById(id);
		notFoundIfNull(article);
		if ("1".equals(index)) {
			if (article.getImage1() != null) {
				renderBinary(article.getImage1().get());
				return;
			} 
		} else if ("2".equals(index)) {
			if (article.getImage2() != null) {
				renderBinary(article.getImage2().get());
				return;
			} 
		}
	}

	public static File getVirtualFile() {
		VirtualFile vf = VirtualFile.fromRelativePath("/public/images/apple120x120.png");
		File f = vf.getRealFile();
		return f;
	}
	
	public static void contact() {
		TheSystem theSystem = new TheSystem();
		theSystem.setShowTopMenu(true);
		List<Article> listArticles = Article.find("highlight = false and isActive = true order by postedAt desc").fetch(6);
		List<Article> bottomNews = listArticles.subList(0, 3);
		Parameter parameter = Parameter.all().first();
		render(theSystem, bottomNews, parameter);
	}

}