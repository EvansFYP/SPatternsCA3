
public class SearchByIdDialogCommand implements Command {
    private EmployeeDetails context;

    public SearchByIdDialogCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        // Open the "Search by ID" dialog
        context.displaySearchByIdDialog(); 
} }
