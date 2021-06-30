package cnr.imaa.baron;

import cnr.imaa.baron.bll.Task;
import cnr.imaa.baron.bll.harmonizer.Rharm;
import cnr.imaa.baron.bll.retriever.GruanRetriever;
import cnr.imaa.baron.bll.retriever.IgraRetriever;
import cnr.imaa.baron.bll.retriever.UscrnRetriever;
import cnr.imaa.baron.commons.exception.IncorrectTaskNameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        new App().run(args);
    }

    private void run(String[] args) {
        try {
            Task task = getTask(args);

            task.run(args);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString();

            log.error("ERROR! Message: " + sStackTrace);
        }
    }

    private Task getTask(String[] args) throws IncorrectTaskNameException {
        Task task = null;

        Task.TASK_NAME task_name;

        try {
            task_name = Task.TASK_NAME.valueOf(args[Task.ARGS.TASK_NAME.position].trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IncorrectTaskNameException("Task name is not expected: " + args[0]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IncorrectTaskNameException("Please define what module you want to run. I.e. java -jar baron.jar gruan_retriever");
        }

        switch (task_name) {
            case IGRA_RETRIEVER:
                task = new IgraRetriever();
                break;
            case RHARM:
                task = new Rharm();
                break;
            case GRUAN_RETRIEVER:
                task = new GruanRetriever();
                break;
            case USCRN_RETRIEVER:
                task = new UscrnRetriever();
                break;
        }

        return task;
    }
}
