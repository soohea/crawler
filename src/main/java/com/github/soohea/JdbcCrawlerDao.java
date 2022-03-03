package com.github.soohea;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcCrawlerDao implements CrawlerDao {

    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";
    private final Connection connection;

    @SuppressFBWarnings("DMI_CONSTANT_DB_PASSWORD")
    public JdbcCrawlerDao() {
        try {
            this.connection = DriverManager.getConnection("jdbc:h2:file:E:\\projects\\crawler\\news", USER_NAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private String getNextLink(String sql) throws SQLException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString(1);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return null;
    }

    public String getNextLinkThenDelete() throws SQLException {
        String link = getNextLink("select LINK from LINKS_TO_BE_PROCESSED LIMIT 1");
        if (link != null) {
            updateDatabase(link, "DELETE FROM LINKS_TO_BE_PROCESSED WHERE LINK = ?");
        }
        return link;
    }


    public void updateDatabase(String link, String sql) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, link);
            statement.executeUpdate();
        }
    }

    public void insertNewsIntoDatabase(String title, String content, String url) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("insert into NEWS(title, content, url, created_at, updated_at) values (?,?,?,now(),now() ) ")) {
            statement.setString(1, title);
            statement.setString(2, content);
            statement.setString(3, url);
            statement.executeUpdate();
        }

    }

    public boolean isLinkProcessed(String link) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT LINK FROM LINKS_ALREADY_PROCESSED WHERE LINK = ?")) {
            statement.setString(1, link);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void insertProcessedLink(String link) {

    }

    @Override
    public void insertLinkToBeProcessed(String href) {

    }
}
