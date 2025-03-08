
public class NextRecordCommand implements Command {
    private EmployeeDetails context;

    public NextRecordCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.checkInput() && !context.checkForChanges()) {
            context.nextRecord();
            context.displayRecords(context.currentEmployee);
        }
    }
}