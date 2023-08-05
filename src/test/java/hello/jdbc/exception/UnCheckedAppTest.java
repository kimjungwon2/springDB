package hello.jdbc.exception;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import hello.jdbc.exception.CheckedAppTest.Controller;
import hello.jdbc.exception.CheckedAppTest.NetworkClient;
import hello.jdbc.exception.CheckedAppTest.Repository;
import hello.jdbc.exception.CheckedAppTest.Service;
import java.net.ConnectException;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class UnCheckedAppTest {
    @Test
    void unchecked(){
        CheckedAppTest.Controller controller = new CheckedAppTest.Controller();
        assertThatThrownBy(()->controller.request())
                .isInstanceOf(Exception.class);
    }

    @Test
    void printEx(){
        Controller controller =new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            log.info("ex",e);
        }

    }

    static class Controller{
        CheckedAppTest.Service service = new CheckedAppTest.Service();

        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }

    static class Service{

        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient{

        public void call()  {
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository{
        public void  call() {
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }

        public void runSQL() throws SQLException{
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException{
        public RuntimeConnectException(String message){
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException{

        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }

}
