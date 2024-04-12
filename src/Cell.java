import java.awt.*;
import java.awt.event.*;

public class Cell{
    boolean top = false;    //上壁
    boolean left = false;   //左壁

    public Cell clone(){        //クローン
        Cell to = new Cell();
        to.top = top;
        to.left = left;
        return to;
    }
}