import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;


public class Knight extends Piece{

    public Knight(Image[] images, CellElement[][] cell, Controller controller){
        super(images, cell, controller);
        this.setCharacter("knight");

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
    }

    public void updateOkToSet(){
        CellElement[][] cell = this.getCell();
        for (int i = 0; i < 8;i++)
            for (int j = 0; j < 8; j++){
                if (((Math.abs(i - this.getX()) == 1 && Math.abs(j - this.getY()) == 2) || (Math.abs(i - this.getX()) == 2 && Math.abs(j - this.getY()) == 1))){
                    if (cell[i][j].getHasPiece() && !cell[i][j].getCurrentpiece().getSide().equals(this.getSide())){
                        this.getLegalplace().add(cell[i][j]);
                    }
                    else if (!cell[i][j].getHasPiece()){
                        this.getLegalplace().add(cell[i][j]);
                    }
                }
            }
    }

}