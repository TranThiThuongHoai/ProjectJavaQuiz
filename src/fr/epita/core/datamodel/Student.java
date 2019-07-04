package fr.epita.core.datamodel;

import java.util.Objects;

public class Student {
    private String name;
    private String id;

    public String getName() {
        return this.name;
    }

    public void setName(String text) {
        this.name = text;
    }

    public String getID() {
        return this.id;
    }

    public void setID(String text) {
        this.id = text;
    }


    @Override
    public String toString() {
        return "{" +
            " name='" + name + "'" +
            ", id='" + id + "'" +
            "}";
    }

    public Student(String name, String id) {
        this.name = name;
        this.id = id;
    }

	public Student() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Student)) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(name, student.name) && Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
    
}
