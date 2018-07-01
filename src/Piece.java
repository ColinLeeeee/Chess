import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;

import java.util.Map;
import java.util.ArrayList;

/**
 *
 *
 */
public class Piece {

    /**
     *Black or White
     */
    private String side;
    private String character;
    private int x,y;
    private int count;//count steps for the piece

    private  Image previewImage = null;
    private  Image activeImage = null;
    private  ImageView currentImage = null;
    private Pane currentPane;

    private Map<String, Piece> items;
    private CellElement[][] cell;

    private Controller controller;

    private ArrayList<CellElement> legalplace;
    private String location;

    public Piece() {
    }

    public Piece(Image[] images, CellElement[][] cell, Controller controller) {
        this.count = 0;
        this.previewImage = images[0];
        this.activeImage = images[1];
        this.cell = cell;
        setController(controller);
        this.legalplace = new ArrayList<CellElement>();

        currentImage = new ImageView();
        currentImage.setImage(previewImage);

        currentImage.setOnDragDetected((MouseEvent event) -> {
            for (int i = 0; i <8;i++)
                for (int j =0;j <8;j++)
                {
                    cell[i][j].setFormerPane(currentPane);
                }

            activate();
            Dragboard db = currentImage.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            // Store the node ID in order to know what is dragged.
            content.putString(currentImage.getId());
            db.setContent(content);
            event.consume();
        });
        currentImage.setOnDragDone((DragEvent event) -> {
            normal();
            this.getLegalplace().clear();
            event.consume();
        });
    }

    public void normalOkToSet(){
        this.getLegalplace().clear();
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }


    public void normal() {
        currentImage.setImage(previewImage);
    }

    public void activate() { currentImage.setImage(activeImage);}

    public String getImageViewId() {
        return currentImage.getId();
    }

    public Node getNode() {
        return currentImage;
    }

    public Pane getCurrentPane() {
        return currentPane;
    }

    public void setCurrentPane(Pane currentPane) {
        this.currentPane = currentPane;
    }

    public void setId(String id){
        currentImage.setId(id);
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCharacter() {
        return character;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCount() {
        return count;
    }

    public void CountPlusOne() {
        this.count += 1;
    }


    public CellElement[][] getCell() {
        return cell;
    }

    public Map<String, Piece> getItems() {
        return items;
    }

    public void setItems(Map<String, Piece> items) {
        this.items = items;
    }

    public void updateOkToSet() {}

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public ArrayList<CellElement> getLegalplace() {
        return legalplace;
    }

    public String getLocation() {
        location = ((8-this.x)+String.format("%c",65+this.y));
        return location;
    }

    public Controller getController() {
        return controller;
    }

    public String getShort(){
        if (this.character.equals("pawn")){
            return "";
        }else if (this.character.equals("king")){
            return "K";
        }else if (this.character.equals("queen")){
            return "Q";
        }else if (this.character.equals("knight")){
            return "N";
        }else if (this.character.equals("bishop")){
            return "B";
        }else {
            return "R";
        }
    }
}
