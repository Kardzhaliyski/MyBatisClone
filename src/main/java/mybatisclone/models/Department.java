package mybatisclone.models;


public class Department {
    public int departmentId;
    public String name;
    public Location location;

    public Department() {
    }

    public Department(int departmentId, String name) {
        this.departmentId = departmentId;
        this.name = name;
    }

    public Department(int departmentId, String name, Location location) {
        this.departmentId = departmentId;
        this.name = name;
        this.location = location;
    }

    @Override
    public String toString() {
        return "Department{" +
                "department_id=" + departmentId +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}
