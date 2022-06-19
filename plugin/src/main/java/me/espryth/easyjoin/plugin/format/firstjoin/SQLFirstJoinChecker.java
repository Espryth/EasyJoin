package me.espryth.easyjoin.plugin.format.firstjoin;

import me.espryth.easyjoin.plugin.database.SQLSource;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class SQLFirstJoinChecker
  implements FirstJoinChecker {

  private static final String QUERY = "uuid VARCHAR(36)";
  private static final String TABLE_NAME = "EJFirstJoin";

  private final SQLSource source;

  public SQLFirstJoinChecker(SQLSource source) {
    this.source = source;
    try (
      Connection connection = source.getConnection();
      PreparedStatement createTable = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + QUERY + ")");
    ) {
      createTable.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    };

  }

  @Override
  public boolean isFirstJoin(Player player) {
    return asyncSearch(player).join();
  }

  public void updateFirstJoin(Player player) {
    System.out.println("UPDATINNG");
    CompletableFuture.runAsync(() -> {
      try (Connection connection = source.getConnection();
           PreparedStatement statement = connection.prepareStatement("INSERT INTO "+TABLE_NAME+" VALUE (?)");
      ){
        statement.setString(1, player.getUniqueId().toString());
        statement.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }

  private CompletableFuture<Boolean> asyncSearch(Player player) {
    return CompletableFuture.supplyAsync(() -> {
      try (Connection connection = source.getConnection();
           PreparedStatement statement = connection.prepareStatement("SELECT * FROM "+TABLE_NAME+" WHERE (uuid=?)");
      ){
        statement.setString(1, player.getUniqueId().toString());
        ResultSet result = statement.executeQuery();
        if(result.next()) {
          return false;
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return true;
    });
  }
}
