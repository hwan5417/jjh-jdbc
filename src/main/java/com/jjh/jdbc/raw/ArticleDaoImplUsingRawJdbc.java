package com.jjh.jdbc.raw;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.mariadb.jdbc.MariaDbDataSource;
import java.sql.Connection;

public class ArticleDaoImplUsingRawJdbc implements ArticleDao {
	/**
	 * 목록 가져오는 sql
	 */
	static final String LIST_ARTICLES = "SELECT articleId, title, name, cdate FROM article LIMIT 20";
	
	/**
	 * 글 1개 가져오는 sql
	 */
	static final String GET_ARTICLE = "SELECT articleId, title, content, name, cdate FROM article WHERE articleId=?";
	
	/**
	 * 글 등록하는 sql
	 */
	static final String ADD_ARTICLE = "INSERT INTO article(title, content, userId, name) VALUES (?,?,?,?)";
	
	/**
	 * 글 수정하는 sql
	 */
	static final String UPDATE_ARTICLE = "UPDATE article SET title=?, content=? WHERE articleId=?";
	
	/**
	 * 글 삭제하는 sql
	 */
	static final String DELETE_ARTICLE = "DELETE FROM article WHERE articleId=?";

	/**
	 * 데이터베이스 DataSource
	 */
	DataSource ds;

	public ArticleDaoImplUsingRawJdbc() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Article> listArticles() {
		// TODO Auto-generated method stub
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(LIST_ARTICLES)) {
			ResultSet rs = ps.executeQuery();
			List<Article> list = new ArrayList<>();
			while (rs.next()) {
				Article article = new Article();
				article.setArticleId(rs.getString("articleId"));
				article.setTitle(rs.getString("title"));
				article.setContent(rs.getString("content"));
				article.setName(rs.getString("name"));
				article.setCdate(rs.getString("cdate"));
				list.add(article);
			}
			rs.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	@Override
	public Article getArticle(String articleId) {
		// TODO Auto-generated method stub
		String url = "jdbc:mariadb://localhost:3306/2015041054db?user=jaehwan&password=wjdwoghks1208";
		String sql = "select articleId,title,content,userId,name,cdate from article where articleId=?";
		DataSource ds = new MariaDbDataSource(url);
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, articleId);
			ResultSet rs = ps.executeQuery();
			Article article = null;
			if(rs.next()) {
				article = new Article();
				article.setArticleId(rs.getString("articleId"));
				article.setTitle(rs.getString("title"));;
				article.setContent(rs.getString("content"));
				article.setUserId(rs.getString("userId"));
				article.setName(rs.getString("name"));
				article.setCdate(rs.getString("cdate"));
			}
			rs.close();
		return article;
		} catch(SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	@Override
	public void addArticle(Article article) {
		// TODO Auto-generated method stub
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(ADD_ARTICLE)) {
			ps.setString(1, article.getTitle());
			ps.setString(2, article.getContent());
			ps.setString(3, article.getUserId());
			ps.setString(4, article.getName());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	@Override
	public void updateArticle(Article article) {
		// TODO Auto-generated method stub
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE_ARTICLE)) {
			ps.setString(1, article.getTitle());
			ps.setString(2, article.getContent());
			ps.setString(3, article.getArticleId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}	
	}

	@Override
	public void deleteArticle(String articleId) {
		// TODO Auto-generated method stub
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(DELETE_ARTICLE)) {
			ps.setString(1, articleId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

}
