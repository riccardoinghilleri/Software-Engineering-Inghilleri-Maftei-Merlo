package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board;
    GameModel gameModel;
    GameModel gameModel3;
    Board board3;

    @BeforeEach
    void setUp() {
        gameModel= new GameModel(false);
        gameModel.createPlayer("manu",1);
        gameModel.createPlayer("ricky",2);
        gameModel.getPlayerByNickname("manu").setColor("WHITE");
        gameModel.getPlayerByNickname("ricky").setColor("BLACK");
        board=new Board(gameModel.getPlayers(),gameModel);

        gameModel3= new GameModel(false);
        gameModel3.createPlayer("manu",1);
        gameModel3.createPlayer("ricky",2);
        gameModel3.createPlayer("dani",3);
        gameModel3.getPlayerByNickname("manu").setColor("WHITE");
        gameModel3.getPlayerByNickname("ricky").setColor("BLACK");
        gameModel3.getPlayerByNickname("dani").setColor("GREY");
        board3 = new Board(gameModel3.getPlayers(),gameModel);
    }

    @Test
    public void Board(){

        assertEquals(2,board.getClouds().length);
        assertEquals(2,board.getSchools().length);
        assertEquals(3,board3.getClouds().length);
        assertEquals(3,board3.getSchools().length);
        assertEquals(board.getSchools()[0],board.getSchoolByOwner("manu"));
        assertEquals(7,board.getSchoolByOwner("manu").getEntrance().size());
        assertEquals(7,board.getSchoolByOwner("ricky").getEntrance().size());
        assertEquals(board.getSchools()[1],board.getSchoolByOwner("ricky"));
        assertEquals(12,board.getIslands().size());
        for(int i=0;i<12;i++) {
            int numstudents = 0;
            for (CharacterColor c : CharacterColor.values()) {
                numstudents += board.getIslands().get(i).getStudents().get(c).size();
            }
            if(i==0 || i==6)
            assertEquals(0,numstudents);
            else assertEquals(1,numstudents);
        }
        for(CharacterColor c:CharacterColor.values()){
            assertNotNull(board.getProfessorByColor(c));
            assertSame(c,board.getProfessorByColor(c).getColor());
        }

    }

    @Test
    public void testgetMotherNaturePosition(){
        assertTrue(board.getIslands().get(0).hasMotherNature());
        assertEquals(0,board.getMotherNaturePosition());
        board.moveMotherNature(3);
        assertTrue(board.getIslands().get(3).hasMotherNature());
        assertFalse(board.getIslands().get(0).hasMotherNature());
        assertEquals(3,board.getMotherNaturePosition());
        board.moveMotherNature(10);
        assertTrue(board.getIslands().get(1).hasMotherNature());
        assertFalse(board.getIslands().get(3).hasMotherNature());
        assertEquals(1,board.getMotherNaturePosition());
    }

    @Test
    public void testInitialEntrance(){
        assertEquals(9,board3.getSchoolByOwner("manu").getEntrance().size());
        assertEquals(9,board3.getSchoolByOwner("ricky").getEntrance().size());
        assertEquals(9,board3.getSchoolByOwner("dani").getEntrance().size());
    }

    @Test
    public void testsetStudentsonClouds(){
        board.setStudentsonClouds();
        assertEquals(3,board.getClouds()[0].getStudents().size());
        assertEquals(3,board.getClouds()[1].getStudents().size());
        board3.setStudentsonClouds();
        assertEquals(4,board3.getClouds()[0].getStudents().size());
        assertEquals(4,board3.getClouds()[1].getStudents().size());
        assertEquals(4,board3.getClouds()[2].getStudents().size());
    }

    @Test
    public void testMoveStudentFromCloud(){
        board.setStudentsonClouds();
        board.moveStudent(0,"manu");
        assertTrue(board.getClouds()[0].getStudents().isEmpty());
        assertEquals(10,board.getSchoolByOwner("manu").getEntrance().size());
        board3.setStudentsonClouds();
        board3.moveStudent(0,"manu");
        assertTrue(board3.getClouds()[0].getStudents().isEmpty());
        assertEquals(13,board3.getSchoolByOwner("manu").getEntrance().size());
    }

    @Test
    public void testMoveStudentFromSchool(){
        CharacterColor color=null;
        assertEquals(7,board.getSchoolByOwner("manu").getEntrance().size());
        for(CharacterColor c: CharacterColor.values())
            assertTrue(board.getIslands().get(6).getStudents().get(c).isEmpty());

        for(CharacterColor c: CharacterColor.values())
        {
            if(board.getSchoolByOwner("manu").hasEntranceStudentColor(c.toString())){
            color=c;
            break;
            }
        }
        board.moveStudent("manu",6,color.toString());
        assertEquals(6,board.getSchoolByOwner("manu").getEntrance().size());
        assertEquals(1,board.getIslands().get(6).getStudents().get(color).size());
        assertEquals(color,board.getIslands().get(6).getStudents().get(color).get(0).getColor());
    }

    @Test
    public void testUpdateProfessor(){
        for(CharacterColor c: CharacterColor.values())
            assertEquals("NONE",board.getProfessorByColor(c).getOwner());
        Student student=new Student(CharacterColor.valueOf("GREEN"));
        Student student1=new Student(CharacterColor.valueOf("PINK"));
        Student student2=new Student(CharacterColor.valueOf("YELLOW"));
        Student student3=new Student(CharacterColor.valueOf("YELLOW"));
        Student student4=new Student(CharacterColor.valueOf("RED"));

        board.getSchoolByOwner("manu").addDiningRoomStudent(student);
        board.getSchoolByOwner("manu").addDiningRoomStudent(student1);
        board.getSchoolByOwner("manu").addDiningRoomStudent(student2);
        board.updateProfessor(student.getColor());
        board.updateProfessor(student1.getColor());
        board.updateProfessor(student2.getColor());
        board.getSchoolByOwner("ricky").addDiningRoomStudent(student3);
        board.getSchoolByOwner("ricky").addDiningRoomStudent(student4);
        board.updateProfessor(student3.getColor());
        board.updateProfessor(student4.getColor());

        assertEquals("manu",board.getProfessorByColor(student.getColor()).getOwner());
        assertEquals("manu",board.getProfessorByColor(student1.getColor()).getOwner());
        assertEquals("manu",board.getProfessorByColor(student2.getColor()).getOwner());
        assertEquals("manu",board.getProfessorByColor(student3.getColor()).getOwner());
        assertEquals("ricky",board.getProfessorByColor(student4.getColor()).getOwner());

    }

    @Test
    public void testGetTotalInfluence(){
        Student student1=new Student(CharacterColor.valueOf("GREEN"));
        Student student2=new Student(CharacterColor.valueOf("PINK"));
        Student student3=new Student(CharacterColor.valueOf("YELLOW"));
        Student student4=new Student(CharacterColor.valueOf("GREEN"));
        Student student5=new Student(CharacterColor.valueOf("YELLOW"));
        board.getIslands().get(6).addStudent(student1);
        board.getIslands().get(6).addStudent(student2);
        board.getIslands().get(6).addStudent(student3);
        board.getSchoolByOwner("manu").addDiningRoomStudent(student4);
        board.updateProfessor(student4.getColor());
        assertEquals("manu",board.getProfessorByColor(CharacterColor.valueOf("GREEN")).getOwner());
        assertEquals("manu",board.getTotalInfluence(6));
        board.getSchoolByOwner("ricky").addDiningRoomStudent(student5);
        board.updateProfessor(CharacterColor.valueOf("YELLOW"));
        assertEquals("ricky",board.getProfessorByColor(student5.getColor()).getOwner());
        assertEquals("NONE",board.getTotalInfluence(6));

        board.moveTower("ricky",6);
        assertEquals("ricky",board.getTotalInfluence(6));
    }

    @Test
    public void testMoveTowerFromIsland(){
        board.moveTower("ricky",6);
        assertEquals(7,board.getSchoolByOwner("ricky").getTowersNumber());
        assertEquals(1,board.getIslands().get(6).getTowers().size());
        board.moveTower(6,"ricky");
        assertEquals(8,board.getSchoolByOwner("ricky").getTowersNumber());
        assertEquals(0,board.getIslands().get(6).getTowers().size());
    }

    @Test
    public void testCheckNearIsland(){
        int num_students=0;
        assertEquals(12,board.getIslands().size());
        board.moveTower("manu",0);
        board.moveTower("manu",1);
        board.moveTower("manu",2);
        assertEquals(5,board.getSchoolByOwner("manu").getTowersNumber());
        board.checkNearIsland(1);
        assertEquals(10,board.getIslands().size());
        assertEquals(3,board.getIslands().get(0).getTowers().size());
        for(CharacterColor c: CharacterColor.values()){
            num_students+=board.getIslands().get(0).getStudents().get(c).size();
        }
        assertEquals(2,num_students);
        num_students=0;
        board.moveTower("manu",1);
        board.checkNearIsland(0);
        assertEquals(9,board.getIslands().size());
        assertEquals(4,board.getIslands().get(0).getTowers().size());
        for(CharacterColor c: CharacterColor.values()){
            num_students+=board.getIslands().get(0).getStudents().get(c).size();
        }
        assertEquals(3,num_students);
    }

    @Test
    public void testToString(){
        int i=0;
        board.getSchoolByOwner("manu").addEntranceStudent(new Student(CharacterColor.valueOf("RED")));
        board.getSchoolByOwner("manu").addEntranceStudent(new Student(CharacterColor.valueOf("YELLOW")));
        board.getSchoolByOwner("manu").addEntranceStudent(new Student(CharacterColor.valueOf("GREEN")));
        board.getSchoolByOwner("ricky").addEntranceStudent(new Student(CharacterColor.valueOf("BLUE")));
        board.getSchoolByOwner("ricky").addEntranceStudent(new Student(CharacterColor.valueOf("PINK")));
        board.getSchoolByOwner("ricky").addEntranceStudent(new Student(CharacterColor.valueOf("PINK")));
        board.getSchoolByOwner("manu").addDiningRoomStudent(new Student(CharacterColor.valueOf("BLUE")));
        board.getSchoolByOwner("manu").addDiningRoomStudent(new Student(CharacterColor.valueOf("GREEN")));
        board.getSchoolByOwner("manu").addDiningRoomStudent(new Student(CharacterColor.valueOf("GREEN")));
        board.getSchoolByOwner("ricky").addDiningRoomStudent(new Student(CharacterColor.valueOf("PINK")));
        board.getSchoolByOwner("ricky").addDiningRoomStudent(new Student(CharacterColor.valueOf("YELLOW")));
        board.getSchoolByOwner("ricky").addDiningRoomStudent(new Student(CharacterColor.valueOf("GREEN")));
        board.setStudentsonClouds();
        String result = "Clouds:\n";
        for(Cloud c: board.getClouds()) {
            result = result.concat("Cloud # " + i + ": ");
            result = result.concat(c.toString() + "\n");
            i++;
        }
        result = result.concat("Schools:\n");
        for(School s : board.getSchools()){
            result=result.concat(s.toString()+ "\n");
        }
        result = result.concat("Professors:\n");
        for(CharacterColor c: CharacterColor.values()){
            board.updateProfessor(c);
            result=result.concat(c.toString() + " Professor: " + board.getProfessorByColor(c).getOwner() + "\n");
        }
        assertEquals(result,board.toString());
    }
}