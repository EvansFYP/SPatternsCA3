
public class DeleteRecordCommand implements Command {
    private EmployeeDetails context;

    public DeleteRecordCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.checkInput() && !context.checkForChanges()) {
            context.deleteRecord();
        }
    }
}