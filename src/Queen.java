import javafx.scene.image.Image;
import javafx.scene.input.*;


public class Queen extends Piece{

    public Queen(Image[] images, CellElement[][] cell, Controller controller){
        super(images, cell, controller);
        this.setCharacter("queen");

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

        int x = this.getX();
        int y = this.getY();

        while(x < 7 && y < 7){
            if (cell[++x][++y].getHasPiece() == false){
                this.getLegalplace().add(cell[x][y]);
            }
            else {
                if (!cell[x][y].getCurrentpiece().getSide().equals(this.getSide())){
                    this.getLegalplace().add(cell[x][y]);
                }
                break;
            }
        }

        x = this.getX();
        y = this.getY();

        while(x > 0 && y < 7){
            if (cell[--x][++y].getHasPiece() == false){
                this.getLegalplace().add(cell[x][y]);
            }
            else {
                if (!cell[x][y].getCurrentpiece().getSide().equals(this.getSide())){
                    this.getLegalplace().add(cell[x][y]);

                }
                break;
            }
        }

        x = this.getX();
        y = this.getY();

        while(x > 0 && y > 0){
            if (cell[--x][--y].getHasPiece() == false){
                this.getLegalplace().add(cell[x][y]);
            }
            else {
                if (!cell[x][y].getCurrentpiece().getSide().equals(this.getSide())){
                    this.getLegalplace().add(cell[x][y]);

                }
                break;
            }
        }

        x = this.getX();
        y = this.getY();

        while(x < 7 && y > 0){
            if (cell[++x][--y].getHasPiece() == false){
                this.getLegalplace().add(cell[x][y]);
            }
            else {
                if (!cell[x][y].getCurrentpiece().getSide().equals(this.getSide())){
                    this.getLegalplace().add(cell[x][y]);
                }
                break;
            }
        }


        x = this.getX();
        y = this.getY();

        while(x < 7){
            if (cell[++x][y].getHasPiece() == false){
                this.getLegalplace().add(cell[x][y]);
            }
            else {
                if (!cell[x][y].getCurrentpiece().getSide().equals(this.getSide())){
                    this.getLegalplace().add(cell[x][y]);
                }
                break;
            }
        }

        x = this.getX();
        y = this.getY();

        while(x > 0){
            if (cell[--x][y].getHasPiece() == false){
                this.getLegalplace().add(cell[x][y]);
            }
            else {
                if (!cell[x][y].getCurrentpiece().getSide().equals(this.getSide())){
                    this.getLegalplace().add(cell[x][y]);
                }
                break;
            }
        }

        x = this.getX();
        y = this.getY();

        while(y < 7){
            if (cell[x][++y].getHasPiece() == false){
                this.getLegalplace().add(cell[x][y]);
            }
            else {
                if (!cell[x][y].getCurrentpiece().getSide().equals(this.getSide())){
                    this.getLegalplace().add(cell[x][y]);
                }
                break;
            }
        }

        x = this.getX();
        y = this.getY();

        while(y > 0){
            if (cell[x][--y].getHasPiece() == false){
                this.getLegalplace().add(cell[x][y]);
            }
            else {
                if (!cell[x][y].getCurrentpiece().getSide().equals(this.getSide())){
                    this.getLegalplace().add(cell[x][y]);
                }
                break;
            }
        }

    }
}
