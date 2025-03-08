
public class SaveFileAsCommand implements Command {
    private EmployeeDetails context;

    public SaveFileAsCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.checkInput() && !context.checkForChanges()) {
            context.saveFileAs();
        }
    }
}
