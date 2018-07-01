import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Map;


public class CellElement {
    private final Pane CellPane;
    private Pane FormerPane;
    private Piece currentpiece = null;

    private final Rectangle CellBoard;
    private final int x,y;
    private Color color;
    private Boolean hasPiece;

    private Map<String, Piece> items;
    private Map<String, CellElement> cells;
    private Launcher launcher;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void setItems(Map<String, Piece> items) {
        this.items = items;
    }

    public CellElement(int x, int y) {
        this.x = x;
        this.y = y;
        this.hasPiece = false;
        CellPane = new Pane();
        CellBoard = new Rectangle(100,100);
        CellBoard.setStroke(Color.BLACK);

        CellPane.setOnDragDropped((DragEvent e) -> {
            Dragboard db = e.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String nodeId = db.getString();
                ImageView piece = (ImageView) items.get(nodeId).getNode();
                if (piece != null) {
                    items.get(nodeId).getLegalplace().clear();


                    //this.launcher.getController().getOutput().print(items.get(nodeId).getCharacter()+items.get(nodeId).getLocation());

                    FormerPane.getChildren().remove(piece);
                    cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
                    cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);

                    Boolean flag = true;
                    try{
                        CellPane.getChildren().get(1);
                    } catch (IndexOutOfBoundsException ex){
                        flag = false;
                    }

                    if (flag) {
                        items.remove(CellPane.getChildren().get(1).getId());//remove this piece from the map
                        CellPane.getChildren().remove(1);
                    }

                    CellPane.getChildren().add(piece);

                    setHasPiece(true);
                    this.setCurrentpiece(items.get(nodeId));
                    items.get(nodeId).CountPlusOne();
                    items.get(nodeId).setX(this.x);
                    items.get(nodeId).setY(this.y);

                    piece.relocate(0, 0);

                    items.get(nodeId).setCurrentPane(this.getCellPane());
                    this.launcher.getController().plusone();
                    /*
                    if (flag){
                        this.launcher.getController().getOutput().println("x"+items.get(nodeId).getLocation());
                    }else {
                        this.launcher.getController().getOutput().println("-"+items.get(nodeId).getLocation());
                    }*/


                    //if user is white side
                    if (this.launcher.getController().getSide().equals("black")){
                        this.launcher.getController().getOutput().print((this.launcher.getController().getCount()/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
                    }else {
                        this.launcher.getController().getOutput().println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
                    }


                    if (this.launcher.getController().judgeWin().equals("no")){
                        this.launcher.getController().oneStep();
                        //this.launcher.getController().plusone();
                    }
                    else if (this.launcher.getController().judgeWin().equals("white")){
                        //结束，显示胜者
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Chess");
                        alert.setHeaderText(null);
                        alert.setContentText("White Side Wins");

                        alert.showAndWait();
                        this.launcher.getController().getOutput().println("white wins");
                        this.launcher.getController().getOutput().close();
                        System.exit(0);
                    }
                    else if (this.launcher.getController().judgeWin().equals("black")){
                        //结束，显示胜者
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Chess");
                        alert.setHeaderText(null);
                        alert.setContentText("Black Side Wins");

                        alert.showAndWait();
                        this.launcher.getController().getOutput().println("black wins");
                        this.launcher.getController().getOutput().close();
                        System.exit(0);
                    }

                    success = true;
                }

            }
            e.setDropCompleted(success);
            e.consume();
        });

        CellPane.setOnDragOver((DragEvent event) -> {
            Dragboard db = event.getDragboard();

            if (db.hasString()){
                String nodeId = db.getString();
                if (items.get(nodeId).getLegalplace().contains(this)) {
                    //加可视化反馈
                    CellBoard.setFill(Color.GREEN);
                    event.acceptTransferModes(TransferMode.MOVE);
                }else {
                    CellBoard.setFill(Color.RED);
                }
            }
            event.consume();
        });

        CellPane.setOnDragExited((DragEvent event) -> {
            CellBoard.setFill(color);
            event.consume();
        });

        CellPane.getChildren().add(CellBoard);
        CellPane.setMinWidth(CellBoard.getWidth());
    }

    public Rectangle getCellBoard() { return CellBoard; }

    public Pane getCellPane() {return CellPane;}

    public void setFormerPane(Pane formerPane) {
        FormerPane = formerPane;
    }

    public Boolean getHasPiece() {
        return hasPiece;
    }

    public void setHasPiece(Boolean hasPiece) {
        this.hasPiece = hasPiece;
    }

    public void setCells(Map<String, CellElement> cells) {
        this.cells = cells;
    }

    public Piece getCurrentpiece() {
        return currentpiece;
    }

    public void setCurrentpiece(Piece currentpiece) {
        this.currentpiece = currentpiece;
    }

    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
