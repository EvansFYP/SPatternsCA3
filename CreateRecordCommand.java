
public class CreateRecordCommand implements Command {
    private EmployeeDetails context;

    public CreateRecordCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.checkInput() && !context.checkForChanges()) {
            new AddRecordDialog(context);
        }
    }
}
