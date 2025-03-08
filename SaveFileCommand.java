
public class SaveFileCommand implements Command {
    private EmployeeDetails context;

    public SaveFileCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.checkInput() && !context.checkForChanges()) {
            context.saveFile();
            
        }
    }
}