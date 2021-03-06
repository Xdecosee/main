//@@author matthewng1996

package wallet.storage;

import wallet.model.record.Budget;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class BudgetStorage extends Storage<Budget> {
    public static final String DEFAULT_STORAGE_FILEPATH_BUDGET = "./data/budget.txt";

    /**
     * loads the list of Budget.
     * @return list of set budget.
     */
    @Override
    public ArrayList<Budget> loadFile() {
        ArrayList<Budget> budgetList = new ArrayList<>();

        try {
            RandomAccessFile raf = new RandomAccessFile(DEFAULT_STORAGE_FILEPATH_BUDGET, "rws");
            String str;
            while (raf.getFilePointer() != raf.length()) {
                str = raf.readLine();
                String[] data = str.split(",");
                if (!data[0].isEmpty() && !data[1].isEmpty() && !data[2].isEmpty()
                        && !data[3].isEmpty() && !data[4].isEmpty()) {
                    Budget budget = new Budget(Double.parseDouble(data[0]),
                            Integer.parseInt(data[1]), Integer.parseInt(data[2]),
                            Boolean.parseBoolean(data[3]), Double.parseDouble(data[4]));
                    budgetList.add(budget);
                }
            }
            raf.close();
        } catch (FileNotFoundException e) {
            System.out.println("No saved budget found.");
        } catch (IOException e) {
            System.out.println("End of file.");
        }

        return budgetList;
    }

    /**
     * Writes budget list to file.
     * @param budgetList The list of budgets.
     */
    @Override
    public void writeListToFile(ArrayList<Budget> budgetList) {
        try {
            RandomAccessFile raf = new RandomAccessFile(DEFAULT_STORAGE_FILEPATH_BUDGET, "rws");
            raf.setLength(0);

            for (Budget budget : budgetList) {
                if (raf.getFilePointer() != 0) {
                    raf.writeBytes("\r\n");
                }
                raf.writeBytes(budget.writeToFile());
            }
            raf.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
