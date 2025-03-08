
public class LastRecordCommand implements Command {
    private EmployeeDetails context;

    public LastRecordCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.checkInput() && !context.checkForChanges()) {
            context.lastRecord();
            context.displayRecords(context.currentEmployee);
        }
    }
}