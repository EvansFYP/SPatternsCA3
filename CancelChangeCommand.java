
public class CancelChangeCommand implements Command {
    private EmployeeDetails context;

    public CancelChangeCommand(EmployeeDetails context) {
        this.context = context;
    }

    @Override
    public void execute() {
        context.cancelChange();
    }
}