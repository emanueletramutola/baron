package cnr.imaa.baron.bll.retriever;

import cnr.imaa.baron.bll.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class UscrnRetriever extends Task {
    private enum USCRN_TASK_NAME {
        USCRN_RETRIEVER_SUBHOURLY,
        USCRN_RETRIEVER_HOURLY,
        USCRN_RETRIEVER_DAILY,
        USCRN_RETRIEVER_MONTHLY
    }

    @Override
    public void run(String... args) {
        USCRN_TASK_NAME[] uscrnTasks = new USCRN_TASK_NAME[4];

        uscrnTasks[0] = USCRN_TASK_NAME.USCRN_RETRIEVER_SUBHOURLY;
        uscrnTasks[1] = USCRN_TASK_NAME.USCRN_RETRIEVER_HOURLY;
        uscrnTasks[2] = USCRN_TASK_NAME.USCRN_RETRIEVER_DAILY;
        uscrnTasks[3] = USCRN_TASK_NAME.USCRN_RETRIEVER_MONTHLY;

        for (USCRN_TASK_NAME uscrnTaskName : uscrnTasks) {
            Task task;

            switch (uscrnTaskName) {
                case USCRN_RETRIEVER_SUBHOURLY:
                    task = new UscrnRetrieverSubhourly();
                    break;
                case USCRN_RETRIEVER_HOURLY:
                    task = new UscrnRetrieverHourly();
                    break;
                case USCRN_RETRIEVER_DAILY:
                    task = new UscrnRetrieverDaily();
                    break;
                case USCRN_RETRIEVER_MONTHLY:
                    task = new UscrnRetrieverMonthly();
                    break;
                default:
                    throw new RuntimeException("Unexpected USCRN task name: '" + uscrnTaskName + "'");
            }

            task.run(args);

            task.quit();
        }
    }

    public static String[] getFileRows(byte[] arr) {
        String[] fileRows;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(arr.length)) {

            baos.write(arr, 0, arr.length);

            fileRows = baos.toString().split(System.lineSeparator());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Arrays.
                stream(fileRows)
                .parallel()
                .forEach(x -> x = cleanTextContent(x));

        return fileRows;
    }

    public static String getSitename(String file) {
        String[] fileField = file.split("/");
        String filenameFull = fileField[fileField.length - 1];
        String[] filenameFullField = filenameFull.split("-");
        return filenameFullField[filenameFullField.length - 1].replace(".txt", "");
    }

    private static String cleanTextContent(String text) {
        // strips off all non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", "");

        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");

        return text.trim();
    }
}
