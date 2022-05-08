package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterColor;
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
        gameModel.createPlayer("manu",0);
        gameModel.createPlayer("ricky",1);
        gameModel.getPlayerById(0).setColor("WHITE");
        gameModel.getPlayerById(1).setColor("BLACK");
        board=new Board(gameModel.getPlayers(),gameModel);

        gameModel3= new GameModel(false);
        gameModel3.createPlayer("manu",0);
        gameModel3.createPlayer("ricky",1);
        gameModel3.createPlayer("dani",2);
        gameModel3.getPlayerById(0).setColor("WHITE");
        gameModel3.getPlayerById(1).setColor("BLACK");
        gameModel3.getPlayerById(2).setColor("GREY");
        board3 = new Board(gameModel3.getPlayers(),gameModel);
    }

    @Test
    public void Board(){

        assertEquals(2,board.getClouds().length);
        assertEquals(2,board.getSchools().length);
        assertEquals(3,board3.getClouds().length);
        assertEquals(3,board3.getSchools().length);
        assertEquals(board.getSchools()[0],board.getSchoolByOwnerId(0));
        assertEquals(7,board.getSchoolByOwnerId(0).getEntrance().size());
        assertEquals(7,board.getSchoolByOwnerId(1).getEntrance().size());
        assertEquals(board.getSchools()[1],board.getSchoolByOwnerId(1));
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
        assertEquals(9,board3.getSchoolByOwnerId(0).getEntrance().size());
        assertEquals(9,board3.getSchoolByOwnerId(1).getEntrance().size());
        assertEquals(9,board3.getSchoolByOwnerId(2).getEntrance().size());
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
        board.moveStudent(0,0);
        assertTrue(board.getClouds()[0].getStudents().isEmpty());
        assertEquals(10,board.getSchoolByOwnerId(0).getEntrance().size());
        board3.setStudentsonClouds();
        board3.moveStudent(0,0);
        assertTrue(board3.getClouds()[0].getStudents().isEmpty());
        assertEquals(13,board3.getSchoolByOwnerId(0).getEntrance().size());
    }

    @Test
    public void testMoveStudentFromSchool(){
        CharacterColor color=null;
        assertEquals(7,board.getSchoolByOwnerId(0).getEntrance().size());
        for(CharacterColor c: CharacterColor.values())
            assertTrue(board.getIslands().get(6).getStudents().get(c).isEmpty());

        for(CharacterColor c: CharacterColor.values())
        {
            if(board.getSchoolByOwnerId(0).hasEntranceStudentColor(c.toString())){
            color=c;
            break;
            }
        }
        board.moveStudent(0,6,color.toString());
        assertEquals(6,board.getSchoolByOwnerId(0).getEntrance().size());
        assertEquals(1,board.getIslands().get(6).getStudents().get(color).size());
        assertEquals(color,board.getIslands().get(6).getStudents().get(color).get(0).getColor());
    }

    @Test
    public void testUpdateProfessor(){
        for(CharacterColor c: CharacterColor.values())
            assertEquals(-1,board.getProfessorByColor(c).getOwner());
        Student student=new Student(CharacterColor.valueOf("GREEN"));
        Student student0=new Student(CharacterColor.valueOf("PINK"));
        Student student1=new Student(CharacterColor.valueOf("YELLOW"));
        Student student2=new Student(CharacterColor.valueOf("YELLOW"));
        Student student4=new Student(CharacterColor.valueOf("RED"));

        board.getSchoolByOwnerId(0).addDiningRoomStudent(student);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(student0);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(student1);
        board.updateProfessor(student.getColor());
        board.updateProfessor(student0.getColor());
        board.updateProfessor(student1.getColor());
        board.getSchoolByOwnerId(1).addDiningRoomStudent(student2);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(student4);
        board.updateProfessor(student2.getColor());
        board.updateProfessor(student4.getColor());

        assertEquals(0,board.getProfessorByColor(student.getColor()).getOwner());
        assertEquals(0,board.getProfessorByColor(student0.getColor()).getOwner());
        assertEquals(0,board.getProfessorByColor(student1.getColor()).getOwner());
        assertEquals(0,board.getProfessorByColor(student2.getColor()).getOwner());
        assertEquals(1,board.getProfessorByColor(student4.getColor()).getOwner());

    }

    @Test
    public void testGetTotalInfluence(){
        Student student0=new Student(CharacterColor.valueOf("GREEN"));
        Student student1=new Student(CharacterColor.valueOf("PINK"));
        Student student2=new Student(CharacterColor.valueOf("YELLOW"));
        Student student4=new Student(CharacterColor.valueOf("GREEN"));
        Student student5=new Student(CharacterColor.valueOf("YELLOW"));
        board.getIslands().get(6).addStudent(student0);
        board.getIslands().get(6).addStudent(student1);
        board.getIslands().get(6).addStudent(student2);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(student4);
        board.updateProfessor(student4.getColor());
        assertEquals(0,board.getProfessorByColor(CharacterColor.valueOf("GREEN")).getOwner());
        assertEquals(0,board.getTotalInfluence(6));
        board.getSchoolByOwnerId(1).addDiningRoomStudent(student5);
        board.updateProfessor(CharacterColor.valueOf("YELLOW"));
        assertEquals(1,board.getProfessorByColor(student5.getColor()).getOwner());
        assertEquals(-1,board.getTotalInfluence(6));

        board.moveTower(1,6,"island");
        assertEquals(1,board.getTotalInfluence(6));
    }

    @Test
    public void testMoveTowerFromIsland(){
        board.moveTower(1,6,"island");
        assertEquals(7,board.getSchoolByOwnerId(1).getTowersNumber());
        assertEquals(1,board.getIslands().get(6).getTowers().size());
        board.moveTower(1,6,"school");
        assertEquals(8,board.getSchoolByOwnerId(1).getTowersNumber());
        assertEquals(0,board.getIslands().get(6).getTowers().size());
    }

    @Test
    public void testCheckNearIsland(){
        int num_students=0;
        assertEquals(12,board.getIslands().size());
        board.moveTower(0,0,"island");
        board.moveTower(0,1,"island");
        board.moveTower(0,2,"island");
        assertEquals(5,board.getSchoolByOwnerId(0).getTowersNumber());
        board.checkNearIsland(1);
        assertEquals(10,board.getIslands().size());
        assertEquals(3,board.getIslands().get(0).getTowers().size());
        for(CharacterColor c: CharacterColor.values()){
            num_students+=board.getIslands().get(0).getStudents().get(c).size();
        }
        assertEquals(2,num_students);
        num_students=0;
        board.moveTower(0,1,"island");
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
        board.getSchoolByOwnerId(0).addEntranceStudent(new Student(CharacterColor.valueOf("RED")));
        board.getSchoolByOwnerId(0).addEntranceStudent(new Student(CharacterColor.valueOf("YELLOW")));
        board.getSchoolByOwnerId(0).addEntranceStudent(new Student(CharacterColor.valueOf("GREEN")));
        board.getSchoolByOwnerId(0).addEntranceStudent(new Student(CharacterColor.valueOf("BLUE")));
        board.getSchoolByOwnerId(0).addEntranceStudent(new Student(CharacterColor.valueOf("PINK")));
        board.getSchoolByOwnerId(0).addEntranceStudent(new Student(CharacterColor.valueOf("PINK")));
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.valueOf("BLUE")));
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.valueOf("GREEN")));
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.valueOf("GREEN")));
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.valueOf("PINK")));
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.valueOf("YELLOW")));
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.valueOf("GREEN")));
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