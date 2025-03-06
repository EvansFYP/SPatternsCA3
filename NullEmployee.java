public class NullEmployee extends Employee {
    public NullEmployee() {
        super(0, "", "", "", '\0', "", 0.0, false); // Initialize with default/empty values
    }

    @Override
    public String getPps() {
        return ""; // Return an empty string for PPS
    }

    @Override
    public String getSurname() {
        return ""; // Return an empty string for surname
    }

    @Override
    public String getFirstName() {
        return ""; // Return an empty string for first name
    }

    @Override
    public char getGender() {
        return '\0'; // Return a default gender (e.g., null character)
    }

    @Override
    public String getDepartment() {
        return ""; // Return an empty string for department
    }

    @Override
    public double getSalary() {
        return 0.0; // Return a default salary (e.g., 0.0)
    }

    @Override
    public boolean getFullTime() {
        return false; // Return a default value for full-time status
    }
}