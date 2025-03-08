
public class PreviousRecordCommand implements Command {
    private EmployeeDetails context;

    public PreviousRecordCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.checkInput() && !context.checkForChanges()) {
            context.previousRecord();
            context.displayRecords(context.currentEmployee);
        }
    }
}