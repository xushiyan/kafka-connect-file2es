import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class WorkerPropertiesGenerator {

    public static final String COMMON_WORKER_PROPS = "common-worker.properties";

    public static void generate(Path dataSourceFilePath, Path workerPropsDirPath) {

        // data source file name w/o extension is used as topic
        String fname = dataSourceFilePath.getFileName().toString();
        String workerTopic = fname.indexOf(".") > 0 ? fname.substring(0, fname.lastIndexOf(".")) : fname;
        Path commonPropsPath = Paths.get(workerPropsDirPath.toString(), COMMON_WORKER_PROPS);
        try (FileInputStream fis = new FileInputStream(commonPropsPath.toFile());
                FileOutputStream fos = new FileOutputStream(Paths
                        .get(workerPropsDirPath.toString(), String.format("%s.properties", workerTopic)).toFile());) {

            // create new properties file for each worker based on common properties
            Properties props = new Properties();
            props.load(fis);
            props.setProperty("name", workerTopic);
            props.setProperty("topic", workerTopic);
            props.setProperty("file", dataSourceFilePath.toString());
            props.store(fos, null);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void deleteCommonProps(Path workerPropsDirPath) throws IOException {
        Path commonPropsPath = Paths.get(workerPropsDirPath.toString(), COMMON_WORKER_PROPS);
        Files.deleteIfExists(commonPropsPath);
    }

    public static void main(String[] args) throws IOException {
        String cwd = System.getProperty("user.dir");
        String dataSourceDirname = System.getenv("DATA_SOURCES_DIRNAME");
        String workerPropsDirname = System.getenv("WORKER_PROPS_DIRNAME");
        Path dataSourcesDir = Paths.get(cwd, dataSourceDirname != null ? dataSourceDirname : "data-sources");
        Path workerPropsDir = Paths.get(cwd, workerPropsDirname != null ? workerPropsDirname : "worker-props");
        Files.walk(dataSourcesDir).filter(Files::isRegularFile).forEach(p -> {
            WorkerPropertiesGenerator.generate(p, workerPropsDir);
        });
        WorkerPropertiesGenerator.deleteCommonProps(workerPropsDir);
    }
}