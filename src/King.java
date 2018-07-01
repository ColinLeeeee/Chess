import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

import java.util.Map;
import java.util.concurrent.BlockingDeque;

public class King extends Piece{


    private Boolean isBeChecked = false;
    private Boolean hasCastling = false;

    public King(Image[] images, CellElement[][] cell, Controller controller){
        super(images, cell, controller);
        this.setCharacter("king");

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

    public Boolean IsBeChecked(){

        this.isBeChecked = false;
        CellElement[][] cell = this.getCell();

        Map<String, Piece> items = this.getItems();
        for (Piece value : items.values()) {
            if (value != null && value != this){
                if (value.getCharacter() != "king" && !value.getSide().equals(this.getSide())){
                    value.updateOkToSet();
                }
            }
        }

        //checked
        for (Piece value : items.values()){
            if (value != null && value != this){
                if (value.getLegalplace().contains(cell[this.getX()][this.getY()]) && !value.getSide().equals(this.getSide())){
                    this.isBeChecked = true;
                    System.out.println(value.getCharacter()+ value.getSide());
                }
            }
        }

        for (Piece value : items.values()){
            if (value != null && value != this){
                if (!value.getSide().equals(this.getSide())){
                    value.getLegalplace().clear();
                }
            }
        }

        return isBeChecked;
    }

    public Boolean IsBeChecked_withoutrecover(){
        this.isBeChecked = false;
        CellElement[][] cell = this.getCell();

        Map<String, Piece> items = this.getItems();
        for (Piece value : items.values()) {
            if (value != null && value != this){
                if (value.getCharacter() != "king" && !value.getSide().equals(this.getSide())){
                    value.updateOkToSet();
                }
            }
        }

        //checked
        for (Piece value : items.values()){
            if (value != null && value != this){
                if (value.getLegalplace().contains(cell[this.getX()][this.getY()]) && !value.getSide().equals(this.getSide())){
                    this.isBeChecked = true;
                    System.out.println(value.getCharacter()+ value.getSide());
                }
            }
        }


        return isBeChecked;
    }


    public void updateOkToSet(){
        CellElement[][] cell = this.getCell();

        Map<String, Piece> items = this.getItems();
        for (Piece value : items.values()) {
            if (value != null && value != this){
                if (value.getCharacter() != "king" && !value.getSide().equals(this.getSide())){
                    value.updateOkToSet();
                }
            }
        }

        System.out.println(this.getLegalplace());

        for (int i = 0; i < 8; i++){

            System.out.println(i);
            for (int j = 0; j < 8; j++){
                int flag = 0;
                if (Math.abs(i - this.getX()) <= 1 && Math.abs(j - this.getY()) <= 1){
                    System.out.println(j);

                    for (Piece value : items.values()){
                        if (value != null && value != this){
                            if (!value.getSide().equals(this.getSide())){
                                if (value.getLegalplace().contains(cell[i][j])){
                                    flag = 1;
                                    System.out.println(i+","+j+value.getCharacter()+value.getSide()+value.getX()+","+value.getY());
                                }
                            }
                        }
                    }

                    if (flag == 0){
                        if (cell[i][j].getHasPiece()){
                            if (!cell[i][j].getCurrentpiece().getSide().equals(this.getSide())){
                                this.getLegalplace().add(cell[i][j]);
                                System.out.println(i+","+j+"status 1");
                            }
                            else {
                                System.out.println(i+","+j+"status 2");
                            }
                        }else {
                            this.getLegalplace().add(cell[i][j]);
                            System.out.println(i+","+j+"status 3");
                        }
                    }
                }
            }
        }


            System.out.println(this.getLegalplace());
        Piece tmp;

        if (this.getSide().equals("White")){
            tmp = items.get("4");
        }else {
            tmp = items.get("20");
        }


        if (this.getLegalplace().contains(cell[tmp.getX()][tmp.getY()])){
            this.getLegalplace().remove(cell[tmp.getX()][tmp.getY()]);
        }
        if (tmp.getX() != 0){
            if (this.getLegalplace().contains(cell[tmp.getX() - 1][tmp.getY()])){
                this.getLegalplace().remove(cell[tmp.getX() - 1][tmp.getY()]);
            }
            if (tmp.getY() != 0){
                if (this.getLegalplace().contains(cell[tmp.getX() - 1][tmp.getY() - 1])){
                    this.getLegalplace().remove(cell[tmp.getX() - 1][tmp.getY() - 1]);
                }
            }
            else if (tmp.getY() != 7){
                if (this.getLegalplace().contains(cell[tmp.getX() - 1][tmp.getY() + 1])){
                    this.getLegalplace().remove(cell[tmp.getX() - 1][tmp.getY() + 1]);
                }
            }
        }
        if (tmp.getX() != 7){
            if (this.getLegalplace().contains(cell[tmp.getX() + 1][tmp.getY()])){
                this.getLegalplace().remove(cell[tmp.getX() + 1][tmp.getY()]);
            }
            if (tmp.getY() != 0){
                if (this.getLegalplace().contains(cell[tmp.getX() + 1][tmp.getY() - 1])){
                    this.getLegalplace().remove(cell[tmp.getX() + 1][tmp.getY() - 1]);
                }
            }
            else if (tmp.getY() != 7){
                if (this.getLegalplace().contains(cell[tmp.getX() + 1][tmp.getY() + 1])){
                    this.getLegalplace().remove(cell[tmp.getX() + 1][tmp.getY() + 1]);
                }
            }
        }

        if (tmp.getY() != 0){
            if (this.getLegalplace().contains(cell[tmp.getX()][tmp.getY() - 1])){
                this.getLegalplace().remove(cell[tmp.getX()][tmp.getY() - 1]);
            }
        }else if (tmp.getY() != 7){
            if (this.getLegalplace().contains(cell[tmp.getX()][tmp.getY() + 1])){
                this.getLegalplace().remove(cell[tmp.getX()][tmp.getY() + 1]);
            }
        }

        for (Piece value : items.values()){
            if (value != null && value != this){
                if (!value.getSide().equals(this.getSide())){
                    value.getLegalplace().clear();
                }
            }
        }

    }


    public Boolean getBeChecked() {
        return isBeChecked;
    }

    public Boolean getHasCastling() {
        return hasCastling;
    }

    public void setHasCastling(Boolean hasCastling) {
        this.hasCastling = hasCastling;
    }

}