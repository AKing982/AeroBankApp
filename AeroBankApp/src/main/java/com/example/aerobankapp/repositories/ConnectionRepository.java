package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.ConnectionsEntity;
import com.example.aerobankapp.workbench.utilities.db.DBType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@PreAuthorize("isAuthenticated()")
public interface ConnectionRepository extends JpaRepository<ConnectionsEntity, Long>
{
    @Query("SELECT e.dbServer FROM ConnectionsEntity e WHERE e.connectionID=:id")
    String getServerAddressById(@Param("id") Long id);

    @Query("SELECT e.dbPort FROM ConnectionsEntity e WHERE e.connectionID=:id")
    int getPortById(@Param("id") Long id);

    @Query("SELECT e.dbUser FROM ConnectionsEntity e WHERE e.connectionID=:id")
    String getUserNameById(@Param("id") Long id);

    @Query("UPDATE ConnectionsEntity u SET u.dbServer=:server, u.dbPort=:port, u.dbDriver=:driver, u.dbName=:name, u.dbUser=:user, u.dbURL=:url, u.dbPass=:pass, u.dateModified=:dateModified, u.dbType=:type")
    @Modifying
    @PreAuthorize("hasRole('ADMIN')")
    void updateConnection(@Param("server") String server, @Param("port") int port, @Param("driver") String driver, @Param("name") String dbName, @Param("user") String user, @Param("url") String url, @Param("pass") String pass, @Param("dateModified") LocalDate dateModified, @Param("type")DBType type);
}
