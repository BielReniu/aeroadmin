package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.jupiter;

import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.FileNotFoundException;
import java.util.Objects;

public class SchemaLoader {
    public static void loadScript(String script)  {
        var datasource = new SingleConnectionDataSource();
        ScriptRunner scriptRunner = new ScriptRunner(datasource.getConnection());
        scriptRunner.setSendFullScript(false);
        scriptRunner.setStopOnError(true);
        try {
            var scriptFile = new java.io.FileReader(Objects.requireNonNull(SchemaLoader.class.getResource(script)).getFile());
            scriptRunner.runScript(scriptFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

