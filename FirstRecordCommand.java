
public class FirstRecordCommand implements Command {
    private EmployeeDetails context;

    public FirstRecordCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.checkInput() && !context.checkForChanges()) {
            context.firstRecord();
            context.displayRecords(context.currentEmployee);
        }
    }
}