import com.thinking.machines.nframework.server.*;
import com.thinking.machines.nframework.server.annotations.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
@Path("/serverChessUpdater")
public class ServerChessUpdater
{
private ServerChessFrame serverChessFrame;
private static boolean needUpdate=false;
public ServerChessUpdater()
{
//System.out.println("ServerChessUpdater object created "+this);
serverChessFrame=ServerChessFrame.getServerChessFrame();
}

@Path("/getChessGameState")
public Map<String,Object> getChessGameState(String uuid,String playerName)
{
return serverChessFrame.getChessGameState(uuid,playerName);
}

@Path("/initialize")
public String intializeClientToServer(String playerName)
{
return serverChessFrame.initializeClientInfo(playerName);
}
@Path("/getPlayerNumber")
public int getPlayerNumber()
{
return serverChessFrame.getPlayerCount();
}
@Path("/populatePieces")
public void populatePieces(java.util.List<String> piecesList)
{
serverChessFrame.setPiecesList(piecesList);
}
@Path("/getPiecesList")
public java.util.List<String> getPiecesList()
{
return serverChessFrame.getPiecesList();
}

@Path("/updateBoard")
public void updatePiece(int startRowIndex,int startColumnIndex,int destinationRowIndex,int destinationColumnIndex)
{
needUpdate=true;
SwingUtilities.invokeLater(()->{
serverChessFrame.movePiece((int)startRowIndex,(int)startColumnIndex,(int)destinationRowIndex,(int)destinationColumnIndex);
});
}
@Path("/setMap")
public void setMap(Map<String,Object> chessGameState)
{
serverChessFrame.setChessGameState(chessGameState);
}

@Path("/setDone")
public void setDone()
{
serverChessFrame.setDone(true);
}
@Path("/getDone")
public boolean getDone()
{
boolean b=serverChessFrame.getDone();
serverChessFrame.setDone(false);
return b;
}
@Path("/updateClientBoard")
public ArrayList<String> updateClientBoard()
{
if(needUpdate==true)
{
needUpdate=false;
ArrayList<String> piecesName;
piecesName=serverChessFrame.getPiecesName();
return piecesName;
}
else
{
return null;
}
}



}
