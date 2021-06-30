package cnr.imaa.baron.bll;

import cnr.imaa.baron.bll.harmonizer.Rharm;
import cnr.imaa.baron.bll.retriever.*;
import cnr.imaa.baron.repository.DataSource;
import cnr.imaa.baron.utils.FileResourceUtils;

public abstract class Task {
    private FileResourceUtils fileResourceUtils;
    private DataSource dataSource;

    public enum ARGS {
        TASK_NAME(0),
        IMPORT_DATA_FROM_DISK(1),
        PATH_DATA(2),
        SAVE_SOURCE_FILE(3),
        PATH_DESTINATION(4),
        REMOTE_PATH(5);

        public final int position;

        ARGS(int position) {
            this.position = position;
        }
    }

    public enum TASK_NAME {
        IGRA_RETRIEVER,
        RHARM,
        GRUAN_RETRIEVER,
        USCRN_RETRIEVER
    }

    public Task() {
        loadConfig();

        dataSource = new DataSource(this.fileResourceUtils);
    }

    private void loadConfig() {
        String taskname_chars_to_replace = "$$TASK_NAME$$";
        String configFilename = "application_" + taskname_chars_to_replace + ".properties";
        String configurationFilename = null;

        if (this instanceof IgraRetriever)
            configurationFilename = configFilename.replace(taskname_chars_to_replace, "igra_retriever");
        else if (this instanceof GruanRetriever)
            configurationFilename = configFilename.replace(taskname_chars_to_replace, "gruan_retriever");
        else if (this instanceof UscrnRetrieverSubhourly)
            configurationFilename = configFilename.replace(taskname_chars_to_replace, "uscrn_retriever_subhourly");
        else if (this instanceof UscrnRetrieverHourly)
            configurationFilename = configFilename.replace(taskname_chars_to_replace, "uscrn_retriever_hourly");
        else if (this instanceof UscrnRetrieverDaily)
            configurationFilename = configFilename.replace(taskname_chars_to_replace, "uscrn_retriever_daily");
        else if (this instanceof UscrnRetrieverMonthly)
            configurationFilename = configFilename.replace(taskname_chars_to_replace, "uscrn_retriever_monthly");
        else if (this instanceof Rharm)
            configurationFilename = configFilename.replace(taskname_chars_to_replace, "rharm");

        if (configurationFilename != null)
            fileResourceUtils = new FileResourceUtils(configurationFilename);
    }

    public abstract void run(String... args);

    public FileResourceUtils getFileResourceUtils() {
        return fileResourceUtils;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void quit() {
        this.dataSource.close();
    }
}
