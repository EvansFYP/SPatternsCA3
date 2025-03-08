
public class SearchByIdCommand implements Command {
    private EmployeeDetails context;

    public SearchByIdCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.checkInput() && !context.checkForChanges()) {
            context.searchEmployeeById();
        }
    }
}