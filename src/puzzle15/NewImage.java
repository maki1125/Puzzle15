package puzzle15;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

//画像を新規登録するクラス
public class NewImage extends JDialog{
	
	private Runnable comp; //コールバックを保持する。

	public String filename;
	public String filename2; //.以下を除いたファイル名。プルダウンの選択肢に使う。
	public String directory; // 選択されたディレクトリ
	public File selectedFile;
	public File deleteFolder; //削除するフォルダ
	private ImgCut ic = new ImgCut();
	public static boolean saveflg = false;
	
	//ダイアログの表示
	public void dialog() {
		// ファイル選択用のダイアログを作成 (モード: LOAD)
		FileDialog dialog = new FileDialog(
            (Frame)null, // 親フレーム (nullならスタンドアロン)
            "追加したい絵を選択してください。", // タイトル
            FileDialog.LOAD// モード (LOADまたはSAVE)
            );
		// ダイアログを表示
	    dialog.setVisible(true);
	    // 選択されたファイルのパスを取得
	    filename = dialog.getFile();// 選択されたファイル名
	    directory = dialog.getDirectory(); // 選択されたディレクトリ
	    if(filename != null) {
	    	selectedFile = new File(directory, filename);
			filename2= filename.substring(0, filename.lastIndexOf("."));// 選択されたファイル名);
			
	    }
	}
	
	//コールバック
	public void setComp(Runnable comp) {
		this.comp = comp;
	}
	
	//選択した画像を表示するメソッド
	public void dispImg(File selectedFile) {
		
		//画像を読み込む
		BufferedImage image = null;
		try {
            // Fileオブジェクトを使って画像を読み込む
			image = ImageIO.read(selectedFile);
			image = ic.resize(image);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
		
		// 画像をアイコンとして表示
        ImageIcon imageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(imageIcon);
        
        //フレームに設定
        JFrame frame = new JFrame("画像表示");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(image.getWidth() + 50, image.getHeight() + 100);
        frame.setLayout(null);
        
        // 画像ラベルをフレームに追加
        imageLabel.setBounds(10, 10, image.getWidth(), image.getHeight());
        frame.add(imageLabel);
        
        //いいよボタンの設定
  		JButton button = new JButton("いいよ");
  		button.setBounds(10, 530, 100, 50);
  		button.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				GameFrame.selectedLabel = filename2;
  				Integer lastValue = Collections.max(GameFrame.imgMap2.values());
  				GameFrame.imgMap2.put(filename2,lastValue+1); //プルダウンの選択肢に追加
  				ic.SplitImageAndSave(); //画像の分割と保存
  				
  				txtEdit();//プルダウンのテキストファイルに書き込み
  	           
  				frame.dispose(); //フレームを閉じる。
  				
  				//処理が完了したらリスナーを呼び出す。
  				if(comp != null) {
  	        	  comp.run();
  	          }
  			}
  		});
  		frame.add(button);
  		
  		//やめるボタンの設定
  		JButton button2 = new JButton("やめる");
  		button2.setBounds(250, 530, 100, 50);
  		button2.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				frame.dispose(); //フレームを閉じる
  			}
  		});
  		frame.add(button2);
  		
  		//フレームの表示
        frame.setVisible(true);  
	}
	
	public void deleteImg(int imageNo) {
		System.out.println(imageNo+"のフォルダを削除します。");
		deleteFolder = new File("img/img" + imageNo );
		for(File file : deleteFolder.listFiles()) {
			System.out.println(file);
			file.delete();
		}
		deleteFolder.delete();
		
		txtEdit();
		
	}
	
	private void txtEdit(){
		//プルダウンのテキストファイルに書き込み
		int size = GameFrame.imgMap2.size(); //現在の登録数を取得
		int count2 = 0; 
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("pulldownData.txt"))) {
			for (String key : GameFrame.imgMap2.keySet()) {
				count2++;
				writer.write(key+","+Integer.valueOf(GameFrame.imgMap2.get(key)));
				if(count2 != size) { //最後の行には改行を入れない
					writer.newLine(); // 改行を追加
			}
			}
		 } catch (IOException e1) {
			 e1.printStackTrace();
		 }
	}
}
