import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MeetingTest {
    private Meeting meeting;

    @Before
    public void setUp() {
        meeting = new Meeting();
    }

    private void sortPawnPositionsById(List<PawnPosition> entries) {
        entries.sort((final PawnPosition p1, final PawnPosition p2) -> p1.pawnId() < p2.pawnId() ? -1 : 1);
    }

    private void assertPawnPositionEquals(final List<PawnPosition> expecteds, final List<PawnPosition> actuals) {
        this.sortPawnPositionsById(expecteds);
        this.sortPawnPositionsById(actuals);
        StringBuffer sb = new StringBuffer();
        for (int i=0; i < expecteds.size(); i++)
        {
            PawnPosition e = expecteds.get(i);
            PawnPosition a = actuals.get(i);
            sb.append(i+ "|" + e.pawnId()+";"+e.x()+";"+e.y()+"|" +a.pawnId()+";"+a.x()+";"+a.y()+"\n");
        }
        assertArrayEquals("Missmatch\n"+ sb.toString(), expecteds.toArray(), actuals.toArray());
    }

    public void quickTest(int x, int y, int ex, int ey)
    {
        Meeting m = new Meeting();
        ArrayList<PawnPosition> positions = new ArrayList<>(List.of(
                new PawnPosition2D(0, x, y)
        ));
        List<PawnPosition> expected = new ArrayList<>(List.of(
                new PawnPosition2D(0, ex, ey)
        ));

        m.addPawns(positions);
        m.addMeetingPoint(new Position2D(0, 0));
      //  System.out.println("QT");
        int res = m.onmove(0);
        List<PawnPosition> actual = new ArrayList<>(m.getAllPawns());
        assertPawnPositionEquals(expected, actual);
    }


    @Test
    public void julkaFirst()
    {
       quickTest(2,1,1,1);
        quickTest(1,2,1,1);
      quickTest(-2,1,-1,1);
        quickTest(-1,2,-1,1);
        quickTest(2,-1,1,-1);
        quickTest(1,-2,1,-1);
        quickTest(-2,-1,-1,-1);
       quickTest(-1,-2,-1,-1);

    }
   @Test
    public void initializesPawnsPositions() {
        ArrayList<PawnPosition> positions = new ArrayList<>(List.of(
                new PawnPosition2D(1, 0, 0),
                new PawnPosition2D(2, -1, 1),
                new PawnPosition2D(3, 0, 1),
                new PawnPosition2D(4, 1, 1),
                new PawnPosition2D(5, 1, 0),
                new PawnPosition2D(6, 1, -1),
                new PawnPosition2D(7, 0, -1),
                new PawnPosition2D(8, -1, -1),
                new PawnPosition2D(9, -1, 0),
                new PawnPosition2D(0, 2, 1)
        ));
        meeting.addPawns(positions);

        List<PawnPosition> actual = new ArrayList<>(meeting.getAllPawns());

        assertEquals(10, actual.size());
        assertPawnPositionEquals(positions, actual);
    }

    @Test
    public void findsNeighbours() {
        // . . . . . . . .
        // . . 2 3 4 . . .
        // . . 9 1 5 . . .
        // . . 8 7 6 0 . .
        // . . . . . . . .
        List<PawnPosition> positions = new ArrayList<>(List.of(
                new PawnPosition2D(1, 0, 0),
                new PawnPosition2D(2, -1, 1),
                new PawnPosition2D(3, 0, 1),
                new PawnPosition2D(4, 1, 1),
                new PawnPosition2D(5, 1, 0),
                new PawnPosition2D(6, 1, -1),
                new PawnPosition2D(7, 0, -1),
                new PawnPosition2D(8, -1, -1),
                new PawnPosition2D(9, -1, 0),
                new PawnPosition2D(0, 2, 1)
        ));
        List<PawnPosition> expected = new ArrayList<>(List.of(
                new PawnPosition2D(2, -1, 1),
                new PawnPosition2D(3, 0, 1),
                new PawnPosition2D(4, 1, 1),
                new PawnPosition2D(5, 1, 0),
                new PawnPosition2D(6, 1, -1),
                new PawnPosition2D(7, 0, -1),
                new PawnPosition2D(8, -1, -1),
                new PawnPosition2D(9, -1, 0)
        ));
        meeting.addPawns(positions);

        Set<PawnPosition> res = meeting.getNeighbours(1);
        List<PawnPosition> actual = new ArrayList<>(res);

        assertEquals(8, res.size());
        assertPawnPositionEquals(expected, actual);
    }

    @Test
    public void movesInMeetingPointDirection() {
        // P(0, 0)
        // 1(-2, 1)
        // 2(0, 1)
        // 3(1, 1)
        // 4(-1, -2)
        // 5(-2, -1)
        // pawns before the move:
        // 1 . 2 . . . . .
        // . . P . . . . .
        // 5 . . 3 . . . .
        // . 4 . . . . . .
        // . . . . . . . .
        // pawns after the first move [original order of moves]:
        // P(0, 0)
        // 1(-1, 1) [x changed]
        // 2(0, 0) [y changed]
        // 3(1, 0) [y changed]
        // 4(-1, -1) [y changed]
        // 5(-2, -1)
        // . 1 . . . . . .
        // . . 2 3 . . . .
        // 5 4 . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // pawns after the second move [reversed order of moves]:
        // P(0, 0)
        // 5(-2, -1)
        // 4(-1, 0) [y changed]
        // 3(1, 0)
        // 2(0, 0)
        // 1(-1, 1)
        // . 1 . . . . . .
        // . 4 2 3 . . . .
        // 5 . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // pawns after the third move [original order of moves]:
        // P(0, 0)
        // 1(-1, 1)
        // 2(0, 0)
        // 3(1, 0)
        // 4(-1, 0)
        // 5(-1, -1) [x changed]
        // . 1 . . . . . .
        // . 4 2 3 . . . .
        // . 5 . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // pawns after the fourth move [reversed order of moves]:
        // P(0, 0)
        // 5(-1, -1)
        // 4(-1, 0)
        // 3(1, 0)
        // 2(0, 0)
        // 1(-1, 1)
        // . 1 . . . . . .
        // . 4 2 3 . . . .
        // . 5 . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // There were no moves in this round. Pawns locked.
        Position meetingPoint = new Position2D(0, 0);

        List<PawnPosition> positions = new ArrayList<>(List.of(
                new PawnPosition2D(1, -2, 1),
                new PawnPosition2D(2, 0, 1),
                new PawnPosition2D(3, 1, 1),
                new PawnPosition2D(4, -1, -2),
                new PawnPosition2D(5, -2, -1)
        ));

        List<PawnPosition> expected = new ArrayList<>(List.of(
                new PawnPosition2D(1, -1, 1),
                new PawnPosition2D(2, 0, 0),
                new PawnPosition2D(3, 1, 0),
                new PawnPosition2D(4, -1, 0),
                new PawnPosition2D(5, -1, -1)
        ));

        meeting.addPawns(positions);
        meeting.addMeetingPoint(meetingPoint);
        meeting.move();

        List<PawnPosition> actual = new ArrayList<>(meeting.getAllPawns());

        assertPawnPositionEquals(expected, actual);
    }

    @Test
    public void movingMultipleTimesDoesNotChangePawnsPosition() {
        Position meetingPoint = new Position2D(0, 0);

        List<PawnPosition> positions = new ArrayList<>(List.of(
                new PawnPosition2D(1, -2, 1),
                new PawnPosition2D(2, 0, 1),
                new PawnPosition2D(3, 1, 1),
                new PawnPosition2D(4, -1, -2),
                new PawnPosition2D(5, -2, -1)
        ));

        List<PawnPosition> expected = new ArrayList<>(List.of(
                new PawnPosition2D(1, -1, 1),
                new PawnPosition2D(2, 0, 0),
                new PawnPosition2D(3, 1, 0),
                new PawnPosition2D(4, -1, 0),
                new PawnPosition2D(5, -1, -1)
        ));

        meeting.addPawns(positions);
        meeting.addMeetingPoint(meetingPoint);

        meeting.move();
       meeting.move();
        meeting.move();
        meeting.move();
        meeting.move();
        meeting.move();
        meeting.move();

        List<PawnPosition> actual = new ArrayList<>(meeting.getAllPawns());

        assertPawnPositionEquals(expected, actual);
    }

    @Test
    public void moveUpdatesNewlyAddedPoints() {
        Position meetingPoint = new Position2D(0, 0);
        meeting.addMeetingPoint(meetingPoint);

        meeting.addPawns(new ArrayList<>(List.of(
                new PawnPosition2D(1, -2, 1)
        )));
        meeting.move();
        assertPawnPositionEquals(
                new ArrayList<>(List.of(
                        new PawnPosition2D(1, 0, 0)
                )),
                new ArrayList<>(meeting.getAllPawns())
        );

        meeting.addPawns(List.of(
                new PawnPosition2D(2, 1, 1)
        ));
        meeting.move();
        assertPawnPositionEquals(
                new ArrayList<>(List.of(
                        new PawnPosition2D(1, 0, 0),
                        new PawnPosition2D(2, 1, 0)
                )),
                new ArrayList<>(meeting.getAllPawns())
        );

        meeting.addPawns(new ArrayList<>(List.of(
                new PawnPosition2D(3, -2, -1),
                new PawnPosition2D(4, -1, 1)
        )));
        meeting.move();
        assertPawnPositionEquals(
                new ArrayList<>(List.of(
                        new PawnPosition2D(1, 0, 0),
                        new PawnPosition2D(2, 1, 0),
                        new PawnPosition2D(3, -1, -1),
                        new PawnPosition2D(4, -1, 0)
                )),
                new ArrayList<>(meeting.getAllPawns())
        );
    }

    @Test
    public void movesToUpdatedMeetingPoint() {
        // Before any move:
        // P(-1, 1)
        // 1(-2, 1)
        // 2(0, 1)
        // . . . . . . . .
        // . . 1 P 2 . . .
        // . . . . . . . .
        // After all legal moves:
        // P(-1, 1)
        // 1(-1, 1)
        // 2(0, 1)
        // . . . . . . . .
        // . . . 1 2 . . .
        // . . . . . . . .
        meeting.addMeetingPoint(new Position2D(-1, 1));
        meeting.addPawns(new ArrayList<>(List.of(
                new PawnPosition2D(1, -2, 1),
                new PawnPosition2D(2, 0, 1)
        )));
        meeting.move();
        assertPawnPositionEquals(
                new ArrayList<>(List.of(
                        new PawnPosition2D(1, -1, 1),
                        new PawnPosition2D(2, 0, 1)
                )),
                new ArrayList<>(meeting.getAllPawns())
        );

        // Meeting point changed.
        // Before any move:
        // P(0, 0)
        // 1(-1, 1)
        // 2(0, 1)
        // . . . . . . . .
        // . . . 1 2 . . .
        // . . . . P . . .
        // After all legal moves:
        // P(0, 0)
        // 1(-1, 0)
        // 2(0, 0)
        // . . . . . . . .
        // . . . . . . . .
        // . . . 1 2 . . .
        meeting.addMeetingPoint(new Position2D(0, 0));
      meeting.addPawns(new ArrayList<>(List.of(
                new PawnPosition2D(1, -2, 1),
                new PawnPosition2D(2, 0, 1)
        )));
        meeting.move();
        assertPawnPositionEquals(
                new ArrayList<>(List.of(
                        new PawnPosition2D(1, -1, 0),
                        new PawnPosition2D(2, 0, 0)
                )),
                new ArrayList<>(meeting.getAllPawns())
        );
    }

    @Test
    public void keepsMovesOrder() {
        meeting.addMeetingPoint(new Position2D(0, 0));
        meeting.addPawns(new ArrayList<>(List.of(
                new PawnPosition2D(2100, 2, 0),
                new PawnPosition2D(1037, -2, 0)
        )));
        // moves:
        // 2100: (2, 0)
        // 1037: (-2, 0)
        //
        // 2100: (1, 0)
        // 1037: (-1, 0)
        //
        // 1037: (0, 0)
        // 2100: (1, 0)
        meeting.move();
        assertPawnPositionEquals(
                new ArrayList<>(List.of(
                        new PawnPosition2D(2100, 1, 0),
                        new PawnPosition2D(1037, 0, 0)
                )),
                new ArrayList<>(meeting.getAllPawns())
        );
    }


}
