import com.thinking.machines.nframework.client.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class ChessStartWindow extends JFrame
{
private Container container;
private JList list;
private Vector<String> v;
private JButton inviteButton;
private javax.swing.Timer availablePlayersTimer;
private NFrameworkClient client;
private String username,password;
public ChessStartWindow(String username,String password)
{
this.username=username;
this.password=password;
container=getContentPane();
container.setLayout(new BorderLayout());
JLabel welcomeLabel=new JLabel("WELCOME");
welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
//welcomeLabel.setVerticalAlignment(SwingConstants.CENTER);
welcomeLabel.setFont(new Font("Arial",0,40));	
container.add(welcomeLabel,BorderLayout.CENTER);
JPanel p1=new JPanel();
p1.setLayout(new FlowLayout());
p1.add(new JLabel("					"));
container.add(p1,BorderLayout.NORTH);
JPanel p2=new JPanel();
p2.setLayout(new BorderLayout());
v=new Vector<>();
list=new JList(v);
inviteButton=new JButton("Invite");
p2.add(new JLabel("Available players"),BorderLayout.NORTH);
p2.add(list,BorderLayout.CENTER);
p2.add(inviteButton,BorderLayout.SOUTH);
container.add(p2,BorderLayout.EAST);
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
int width=650;
int height=600;
setSize(width,height);
int x=(d.width/2)-(width/2);
int y=(d.height/2)-(height/2);
setLocation(x,y);
setVisible(true);
client=new NFrameworkClient();
availablePlayersTimer=new javax.swing.Timer(2000,new ActionListener()
{
public void actionPerformed(ActionEvent ev)
{
try
{
ArrayList<String> arr=(ArrayList<String>)(client.execute("/serverChessUpdater/getAvailablePlayers"));
arr.remove(username);
String array[]=arr.toArray(new String[0]);
list.setListData(array);
}catch(Throwable t)
{
System.out.println(t);
}
}
}
);
availablePlayersTimer.start();
javax.swing.Timer isInvitationAccepted=new javax.swing.Timer(1000,new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
try
{
java.util.List<String> acceptedList=(java.util.List<String>)client.execute("/serverChessUpdater/invitationReply",username);
if(acceptedList.size()!=0)
{
System.out.println("Invitation accepted");
System.out.println("Accepted by : "+acceptedList.get(0));
System.out.println("Starting game");
((javax.swing.Timer)ev.getSource()).stop();
client.execute("/serverChessUpdater/setBusy",username);
ChessStartWindow.this.setVisible(false);

Chess chess=new Chess(ChessStartWindow.this,username);

}
else
{
System.out.println("Invitation rejected");
}
}catch(Throwable t)
{
System.out.println("Accepted list : "+t);
}
}
});


javax.swing.Timer areThereAnyInvitation=new javax.swing.Timer(1000,ev->{
try
{
java.util.List<String> invitationList=(java.util.List<String>)client.execute("/serverChessUpdater/areThereAnyInvitation",username);
//System.out.println(invitationList.size());
if(invitationList.size()!=0)
{
System.out.println("Yes, invitation");
int selectedOption=JOptionPane.showConfirmDialog(ChessStartWindow.this,"Invitation from "+invitationList.get(0)+"\n Accept?","Invitation",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
if(selectedOption==JOptionPane.YES_OPTION)
{
System.out.println("Invitation accepted");
((javax.swing.Timer)ev.getSource()).stop();
//stop the timer and start the game
try
{
client.execute("/serverChessUpdater/invitationAccepted",username,invitationList.get(0));
//set busy this user
client.execute("/serverChessUpdater/setBusy",username);
}catch(Throwable t)
{
System.out.println("Sending accepted : "+t);
}
ChessStartWindow.this.setVisible(false);
System.out.println("Sleeping thread");



Thread.sleep(2000);
System.out.println("Creating chess frame");
Chess chess=new Chess(ChessStartWindow.this,username);
//isPlayerLeft.start();
}
else if(selectedOption==JOptionPane.NO_OPTION)
{
System.out.println("Invitation rejected");
//areThereAnyInvitation.stop();
}

}
}catch(Throwable t)
{
System.out.println("are there "+t);
}
});

areThereAnyInvitation.start();
inviteButton.addActionListener(ev->{
if(list.getSelectedIndex()>=0)
{
String selectedName=(String)list.getSelectedValue();
System.out.println(selectedName);
//from username to opponent
try
{
client.execute("/serverChessUpdater/sendInvitation",username,selectedName);
}catch(Throwable t)
{
System.out.println("are there "+t);
}
isInvitationAccepted.start();
}
});




addWindowListener(new WindowAdapter()
{
@Override
public void windowClosing(WindowEvent ev)
{
try
{
client.execute("/serverChessUpdater/logout",username);
}catch(Throwable t)
{
System.out.println(t);
}
System.out.println("Closing");
System.exit(0);
}
});


}
public void setAvailable()
{
try
{
client.execute("/serverChessUpdater/setAvailable",username);
}catch(Throwable t)
{
System.out.println(t);
}
}
}