package com.github.soohea;

import java.sql.SQLException;

public interface CrawlerDao {


    String getNextLinkThenDelete() throws SQLException;

    void insertNewsIntoDatabase(String title, String content, String url) throws SQLException;

    boolean isLinkProcessed(String link) throws SQLException;


    void insertProcessedLink(String link);

    void insertLinkToBeProcessed(String href);
}
