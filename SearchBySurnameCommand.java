
public class SearchBySurnameCommand implements Command {
    private EmployeeDetails context;

    public SearchBySurnameCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        if (context.checkInput() && !context.checkForChanges()) {
          
            context.searchEmployeeBySurname(); 

           
        }
    }
}
