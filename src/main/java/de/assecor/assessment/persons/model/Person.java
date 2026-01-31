package de.assecor.assessment.persons.model;

public record Person(int id, String name,String lastname,String city,String color){
    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", city='" + city + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
