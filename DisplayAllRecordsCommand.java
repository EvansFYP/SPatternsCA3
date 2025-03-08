
public class DisplayAllRecordsCommand implements Command {
    private EmployeeDetails context;

    public DisplayAllRecordsCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.checkInput() && !context.checkForChanges()) {
            if (context.isSomeoneToDisplay()) {
                context.displayEmployeeSummaryDialog();
            }
        }
    }
}
