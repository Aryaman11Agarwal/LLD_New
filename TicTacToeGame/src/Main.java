import java.util.Scanner;

interface PlayerStrategy{

    Cell getMove(String name);
}

//class PlayerStrategyFactory{
//
//    public PlayerStrategy getPlayerStrategy(String strategyName){
//
//        if(strategyName.equals("HumanPlayerStrategy")){
//            return new HumanPlayerStrategy();
//        }
//
//        else return null;
//
//    }
//}

class HumanPlayerStrategy implements PlayerStrategy{

    Scanner sc;

    HumanPlayerStrategy(){


        sc=new Scanner(System.in);
    }

    public Cell getMove(String name){

        System.out.println("Hey "+ name+" make your next move!");
        System.out.println();
        int row=sc.nextInt();
        int col=sc.nextInt();

        return new Cell(row,col);
    }

}
class Cell{
    private final int row,col;

    Cell(int row,int col){
        this.row=row;
        this.col=col;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
}
class Board{

    char [][] grid;
    private int boardSize;

    Board(int boardSize){
        this.boardSize=boardSize;
        grid=new char[boardSize][boardSize];

        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                grid[i][j]=' ';
            }
        }

    }

    public int getBoardSize() {
        return boardSize;
    }

    char getChar(int row, int col){
        return grid[row][col];
    }

    void printBoard(){

        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                System.out.print("["+grid[i][j]+"]");
            }
            System.out.println();
        }
    }

    boolean isFull(){

        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                if(grid[i][j]==' ') return false;
            }
        }

        return true;
    }


    boolean hasLine(char symbol) {
        int n = grid.length;

        // Rows
        for (int i = 0; i < n; i++) {
            boolean ok = true;
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != symbol) {
                    ok = false;
                    break;
                }
            }
            if (ok) return true;
        }

        // Columns
        for (int j = 0; j < n; j++) {
            boolean ok = true;
            for (int i = 0; i < n; i++) {
                if (grid[i][j] != symbol) {
                    ok = false;
                    break;
                }
            }
            if (ok) return true;
        }

        // Main diagonal
        boolean ok = true;
        for (int i = 0; i < n; i++) {
            if (grid[i][i] != symbol) {
                ok = false;
                break;
            }
        }
        if (ok) return true;

        // Other diagonal
        ok = true;
        for (int i = 0; i < n; i++) {
            if (grid[i][n - 1 - i] != symbol) {
                ok = false;
                break;
            }
        }

        return ok;
    }

    boolean updateRowCol(int row,int col,char symbol){

        if(row>=0 && row<boardSize && col>=0 && col<boardSize && grid[row][col]==' ')
        {
            grid[row][col]=symbol;
            return true;
        }

        System.out.println("Invalid move! Try again");

        return false;
    }



}

class Player{

    private final String name;
  //  private int ID;
    private final char symbol;

    private final PlayerStrategy playerStrategy;
   // Scanner sc;

    Player(String name,char symbol,PlayerStrategy playerStrategy){
        this.name=name;
        this.symbol=symbol;
     //   sc=new Scanner(System.in);
        this.playerStrategy=playerStrategy;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

    public Cell getMove(){

        return playerStrategy.getMove(this.name);

    }



}
class GameEngine{

    private Player player1, player2,currentPlayer;

    private Board board;

   // private boolean turn;



    GameEngine(int n,Player player1 , Player player2){
        this.player1=player1;
        this.player2=player2;
        currentPlayer=player1;

        this.board=new Board(n);
    }

    void placeSymbol(int row, int col){
        board.updateRowCol(row,col, currentPlayer.getSymbol());
    }

    void switchPlayer(){

        if(currentPlayer==player1) currentPlayer=player2;
        else currentPlayer=player1;
    }



    void play(){

        while(true){
            Cell c = currentPlayer.getMove();

            boolean b = board.updateRowCol(c.getRow(),c.getCol(),currentPlayer.getSymbol());

            if(!b) continue;

            board.printBoard();

            boolean isWin=board.hasLine(currentPlayer.getSymbol());

            if(isWin){
                System.out.println(currentPlayer.getName()+" has won the game! ");
                return;

            }

            if(board.isFull()){
                System.out.println("Its a tie");
                return;
            }

            switchPlayer();
        }



    }


}

public class Main {
    public static void main(String[] args) {

    //    PlayerStrategyFactory playerStrategyFactory=new PlayerStrategyFactory();
        PlayerStrategy humanPlayerStrategy=new HumanPlayerStrategy();
        Player player1=new Player("Alice",'X',humanPlayerStrategy);
        Player player2=new Player("Bob",'O',humanPlayerStrategy);

        GameEngine gameEngine=new GameEngine(3,player1,player2);

        gameEngine.play();
    }
}