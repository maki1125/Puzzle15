package puzzle15;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameFrame extends JFrame implements MouseListener{
	private static final int GAME_WAIT = 0; //ゲーム状態フラグ定数（タイトル画面時）
	private static final int GAME_ING = 1; //ゲーム状態フラグ定数（ゲーム中）
	private static final int GRID_X = 4; //ボードの横マス数
	private static final int GRID_Y = 4; //ボードの縦マス数
	
	private int GRID_WIDTH = 128; //マスの横幅
	private int GRID_HEIGTH = 128; //マスの縦幅
	private int frameSizeX = 512; //フレームの横サイズ
	private int frameSizeY = 612; //フレームの縦サイズ
	private String gara = "ぷにる"; //絵柄
	
	private static int gameFlg; //ゲーム状態フラグ
	private static GridInfo GInfo; //グリッドクラス
	private static ImageIcon tileImage1[]; //マスの画像を保存する配列（ぷにる）
	private static JLabel label1[]; //マスの画像を貼り付けるラベルの配列（ぷにる）
	private static ImageIcon tileImage2[]; //マスの画像を保存する配列(数字）
	private static JLabel label2[]; //マスの画像を貼り付けるラベルの配列（数字）
	
	//コンストラクタ
	GameFrame(){
		GInfo = new GridInfo(GRID_X, GRID_Y);
		tileImage1 = new ImageIcon[GRID_X * GRID_Y + 1]; //なぜ＋１？
		label1 = new JLabel[GRID_X * GRID_Y + 1]; //なぜ＋１？
		tileImage2 = new ImageIcon[GRID_X * GRID_Y + 1]; //なぜ＋１？
		label2 = new JLabel[GRID_X * GRID_Y + 1]; //なぜ＋１？
		
		//1~15までのコマの画像を読み込み
		DecimalFormat decimalFormat = new DecimalFormat("00");
		for(int i = 1; i<GRID_X * GRID_Y; i++) {
			tileImage1[i] = new ImageIcon("puni_img/"+decimalFormat.format(i)+".png");
			label1[i] = new JLabel(tileImage1[i]);
			this.getContentPane().add(label1[i]); //フレームにラベルを追加
		}
		for(int i = 1; i<GRID_X * GRID_Y; i++) {
			tileImage2[i] = new ImageIcon("img/"+decimalFormat.format(i)+".gif");
			label2[i] = new JLabel(tileImage2[i]);
			this.getContentPane().add(label2[i]); //フレームにラベルを追加
		}
		
		//1~15までのコマをボード上に配置
		for(int y=0; y<GRID_Y; y++) {
			for(int x=0; x<GRID_X; x++) {
				if(GInfo.getTileNum(x, y) != 0) {
					label1[GInfo.getTileNum(x, y)].setBounds(x*GRID_WIDTH, y*GRID_HEIGTH, GRID_WIDTH, GRID_HEIGTH);
				}
			}
		}
		
		//マウスイベントの取得を開始
		this.getContentPane().addMouseListener(this);
		this.setTitle("Puzzle15");
		this.setSize(frameSizeX, frameSizeY);
		
		//選択ボタンの設定
		JButton button = new JButton(gara);
		button.setBounds(10, frameSizeX+10, 100, 50);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(gara=="ぷにる") {
					gara = "すうじ";
					GRID_WIDTH = 64; //マスの横幅
					GRID_HEIGTH = 64; //マスの縦幅
					//描写
					for(int y=0; y<GRID_Y; y++) {
						for(int x=0; x<GRID_X; x++) {
							getContentPane().remove(label1[GInfo.getTileNum(x, y)]);
							if(GInfo.getTileNum(x, y) != 0) {
								label2[GInfo.getTileNum(x, y)].setBounds(x*GRID_WIDTH, y*GRID_HEIGTH, GRID_WIDTH, GRID_HEIGTH);
							}
						}
					}
				}else {
					gara = "ぷにる";
					GRID_WIDTH = 128; //マスの横幅
					GRID_HEIGTH = 128; //マスの縦幅
					//描写
					for(int y=0; y<GRID_Y; y++) {
						for(int x=0; x<GRID_X; x++) {
							if(GInfo.getTileNum(x, y) != 0) {
								label1[GInfo.getTileNum(x, y)].setBounds(x*GRID_WIDTH, y*GRID_HEIGTH, GRID_WIDTH, GRID_HEIGTH);
							}
						}
					}
				}
				button.setText(gara);
			}
			
		});
		this.getContentPane().add(button); 
	}
	//ゲーム初期化メソッド
	public void gameInit() {
		//GInfo.shfleTile();
		gameFlg = GAME_ING;
	}
	
	//クリックイベントの設定
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		int clickTileX;
		int clickTileY;
		boolean blnRet;
		
		switch(gameFlg) {
			case GAME_WAIT:
				System.out.println("GAME START");
				gameInit();
				System.out.println(GInfo.getTileNum(3, 3));
				
				break;
			case GAME_ING:
				//クリックしたマスを取得
				clickTileX = (int)(e.getX() / GRID_WIDTH);
				clickTileY = (int)(e.getY() / GRID_HEIGTH);
				System.out.println(clickTileX +clickTileY );
				//コマを移動させる。
				blnRet = GInfo.moveTile(clickTileX, clickTileY);
				break;
		}
		//描写
		for(int y=0; y<GRID_Y; y++) {
			for(int x=0; x<GRID_X; x++) {
				if(GInfo.getTileNum(x, y) != 0) {
					label1[GInfo.getTileNum(x, y)].setBounds(x*GRID_WIDTH, y*GRID_HEIGTH, GRID_WIDTH, GRID_HEIGTH);
				}
			}
		}
		this.setVisible(true);
		
	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	

	
	
}