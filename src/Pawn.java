import images.ImageManager;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.*;

import java.util.Optional;


public class Pawn extends Piece{
    private Piece promoted;

    public Pawn (Image[] images, CellElement[][] cell, Controller controller){
        super(images, cell, controller);
        this.setCharacter("pawn");

        this.getNode().setOnDragDetected((MouseEvent event) -> {
            for (int i = 0; i <8;i++)
                for (int j =0;j <8;j++)
                {
                    cell[i][j].setFormerPane(this.getCurrentPane());
                }

            this.activate();
            Dragboard db = this.getNode().startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            // Store the node ID in order to know what is dragged.
            content.putString(this.getNode().getId());
            db.setContent(content);
            updateOkToSet();
            event.consume();
        });


        this.getNode().setOnDragDone((DragEvent event) -> {
            normal();
            this.getLegalplace().clear();
            if (this.getX() == 0 || this.getX() == 7){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Chess");
                alert.setHeaderText("Promotion");
                alert.setContentText("Choose the new character for the pawn: ");


                ButtonType buttonType1 = new ButtonType("Queen");
                ButtonType buttonType2 = new ButtonType("Rook");
                ButtonType buttonType3 = new ButtonType("Knight");
                ButtonType buttonType4 = new ButtonType("Bishop");
                alert.getButtonTypes().setAll(buttonType1, buttonType2, buttonType3, buttonType4);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonType1){
                    // promote to queen
                    //this.getController().getOutput().println("Promotion to Queen");
                    Image[] pieceImages = new Image[2];
                    pieceImages[1] = ImageManager.getImage("pieces/active.png");
                    if (this.getSide().equals("Black")){
                        pieceImages[0] = ImageManager.getImage("pieces/black_queen.png");
                    }else {
                        pieceImages[0] = ImageManager.getImage("pieces/white_queen.png");
                    }

                    this.promoted = new Queen(pieceImages,cell,controller);

                    promoted.setCurrentPane(this.getCurrentPane());
                    promoted.setSide(this.getSide());
                    promoted.setX(this.getX());
                    promoted.setY(this.getY());
                    promoted.setId(this.getImageViewId());
                    promoted.setItems(this.getItems());

                    //replace image
                    this.getCurrentPane().getChildren().remove(1);
                    this.getCurrentPane().getChildren().add(promoted.getNode());

                    //replace the pawn in the map
                    promoted.getItems().remove(this.getImageViewId());
                    promoted.getItems().put(promoted.getImageViewId(),promoted);

                }else if (result.get() == buttonType2){
                    // promote to rook

                    //this.getController().getOutput().println("Promotion to Rook");
                    Image[] pieceImages = new Image[2];
                    pieceImages[1] = ImageManager.getImage("pieces/active.png");
                    if (this.getSide().equals("Black")){
                        pieceImages[0] = ImageManager.getImage("pieces/black_rook.png");
                    }else {
                        pieceImages[0] = ImageManager.getImage("pieces/white_rook.png");
                    }

                    this.promoted = new Rook(pieceImages,cell,controller);

                    promoted.setCurrentPane(this.getCurrentPane());
                    promoted.setSide(this.getSide());
                    promoted.setX(this.getX());
                    promoted.setY(this.getY());
                    promoted.setId(this.getImageViewId());
                    promoted.setItems(this.getItems());

                    //replace image
                    this.getCurrentPane().getChildren().remove(1);
                    this.getCurrentPane().getChildren().add(promoted.getNode());

                    //replace the pawn in the map
                    promoted.getItems().remove(this.getImageViewId());
                    promoted.getItems().put(promoted.getImageViewId(),promoted);
                }else if (result.get() == buttonType3){
                    // promote to knight

                    //this.getController().getOutput().println("Promotion to Knight");
                    Image[] pieceImages = new Image[2];
                    pieceImages[1] = ImageManager.getImage("pieces/active.png");
                    if (this.getSide().equals("Black")){
                        pieceImages[0] = ImageManager.getImage("pieces/black_knight.png");
                    }else {
                        pieceImages[0] = ImageManager.getImage("pieces/white_knight.png");
                    }

                    this.promoted = new Knight(pieceImages,cell,controller);

                    promoted.setCurrentPane(this.getCurrentPane());
                    promoted.setSide(this.getSide());
                    promoted.setX(this.getX());
                    promoted.setY(this.getY());
                    promoted.setId(this.getImageViewId());
                    promoted.setItems(this.getItems());

                    //replace image
                    this.getCurrentPane().getChildren().remove(1);
                    this.getCurrentPane().getChildren().add(promoted.getNode());

                    //replace the pawn in the map
                    promoted.getItems().remove(this.getImageViewId());
                    promoted.getItems().put(promoted.getImageViewId(),promoted);
                }else {
                    // promote to bishop

                    //this.getController().getOutput().println("Promotion to Bishop");
                    Image[] pieceImages = new Image[2];
                    pieceImages[1] = ImageManager.getImage("pieces/active.png");
                    if (this.getSide().equals("Black")){
                        pieceImages[0] = ImageManager.getImage("pieces/black_bishop.png");
                    }else {
                        pieceImages[0] = ImageManager.getImage("pieces/white_bishop.png");
                    }

                    this.promoted = new Bishop(pieceImages,cell,controller);

                    promoted.setCurrentPane(this.getCurrentPane());
                    promoted.setSide(this.getSide());
                    promoted.setX(this.getX());
                    promoted.setY(this.getY());
                    promoted.setId(this.getImageViewId());
                    promoted.setItems(this.getItems());

                    //replace image
                    this.getCurrentPane().getChildren().remove(1);
                    this.getCurrentPane().getChildren().add(promoted.getNode());

                    //replace the pawn in the map
                    promoted.getItems().remove(this.getImageViewId());
                    promoted.getItems().put(promoted.getImageViewId(),promoted);
                }
            }
            event.consume();
        });
    }

    public void updateOkToSet(){
        CellElement[][] cell = this.getCell();

        int x = this.getX();
        int y = this.getY();

        if (x >= 1 && x <= 6){
            if (this.getSide().equals("White")){
                //斜吃
                if (y > 0){
                    if (cell[x - 1][y - 1].getHasPiece() == true && !cell[x - 1][y - 1].getCurrentpiece().getSide().equals(this.getSide())){
                        this.getLegalplace().add(cell[x - 1][y - 1]);
                    }
                }
                if (y < 7){
                    if (cell[x - 1][y + 1].getHasPiece() == true && !cell[x - 1][y + 1].getCurrentpiece().getSide().equals(this.getSide())){
                        this.getLegalplace().add(cell[x - 1][y + 1]);
                    }
                }

                if (this.getCount() == 0){
                    for (int i = 0; i < 2; i++){
                        if (cell[x - 1 - i][y].getHasPiece() == false){
                            this.getLegalplace().add(cell[x - 1 - i][y]);
                        }
                        else {
                            break;
                        }
                    }
                } else{
                    if (cell[x - 1][y].getHasPiece() == false){
                        this.getLegalplace().add(cell[x - 1][y]);
                    }
                }

            }else if (this.getSide().equals("Black")){
                //斜吃
                if (y > 0){
                    if (cell[x + 1][y - 1].getHasPiece() == true && !cell[x + 1][y - 1].getCurrentpiece().getSide().equals(this.getSide())){
                        this.getLegalplace().add(cell[x + 1][y - 1]);
                    }
                }
                if (y < 7){
                    if (cell[x + 1][y + 1].getHasPiece() == true && !cell[x + 1][y + 1].getCurrentpiece().getSide().equals(this.getSide())){
                        this.getLegalplace().add(cell[x + 1][y + 1]);
                    }
                }
                if (this.getCount() == 0){
                    for (int i = 0; i < 2; i++){
                        if (cell[x + 1 + i][y].getHasPiece() == false){
                            this.getLegalplace().add(cell[x + 1 + i][y]);
                        }
                        else {
                            break;
                        }
                    }
                } else{
                    if (cell[x + 1][y].getHasPiece() == false){
                        this.getLegalplace().add(cell[x + 1][y]);
                    }
                }

            }
        }

    }

}