package Migration;

import lombok.AllArgsConstructor;

import java.sql.Connection;


@AllArgsConstructor
public class MigrationStrategy {

    private final Connection connection;


    public void executeMigration(){
        String resourcePath = "sql/init.sql";



    }
}
