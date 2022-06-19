package me.espryth.easyjoin.plugin.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLSource {

  private final HikariDataSource source;

  public SQLSource(ConfigurationSection section) {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setUsername(section.getString("user"));
    hikariConfig.setPassword(section.getString("password"));
    hikariConfig.setJdbcUrl(getUri(section));
    this.source = new HikariDataSource(hikariConfig);
  }

  public Connection getConnection() throws SQLException {
    return this.source.getConnection();
  }

  public HikariDataSource getSource() {
    return this.source;
  }

  private String getUri(ConfigurationSection section) {
    return "jdbc:mysql://" + section.getString("address") + "/" + section.getString("database");
  }

}
