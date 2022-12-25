import javax.swing.JFrame;
public class Frame extends JFrame{
    Frame(){
        this.add(new Panel());
        this.setTitle("Snake Xenzia");//for title in top
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//does not run in background if not run the game
        this.setResizable(false);//for display same,not resize the frame for all,window is same for all,
        this.pack();//set to preferable resolution by user,
        this.setVisible(true);//for visible the frame,by default it is false
        this.setLocationRelativeTo(null);//always starting the snake in centre of the screen
    }
}
