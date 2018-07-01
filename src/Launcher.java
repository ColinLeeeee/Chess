import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import images.ImageManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class Launcher extends Application{
    private HashMap<String, CellElement> cells;
    private HashMap<String, Piece> items;
    private CellElement[][] cell;
    private GridPane root;
    private Controller controller;
    private Button castling = new Button("CASTLING");

    private final double WIDTH = 1000;
    private final double HEIGHT = 900;


    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage primaryStage) throws IOException {
        this.root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #FFF8DC;");

        this.cell = new CellElement[8][8];

        //pieceman
        Piece[] piece_black = new Piece[16];
        Piece[] piece_white = new Piece[16];

        //the map of piece.imageview's id and piece
        items = new HashMap<>();
        cells = new HashMap<>();

        //build the chessboard
        Build_Board();

        controller = new Controller(items,cell,cells);
        //initiate the chessboard
        Initiate_Board(piece_black,piece_white);

        primaryStage.setTitle("Chess");
        Scene s = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(s);
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.show();


        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Chess");
        alert.setHeaderText("Come on!");
        alert.setContentText("Choose your side.");

        ButtonType buttonTypeOne = new ButtonType("Black");
        ButtonType buttonTypeTwo = new ButtonType("White");
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            // ... user chose black
            controller.setSide("white");
            controller.oneStep();
        }else {
            controller.setSide("black");
        }


        castling.setOnAction(event -> {
            String nodeId;
            if (this.getController().getSide().equals("black")){
                nodeId = "20";
            }else {
                nodeId = "4";
            }

            King king = (King)items.get(nodeId);
            if (king.getHasCastling()){
                Alert a1 = new Alert(Alert.AlertType.INFORMATION);
                a1.setTitle("Chess");
                a1.setHeaderText(null);
                a1.setContentText("Your King has castled! Can't castle!");

                a1.showAndWait();
            }
            if (king.getBeChecked()){
                Alert a1 = new Alert(Alert.AlertType.INFORMATION);
                a1.setTitle("Chess");
                a1.setHeaderText(null);
                a1.setContentText("Your King is being checked! Can't castle!");

                a1.showAndWait();
            }else if (king.getCount() > 0){
                Alert a2 = new Alert(Alert.AlertType.INFORMATION);
                a2.setTitle("Chess");
                a2.setHeaderText(null);
                a2.setContentText("Your King has moved! Can't castle!");

                a2.showAndWait();
            } else {
                Alert a = new Alert(AlertType.CONFIRMATION);
                a.setTitle("Chess");
                a.setHeaderText("Castling");
                a.setContentText("Choose the kind of castling: ");

                ButtonType longcastling = new ButtonType("Long");
                ButtonType shortcastling = new ButtonType("Short");
                a.getButtonTypes().setAll(longcastling, shortcastling);

                Optional<ButtonType> r = a.showAndWait();
                if (r.get() == longcastling){
                    int index1 = 0;
                    int index2 = 0;
                    if (king.getSide().equals("White")){
                        index1 = 16;
                        index2 = 7;
                    }

                    Rook rook = (Rook) items.get(index1+"");
                    king.updateOkToSet();
                    if (rook.getCount() > 0){
                        Alert a3 = new Alert(Alert.AlertType.INFORMATION);
                        a3.setTitle("Chess");
                        a3.setHeaderText(null);
                        a3.setContentText("This Rook has moved! Can't castle!");

                        a3.showAndWait();
                    }
                    else if (cell[index2][1].getHasPiece() || cell[index2][2].getHasPiece() || cell[index2][3].getHasPiece()){
                        Alert a4 = new Alert(Alert.AlertType.INFORMATION);
                        a4.setTitle("Chess");
                        a4.setHeaderText(null);
                        a4.setContentText("There are pieces between King and Rook! Can't castle!");

                        a4.showAndWait();
                    }else if (!king.getLegalplace().contains(cell[index2][3])){
                        Alert a4 = new Alert(Alert.AlertType.INFORMATION);
                        a4.setTitle("Chess");
                        a4.setHeaderText(null);
                        a4.setContentText("The path is not ok for King! Can't castle!");

                        a4.showAndWait();
                    }else {
                        //move king

                        //getController().getOutput().print("Castling: ");
                        ImageView piece = (ImageView) king.getNode();
                        Pane formerPane = (Pane) piece.getParent();
                        formerPane.getChildren().remove(piece);

                        getController().getOutput().print(king.getCharacter()+king.getLocation());
                        cells.get(king.getX() + "" + king.getY()).setCurrentpiece(null);
                        cells.get(king.getX() + "" + king.getY()).setHasPiece(false);
                        cell[index2][2].getCellPane().getChildren().add(piece);
                        cell[index2][2].setHasPiece(true);
                        cell[index2][2].setCurrentpiece(items.get(nodeId));
                        //castling is a count for king
                        king.CountPlusOne();
                        king.setX(index2);
                        king.setY(2);
                        king.setCurrentPane((Pane) piece.getParent());

                        //getController().getOutput().print("-"+king.getLocation()+",");

                        //if user is white side
                        if (getController().getSide().equals("black")){
                            getController().getOutput().print((getController().getCount()/2+1)+"."+"Castling: "+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
                        }else {
                            getController().getOutput().println("-"+"Castling: "+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
                        }

                        //move rook
                        piece = (ImageView) rook.getNode();
                        formerPane = (Pane) piece.getParent();
                        formerPane.getChildren().remove(piece);

                        //getController().getOutput().print(rook.getCharacter()+rook.getLocation());
                        cells.get(rook.getX() + "" + rook.getY()).setCurrentpiece(null);
                        cells.get(rook.getX() + "" + rook.getY()).setHasPiece(false);
                        cell[index2][3].getCellPane().getChildren().add(piece);
                        cell[index2][3].setHasPiece(true);
                        cell[index2][3].setCurrentpiece(items.get(nodeId));
                        //castling is a count for rook
                        rook.setX(index2);
                        rook.setY(3);
                        rook.setCurrentPane((Pane) piece.getParent());

                        //getController().getOutput().println("-"+rook.getLocation());

                        king.setHasCastling(true);
                        //count plus one
                        this.getController().plusone();
                        king.normalOkToSet();


                        this.getController().oneStep();
                    }
                    king.normalOkToSet();
                }else {

                    int index1 = 0;
                    int index2 = 0;
                    if (king.getSide().equals("White")){
                        index1 = 16;
                        index2 = 7;
                    }

                    Rook rook = (Rook) items.get(index1+7+"");
                    king.updateOkToSet();
                    if (rook.getCount() > 0){
                        Alert a3 = new Alert(Alert.AlertType.INFORMATION);
                        a3.setTitle("Chess");
                        a3.setHeaderText(null);
                        a3.setContentText("This Rook has moved! Can't castle!");

                        a3.showAndWait();
                    }
                    else if (cell[index2][5].getHasPiece() || cell[index2][6].getHasPiece()){
                        Alert a4 = new Alert(Alert.AlertType.INFORMATION);
                        a4.setTitle("Chess");
                        a4.setHeaderText(null);
                        a4.setContentText("There are pieces between King and Rook! Can't castle!");

                        a4.showAndWait();
                    }else if (!king.getLegalplace().contains(cell[index2][5])){
                        Alert a4 = new Alert(Alert.AlertType.INFORMATION);
                        a4.setTitle("Chess");
                        a4.setHeaderText(null);
                        a4.setContentText("The path is not ok for King! Can't castle!");

                        a4.showAndWait();
                    }else {
                        //move king
                        //getController().getOutput().print("Castling: ");

                        ImageView piece = (ImageView) king.getNode();
                        Pane formerPane = (Pane) piece.getParent();
                        formerPane.getChildren().remove(piece);

                        //getController().getOutput().print(king.getCharacter()+king.getLocation());
                        cells.get(king.getX() + "" + king.getY()).setCurrentpiece(null);
                        cells.get(king.getX() + "" + king.getY()).setHasPiece(false);
                        cell[index2][6].getCellPane().getChildren().add(piece);
                        cell[index2][6].setHasPiece(true);
                        cell[index2][6].setCurrentpiece(items.get(nodeId));
                        //castling is a count for king
                        king.CountPlusOne();
                        king.setX(index2);
                        king.setY(6);
                        king.setCurrentPane((Pane) piece.getParent());

                        //getController().getOutput().print("-"+king.getLocation()+",");

                        //if user is white side
                        if (getController().getSide().equals("black")){
                            getController().getOutput().print((getController().getCount()/2+1)+"."+"Castling: "+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
                        }else {
                            getController().getOutput().println("-"+"Castling: "+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
                        }

                        //move rook
                        piece = (ImageView) rook.getNode();
                        formerPane = (Pane) piece.getParent();
                        formerPane.getChildren().remove(piece);

                        //getController().getOutput().print(rook.getCharacter()+rook.getLocation());
                        cells.get(rook.getX() + "" + rook.getY()).setCurrentpiece(null);
                        cells.get(rook.getX() + "" + rook.getY()).setHasPiece(false);
                        cell[index2][5].getCellPane().getChildren().add(piece);
                        cell[index2][5].setHasPiece(true);
                        cell[index2][5].setCurrentpiece(items.get(nodeId));
                        //castling is a count for rook
                        rook.setX(index2);
                        rook.setY(5);
                        rook.setCurrentPane((Pane) piece.getParent());

                        //getController().getOutput().println("-"+rook.getLocation());

                        king.setHasCastling(true);
                        //count plus one
                        this.getController().plusone();
                        king.normalOkToSet();


                        this.getController().oneStep();
                    }
                    king.normalOkToSet();
                }
            }
            event.consume();
        });
    }

    /**
     * Pieces index
     *
     * Rook Knight Bishop Queen King Bishop Knight Rook
     * 0    1      2      3     4    5      6      7
     * Pawn 8~15
     *
     * for black and white
     */

    public void Build_Board(){
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                cell[i][j] = new CellElement(i,j);
                if ((i%2 == 0 && j%2 == 0) || (i%2 == 1 && j%2 == 1)){
                    cell[i][j].getCellBoard().setFill(Color.rgb(252,240,210));
                    cell[i][j].setColor(Color.rgb(252,240,210));
                }
                else {
                    cell[i][j].getCellBoard().setFill(Color.rgb(172,36,62));
                    cell[i][j].setColor(Color.rgb(172,36,62));
                }

                root.add(cell[i][j].getCellPane(), j+1, i+1);

                cells.put(i+""+j,cell[i][j]);
                cell[i][j].setCells(cells);
                cell[i][j].setLauncher(this);

            }

        for (int i = 0; i < 8; i++){
            Text t = new Text();
            t.setText(i+1+"");
            t.setFill(Color.BROWN);
            t.setFont(Font.font("sans-serif", FontWeight.BOLD, 30));
            root.add(t,0,8-i);
        }

        for (int i = 0; i < 8; i++){
            Text t = new Text();
            t.setText(i+1+"");
            t.setFill(Color.BROWN);
            t.setFont(Font.font("sans-serif", FontWeight.BOLD, 30));
            root.add(t,9,8-i);
        }

        for (int i = 0; i < 8; i++){
            Text t = new Text();
            t.setText(String.format("%c",65+i));
            t.setFill(Color.BROWN);
            t.setFont(Font.font("sans-serif", FontWeight.BOLD, 30));
            root.add(t,i+1,0);
        }

        for (int i = 0; i < 8; i++){
            Text t = new Text();
            t.setText(String.format("%c",65+i));
            t.setFill(Color.BROWN);
            t.setFont(Font.font("sans-serif", FontWeight.BOLD, 30));
            root.add(t,i+1,9);
        }

        ColumnConstraints column = new ColumnConstraints(100);
        column.setHalignment(HPos.CENTER);
        root.getColumnConstraints().add(column);
        RowConstraints row = new RowConstraints(50);
        row.setValignment(VPos.CENTER);
        root.getRowConstraints().add(row);

        for (int i = 0; i < 8; i++)
        {
            column = new ColumnConstraints(cell[0][0].getCellBoard().getWidth());
            column.setHalignment(HPos.CENTER);
            root.getColumnConstraints().add(column);
            row = new RowConstraints(cell[0][0].getCellBoard().getWidth());
            root.getRowConstraints().add(row);
        }

        column = new ColumnConstraints(100);
        column.setHalignment(HPos.CENTER);
        root.getColumnConstraints().add(column);
        row = new RowConstraints(50);
        row.setValignment(VPos.CENTER);
        root.getRowConstraints().add(row);

        root.add(castling,9,9);

    }

    public void Initiate_Board(Piece[] piece_black, Piece[] piece_white){
        /**
         * get image
         */

        final String active_image = "pieces/active.png";


        Image[][] pieceImages = new Image[32][2];

        for (int i = 0; i < 32;i++)
        {
            pieceImages[i][1] = ImageManager.getImage(active_image);
        }


        //pawn
        for (int i = 8; i < 16; i++)
        {
            pieceImages[i][0] = ImageManager.getImage("pieces/black_pawn.png");
            pieceImages[i+16][0] = ImageManager.getImage("pieces/white_pawn.png");
        }
        for (int i = 8; i < 16; i++) {
            piece_black[i] = new Pawn(pieceImages[i],cell,this.controller);
            piece_white[i] = new Pawn(pieceImages[i+16],cell,this.controller);
        }

        //rook
        pieceImages[0][0] = ImageManager.getImage("pieces/black_rook.png");
        pieceImages[16][0] = ImageManager.getImage("pieces/white_rook.png");

        piece_black[0] = new Rook(pieceImages[0],cell,controller);
        piece_white[0] = new Rook(pieceImages[16],cell,controller);

        //knight
        pieceImages[1][0] = ImageManager.getImage("pieces/black_knight.png");
        pieceImages[17][0] = ImageManager.getImage("pieces/white_knight.png");

        piece_black[1] = new Knight(pieceImages[1],cell,controller);
        piece_white[1] = new Knight(pieceImages[17],cell,controller);

        //bishop
        pieceImages[2][0] = ImageManager.getImage("pieces/black_bishop.png");
        pieceImages[18][0] = ImageManager.getImage("pieces/white_bishop.png");

        piece_black[2] = new Bishop(pieceImages[2],cell,controller);
        piece_white[2] = new Bishop(pieceImages[18],cell,controller);

        //queen
        pieceImages[3][0] = ImageManager.getImage("pieces/black_queen.png");
        pieceImages[19][0] = ImageManager.getImage("pieces/white_queen.png");

        piece_black[3] = new Queen(pieceImages[3],cell,controller);
        piece_white[3] = new Queen(pieceImages[19],cell,controller);

        //king
        pieceImages[4][0] = ImageManager.getImage("pieces/black_king.png");
        pieceImages[20][0] = ImageManager.getImage("pieces/white_king.png");

        piece_black[4] = new King(pieceImages[4],cell,controller);
        piece_white[4] = new King(pieceImages[20],cell,controller);

        //bishop
        pieceImages[5][0] = ImageManager.getImage("pieces/black_bishop.png");
        pieceImages[21][0] = ImageManager.getImage("pieces/white_bishop.png");

        piece_black[5] = new Bishop(pieceImages[5],cell,controller);
        piece_white[5] = new Bishop(pieceImages[21],cell,controller);

        //knight
        pieceImages[6][0] = ImageManager.getImage("pieces/black_knight.png");
        pieceImages[22][0] = ImageManager.getImage("pieces/white_knight.png");

        piece_black[6] = new Knight(pieceImages[6],cell,controller);
        piece_white[6] = new Knight(pieceImages[22],cell,controller);

        //rook
        pieceImages[7][0] = ImageManager.getImage("pieces/black_rook.png");
        pieceImages[23][0] = ImageManager.getImage("pieces/white_rook.png");

        piece_black[7] = new Rook(pieceImages[7],cell,controller);
        piece_white[7] = new Rook(pieceImages[23],cell,controller);



        /**
         * add piece to cell
         */


        for (int i = 0; i < 8; i++)
        {
            cell[0][i].getCellPane().getChildren().add(piece_black[i].getNode());
            piece_black[i].setX(0);
            piece_black[i].setY(i);

            piece_black[i].setCurrentPane(cell[0][i].getCellPane());
            cell[0][i].setHasPiece(true);
            cell[0][i].setCurrentpiece(piece_black[i]);

            cell[1][i].getCellPane().getChildren().add(piece_black[i+8].getNode());
            piece_black[i+8].setX(1);
            piece_black[i+8].setY(i);
            piece_black[i+8].setCurrentPane(cell[1][i].getCellPane());
            cell[1][i].setHasPiece(true);
            cell[1][i].setCurrentpiece(piece_black[i+8]);

            cell[7][i].getCellPane().getChildren().add(piece_white[i].getNode());
            piece_white[i].setX(7);
            piece_white[i].setY(i);
            piece_white[i].setCurrentPane(cell[7][i].getCellPane());
            cell[7][i].setHasPiece(true);
            cell[7][i].setCurrentpiece(piece_white[i]);

            cell[6][i].getCellPane().getChildren().add(piece_white[i+8].getNode());
            piece_white[i+8].setX(6);
            piece_white[i+8].setY(i);
            piece_white[i+8].setCurrentPane(cell[6][i].getCellPane());
            cell[6][i].setHasPiece(true);
            cell[6][i].setCurrentpiece(piece_white[i+8]);
        }




        /**
         * set side
         */

        for (int i = 0;i < 16; i++)
        {
            piece_black[i].setSide("Black");
            piece_white[i].setSide("White");
            piece_black[i].setItems(this.items);
            piece_white[i].setItems(this.items);
        }


        /**
         * add pieces to the map
         */

        for (int i = 0; i < 16; i++){
            piece_black[i].setId(i+"");
            int tmp = i+16;
            piece_white[i].setId(tmp+"");
            items.put(piece_black[i].getImageViewId(), piece_black[i]);
            items.put(piece_white[i].getImageViewId(), piece_white[i]);
        }

        for (int i = 0; i < 8; i++)
            for (int j = 0; j <8; j++)
            {
                cell[i][j].setItems(items);
            }


    }

    public Controller getController() {
        return controller;
    }
}

