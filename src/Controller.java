import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

public class Controller {

    private Map<String, Piece> items;
    private CellElement[][] cell;
    private Map<String, CellElement> cells;
    private String side;//the side for ai
    private int count = 0;

    private File scorebook;
    private PrintWriter output;
    private Calendar ca;

    Controller(Map<String, Piece> items, CellElement[][] cell, Map<String, CellElement> cells)
        throws IOException {
        this.items = items;
        this.cell = cell;
        this.cells = cells;


        ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);//获取年份
        int month=ca.get(Calendar.MONTH);//获取月份
        int day=ca.get(Calendar.DATE);//获取日
        int minute=ca.get(Calendar.MINUTE);//分
        int hour=ca.get(Calendar.HOUR);//小时
        int second=ca.get(Calendar.SECOND);//秒

        String time = String.format("%d_%d_%d_%d:%d", year,month+1,day,hour,minute);
        System.out.println(time);
        // dir = new File("")
        new File("ScoreBook").mkdir();
        scorebook = new File("ScoreBook/ScoreBook_"+time+".txt");
        output = new PrintWriter(scorebook);

        output.println(time+":"+second);
    }
    public String judgeWin() {
        /**
         * judge if the game has ended.
         * @return 'black' for Black side wins, 'white' for white side wins, 'no' for game continues.
         * */

        int flag = 0;

        King black_king = (King)items.get("4");

        if (black_king == null){
            return "white";
        }

        if (black_king.IsBeChecked()){
            System.out.println("black is checked");
            for (Piece value : items.values()) {
                if (value != null){
                    if (value.getSide().equals("Black")){
                        value.updateOkToSet();
                        int n = value.getLegalplace().size();
                        for (int i = 0; i < n;i++){

                            int x = value.getX();
                            int y = value.getY();
                            //simulate a step
                            cells.get(value.getX()+""+value.getY()).setCurrentpiece(null);
                            cells.get(value.getX()+""+value.getY()).setHasPiece(false);

                            Piece tmp = null;
                            if (value.getLegalplace().get(i).getHasPiece()){
                                tmp = value.getLegalplace().get(i).getCurrentpiece();
                                items.remove(tmp);
                            }

                            value.getLegalplace().get(i).setHasPiece(true);
                            value.getLegalplace().get(i).setCurrentpiece(value);

                            value.setX(value.getLegalplace().get(i).getX());
                            value.setY(value.getLegalplace().get(i).getY());

                            //check if king is checked
                            if (!black_king.IsBeChecked_withoutrecover()){
                                flag = 1;
                            }

                            //recovery
                            value.setX(x);
                            value.setY(y);


                            cells.get(value.getX()+""+value.getY()).setCurrentpiece(value);
                            cells.get(value.getX()+""+value.getY()).setHasPiece(true);

                            if (tmp != null){
                                value.getLegalplace().get(i).setCurrentpiece(tmp);
                                items.put(tmp.getImageViewId(),tmp);
                            }else {
                                value.getLegalplace().get(i).setHasPiece(false);
                                value.getLegalplace().get(i).setCurrentpiece(null);
                            }
                        }
                        for (Piece value1 : items.values()){
                            if (value != null && value1 != black_king){
                                if (!value1.getSide().equals(black_king.getSide())){
                                    value1.getLegalplace().clear();
                                }
                            }
                        }
                        value.getLegalplace().clear();
                    }
                }
            }

            if (flag == 1){
                return "no";
            }else {
                return "white";
            }
        }

        flag = 0;

        King white_king = (King)items.get("20");

        if (white_king == null){
            return "black";
        }

        if (white_king.IsBeChecked()){
            System.out.println("white is checked");
            for (Piece value : items.values()) {
                if (value != null){
                    if (value.getSide().equals("White")){
                        value.updateOkToSet();
                        int n = value.getLegalplace().size();
                        System.out.println(value + "," + value.getImageViewId()+","+n);
                        for (int i = 0; i < n;i++){
                            //simulate a step
                            int x = value.getX();
                            int y = value.getY();
                            cells.get(value.getX()+""+value.getY()).setCurrentpiece(null);
                            cells.get(value.getX()+""+value.getY()).setHasPiece(false);

                            Piece tmp = null;
                            if (value.getLegalplace().get(i).getHasPiece()){
                                tmp = value.getLegalplace().get(i).getCurrentpiece();
                                items.remove(tmp);
                            }

                            value.getLegalplace().get(i).setHasPiece(true);
                            value.getLegalplace().get(i).setCurrentpiece(value);

                            //System.out.println(value.getLegalplace().get(i).toString());

                            value.setX(value.getLegalplace().get(i).getX());
                            value.setY(value.getLegalplace().get(i).getY());


                            //check if king is checked


                            if (!white_king.IsBeChecked_withoutrecover()){
                                System.out.println("has 1");
                                flag = 1;
                            }

                            //recovery

                            value.setX(x);
                            value.setY(y);
                            cells.get(value.getX()+""+value.getY()).setCurrentpiece(value);
                            cells.get(value.getX()+""+value.getY()).setHasPiece(true);

                            if (tmp != null){
                                value.getLegalplace().get(i).setCurrentpiece(tmp);
                                items.put(tmp.getImageViewId(),tmp);
                            }else {
                                if (value.getLegalplace().get(i) == null){
                                    System.out.println("null");
                                }
                                value.getLegalplace().get(i).setHasPiece(false);
                                value.getLegalplace().get(i).setCurrentpiece(null);
                            }
                        }


                        for (Piece value1 : items.values()){
                            if (value != null && value1 != white_king){
                                if (!value1.getSide().equals(white_king.getSide())){
                                    value1.getLegalplace().clear();
                                }
                            }
                        }
                        value.getLegalplace().clear();
                    }
                }
            }

            if (flag == 1){
                return "no";
            }else {
                return "black";
            }
        }

        return "no";
        
    }


    public void oneStep(){

        /**
         * For simplify, just randomly choose a piece and a movement
         */
        System.out.println("Count"+count);

        if (this.count < 24){
            italy();
            return;
        }else if ((this.side.equals("white") && this.count >= 23) || (this.side.equals("black") && this.count >= 25)){
            int index = 0;
            if (side.equals("white")){
                index = 16;
            }

            Random ran = new Random();

            //0~15
            int apiece = ran.nextInt(16);
            String nodeId = (index + apiece) + "";
            int flag1 = 0;
            while (flag1 == 0) {
                if (items.get(nodeId) != null) {
                    items.get(nodeId).updateOkToSet();
                    if (!items.get(nodeId).getLegalplace().isEmpty()) {
                        flag1 = 1;
                    } else {
                        items.get(nodeId).getLegalplace().clear();
                        apiece = ran.nextInt(16);
                        nodeId = (index + apiece) + "";
                        System.out.println(nodeId + "test");

                    }
                } else {
                    apiece = ran.nextInt(16);
                    nodeId = (index + apiece) + "";
                }
            }

            System.out.println("Character: "+ items.get(nodeId).getCharacter());
            System.out.println("location: "+ items.get(nodeId).getX()+"."+ items.get(nodeId).getY());
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++){
                    if (items.get(nodeId).getLegalplace().contains(cell[i][j])){
                        System.out.println("Can: "+ i+ ","+j);
                    }
                }

            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    if (items.get(nodeId).getLegalplace().contains(cell[i][j])) {
                        System.out.println(i + "," + j);
                        ImageView piece = (ImageView) items.get(nodeId).getNode();
                        Pane formerPane = (Pane) piece.getParent();
                        formerPane.getChildren().remove(piece);
                        cells.get(items.get(nodeId).getX() + "" + items.get(nodeId).getY()).setCurrentpiece(null);
                        cells.get(items.get(nodeId).getX() + "" + items.get(nodeId).getY()).setHasPiece(false);



                        //output.print(items.get(nodeId).getCharacter()+items.get(nodeId).getLocation());

                        Boolean flag = true;
                        try {
                            cell[i][j].getCellPane().getChildren().get(1);
                        } catch (IndexOutOfBoundsException ex) {
                            flag = false;
                        }

                        if (flag) {
                            items.remove(cell[i][j].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                            cell[i][j].getCellPane().getChildren().remove(1);
                        }

                        cell[i][j].getCellPane().getChildren().add(piece);

                        cell[i][j].setHasPiece(true);
                        cell[i][j].setCurrentpiece(items.get(nodeId));
                        items.get(nodeId).CountPlusOne();
                        items.get(nodeId).setX(i);
                        items.get(nodeId).setY(j);
                        //piece.relocate(0, 0);


                        /*
                        if (flag){
                            output.println("x"+items.get(nodeId).getLocation());
                        }else {
                            output.println("-"+items.get(nodeId).getLocation());
                        }
                        */
                        if (this.getSide().equals("white")){
                            output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
                        }else {
                            output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
                        }

                        items.get(nodeId).normalOkToSet();

                        items.get(nodeId).setCurrentPane((Pane) piece.getParent());

                        //this.launcher.setPlayer("ai");
                        break;
                    }
                }

            this.plusone();
        }

    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getSide() {
        return side;
    }

    public void plusone(){
        this.count += 1;
    }

    public void italy(){
        //实现意大利开局
        String nodeId = "";


        if (this.count == 0){


            nodeId = "28";
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[6][4].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);

            //output.print(items.get(nodeId).getCharacter()+items.get(nodeId).getLocation());
            Boolean flag = true;
            try{
                cell[4][4].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[4][4].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[4][4].getCellPane().getChildren().remove(1);
            }

            cell[4][4].getCellPane().getChildren().add(piece);

            cell[4][4].setHasPiece(true);
            cell[4][4].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(4);
            items.get(nodeId).setY(4);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        }else if (this.count == 2){
            nodeId = "22";
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[7][6].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);

            //output.print(items.get(nodeId).getCharacter()+items.get(nodeId).getLocation());
            Boolean flag = true;
            try{
                cell[5][5].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[5][5].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[5][5].getCellPane().getChildren().remove(1);
            }
            cell[5][5].getCellPane().getChildren().add(piece);

            cell[5][5].setHasPiece(true);
            cell[5][5].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(5);
            items.get(nodeId).setY(5);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        } else if (this.count == 4){
            nodeId = "21";
            int x = 4;
            int y = 2;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[7][5].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);


            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        } else if (this.count == 6){
            nodeId = "26";
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[6][2].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);



            Boolean flag = true;
            try{
                cell[5][2].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[5][2].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[5][2].getCellPane().getChildren().remove(1);
            }
            cell[5][2].getCellPane().getChildren().add(piece);

            cell[5][2].setHasPiece(true);
            cell[5][2].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(5);
            items.get(nodeId).setY(2);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        } else if (this.count == 8){
            nodeId = "27";
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[6][3].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);


            Boolean flag = true;
            try{
                cell[4][3].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[4][3].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[4][3].getCellPane().getChildren().remove(1);
            }


            cell[4][3].getCellPane().getChildren().add(piece);

            cell[4][3].setHasPiece(true);
            cell[4][3].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(4);
            items.get(nodeId).setY(3);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        }else if (this.count == 10){
            nodeId = "26";
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[5][2].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);

            Boolean flag = true;
            try{
                cell[4][3].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[4][3].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[4][3].getCellPane().getChildren().remove(1);

            }

            cell[4][3].getCellPane().getChildren().add(piece);

            cell[4][3].setHasPiece(true);
            cell[4][3].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(4);
            items.get(nodeId).setY(3);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        } else if (this.count == 12){
            nodeId = "18";
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[7][2].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);


            Boolean flag = true;
            try{
                cell[6][3].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[6][3].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[6][3].getCellPane().getChildren().remove(1);
            }

            cell[6][3].getCellPane().getChildren().add(piece);

            cell[6][3].setHasPiece(true);
            cell[6][3].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(6);
            items.get(nodeId).setY(3);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        } else if (this.count == 14){
            nodeId = "17";
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[7][1].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);

            Boolean flag = true;
            try{
                cell[6][3].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[6][3].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[6][3].getCellPane().getChildren().remove(1);
            }

            cell[6][3].getCellPane().getChildren().add(piece);

            cell[6][3].setHasPiece(true);
            cell[6][3].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(6);
            items.get(nodeId).setY(3);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        }else if (this.count == 16){
            nodeId = "28";
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[4][4].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);


            Boolean flag = true;
            try{
                cell[3][3].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[3][3].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[3][3].getCellPane().getChildren().remove(1);
            }

            cell[3][3].getCellPane().getChildren().add(piece);

            cell[3][3].setHasPiece(true);
            cell[3][3].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(3);
            items.get(nodeId).setY(3);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        }else if (this.count == 18){
            nodeId = "19";
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[7][3].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);



            Boolean flag = true;
            try{
                cell[5][1].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[5][1].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[5][1].getCellPane().getChildren().remove(1);
            }

            cell[5][1].getCellPane().getChildren().add(piece);

            cell[5][1].setHasPiece(true);
            cell[5][1].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(5);
            items.get(nodeId).setY(1);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        } else if (this.count == 20){
            //王车易位

            //output.print("Castling: ");
            nodeId = "20";
            int x = 7;
            int y = 6;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[7][4].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);


            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);


            if (this.getSide().equals("white")){
                this.getOutput().print((count/2+1)+"."+"Castling: "+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                this.getOutput().println("-"+"Castling: "+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());


            nodeId = "23";
            x = 7;
            y = 5;
            piece = (ImageView) items.get(nodeId).getNode();
            formerPane = cell[7][7].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);


            //output.print(items.get(nodeId).getCharacter()+items.get(nodeId).getLocation());
            flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);



            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        }else if (this.count == 22){

            nodeId = "23";
            int x = 7;
            int y = 4;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[7][5].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);


            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        }
        if (this.count == 1){
            nodeId = "12";
            int x = 3;
            int y = 4;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[1][4].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);



            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        }else if (this.count == 3){
            nodeId = "1";
            int x = 2;
            int y = 2;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[0][1].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);

            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        } else if (this.count == 5){
            nodeId = "5";
            int x = 3;
            int y = 2;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[0][5].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);



            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        } else if (this.count == 7){
            nodeId = "6";
            int x = 2;
            int y = 5;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[1][4].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);



            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        } else if (this.count == 9){
            nodeId = "12";
            int x = 4;
            int y = 3;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[3][4].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);


            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        }else if (this.count == 11){
            nodeId = "5";
            int x = 4;
            int y = 1;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[3][2].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);



            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        } else if (this.count == 13){
            nodeId = "5";
            int x = 6;
            int y = 3;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[4][1].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);


            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        } else if (this.count == 15){
            nodeId = "11";
            int x = 3;
            int y = 3;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[1][3].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);


            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        }else if (this.count == 17){
            nodeId = "6";
            int x = 3;
            int y = 3;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[2][5].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);

            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        }else if (this.count == 19){
            nodeId = "1";
            int x = 1;
            int y = 4;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[2][2].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);


            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);

            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        } else if (this.count == 21){
            //王车易位

            nodeId = "4";
            int x = 0;
            int y = 6;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[0][4].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);


            //output.print(items.get(nodeId).getCharacter()+items.get(nodeId).getLocation());
            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);

            if (this.getSide().equals("white")){
                this.getOutput().print((count/2+1)+"."+"Castling: "+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                this.getOutput().println("-"+"Castling: "+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }
            items.get(nodeId).setCurrentPane((Pane)piece.getParent());


            nodeId = "7";
            x = 0;
            y = 5;
            piece = (ImageView) items.get(nodeId).getNode();
            formerPane = cell[0][7].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);

            //output.print(items.get(nodeId).getCharacter()+items.get(nodeId).getLocation());
            flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        }else if (this.count == 23){
            nodeId = "10";
            int x = 3;
            int y = 2;
            ImageView piece = (ImageView) items.get(nodeId).getNode();
            Pane formerPane = cell[1][2].getCellPane();
            formerPane.getChildren().remove(piece);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setCurrentpiece(null);
            cells.get(items.get(nodeId).getX()+""+items.get(nodeId).getY()).setHasPiece(false);



            Boolean flag = true;
            try{
                cell[x][y].getCellPane().getChildren().get(1);
            }
            catch (IndexOutOfBoundsException ex){
                flag = false;
            }

            if (flag)
            {
                items.remove(cell[x][y].getCellPane().getChildren().get(1).getId());//remove this piece from the map
                cell[x][y].getCellPane().getChildren().remove(1);
            }

            cell[x][y].getCellPane().getChildren().add(piece);

            cell[x][y].setHasPiece(true);
            cell[x][y].setCurrentpiece(items.get(nodeId));
            items.get(nodeId).CountPlusOne();
            items.get(nodeId).setX(x);
            items.get(nodeId).setY(y);
            if (this.getSide().equals("white")){
                output.print((count/2+1)+"."+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }else {
                output.println("-"+items.get(nodeId).getShort()+items.get(nodeId).getLocation());
            }

            items.get(nodeId).setCurrentPane((Pane)piece.getParent());

            this.plusone();
            return;
        }
    }

    public PrintWriter getOutput() {
        return output;
    }

    public int getCount() {
        return count;
    }
}
