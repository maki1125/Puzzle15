package puzzle15;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;

import javax.swing.DefaultComboBoxModel;
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
	private LinkedHashMap<String, Integer> imgMap = new LinkedHashMap<>() {};
	//{{
	    //put("すうじ", 0);
	    //put("ぷにる", 1);
	//}};// 画像番号の選択肢
	private DefaultComboBoxModel<String> comboBoxModel;
    private String filePath = "pulldownData.txt";// プルダウンの中身のデータファイルパス
	
	private static int gameFlg = GAME_ING; //ゲーム状態フラグ
	private static GridInfo GInfo = new GridInfo(GRID_X, GRID_Y);; //グリッドクラス
	private static ImageIcon tileImage[] = new ImageIcon[GRID_X * GRID_Y + 1];; //マスの画像を保存する配列（ぷにる）
	private static JLabel label[] = new JLabel[GRID_X * GRID_Y + 1]; ; //マスの画像を貼り付けるラベルの配列（ぷにる）
	//private static ImageIcon tileImage2[]; //マスの画像を保存する配列(数字）
	//private static JLabel label2[]; //マスの画像を貼り付けるラベルの配列（数字）
	
	
	
	//コンストラクタ
	GameFrame(){
		//マスの表示
		imgReadDisp(imageNo);
		
		//フレームの設定
		this.getContentPane().addMouseListener(this);//マウスイベント取得開始
		this.setTitle("Puzzle15");
		this.setSize(frameSizeX, frameSizeY);
		
		//スタートボタンの設定
		JButton button = new JButton("すたーと");
		button.setBounds(200, 10, 100, 50);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GInfo.shfleTile();
				imgReadDisp(imageNo);
				System.out.println("スタートボタンを押しました。"+imageNo);
			}
		});
		this.getContentPane().add(button); 
		
		//クリアボタンの設定
		JButton button2 = new JButton("くりあ");
		button2.setBounds(350, 10, 100, 50);
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GInfo.clearTile();
				imgReadDisp(imageNo);
			}
		});
		this.getContentPane().add(button2); 
		
		//絵の新規登録ボタンの設定
		JButton button3 = new JButton("あたらしいえをふやす");
		button3.setBounds(10, 620, 200, 30);
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageNewAdd();
			}
		});
		this.getContentPane().add(button3); 
		
		
		//プルダウンの設定
		readDatePdw();//プルダウンのデータ読み込み
		// プルダウンメニュー（JComboBox）の作成。imgMap変更時にプルダウンの内容を変更するためにDefaultComboBoxModelを使用する。
		comboBoxModel = new DefaultComboBoxModel<>(imgMap.keySet().toArray(new String[0]));
		JComboBox<String> comboBox = new JComboBox<>(comboBoxModel); // モデルをセットしてプルダウン作成
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
	
	//プルダウンのデータの読み込みメソッド
	public void readDatePdw() {
		// ファイルを読み込み、データをLinkedHashMapに追加
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // カンマで分割してキーと値を取得
                String[] parts = line.split(",", 2); // 2要素に分割
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    Integer value = Integer.parseInt(parts[1].trim());
                    imgMap.put(key, value); // LinkedHashMapに追加
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // データの確認
        for (String key : imgMap.keySet()) {
            System.out.println(key + ": " + imgMap.get(key));
        }
	}
	//プルダウンの内容を更新するメソッド
	public void updateComboBox() {
	    comboBoxModel.removeAllElements(); // 既存の項目を削除
	    for (String key : imgMap.keySet()) {
	        comboBoxModel.addElement(key); // 新しい項目を追加
	    }
	}
	//画像の新規登録の処理
	public void imageNewAdd() {
		// ファイル選択用のダイアログを作成 (モード: LOAD)
		FileDialog dialog = new FileDialog(
	            (Frame)null, // 親フレーム (nullならスタンドアロン)
	            "追加したい絵を選択してください。", // タイトル
	            FileDialog.LOAD// モード (LOADまたはSAVE)
	            );
		// ダイアログを表示
        dialog.setVisible(true);
        // 選択されたファイルのパスを取得
        String filename = dialog.getFile();// 選択されたファイル名
        String filename2= filename.substring(0, filename.lastIndexOf("."));// 選択されたファイル名);
        String directory = dialog.getDirectory(); // 選択されたディレクトリ
        //ファイルが選択された時のみ処理する
        if (filename != null){
            System.out.println(filename);
            System.out.println(directory);
            File selectedFile = new File(directory, filename);
            
            //選択した画像を保存する処理
            // コピー先のフォルダ
            int size = imgMap.size(); //現在の登録数を取得
            File destinationFolder = new File("img" + size );
            imgMap.put(filename2, size);
            // フォルダが存在しない場合は作成
            if (!destinationFolder.exists()) {
                boolean created = destinationFolder.mkdir();
                if (created) {
                    System.out.println("コピー先フォルダを作成しました: " + destinationFolder.getAbsolutePath());
                } else {
                    System.err.println("コピー先フォルダの作成に失敗しました。");
                    return;
                }
            }
            // コピー先のファイルパスを作成
            File destinationFile = new File(destinationFolder, selectedFile.getName());
            // ファイルをコピー
            try {
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("ファイルをコピーしました: " + destinationFile.getAbsolutePath());
            } catch (IOException e1) {
                System.err.println("ファイルコピー中にエラーが発生しました: " + e1.getMessage());
            }
            
            //プルダウンの内容を更新
            updateComboBox();
            
            //pulldownData.txtファイルにデータ書き込み
         // ファイルに書き込み
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String key : imgMap.keySet()) {
                    String line = key + "," + imgMap.get(key); // キーと値をカンマで結合
                    writer.write(line);
                    writer.newLine(); // 改行を追加
                }
                System.out.println("imgMapの内容を" + filePath + "に書き込みました。");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	    /* JFileChooserを利用したファイル選択ダイアログ。出てこない画像があるため上記のFileDialogを利用する。
		//ファイル選択ダイアログを作成
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("ついかしたいえをえらんでね！");//ダイアログの上の表示
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//ファイルのみ選択可能に設定
		fileChooser.setAcceptAllFileFilterUsed(true); //ファイルの種類の設定
		
		// ダイアログを表示
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            // 選択したファイルを取得
            File selectedFile = fileChooser.getSelectedFile();
            // ファイルパスを取得
            String filePath = selectedFile.getAbsolutePath();
            // コンソールに出力
            System.out.println("選択されたファイルのパス: " + filePath);
            
            
        } else {
            System.out.println("ファイルが選択されませんでした。");
        }
        */
	}
	
	//ゲーム初期化メソッド
	public void gameInit() {
		//GInfo.shfleTile();
		//gameFlg = GAME_ING;
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
			String basePath = "img" + imageNo + "/" + decimalFormat.format(i);
            File pngFile = new File(basePath + ".png");
            File jpgFile = new File(basePath + ".jpg");
            if (pngFile.exists()) {
                tileImage[i] = new ImageIcon(pngFile.getPath());
            } else if (jpgFile.exists()) {
                tileImage[i] = new ImageIcon(jpgFile.getPath());
            }
			//tileImage[i] = new ImageIcon("img"+imageNo+"/"+decimalFormat.format(i)+".png");
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
				//gameInit();
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