package data;

import lombok.val;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;

public class Storage {

    static String url = System.getProperty("db.url");
    static String user = System.getProperty("db.user");
    static String password = System.getProperty("db.password");

    @SneakyThrows
    public static void clearTables() {
        val cleanOrderEntity = "DELETE FROM order_entity;";
        val cleanPaymentEntity = "DELETE FROM payment_entity;";
        val cleanCreditRequestEntity = "DELETE FROM credit_request_entity;";
        val runner = new QueryRunner();
        val conn = DriverManager.getConnection(url, user, password);
        runner.update(conn, cleanOrderEntity);
        runner.update(conn, cleanPaymentEntity);
        runner.update(conn, cleanCreditRequestEntity);

    }

    public static String findPaymentStatus() {
        val statusSQL = "SELECT status FROM payment_entity WHERE created = (SELECT max(created) FROM payment_entity);";
        return getData(statusSQL);
    }

    public static String findCreditStatus() {
        val statusSQL = "SELECT status FROM credit_request_entity WHERE created = (SELECT max(created) FROM credit_request_entity);";
        return getData(statusSQL);
    }

    @SneakyThrows
    public static String countRecords() {
        val countSQL = "SELECT COUNT(*) FROM order_entity;";
        val runner = new QueryRunner();
        Long count = null;
        val conn = DriverManager.getConnection(url, user, password);
        count = runner.query(conn, countSQL, new ScalarHandler<>());
        return Long.toString(count);
    }

    @SneakyThrows
    private static String getData(String query) {
        String data = "";
        val runner = new QueryRunner();
        val conn = DriverManager.getConnection(url, user, password);
        data = runner.query(conn, query, new ScalarHandler<>());
        return data;
    }
}
