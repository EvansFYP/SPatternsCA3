
public class EditRecordCommand implements Command {
    private EmployeeDetails context;

    public EditRecordCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.checkInput() && !context.checkForChanges()) {
            context.editDetails();
        }
    }
}