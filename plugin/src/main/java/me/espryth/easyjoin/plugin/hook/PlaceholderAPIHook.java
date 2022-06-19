package me.espryth.easyjoin.plugin.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.espryth.easyjoin.plugin.database.SQLSource;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;
import static me.espryth.easyjoin.plugin.format.firstjoin.SQLFirstJoinChecker.TABLE_NAME;

public class PlaceholderAPIHook
  extends PlaceholderExpansion {

  private final Optional<SQLSource> source;
  private final Plugin plugin;

  public PlaceholderAPIHook() {
    this.source = CONTAINER.getOptional(SQLSource.class);
    this.plugin = CONTAINER.get(Plugin.class);
  }

  @Override
  public String onPlaceholderRequest(Player player, @NotNull String params) {

    if (player == null) {
      return "";
    }

    if (params.equals("count")) {

      int count;

      if(source.isPresent()) {
        count = asyncCount().join();
      } else {
        count = Bukkit.getOfflinePlayers().length;
      }

      return String.valueOf(count);
    }


    return null;
  }

  private CompletableFuture<Integer> asyncCount() {
    return CompletableFuture.supplyAsync(() -> {
      try (Connection connection = source.get().getConnection();
           PreparedStatement statement = connection.prepareStatement("SELECT * FROM "+TABLE_NAME);
      ){
        ResultSet result = statement.executeQuery();
        int count = 0;
        while(result.next()) {
          count++;
        }
        return count;
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return 0;
    });
  }

  public boolean persist() {
    return true;
  }

  public boolean canRegister() {
    return true;
  }

  public String getAuthor() {
    return plugin.getDescription().getAuthors().get(0);
  }

  public String getIdentifier() {
    return "easyjoin";
  }

  public String getVersion() {
    return plugin.getDescription().getVersion();
  }

}
