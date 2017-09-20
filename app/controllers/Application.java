package controllers;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.Article;
import models.BodyMail;
import models.Institution;
import models.MailList;
import models.Message;
import models.MoipNotification;
import models.OrderOfService;
import models.OrderOfServiceStep;
import models.OrderOfServiceValue;
import models.Parameter;
import models.SendTo;
import models.Sender;
import models.Service;
import models.StatusMail;
import models.StatusSMS;
import models.TheSystem;
import models.User;
import play.data.validation.Error;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;
import play.vfs.VirtualFile;
import util.FromEnum;
import util.UserInstitutionParameter;
import util.Utils;

public class Application extends Controller {

	public static void index() {
		List<Article> highlightArticles = Article.find("highlight = true and isActive = true").fetch(2);
		List<Article> listArticles = Article.find("highlight = false and isActive = true order by postedAt desc").fetch(10);
		List<Article> sidebarRightNews = listArticles.subList(0, 2);
		List<Article> bottomNews = listArticles.subList(2, listArticles.size());
		Parameter parameter = Parameter.all().first();
		List<TheSystem> listTheSystems = TheSystem.find("highlight = false and isActive = true order by postedAt desc")
				.fetch(6);
		TheSystem theSystem = new TheSystem();
		theSystem.setShowTopMenu(true);
		render(bottomNews, sidebarRightNews, highlightArticles, parameter, listTheSystems, theSystem);
	}

	public static void details(String id) {
		Article article = Article.findById(Long.valueOf(id));
		List<Article> listArticles = Article.find("highlight = false and isActive = true and id <>  " + Long.valueOf(id) + " order by postedAt desc").fetch(6);
		List<Article> sidebarRightNews = listArticles.subList(0, 2);
		List<Article> bottomNews = listArticles.subList(2, listArticles.size());
		Parameter parameter = Parameter.all().first();
		List<TheSystem> listTheSystems = TheSystem.find("highlight = false and isActive = true order by postedAt desc")
				.fetch(6);
		TheSystem theSystem = new TheSystem();
		theSystem.setShowTopMenu(true);
		String title = Utils.removeHTML(article.getTitle());
		render(article, bottomNews, sidebarRightNews, parameter, listTheSystems, theSystem, title);
	}
	
	public static void getImage(long id, String index) {
		final Article article = Article.findById(id);
		notFoundIfNull(article);
		if ("1".equals(index)) {
			if (article.getImage1() != null) {
				renderBinary(article.getImage1().get());
				return;
			} else {
				renderBinary(getVirtualFile());
			}
		} else if ("2".equals(index)) {
			if (article.getImage2() != null) {
				renderBinary(article.getImage2().get());
				return;
			} else {
				renderBinary(getVirtualFile());
			}
		}
	}

	public static File getVirtualFile() {
		VirtualFile vf = VirtualFile.fromRelativePath("/public/images/logo-271x149.png");
		File f = vf.getRealFile();
		return f;
	}

}