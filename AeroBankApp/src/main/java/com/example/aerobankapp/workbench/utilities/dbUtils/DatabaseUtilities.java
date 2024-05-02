package com.example.aerobankapp.workbench.utilities.dbUtils;

import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Service
public class DatabaseUtilities
{
    private JdbcTemplate jdbcTemplate;
    private Logger LOGGER = LoggerFactory.getLogger(DatabaseUtilities.class);

    @Autowired
    public DatabaseUtilities(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public boolean resetAutoIncrementsForTables() {
        try {
            // Reset auto increment for the 'account' table
            Integer maxAccountId = jdbcTemplate.queryForObject("SELECT MAX(acctID) FROM account", Integer.class);
            int nextAutoIncValue = (maxAccountId == null ? 1 : maxAccountId + 1);
            jdbcTemplate.update("ALTER TABLE account AUTO_INCREMENT = ?", nextAutoIncValue);
            LOGGER.info("Successfully reset AUTO INCREMENT for account table.");
            // Repeat for other tables
            Integer maxPropsId = jdbcTemplate.queryForObject("SELECT MAX(accountPropsID) FROM accountProperties", Integer.class);
            jdbcTemplate.update("ALTER TABLE accountProperties AUTO_INCREMENT = ?", maxPropsId == null ? 1 : maxPropsId + 1);
            LOGGER.info("Successfully reset AUTO INCREMENT for account properties table.");

            Integer maxAcctSecurityId = jdbcTemplate.queryForObject("SELECT MAX(accountSecurityID) FROM accountSecurity", Integer.class);
            jdbcTemplate.update("ALTER TABLE accountSecurity AUTO_INCREMENT = ?", maxAcctSecurityId == null ? 1 : maxAcctSecurityId + 1);
            LOGGER.info("Successfully reset AUTO INCREMENT for account security table.");

            Integer maxAcctCodeId = jdbcTemplate.queryForObject("SELECT MAX(acctCodeID) FROM accountCode", Integer.class);
            jdbcTemplate.update("ALTER TABLE accountCode AUTO_INCREMENT = ?", maxAcctCodeId == null ? 1 : maxAcctCodeId + 1);
            LOGGER.info("Successfully reset AUTO INCREMENT for account code table.");

            Integer maxUserLogId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM userLog", Integer.class);
            jdbcTemplate.update("ALTER TABLE userLog AUTO_INCREMENT = ?", maxUserLogId == null ? 1 : maxUserLogId + 1);
            LOGGER.info("Successfully reset AUTO INCREMENT for user log table.");

            Integer maxUserId = jdbcTemplate.queryForObject("SELECT MAX(userID) FROM users", Integer.class);
            jdbcTemplate.update("ALTER TABLE users AUTO_INCREMENT = ?", maxUserId == null ? 1 : maxUserId + 1);
            LOGGER.info("Successfully reset AUTO INCREMENT for users table.");

            // Add similar blocks for other tables as needed

            return true;
        } catch (Exception e) {
            LOGGER.error("Error resetting auto increments: " + e.getMessage());
            return false;
        }
    }


}
