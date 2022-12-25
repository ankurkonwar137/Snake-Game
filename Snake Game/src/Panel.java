import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;
//inheriting panel class from jpanel class and adding action listener
public class Panel extends JPanel implements ActionListener {
    //dimension of the panel
    static int width=1200;
    static int height=600;
    //per grid/block for moving  size
    static int unit=50;
    //for checking the state of the game at regular intervals
    Timer timer;
    static int delay=160;//160 ms,game is running or not check after 160 ms,snake is running good speed

    //for food spawns
    Random random;
    int fx, fy;//fx 0-1200  fy 0-600

    int body=3;//initially snake has one head and two body part
    char dir='R';//initially going to right
    int score=0;//for score init with 0
    boolean flag=false;//initially game is not running
    //total no of units
    static int size=(width*height)/(unit*unit);
    //the x and y blocks of the snake in these arrays
    int[] xsnake=new int[size];
    int[] ysnake=new int[size];
    Panel(){
        //for preferred panel size width*height
        this.setPreferredSize(new Dimension(width,height));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);//when key is pressed then it focused to game,not to any other components,keyboard input is not work without focusable
        random=new Random();//for random food
        this.addKeyListener(new Key());//listen the key input,up down
        game_start();
    }

    public void game_start(){
        //for spawning the food
        spawnFood();
        //setting the game running flag to true,true means running false means stop
        flag=true;
        //starting the timer with delay
        timer=new Timer(delay,this);
        timer.start();
    }
    public void spawnFood(){
        //setting random coordinates for the food in 50 multiple,
        fx=random.nextInt((int)(width/unit))*unit;//for vertical placed in food in exact place between block
        fy=random.nextInt((int)(height/unit))*unit;
    }

    //checking the snake head collision with its own body or walls
    public void checkHit(){
        //checking with body parts
        for(int i=body;i>0;i--){
            //[0] is head part and [i] is body part
            if ((xsnake[0] == xsnake[i]) && (ysnake[0] == ysnake[i])) {//vertically and horizonatally collides

                flag=false;//collided
            }
        }
        //collision in wall
        if(xsnake[0]<0)//left wall collid
            flag=false;
        if(xsnake[0]>width)//right wall collid
            flag=false;
        if(ysnake[0]<0)//top wall
            flag=false;
        if(ysnake[0]>height)//bottom wall
            flag=false;
        if(flag==false) timer.stop();//if collid then stop the game
    }
    //intermediate function to call the draw function
    public void paintComponent(Graphics graphic){
        super.paintComponent(graphic);
        draw(graphic);
    }
    public void draw(Graphics graphic){
        if(flag){
            //settings parameters for the food block
            graphic.setColor(Color.red);
            graphic.fillOval(fx,fy,unit,unit);//food is spawning,in round shape
            //setting params for the snake
            for(int i=0;i<body;i++){
                //for the head
                if(i==0){
                    graphic.setColor(Color.green);
                    graphic.fillRect(xsnake[i],ysnake[i],unit,unit);
                }
                //for other parts
                else{
                    graphic.setColor(Color.pink);
                    graphic.fillRect(xsnake[i],ysnake[i],unit,unit);
                }
            }
            //drawing the score
            graphic.setColor(Color.blue);
            graphic.setFont(new Font("Comic Sans",Font.BOLD,40));
            FontMetrics f=getFontMetrics(graphic.getFont());
            //drawstring takes the string to draw,starting position in x and the starting position in y
            graphic.drawString("SCORE"+score,(width-f.stringWidth("SCORE"+score))/2,graphic.getFont().getSize());

        }
        else{
            gameOver(graphic);
        }
    }

    //game over function
    public void gameOver(Graphics graphic){
        //drawing the score
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,40));
        FontMetrics f=getFontMetrics(graphic.getFont());
        //drawstring takes the string to draw,starting position in x and the starting position in y
        graphic.drawString("SCORE"+score,(width-f.stringWidth("SCORE"+score))/2,graphic.getFont().getSize());

        //graphic for gameover text
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,80));
        FontMetrics f2=getFontMetrics(graphic.getFont());
        //drawstring takes the string to draw,starting position in x and the starting position in y
        graphic.drawString("Game Over!",(width-f2.stringWidth("Game Over!"))/2,height/2);

        //graphics for replay prompt
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,40));
        FontMetrics f3=getFontMetrics(graphic.getFont());
        //drawstring takes the string to draw,starting position in x and the starting position in y
        graphic.drawString("Press R to replay",(width-f3.stringWidth("Press R to replay"))/2,height/2-180);

    }

    public void move(){
        //loop for updating the body parts except the head
        for(int i=body;i>0;i--){
            xsnake[i]=xsnake[i-1];
            ysnake[i]=ysnake[i-1];
        }
        //for the update of head coordinates
        switch (dir){
            case 'U':
                ysnake[0]=ysnake[0]-unit;
                break;
            case 'D':
                ysnake[0]=ysnake[0]+unit;
                break;
            case 'L':
                xsnake[0]=xsnake[0]-unit;
                break;
            case 'R':
                xsnake[0]=xsnake[0]+unit;
                break;
        }
    }

    public void checkScore(){
        //checking if the head and the food block coincide
        if((fx==xsnake[0]) && fy==ysnake[0]){
            body++;
            score++;
            spawnFood();
        }

    }
    public class Key extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(dir!='R'){//if not opposite of left then goes to left,otherwise it collides,same below
                        dir='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(dir!='L'){
                        dir='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(dir!='D'){
                        dir='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(dir!='U'){
                        dir='D';
                    }
                    break;
                case KeyEvent.VK_R:
                    if(!flag){//if game is not running then replay otherwise game will start from again
                        score=0;
                        body=3;
                        dir='R';
                        Arrays.fill(xsnake,0);
                        Arrays.fill(ysnake,0);
                        game_start();
                    }
                    break;
            }
        }

    }

    @Override
    //for reset part
    public void actionPerformed(ActionEvent arg0){
        if(flag){//if game is running
            move();
            checkScore();
            checkHit();
        }
        repaint();//if not running,same graphics

    }
}
