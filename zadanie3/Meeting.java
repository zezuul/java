//Julia Zezula listopad 2022
import java.util.*;
import java.lang.IndexOutOfBoundsException;

public class Meeting implements MeetingInterface {
    //list of pawns and their positions
    ArrayList<PawnPosition2D> pawns = new ArrayList<PawnPosition2D>();
    Position MeetingPoint;

    public void addPawns(List<PawnPosition> positions) {
        for(int i = 0; i < positions.size(); i++)
        {
            PawnPosition p = positions.get(i);
            int found = -1;
            for (int j =0 ; j < pawns.size(); j++)
            {
                if (pawns.get(j).pawnId() == p.pawnId())
                {
                    found = j;
                    break;
                }
            }
            if (found > -1)
            {
              pawns.set(found,new PawnPosition2D(p.pawnId(), p.x(), p.y())) ;

            }
            else {
                pawns.add(new PawnPosition2D(p.pawnId(), p.x(), p.y()));
            }
        }
    }


    public void addMeetingPoint(Position meetingPointPosition) {
        //setting meetingpoint
        MeetingPoint = meetingPointPosition;
    }
    //przesun pionek
    public void moveto(int pawnListID, Position movedto){
        PawnPosition2D p = pawns.get(pawnListID);
      //  System.out.println ("Moving: " + p.pawnId() + " from: " + p.x() +":" + p.y() + " to " + movedto.x() + ":" + movedto.y());
        pawns.set(pawnListID, new PawnPosition2D (p.pawnId(), movedto.x(), movedto.y()) ) ;
    }
    //sprawdz czy pionek moze zostac przesuniety
    public boolean checkifpossible(Position movedto){
        for(int i=0; i< pawns.size(); i++)
        {
            PawnPosition2D pos = pawns.get(i);
            if (pos.x() == movedto.x() && pos.y() == movedto.y())
                return false;
        }
        return true;
    }

    public int onmove(int pawnListID){
        PawnPosition2D pos = null;
        int cn = 0;
        //for(int i = 0; i < pawns.size(); i++){
          //  if(i == pawnListID){
        pos = pawns.get(pawnListID);
            //}
        //}
        int dx  = Math.abs(MeetingPoint.x() - pos.x());
        //dx = Math.abs(dx);
        int dy = Math.abs(MeetingPoint.y() - pos.y());
        //dy = Math.abs(dy);

        Position movedto = null;
        //dotarcie do punktu zbornego
        if(dx == 0 && dy == 0) { return 0; }
        //ruch w poziomie
        if (dx > dy && dx > 0) {
            if(pos.x() > MeetingPoint.x()) {
                //ruch w lewo
                movedto = new Position2D(pos.x() - 1, pos.y());
            }
            else{
                movedto = new Position2D(pos.x() + 1, pos.y());
            }
        }
        //ruch w pionie
        else if (dy > 0){
            if(pos.y() > MeetingPoint.y()) {
                movedto = new Position2D(pos.x(), pos.y() - 1);
            }
            else{
                //ruch w gore
                movedto = new Position2D(pos.x(), pos.y() + 1);
            }
        }
        else
        {
            throw new IndexOutOfBoundsException("Not expected combination");
        }
        if(movedto!= null && checkifpossible(movedto)){
            moveto(pawnListID, movedto);
            return 1;
        }
        return 0;
    }

    public void move() {
        int direction = 1;
        int moved = 0;
        //int loops = 100;
        int pawnListID = 0;
        boolean loopdone = false;
        int lastpos = 0;
        while(true) {

            for (int i = lastpos; i >= 0 && i < pawns.size() ; i += direction )
            {
                moved += onmove(i);
            }
            if (lastpos == 0) lastpos = pawns.size() - 1;
            else  {
                lastpos = 0;
            }
            if (moved == 0)
            {
                return;
            }
            direction = -direction;
            moved = 0;
        }

    }

    public Set<PawnPosition> getAllPawns() {
        HashSet<PawnPosition> endingboard = new HashSet<PawnPosition>();
        PawnPosition2D pos = null;
        for(int i=0; i< pawns.size(); i++){
            pos = pawns.get(i);
            endingboard.add(pos);
        }

        return endingboard;
    }

    public Set<PawnPosition> getNeighbours(int pawnId) {
        HashSet<PawnPosition> neighbours = new HashSet<PawnPosition>();
        PawnPosition2D pos = null;
        int i;
        for(i=0; i< pawns.size(); i++){
            pos = pawns.get(i);
            if (pos.pawnId() == pawnId) break;
        }
        if (pos.pawnId() != pawnId){ return neighbours;}

        for( i=0; i< pawns.size(); i++)
        {
            PawnPosition2D pos_neigh = pawns.get(i);
            if(Math.abs(pos.x()-pos_neigh.x()) <= 1 && Math.abs(pos.y()-pos_neigh.y()) <= 1 && pos_neigh.pawnId() != pos.pawnId()) {
                //System.out.println ("Addinbg: " + pos_neigh.pawnId());
                neighbours.add(pos_neigh);
            }

        }
      //  System.out.println ("end ");
        return neighbours;
    }


}
