package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.CharacterCardName;
import it.polimi.ingsw.enums.CharacterColor;

public class CharacterCardwithStudents extends CharacterCard {
    private final List<Student> students;

    public CharacterCardwithStudents(CharacterCardName name, int cost, String description, List<Student> students)
    {
        super(name, cost, description);
        this.students = new ArrayList<Student>(students);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student)
    {
        students.add(student);
    }

    public Student removeStudent(CharacterColor studentColor) {
        //TODO lanciare un'eccezione se non è presente uno studente di quel colore
        Student student = null;
        for(int i=0; i<students.size() && student==null; i++) {
            if(students.get(i).getColor().equals(studentColor)) {
                student = students.remove(i);
            }
        }
        return student;
    }

    @Override
    public StringBuilder draw(int x, int y) {
        StringBuilder card = new StringBuilder();
        card.append(Constants.cursorUp(y));
        String top_wall = "╒════════════╕\n";//14
        String middle_wall = "├────────────┤\n";
        String bottom_wall = "╘════════════╛\n";
        String vertical = "│";
        String cost;
        if (super.getCost() < 10)
            cost = vertical + "  COST: " + super.getCost() + "   " + vertical+"\n";
        else cost = vertical + "  COST: " + super.getCost() + "  " + vertical+"\n";
        int name_index = 0;
        int students_index=0;
        Constants.moveObject(card,x,top_wall);
        card.append(Constants.cursorRight(x));
        for (int j = 0; j < 14; j++) {
            if (j == 0 || j == 13) card.append(vertical);
            else if (j > (12-super.getName().toString().length())/2 && name_index < super.getName().toString().length()) {
                card.append(super.getName().toString().charAt(name_index));
                name_index++;
            } else card.append(" ");
        }
        card.append("\n");
        Constants.moveObject(card,x,middle_wall);
        Constants.moveObject(card,x,cost);
        Constants.moveObject(card,x,middle_wall);
        card.append(Constants.cursorRight(x));
        for(int j=0;j<14;j++){
            if (j == 0 || j == 13) card.append(vertical);
            else if (((j%2!=0 && ((12-students.size()*2-1)/2)%2==0) || (j%2==0 && ((12-students.size()*2-1)/2)%2!=0))
                    && j>(12-students.size()*2-1)/2 && students_index<students.size() ) {
                card.append(students.get(students_index));
                students_index++;
            }
            else card.append(" ");
        }
        card.append("\n");
        Constants.moveObject(card,x,bottom_wall);
        return card;
    }

    @Override
    public String toString() {
        String result = super.getName() + ": " + super.getDescription() + "\nCost: " + super.getCost() + "\nStudents: ";
        for(Student student: students) {
            result= result.concat(student.toString() + " ");
        }
        return result;
    }

}
