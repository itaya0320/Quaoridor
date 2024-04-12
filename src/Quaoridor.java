import java.awt.*;
import java.awt.event.*;

public class Quaoridor extends Frame {


    int tilesize = 50;  //一マスのサイズ
    GameMaster gm;      //ゲームマスター

    public static void main(String[] args) {
        new Quaoridor(); // 自分自身のオブジェクトを作成
    }

    Quaoridor() {   //画面設定、実行
        super("Quoridor");  //ウィンドウ名称
        this.setSize(1000, 1000);   //ウィンドウのサイズ
        this.setLayout(new BorderLayout(0,0));  //画面のレイアウト
        gm = new GameMaster(tilesize*9,tilesize*9,tilesize);    //ゲームマスター
        this.add(gm,  BorderLayout.CENTER);     //レイアウトに配置
        this.setVisible(true);      //可視化
        gm.repaint();   //再描画
    }
}