import java.awt.*;
import java.awt.event.*;

import javax.xml.xpath.XPath;

public class Player {
    int x,y;
    Color color;


    Player(int x,int y, Color color){
        this.x = x;             //プレイヤーの初期値(横)
        this.y = y;             //プレイヤーの初期値(縦)
        this.color = color;     //プレイヤーの初期値(色)
    }

    public boolean update(int x, int y, Player player,Cell[][] field){  //プレイヤー位置更新
        if(Math.abs(player.x - this.x) == 1 && player.y == this.y || Math.abs(player.y - this.y) == 1 && player.x == this.x){   //敵プレイヤーが隣接している時の移動条件
            if(check(x, y, player, field) || player.check(x, y, this,field)){       //移動判定
                this.x = x;
                this.y = y;
                return true;                //移動可能
            }
            else{
                return false;               //移動不可
            }
        }
        else{
            if(check(x,y,player,field)){    //移動判定
                this.x = x;
                this.y = y;
                return true;                //移動可能
            }
            else{
                return false;               //移動不可
            }
        }
    }
    public boolean check(int x, int y, Player player,Cell[][] field){
        if(Math.abs(x - this.x) == 1 && y == this.y || Math.abs(y - this.y) == 1 && x == this.x){   //プレイヤーの移動条件
            if(!(player.x == x && player.y == y || x == this.x && y == this.y)){    //プレイヤーの移動条件（敵プレイヤーの位置をクリックした場合）
                if(y < this.y && field[this.y][this.x].top){        //壁がある場合
                    return false;                                   //移動不可
                }
                if(x < this.x && field[this.y][this.x].left){       //壁がある場合
                    return false;                                   //移動不可
                }
                if(y > this.y && field[this.y + 1][this.x].top){    //壁がある場合
                    return false;                                   //移動不可
                }
                if(x > this.x && field[this.y][this.x + 1].left){   //壁がある場合
                    return false;                                   //移動不可
                }
                return true;                                        //移動可能
            }else{
                return false;                                       //移動不可
            }
        }
        else{
            return false;                                           //移動不可
        }
    }

    public void paint(Graphics g, int tilesize) {
        g.setColor(color);                                                      //自機の色指定
        g.fillOval(x*tilesize+5, y*tilesize+5, tilesize-10, tilesize-10);       //自機の描画
    }
}
