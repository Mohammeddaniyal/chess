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
/*
@Path("/updateBoard")
public void updatePiece(int startRowIndex,int startColumnIndex,int destinationRowIndex,int destinationColumnIndex)
{
needUpdate=true;
SwingUtilities.invokeLater(()->{
serverChessFrame.movePiece((int)startRowIndex,(int)startColumnIndex,(int)destinationRowIndex,(int)destinationColumnIndex);
});
}
*/
@Path("/setMap")
public void setMap(Map<String,Object> chessGameState)
{
serverChessFrame.setChessGameState(chessGameState);
}
@Path("/getAvailablePlayers")
public ArrayList<String> getAvailablePlayers()
{
return serverChessFrame.getAvailablePlayers();
}
@Path("/login")
public boolean login(String username,String password)
{
return serverChessFrame.login(username,password);
}
@Path("/logout")
public void logout(String username)
{
serverChessFrame.logout(username);
}
@Path("/setBusy")
public void setBusy(String username)
{
serverChessFrame.setBusy(username);
}
@Path("/isPlayerLeft")
public boolean isPlayerLeft(String uuid)
{
return serverChessFrame.isPlayerLeft(uuid);
}
@Path("/quitGame")
public void quitGame(String uuid)
{
serverChessFrame.quitGame(uuid);
}
@Path("/setAvailable")
public void setAvailable(String username)
{
serverChessFrame.setAvailable(username);
}
@Path("/invitationReply")
public java.util.List<String> invitationReply(String player)
{
return serverChessFrame.invitationReply(player);
}
@Path("/invitationAccepted")
public void invitationAccepted(String from,String of)
{
serverChessFrame.invitationAccepted(from,of);
}
@Path("/sendInvitation")
public void sendInvitation(String from,String to)
{
serverChessFrame.sendInvitationTo(from,to);
}
@Path("/areThereAnyInvitation")
public java.util.List<String> areThereAnyInvitation(String forPlayer)
{
return serverChessFrame.areThereAnyInvitation(forPlayer);
}


/*

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
*/


}
