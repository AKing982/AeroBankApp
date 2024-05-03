package com.example.aerobankapp.workbench.transactionHistory.queries;

public class QueryConstants
{
    public static String STATUS_DESCRIPTION_DEPOSIT = "SELECT e.description, e.amount, e.scheduledDate, e.scheduledTime, e.status \n" +
                                                      "FROM DepositsEntity e WHERE status IN (':status') AND e.user.userID=:userID AND e.description LIKE '%:descr%'";
    public static String STATUS_DESCRIPTION_START_DATE_DEPOSIT = "SELECT e.description, e.amount, e.scheduledDate, e.scheduledTime, e.status \n" +
                                                                "FROM DepositsEntity e WHERE e.status IN (':status') AND description LIKE '%:descr%' AND scheduledDate=:startDate";

    public static String DESCRIPTION_START_DATE_END_DATE_DEPOSIT = "SELECT e.description, e.amount, e.scheduledDate, e.scheduledTime, e.status \n" +
                                                                   "FROM DepositsEntity e WHERE e.description LIKE '%:descr%' AND scheduledDate BETWEEN ':startDate' AND ':endDate'";

    public static String
}
