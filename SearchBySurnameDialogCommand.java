
public class SearchBySurnameDialogCommand implements Command {
    private EmployeeDetails context;

    public SearchBySurnameDialogCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        
        context.displaySearchBySurnameDialog(); 
    }
}
