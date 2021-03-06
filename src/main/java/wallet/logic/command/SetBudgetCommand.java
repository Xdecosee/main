//@@author matthewng1996

package wallet.logic.command;

import wallet.model.Wallet;
import wallet.model.record.Budget;
import wallet.model.record.BudgetList;
import wallet.model.record.Expense;
import wallet.ui.Ui;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * The SetBudgetCommand Class deals with the 'budget' command.
 */
public class SetBudgetCommand extends Command {
    public static final String COMMAND_WORD = "budget";
    public static final String MESSAGE_SET_BUDGET = " dollars is a new budget set for ";
    public static final String MESSAGE_NOTE = "Note that to update your budget, "
            + "simply set the budget for the same month and year again.";
    public static final String MESSAGE_USAGE = "Error in format for command."
            + "\nExample: " + COMMAND_WORD + " $100 01/2019"
            + "\nExample: " + COMMAND_WORD + " $0 01/2019 " + "(To remove your budget for the month/year)";
    public static final String MESSAGE_EDIT_BUDGET = "You successfully edited your budget for ";
    public static final String MESSAGE_REMOVE_BUDGET = "You have successfully removed your budget for ";
    public static final String MESSAGE_NO_BUDGET_TO_REMOVE = "There is no budget for removal";
    public static final String MESSAGE_NO_NEGATIVE_BUDGET = "Budget must be more than or equals to zero";
    public static final String MESSAGE_EXISTING_EXPENSES = "There are existing expenses for ";
    public static final String MESSAGE_ADD_EXISTING_EXPENSES = "Would you like to add them into the budget? (Yes/No)";
    public static final String MESSAGE_YES_OR_NO = "Please reply with a Yes or No (Not case sensitive)";
    public static final String MESSAGE_CURRENT_BUDGET = "Your budget set for ";
    public static final String MESSAGE_REMAINING_BUDGET = "Your remaining budget for ";

    private Budget budget;

    public SetBudgetCommand(Budget budget) {
        this.budget = budget;
    }

    @Override
    public boolean execute(Wallet wallet) {
        try {
            if (budget != null) {
                BudgetList budgetList = wallet.getBudgetList();
                if (budget.getAmount() < 0) {
                    Ui.printError(MESSAGE_NO_NEGATIVE_BUDGET);
                    System.out.println(MESSAGE_USAGE);
                    return false;
                } else if (budget.getAmount() > 0) {
                    if (budgetList.getBudgetList().size() != 0) {
                        if (checkExpensesMatch(wallet)) {
                            System.out.println(MESSAGE_EXISTING_EXPENSES
                                    + new DateFormatSymbols().getMonths()[budget.getMonth() - 1]
                                    + " " + budget.getYear());
                            System.out.println(MESSAGE_ADD_EXISTING_EXPENSES);
                            takeInUserReply(wallet);
                        }
                        int index = checkDuplicates(budgetList);
                        if (index != -1) {
                            wallet.getBudgetList().editBudget(index, budget);
                            System.out.println(MESSAGE_EDIT_BUDGET
                                    + new DateFormatSymbols().getMonths()[budget.getMonth() - 1]
                                    + " " + budget.getYear());
                        } else {
                            System.out.println(budget.getAmount() + MESSAGE_SET_BUDGET
                                    + new DateFormatSymbols().getMonths()[budget.getMonth() - 1]
                                    + " " + budget.getYear());
                            wallet.getBudgetList().addBudget(budget);
                        }
                        updateSaveFile(wallet);
                    } else {
                        if (checkExpensesMatch(wallet)) {
                            System.out.println(MESSAGE_EXISTING_EXPENSES
                                    + new DateFormatSymbols().getMonths()[budget.getMonth() - 1]
                                    + " " + budget.getYear());
                            System.out.println(MESSAGE_ADD_EXISTING_EXPENSES);
                            takeInUserReply(wallet);
                        } else {
                            System.out.println(budget.getAmount() + MESSAGE_SET_BUDGET
                                    + new DateFormatSymbols().getMonths()[budget.getMonth() - 1]
                                    + " " + budget.getYear());
                        }
                        wallet.getBudgetList().addBudget(budget);
                        updateSaveFile(wallet);
                        System.out.println(MESSAGE_NOTE);
                    }
                    return false;
                } else if (budget.getAmount() == 0) {
                    if (budgetList.getBudgetList().size() == 0) {
                        Ui.printError(MESSAGE_NO_BUDGET_TO_REMOVE);
                        return false;
                    } else {
                        int index = checkDuplicates(budgetList);
                        if (index != -1) {
                            budgetList.getBudgetList().remove(index);
                            updateSaveFile(wallet);
                            System.out.println(MESSAGE_REMOVE_BUDGET
                                    + new DateFormatSymbols().getMonths()[budget.getMonth() - 1]
                                    + " " + budget.getYear());
                        } else {
                            Ui.printError(MESSAGE_NO_BUDGET_TO_REMOVE);
                        }
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(MESSAGE_USAGE);
        }
        return false;
    }

    private boolean checkExpensesMatch(Wallet wallet) {
        for (Expense e : wallet.getExpenseList().getExpenseList()) {
            LocalDate expenseDate = e.getDate();
            if (expenseDate.getMonthValue() == budget.getMonth()
                    && expenseDate.getYear() == budget.getYear()) {
                return true;
            }
        }
        return false;
    }

    private void takeInUserReply(Wallet wallet) {
        Scanner scanner = new Scanner(System.in);
        String reply = scanner.nextLine().toLowerCase();
        while (!"yes".equals(reply) || !"no".equals(reply)) {
            if ("yes".equals(reply)) {
                BigDecimal monthBudget = BigDecimal.valueOf(budget.getAmount());
                BigDecimal expenseSum = BigDecimal.valueOf(wallet.getExpenseList()
                        .getMonthExpenses(budget.getMonth(), budget.getYear()));
                double remainingBudget = monthBudget.subtract(expenseSum).doubleValue();
                budget.setAmount(remainingBudget);
                if (remainingBudget < 0) {
                    System.out.println(AddCommand.MESSAGE_EXCEED_BUDGET);
                    System.out.println(MESSAGE_CURRENT_BUDGET
                            + new DateFormatSymbols().getMonths()[budget.getMonth() - 1]
                            + " " + budget.getYear() + " is " + "$" + budget.getAmount());
                    System.out.println(MESSAGE_REMAINING_BUDGET
                            + new DateFormatSymbols().getMonths()[budget.getMonth() - 1]
                            + " " + budget.getYear() + " is " + "$" + remainingBudget);
                } else if (remainingBudget == 0) {
                    System.out.println(AddCommand.MESSAGE_REACH_BUDGET);
                    System.out.println(MESSAGE_CURRENT_BUDGET
                            + new DateFormatSymbols().getMonths()[budget.getMonth() - 1]
                            + " " + budget.getYear() + " is " + "$" + budget.getAmount());
                    System.out.println(MESSAGE_REMAINING_BUDGET
                            + new DateFormatSymbols().getMonths()[budget.getMonth() - 1]
                            + " " + budget.getYear() + " is " + "$" + remainingBudget);
                } else {
                    System.out.println("Budget updated.");
                    System.out.println(MESSAGE_CURRENT_BUDGET
                            + new DateFormatSymbols().getMonths()[budget.getMonth() - 1]
                            + " " + budget.getYear() + " is " + "$" + budget.getAmount());
                    System.out.println(MESSAGE_REMAINING_BUDGET
                            + new DateFormatSymbols().getMonths()[budget.getMonth() - 1]
                            + " " + budget.getYear() + " is " + "$" + remainingBudget);
                }
                break;
            } else if ("no".equals(reply)) {
                BigDecimal expenseSum = BigDecimal.valueOf(wallet.getExpenseList()
                        .getMonthExpenses(budget.getMonth(), budget.getYear()));
                budget.setAccountedExpenseAmount(expenseSum.doubleValue());
                System.out.println(MESSAGE_CURRENT_BUDGET
                        + new DateFormatSymbols().getMonths()[budget.getMonth() - 1]
                        + " " + budget.getYear() + " is " + "$" + budget.getAmount());
                System.out.println(MESSAGE_REMAINING_BUDGET
                        + new DateFormatSymbols().getMonths()[budget.getMonth() - 1]
                        + " " + budget.getYear() + " is " + "$" + budget.getAmount());
                break;
            } else {
                System.out.println(MESSAGE_YES_OR_NO);
                reply = scanner.nextLine().toLowerCase();
            }
        }
    }

    private int checkDuplicates(BudgetList budgetList) {
        for (int i = 0; i < budgetList.getBudgetList().size(); i++) {
            if (budget.getMonth() == budgetList.getBudgetList().get(i).getMonth()
                    && budget.getYear() == budgetList.getBudgetList().get(i).getYear()) {
                return i;
            }
        }
        return -1;
    }

    private void updateSaveFile(Wallet wallet) {
        wallet.getBudgetList().setModified(true);
    }
}

