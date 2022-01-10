/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astar;
import java.util.*; 
import java.util.Random;

/**
 * @author Tyler Ho
 * @date 10/29/2020
 * 
 */
public class Astar {

    //creates an the platform for nodes
     public static Node[][] astar = new Node[15][15];
     //simulates the platform but easy to see
     public static int[][] n = new int[15][15];
     // the open list
     public static LinkedList<Node> openList = new LinkedList<>();
     //the closed list
     public static LinkedList<Node> closedList = new LinkedList<>();
     //diagonal cost
     public static int dia = 14;
     //norm cost
     public static int norm = 10;
     //start x and y coordinated
     //end x and y coordinates
     public static int startx, starty, endx, endy;

    /**
     * the start of the program
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Astar a = new Astar();
        //creates the tile based environment
        astar = a.create();
        //shows the status
        show(astar);
        //sets the xy coordintes for both end and start
        set();
        show(astar);
        boolean start = true;
        //while the user wants to input more coordinates/ the program is runnnig
        while(start == true){
          //goes to the A* algorithim
           solve();
           System.out.println("Do you want to do this again? (y/n)");
           Scanner n = new Scanner(System.in);
           String s = n.nextLine();
           if((s).equals("n")){
               start = false;
           }else if(s.equals("y")){
               reset();
               show(astar);
               set();
              
       
           }
        }
       
    }
    /**
    * create the board
    */
    public Node[][] create(){
        //makes a new board
       Node[][] arr = new Node[15][15];  
       for(int i=0; i<15; i++) {
           for(int j=0; j<15 ; j++) {   
               Random rand = new Random();
               int luckyNumber = rand.nextInt(10);
               //10% chance of the node will be null
               if(luckyNumber == 1){      
                   n[i][j] = 1;
                   arr[i][j] = null;            
               }else{

                    n[i][j] = 0;
                    arr[i][j] = new Node(i,j,0);       
               }
           } 
       }
       return arr;   
    }
    
    /*
    * shows the state of the board
    * @param Node[][] arr - the board
    */
    public static void show(Node[][] arr){
       for(int i=0; i<15; i++) {
           for(int j=0; j<15; j++) {
                System.out.print((n[i][j])+ "  ");   
           }
           System.out.println();
       }
    }
   
    /*
    * Sets the starting and ending nodes for the board
    */
    public static Node[][] set(){
        Scanner scan = new Scanner(System.in);
        boolean looksgood = false;
        while(looksgood == false){
            try{
                System.out.print("\nSet your starting node");
                System.out.print("\nSet your starting X: ");
                startx = scan.nextInt();
                System.out.print("\nSet your starting Y: ");
                starty = scan.nextInt();
                if((astar[startx][starty]) != null ){
                    for(int i = 0; i < 15; i++){
                        for(int j = 0; j < 15; j++){
                            if(startx == i && starty == j){
                                n[i][j] =  3 ;
                            }
                        }
                    }   
                    looksgood = true;
                }else{
                    System.out.print("Try Again");
                }
                }catch(ArrayIndexOutOfBoundsException e){
                System.out.print("Try Again");
            } 
          
        }
        looksgood = false;
        while(looksgood == false){
            try{
                System.out.print("\nSet your ending node");
                System.out.print("\nSet your end X: ");
                endx = scan.nextInt();
                System.out.print("\nSet your end Y: ");
                endy = scan.nextInt();
                if((astar[endx][endy]).getType() == 1){
                    System.out.print("Try Again");    
                }else{
                    looksgood = true;
                    for(int i = 0; i < 15; i++){
                        for(int j = 0; j < 15; j++){
                            if(endx == i && endy == j){
                                n[i][j] =  8 ;
                            }
                        }
                    }
                }
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.print("Try again");
            }catch(NullPointerException e){
                System.out.print("Try again");
            }
        }
        return astar;
    }
    
    /*
    * performs the A* glorithim
    */
    public static void solve(){
        //creates a new open list 
        openList = new LinkedList<>();
        //creates a new closed list 
        closedList = new LinkedList<>();
        boolean cont = true;
        Node n;
        //shows the starting node
        Node start = astar[startx][starty];
        //shows the ending node
        Node goal = astar[endx][endy];
        //creates the H for the node
        h(start, goal);
        //adds start to the open list
        openList.add(start);
        boolean solved = false;
        
        while(cont == true){
            //if the openList is empty, end it with an error message
            if(openList.isEmpty()){
                System.out.println("Looks like you reached dead end");
                break;
            }else{
               //takes pops to the current node
                Node current = openList.pop();
                //if the current nodes equals the goal node, end it
                if(current.equals(astar[endx][endy])){
                    System.out.println("Solved");
                    path();   
                    cont = false;
                }       

                
            
            //adds the current node to the closed list
            closedList.add(current);
            //if the row that the current node is in does not go out of bounds
            if(current.getRow()-1 >= 0){
                n = astar[current.getRow()-1][current.getRow()];
                neighbor(current, n, goal, current.getF()+norm); 
                //check if the row and col is bigger then 0
                if(current.getRow()-1 >=0 && current.getCol()-1 >= 0){                      
                    n = astar[current.getRow()-1][current.getCol()-1];
                    neighbor(current, n, goal, current.getF()+dia); 
                }
                //if the col of the current node is less then the astar length
                //find the neighbor for that
                if(current.getCol()+1 < astar.length){
                    n = astar[current.getRow()-1][current.getCol()+1];
                    neighbor(current, n, goal,  current.getF()+dia); 
                }
            }
               //if the Col that the current node is in does not go out of bounds
               //if not check the possible neighbor
             if(current.getCol()-1 >= 0){
                n = astar[current.getRow()][current.getCol()-1];
                neighbor(current, n,goal, current.getF()+norm); 
            }
                 //if the Col that the current node is in does not go out of bounds
               //if not check the possible neighbor
            if(current.getCol()+ 1< astar.length){
                n = astar[current.getRow()][current.getCol()+1];
                neighbor(current, n,goal,  current.getF()+norm); 
            }
                //if the Col that the current node is in does not go out of bounds
               //if not check the possible neighbor
            if(current.getRow()+1 < astar.length){
                n = astar[current.getRow()+1][current.getCol()];
                neighbor(current, n,goal,  current.getF()+norm); 
                if(current.getCol()-1>=0){
                    n = astar[current.getRow()+1][current.getCol()-1];
                    neighbor(current, n,goal,  current.getF()+dia); 
                }
                
                //if the current node next col is less then the length of 15
                //find the neighbor
                if(current.getCol()+1<astar.length){
                   n = astar[current.getRow()+1][current.getCol()+1];
                   neighbor(current, n,goal,  current.getF()+dia); 
                }  
            }
            
        }
        }
    
    }   
    /**
     * goes through the motion of the calculations
     * @param current = current Node
     * @param n - node that is being compared
     * @param g - goal node, used for calculating heuristic
     * @param move 
     */
    public static void neighbor(Node current, Node n, Node g, int move ){
        
            //if the closed list containd the neighbor node 
            //or if the neighbor node is null; return
            if(closedList.contains(n) || n == null ){
                return;
            }
            //sets the new G
            n.setG(move);
            //sets node h
            h(n, g);
            //makes a theortical final cost based on the g and the move
            int finalCost = n.getH() + n.getG();
            //checks if Node n is not in the openList yet or 
            //if the estimated F is lower then the actual F
            if(!(openList.contains(n)) || finalCost < n.getF()){
                //sets the new F
                n.setF();
                //sets the path
                n.setParent(current);
                //if the openlist does not have node N, add N
                if(!(openList.contains(n))){
                    openList.add(n);
                }
                
            }
    }
    /**
     * this goes through the entire rows in order to figure out what 
     * the Hueristic from the current node to the goal node is
     * @param current - the current node
     * @param goal -  goal node
     */
    public static void h(Node current, Node goal){
        boolean found = false;
        int count =  -20;
        int count2 = 0;
        int check = current.getRow();
        int checkC= current.getCol();
        //checks the loop right from left to right
        for(int i = current.getRow(); i < 15; i++){
            check++;
            count += 10;
            if(i == goal.getRow()){
                for(int j = current.getCol(); j< 15; j++){
                    count2++;
                    checkC++;
                    count+=10;
                    if( j == goal.getCol()){
                        found = true;
                        current.setH(count);      
                        break;
                    }
                }
                //reset the count
                if(checkC == 15 && found == false){
                    count = count - (count2 *10);
                    count2 = 0;
                }
                //performs the col search but backwards
                for(int j = current.getCol(); j > 0; j--){
                    count2++;
                    checkC++;
                    count+=10;
                    if( j == goal.getCol()){
                        found = true;
                        current.setH(count);
                        break;
                    }
                }
            }
        }
             //if the loop cant find its goal node
                //reset the board and go backwards
        if( check == 15 && found == false){
                count = -20;
                //if the loop cant find its goal node
                //reset the board and go backwards
                for(int i = current.getRow(); i > -1; i--){
                check++;
                count += 10;
                if(i == goal.getRow()){
                for(int j = current.getCol(); j< 15; j++){
                    count2++;
                    checkC++;
                    count+=10;
                    if( j == goal.getCol()){
                        found = true;
                 
                        current.setH(count);
                        break;
                    }
                }
                //resets the count
                if(checkC == 15 && found == false){
                    count = count - (count2 *10);
                    count2 = 0;
                }
                //performs the col search backwards
                for(int j = current.getCol(); j > -1; j--){
                    count2++;
                    checkC++;
                    count+=10;
                    if( j == goal.getCol()){
                        found = true;       
                        current.setH(count);
                        break;
                    }
                }
            }
        }
    }
} 
    /**
     * outputs the pathing of the node to the goal node
     */
    public static void path(){
        LinkedList<Node> order = new LinkedList();
        Node goal = astar[endx][endy];
        System.out.println("Path taken from start to finish: ");
        while(goal.getParent()!=null){ 
           order.push(goal.getParent());
           goal = goal.getParent();
        } 
        //while the order list is not empty. print out the list 
        while(!(order.isEmpty())){ 
            Node temp = order.pop();
            System.out.print("(" + temp.getRow() + "," + temp.getCol() + ") " + "-> ");
            
        }     
        System.out.print("(" + (astar[endx][endy]).getRow() + "," + (astar[endx][endy]).getCol() + ") ");
    } 
    /**
     * resets the board in terms of the node and their parents and the
     * visual aspect too
     */
    public static void reset(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(startx == i && starty == j){
                    n[i][j] =  0 ;
                }
                if(endx == i && endy == j){
                    n[i][j] = 0;
                }
                if(astar[i][j] != null){
                    astar[i][j] = new Node(i,j,0);
                }
          

            }
        }
        
        
    }
}
