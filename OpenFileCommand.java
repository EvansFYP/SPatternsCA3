
public class OpenFileCommand implements Command {
    private EmployeeDetails context;  

    public OpenFileCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.checkInput() && !context.checkForChanges()) {
            context.openFile();
        }
    }
}