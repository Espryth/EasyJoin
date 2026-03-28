package me.espryth.easyjoin.database;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface FirstJoinDao {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS EJFirstJoin(uuid VARCHAR(36) PRIMARY KEY)")
    void createTable();

    @SqlQuery("SELECT COUNT(*) > 0 FROM EJFirstJoin WHERE uuid = :uuid")
    boolean exists(@Bind("uuid") String uuid);

    @SqlUpdate("INSERT IGNORE INTO EJFirstJoin(uuid) VALUES(:uuid)")
    void insert(@Bind("uuid") String uuid);
}
