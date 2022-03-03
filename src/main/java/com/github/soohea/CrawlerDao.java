package com.github.soohea;

import java.sql.SQLException;

public interface CrawlerDao {
    String getNextLink(String sql) throws SQLException;

    String getNextLinkAndDelete() throws SQLException;

    void updateDatabase(String link, String sql) throws SQLException;

    void insertNewsIntoDatabase(String title, String content, String url) throws SQLException;

    boolean isLinkProcessed(String link) throws SQLException;


}
