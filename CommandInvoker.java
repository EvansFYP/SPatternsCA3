public class CommandInvoker {
    private Command openCommand;
    private Command saveCommand;
    private Command saveAsCommand;
    private Command createCommand;
    private Command editCommand;
    private Command deleteCommand;
    private Command firstCommand;
    private Command prevCommand;
    private Command nextCommand;
    private Command lastCommand;
    private Command displayAllCommand;
    private Command searchByIdCommand;
    private Command searchBySurnameCommand;
    private Command cancelChangeCommand;
    private Command searchByIdDialogCommand;  
    private Command searchBySurnameDialogCommand;  

    public CommandInvoker(EmployeeDetails context) {
        // Initialize all the existing commands
        this.openCommand = new OpenFileCommand(context);
        this.saveCommand = new SaveFileCommand(context);
        this.saveAsCommand = new SaveFileAsCommand(context);
        this.createCommand = new CreateRecordCommand(context);
        this.editCommand = new EditRecordCommand(context);
        this.deleteCommand = new DeleteRecordCommand(context);
        this.firstCommand = new FirstRecordCommand(context);
        this.prevCommand = new PreviousRecordCommand(context);
        this.nextCommand = new NextRecordCommand(context);
        this.lastCommand = new LastRecordCommand(context);
        this.displayAllCommand = new DisplayAllRecordsCommand(context);
        this.searchByIdCommand = new SearchByIdCommand(context);
        this.searchBySurnameCommand = new SearchBySurnameCommand(context);
        this.cancelChangeCommand = new CancelChangeCommand(context);

        
        this.searchByIdDialogCommand = new SearchByIdDialogCommand(context);
        this.searchBySurnameDialogCommand = new SearchBySurnameDialogCommand(context);
    }

 // Set commands for each button
    public void setOpenCommand(Command openCommand) {
        this.openCommand = openCommand;
    }

    public void setSaveCommand(Command saveCommand) {
        this.saveCommand = saveCommand;
    }

    public void setSaveAsCommand(Command saveAsCommand) {
        this.saveAsCommand = saveAsCommand;
    }

    public void setCreateCommand(Command createCommand) {
        this.createCommand = createCommand;
    }

    public void setEditCommand(Command editCommand) {
        this.editCommand = editCommand;
    }

    public void setDeleteCommand(Command deleteCommand) {
        this.deleteCommand = deleteCommand;
    }

    public void setFirstCommand(Command firstCommand) {
        this.firstCommand = firstCommand;
    }

    public void setPrevCommand(Command prevCommand) {
        this.prevCommand = prevCommand;
    }

    public void setNextCommand(Command nextCommand) {
        this.nextCommand = nextCommand;
    }

    public void setLastCommand(Command lastCommand) {
        this.lastCommand = lastCommand;
    }

    public void setDisplayAllCommand(Command displayAllCommand) {
        this.displayAllCommand = displayAllCommand;
    }

    public void setSearchByIdCommand(Command searchByIdCommand) {
        this.searchByIdCommand = searchByIdCommand;
    }

    public void setSearchBySurnameCommand(Command searchBySurnameCommand) {
        this.searchBySurnameCommand = searchBySurnameCommand;
    }
    
    public void setCancelChangeCommand(Command cancelChangeCommand) {
        this.cancelChangeCommand = cancelChangeCommand;
    }


    
    public void setSearchByIdDialogCommand(Command searchByIdDialogCommand) {
        this.searchByIdCommand = searchByIdDialogCommand;
    }

    public void setSearchBySurnameDialogCommand(Command searchBySurnameDialogCommand) {
        this.searchBySurnameCommand = searchBySurnameDialogCommand;
    }

    // Trigger the command's execute method based on the button press
    public void invokeOpen() {
        openCommand.execute();
    }

    public void invokeSave() {
        saveCommand.execute();
    }

    public void invokeSaveAs() {
        saveAsCommand.execute();
    }

    public void invokeCreate() {
        createCommand.execute();
    }

    public void invokeEdit() {
        editCommand.execute();
    }

    public void invokeDelete() {
        deleteCommand.execute();
    }

    public void invokeFirst() {
        firstCommand.execute();
    }

    public void invokePrev() {
        prevCommand.execute();
    }

    public void invokeNext() {
        nextCommand.execute();
    }

    public void invokeLast() {
        lastCommand.execute();
    }

    public void invokeDisplayAll() {
        displayAllCommand.execute();
    }

    public void invokeSearchById() {
        searchByIdCommand.execute();
    }

    public void invokeSearchBySurname() {
        searchBySurnameCommand.execute();
    }

    public void invokeCancelChange() {
        cancelChangeCommand.execute();
    }

    
    public void invokeSearchByIdDialog() {
        searchByIdDialogCommand.execute();
    }

    public void invokeSearchBySurnameDialog() {
        searchBySurnameDialogCommand.execute();
    }
}