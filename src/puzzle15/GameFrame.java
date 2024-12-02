package puzzle15;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

//画面表示に関連するクラス
public class GameFrame extends JFrame implements MouseListener{

	public static final int GRID_X = 4; //ボードの横マス数
	public static final int GRID_Y = 4; //ボードの縦マス数
	private static final int GRID_WIDTH = 128; //マスの横幅
	private static final int GRID_HEIGTH = 128; //マスの縦幅
	private static final int FRAME_SIZE_X = 512; //フレームの横サイズ
	private static final int FRAME_SIZE_Y = 712; //フレームの縦サイズ
	
	private Integer imageNo; //選択された画像番号
	private DefaultComboBoxModel<String> comboBoxModel; //プルダウンの処理に使用
	private JComboBox<String> comboBox; //プルダウン
    private String filePath = "pulldownData.txt";// プルダウンの中身のデータファイルパス
    public static String selectedLabel; //現在選択しているプルダウンの表示
    public static ArrayList<String> imgMap = new ArrayList<>();//プルダウンの選択肢.他クラスでも使用。
    public static LinkedHashMap<String, Integer> imgMap2 = new LinkedHashMap<>();
	private static GridInfo GInfo = new GridInfo(GRID_X, GRID_Y);; //グリッドクラス
	private static ImageIcon tileImage[] = new ImageIcon[GRID_X * GRID_Y + 1];; //マスの画像を保存する配列
	private static JLabel label[] = new JLabel[GRID_X * GRID_Y + 1]; ; //マスの画像を貼り付けるラベルの配列
	private NewImage newImage = new NewImage(); //新規画像登録のクラスをインスタンス化
	private boolean skipComboboxUpdateFlg = false; //プルダウンの中身を更新をスキップするフラグ。

	//コンストラクタ
	GameFrame(){
		
		//フレームの設定
		this.getContentPane().addMouseListener(this);//マウスイベント取得開始
		this.setTitle("Puzzle15");
		this.setSize(FRAME_SIZE_X, FRAME_SIZE_Y);
		this.getContentPane().setLayout(null);//レイアウトマネージャが無効となるので、位置やサイズの指定を行う必要あり。
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//閉じ方の設定
		
		//プルダウンの設定
		readDatePdw();//プルダウンのデータ読み込み
		comboBoxModel = new DefaultComboBoxModel<>(imgMap2.keySet().toArray(new String[0]));// プルダウンメニュー（JComboBox）の作成。imgMap変更時にプルダウンの内容を変更するためにDefaultComboBoxModelを使用する。
		comboBox = new JComboBox<>(comboBoxModel); // モデルをセットしてプルダウン作成
        comboBox.setBounds(10, 10, 150, 50);  // プルダウンの位置とサイズを設定
        ////プルダウンの選択が変わったときの動作を設定
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	System.out.println("プルダウンの選択が変更されました。");
            	if(!skipComboboxUpdateFlg) {//画像保存の時プルダウンの更新をするため選択が変更となるが、その時表示されているものに画像が変わると登録した画像が表示されなくなるため。
            		System.out.println("画像保存ないので変更されたラベルに画像を変更します。");
	            	selectedLabel = (String) comboBox.getSelectedItem(); // 表示用文字列を取得
	            	imageNo = Integer.valueOf(imgMap2.get(selectedLabel)); //選択されたプルダウンの番号取得
	                GInfo.clearTile(); //マスの位置の初期化
					imgReadDisp(imageNo); //マスの表示
            	}
            }
        });
        this.add(comboBox);  // プルダウンを追加
		        
		//マスの表示
		selectedLabel = GameFrame.imgMap2.keySet().iterator().next(); //最初の要素を初期の画像とする。
		imgReadDisp(Integer.valueOf(imgMap2.get(selectedLabel))); //マスの表示
		
		//スタートボタンの設定
		JButton button = new JButton("すたーと");
		button.setBounds(200, 10, 100, 50);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GInfo.shfleTile(); //マスの位置をシャッフル
				imgReadDisp(imageNo); //マスの表示
				System.out.println("スタートボタンを押しました。"+imageNo);
			}
		});
		this.getContentPane().add(button); 
		
		//クリアボタンの設定
		JButton button2 = new JButton("くりあ");
		button2.setBounds(350, 10, 100, 50);
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GInfo.clearTile(); //マスの位置を初期化
				imgReadDisp(imageNo); //マスの表示
			}
		});
		this.getContentPane().add(button2); 
		
		//絵の新規登録ボタンの設定
		JButton button3 = new JButton("あたらしいえをふやす");
		button3.setBounds(10, 620, 200, 50);
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				newImage.dialog(); //画像選択ダイアログの表示
				
				//画像を選択された時のみ処理実施
				if(newImage.selectedFile != null) {
					newImage.dispImg(newImage.selectedFile); //別のフレームの画像を表示
					
					//コールバックを設定。分割画像を保存したら以下処理を実行する。
					newImage.setComp(() -> {
						skipComboboxUpdateFlg=true;
						System.out.println("新規画像にマスの表示を変更します。");
						GInfo.clearTile(); //マスの位置の初期化
						imgReadDisp(Integer.valueOf(imgMap2.get(selectedLabel))); //マスの表示
						updateComboBox(); //プルダウンの表示の更新
						skipComboboxUpdateFlg=false;
					});
				}
			}
		});
		this.getContentPane().add(button3); 
		
		//絵の削除ボタンの設定
		JButton button4 = new JButton("このえをさくじょする");
		button4.setBounds(250, 620, 200, 50);
		button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				skipComboboxUpdateFlg = true;
				selectedLabel = (String) comboBox.getSelectedItem(); // 表示用文字列を取得
				imageNo = Integer.valueOf(imgMap2.get(selectedLabel)); 
				imgMap2.remove(GameFrame.selectedLabel); //プルダウンの選択肢の削除
				newImage.deleteImg(imageNo);//画像の削除
				selectedLabel = GameFrame.imgMap2.keySet().iterator().next(); //最初の要素を表示する。
				GInfo.clearTile(); //マスの位置の初期化
				imgReadDisp(Integer.valueOf(imgMap2.get(selectedLabel))); //マスの表示
				updateComboBox(); //プルダウンの表示の更新
				skipComboboxUpdateFlg = false; //画像保存時のプルダウンの処理を分けているので、保存が終わったらフラグを落とす。
				
			}
		});
		this.getContentPane().add(button4); 
	}
	
	//プルダウンのデータの読み込みメソッド
	public void readDatePdw() {
		System.out.println("プルダウンのデータを読み込みます");
		// ファイルを読み込み、データをリストに追加
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
            		imgMap2.put(line.split(",")[0], Integer.valueOf(line.split(",")[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	//プルダウンの内容を更新するメソッド
	public void updateComboBox() {
	    comboBoxModel.removeAllElements(); // 既存の項目を削除
	    for (String key : imgMap2.keySet()) {
	        comboBoxModel.addElement(key); // 新しい項目を追加
	    }
	    comboBoxModel.setSelectedItem(selectedLabel);
	    
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
			//png,jpgに対応
			String basePath = "img/img" + imageNo + "/" + decimalFormat.format(i);
            File pngFile = new File(basePath + ".png");
            File jpgFile = new File(basePath + ".jpg");
            if (pngFile.exists()) {
                tileImage[i] = new ImageIcon(pngFile.getPath());
            } else if (jpgFile.exists()) {
                tileImage[i] = new ImageIcon(jpgFile.getPath());
            }
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
		
		// コンポーネントを再描画
	    this.getContentPane().revalidate();
	    this.getContentPane().repaint();
	}
	
	//クリックイベントの設定
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		
		int clickTileX;
		int clickTileY;
		
		//クリックしたマスを取得
		clickTileX = (int)(e.getX() / GRID_WIDTH);
		clickTileY = (int)((e.getY()-100)/ GRID_HEIGTH);
		
		//コマを移動させる。
		GInfo.moveTile(clickTileX, clickTileY);
		
		//描写
		for(int y=0; y<GRID_Y; y++) {
			for(int x=0; x<GRID_X; x++) {
				if(GInfo.getTileNum(x, y) != 0) {
					label[GInfo.getTileNum(x, y)].setBounds(x*GRID_WIDTH, y*GRID_HEIGTH+100, GRID_WIDTH, GRID_HEIGTH);
				}
			}
		}
	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
}