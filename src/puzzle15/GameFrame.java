package puzzle15;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
	private int frameSizeY = 712; //フレームの縦サイズ
	//private String gara = "ぷにる"; //絵柄
	private Integer imageNo = 0; //画像番号
	private HashMap<String, Integer> imgMap = new HashMap<>(){{
	    put("すうじ", 0);
	    put("ぷにる", 1);
	}};// 画像番号の選択肢
	
	private static int gameFlg; //ゲーム状態フラグ
	private static GridInfo GInfo = new GridInfo(GRID_X, GRID_Y);; //グリッドクラス
	private static ImageIcon tileImage[] = new ImageIcon[GRID_X * GRID_Y + 1];; //マスの画像を保存する配列（ぷにる）
	private static JLabel label[] = new JLabel[GRID_X * GRID_Y + 1]; ; //マスの画像を貼り付けるラベルの配列（ぷにる）
	//private static ImageIcon tileImage2[]; //マスの画像を保存する配列(数字）
	//private static JLabel label2[]; //マスの画像を貼り付けるラベルの配列（数字）
	
	
	
	//コンストラクタ
	GameFrame(){
		//マスの表示
		imgReadDisp(imageNo);
		
		//マウスイベントの取得を開始
		this.getContentPane().addMouseListener(this);
		this.setTitle("Puzzle15");
		this.setSize(frameSizeX, frameSizeY);
		
		//スタートボタンの設定
		JButton button = new JButton("すたーと");
		button.setBounds(200, 10, 100, 50);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imgReadDisp(imageNo);
				System.out.println("スタートボタンを押しました。"+imageNo);
				GInfo.shfleTile();
			}
		});
		this.getContentPane().add(button); 
		
		//プルダウンの設定
		// プルダウンメニュー（JComboBox）の作成
        JComboBox<String> comboBox = new JComboBox<>(imgMap.keySet().toArray(new String[0]));  // 選択肢を渡してプルダウンを作成
        comboBox.setBounds(10, 10, 150, 30);  // プルダウンの位置とサイズを設定

        // プルダウンの選択が変わったときの動作を設定
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Integer selectedNo = (Integer) comboBox.getSelectedItem();  // 選択された項目を取得
                String selectedLabel = (String) comboBox.getSelectedItem(); // 表示用文字列を取得
                Integer selectedId = imgMap.get(selectedLabel); 
                imageNo = selectedId;
                System.out.println("選択された項目: " + imageNo);
            }
        });
        // フレームにプルダウンを追加
        this.setLayout(null);  // 自由に配置できるようにレイアウトを無効化
        this.add(comboBox);  // プルダウンを追加
	}
	
	//ゲーム初期化メソッド
	public void gameInit() {
		//GInfo.shfleTile();
		gameFlg = GAME_ING;
	}
	//画像を読みこんで表示
	public void imgReadDisp(Integer imageNo) {
		
		// 1~15までのラベルをクリア（対象のラベルのみ）
	    for (int i = 1; i < GRID_X * GRID_Y; i++) {
	        if (label[i] != null) {
	            this.getContentPane().remove(label[i]); // ラベルを削除
	            label[i] = null; // 配列もクリア
	        }
	    }
	    
		//1~15までのコマの画像を読み込み
		DecimalFormat decimalFormat = new DecimalFormat("00");
		for(int i = 1; i<GRID_X * GRID_Y; i++) {
			tileImage[i] = new ImageIcon("img"+imageNo+"/"+decimalFormat.format(i)+".png");
			label[i] = new JLabel(tileImage[i]);
			this.getContentPane().add(label[i]); //フレームにラベルを追加
		}
		
		//1~15までのコマをボード上に配置
		for(int y=0; y<GRID_Y; y++) {
			for(int x=0; x<GRID_X; x++) {
				if(GInfo.getTileNum(x, y) != 0) {
					label[GInfo.getTileNum(x, y)].setBounds(x*GRID_WIDTH, y*GRID_HEIGTH+100, GRID_WIDTH, GRID_HEIGTH);
				}
			}
		}
		//描写
		for(int y=0; y<GRID_Y; y++) {
			for(int x=0; x<GRID_X; x++) {
				if(GInfo.getTileNum(x, y) != 0) {
					label[GInfo.getTileNum(x, y)].setBounds(x*GRID_WIDTH, y*GRID_HEIGTH+100, GRID_WIDTH, GRID_HEIGTH);
				}
			}
		}
		// コンポーネントを再描画
	    this.getContentPane().revalidate();
	    this.getContentPane().repaint();
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
				clickTileY = (int)((e.getY()-100)/ GRID_HEIGTH);
				System.out.println(clickTileX +clickTileY );
				//コマを移動させる。
				blnRet = GInfo.moveTile(clickTileX, clickTileY);
				break;
		}
		//描写
		for(int y=0; y<GRID_Y; y++) {
			for(int x=0; x<GRID_X; x++) {
				if(GInfo.getTileNum(x, y) != 0) {
					label[GInfo.getTileNum(x, y)].setBounds(x*GRID_WIDTH, y*GRID_HEIGTH+100, GRID_WIDTH, GRID_HEIGTH);
				}
			}
		}
		this.setVisible(true);
		
	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	

	
	
}