import java.awt.*;
import java.awt.event.*;

public class GameMaster extends Canvas implements MouseListener{

    int imgW;   //横
    int imgH;   //縦
    int tilesize;   //ひとマスのサイズ
    Player player1, player2;    //プレイヤー１、２の宣言
    final int TOP = 1;  //マスの上
    final int LEFT = 2; //ますの左
    final int P1 = 11;  //定数
    final int P2 = 12;  //定数
    int mode = P1;  //ターン制御
    Graphics buf_gc = null;
    Image buf = null;   // 仮の画用紙
    Dimension d; // キャンバスの大きさ取得用
    Cell[][] field = new Cell[10][10];  //ますの作成
    boolean turn = true;    //ターン制御
    Font font1 = new Font("HGP創英角ﾎﾟｯﾌﾟ体",Font.ITALIC,30); //字体の変更

    GameMaster(int w,int h,int tilesize){
        this.tilesize = tilesize;   //一マスのサイズ
        imgW = w;   //一マスの横の長さ
        imgH = h;   //一マスの縦の長さ
        setSize(w,h);   //画面サイズ
        addMouseListener(this); //マウス制御
        player1 = new Player(4,0,Color.red);    //プレイヤー1の生成
        player2 = new Player(4,8,Color.blue);   //プレイヤー2の生成
        resetField();   //再描画
    }
    public void update(Graphics g) {
        paint(g); // 下記の paint を呼び出す
    }

    public void resetField(){   //フィルードの生成
        field = new Cell[10][10];   //フィールドの生成
        for(int i = 0; i < 10; i++){    //10回繰り返す
            for(int j = 0; j < 10; j++){    //10回繰り返す
                field[i][j] = new Cell();   //セルの生成
            }
        }
        for(int i = 0; i < 10; i++){    //10改繰り返す
            field[0][i].top = true;     //セルの上枠の生成
            field[i][0].left = true;    //セルの左枠の生成
            field[9][i].top = true;     //セルの上枠の生成
            field[i][9].left = true;    //セルの左枠の生成
        }
    }

    public double distance(int x1, int y1, int x2, int y2){     //距離を返すメソッド
        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        return d;
    }

    public boolean canSetWall(int mode, int x, int y, Cell[][] cells, boolean[][] route){   //壁の設置判定
        if(route == null){      
            route = new boolean[9][9];      //プレイヤーの移動可能領域
            for(int i = 0;i < 9; i++){      
                for(int j = 0;j < 9; j++){  
                    route[i][j] = false;    //初期状態
                }
            }
        }
        route[y][x] = true;     //ｘ、ｙをtrueに
        if(!cells[y][x].top && y > 0 && !route[y - 1][x]){      //壁の有無を判定
            canSetWall(mode, x, y-1, cells, route);             //再帰よびだし
        }

        if(!cells[y + 1][x].top && y < 9 && !route[y + 1][x]){  //壁の有無を判定
            canSetWall(mode, x, y+1, cells, route);             //再帰よびだし
        }

        if(!cells[y][x].left && x > 0 && !route[y][x - 1]){     //壁の有無を判定
            canSetWall(mode, x-1, y, cells, route);             //再帰よびだし
        }

        if(!cells[y][x + 1].left && x < 9 && !route[y][x + 1]){ //壁の有無を判定
            canSetWall(mode, x+1, y, cells, route);             //再帰よびだし
        }
        if(mode == P1){     //プレイヤー1の時
            for (int i = 0; i < 9; i++){
                if(route[8][i]){
                    return true;    //設置可能
                }
            }
            return false;           //設置不可
        }
        else{
            for (int i = 0; i < 9; i++){
                if(route[0][i]){
                    return true;    //設置可能
                }
            }
            return false;           //設置不可
        }
    }


    public void paint(Graphics g) {
        d = getSize();   // キャンバスのサイズを取得
        buf_gc.setColor(new Color(136,69,19));  //色指定
        buf_gc.fillRect(0, 0, (int)d.getWidth(), (int)d.getHeight());   //背景の生成
        Graphics2D g2 = (Graphics2D)buf_gc; //(下の行含めて)線を太くしている。
        g2.setStroke(new BasicStroke(5));
        buf_gc.setColor(Color.black);   //色指定
        for(int i=0; i<10 ;i++){    //10回繰り返す
            buf_gc.drawLine(0, i*tilesize, imgW, i*tilesize);   //線を描く
        }
        for(int i=0; i<10 ;i++){    //10回繰り返す
            buf_gc.drawLine(i*tilesize, 0, i*tilesize, imgH);   //線を描く
        }
        player1.paint(buf_gc, tilesize);    //自機1(赤)の描画
        player2.paint(buf_gc, tilesize);    //自機2(青)の描画
        for(int i = 0;i < 10 ; i++){    //壁の生成の判定
            for(int j = 0;j < 10 ; j++){
                if(field[i][j].top == true){    //壁の生成条件
                    buf_gc.setColor(Color.yellow);  //色指定
                    buf_gc.drawLine(j*tilesize, i*tilesize, (j + 1)*tilesize, i*tilesize);  //線を描く
                }

                if(field[i][j].left == true){   //壁の生成条件
                    buf_gc.setColor(Color.yellow);  //色指定
                    buf_gc.drawLine(j*tilesize, i*tilesize, j*tilesize, (i + 1)*tilesize);  //線を描く
                }
            }
        }
        if(turn == true){   //ターンの表記
            if(mode == P1){     //自機1(赤)のターン
                buf_gc.setColor(Color.red);     //色指定
                buf_gc.drawString("赤のターン", imgW/2-70, imgH/2+10);  //表示文字、位置指定
                buf_gc.setFont(font1);  //フォント指定
            }
            else{   //自機2(青)のターン
                buf_gc.setColor(Color.blue);    //色指定
                buf_gc.drawString("青のターン", imgW/2-70, imgH/2+10);  //表示文字、位置指定
                buf_gc.setFont(font1);  //フォント指定
            }
        }
        g.drawImage(buf, 0, 0, this);     //仮の画用紙を貼り付け
    }

    public void addNotify(){
        super.addNotify();
        buf = createImage(imgW, imgH); // buffer を画面と同サイズで作成
        buf_gc = buf.getGraphics();
    }




    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        if(turn){           //ターン表示を消す
            turn = false;   
            repaint();      //再描画
            return;
        }
        if(e.getButton() == 1){         //左クリック
            int x,y;                    //マス
            x = e.getX()/tilesize;      //横
            y = e.getY()/tilesize;      //縦
            switch (mode){      //ターン制御
                case P1:        //プレイヤー1のターン
                    if(player1.update(x,y,player2,field)){      //プレイヤーの位置更新
                        mode = P2;      //プレイヤーのターンを変更
                        turn = true;
                    }
                    break;
                case P2:
                    if(player2.update(x,y,player1,field)){      //プレイヤーの位置更新
                        mode = P1;      //プレイヤーのターンを変更
                        turn = true;
                    }
                    break;
            }
        }
        repaint();      //再描画

        if(e.getButton() == 3){     //右クリック
            int x,y;                //押された座標
            x = e.getX();           //横
            y = e.getY();           //縦
            int fieldx = 0;         //壁を立てるマスの座標
            int fieldy = 0;         //壁を立てるマスの座標
            double dist = Double.POSITIVE_INFINITY;     //距離
            int dir = 0;    //方向
            for(int i = 0; i < 10; i++){        //押された座標から最も近い壁の選択
                for(int j = 0; j < 10; j++){
                double newDist = distance(tilesize * j + tilesize / 2,tilesize*i,x,y);  //上壁との距離
                    if(newDist < dist){     //距離の比較
                        fieldx = j;         //壁を立てるマスの座標の決定
                        fieldy = i;         //壁を立てるマスの座標の決定
                        dir = TOP;          //上壁
                        dist = newDist;     //距離の更新
                    }
                newDist = distance(tilesize * j,tilesize*i+ tilesize / 2,x,y);          //左壁との距離
                    if(newDist < dist){     //距離の比較
                        fieldx = j;         //壁を立てるマスの座標の決定
                        fieldy = i;         //壁を立てるマスの座標の決定
                        dir = LEFT;         //上壁
                        dist = newDist;     //距離の更新
                    }
                }
            }
            // System.out.println(fieldx + "");
            // System.out.println(fieldy + "");
            // System.out.println(dir + "");
            Cell[][] clone = new Cell[10][10];  //クローンの宣言
            for(int i = 0; i < 10; i++){
                for(int j = 0; j < 10; j++){
                    clone[i][j] = field[i][j].clone();  //クローンの作成
                }
            }
            boolean flag = false;   //falseで壁設置可能、trueで設置不可
            switch (dir) {
                case TOP:   //上壁について
                if(clone[fieldy][fieldx].top == false && clone[fieldy][fieldx + 1].top == false && (clone[fieldy][fieldx + 1].left == false || clone[fieldy - 1][fieldx + 1].left == false)){   //壁の設置判定
                    clone[fieldy][fieldx].top = true;           //クローンで壁設置
                    clone[fieldy][fieldx + 1].top = true;       //クローンで隣の壁設置
                    break;
                }
                else{
                    flag = true;    //壁設置不可
                }

            
                case LEFT:  //左壁について
                if(clone[fieldy][fieldx].left == false &&clone[fieldy + 1][fieldx].left == false && (clone[fieldy + 1][fieldx - 1].top == false || clone[fieldy + 1][fieldx].top == false)){    //壁の設置判定
                    clone[fieldy][fieldx].left = true;      //クローンで壁設置
                    clone[fieldy + 1][fieldx].left = true;  //クローンで隣の壁設置
                    break;
                }
                else{
                    flag = true;    //壁設置不可
                }
            }
            if(flag == false && canSetWall(P1, player1.x, player1.y, clone, null) && canSetWall(P2, player2.x, player2.y, clone, null)){    //壁の設置判定まとめ
                for(int i = 0; i < 10; i++){    
                    for(int j = 0; j < 10; j++){
                        field[i][j] = clone[i][j].clone();  //実際に壁設置
                    }
                }
                // System.out.println(mode);
                mode = (P1 + P2) - mode;    //ターン変更
                turn = true;    //ターンを表示
            }
        }


    }
    

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

}