package puzzle15;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//画像を編集するクラス
public class ImgCut {
	private static final int RSIZE_WIDTH = 512;
	private static final int RSIZE_HEIGHT = 512;
	public static boolean saveImgFlg=false;
	public BufferedImage resizedImage;
	
	//画像をリサイズ、１６分割の線を書くクラス
	public BufferedImage  resize(BufferedImage originalImage) throws IOException {
		
        resizedImage = new BufferedImage(RSIZE_WIDTH, RSIZE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, RSIZE_WIDTH, RSIZE_HEIGHT, null);
        // 描画設定
        g2d.setColor(Color.BLACK);  // 線の色
        g2d.setStroke(new BasicStroke(2));  // 線の太さ

        // 16分割 (4×4) の各マスの幅と高さを計算
        int gridWidth = RSIZE_WIDTH / GameFrame.GRID_X;
        int gridHeight = RSIZE_HEIGHT / GameFrame.GRID_Y;

        // 縦線を描画
        for (int i = 1; i < 4; i++) {
            int x = i * RSIZE_WIDTH / GameFrame.GRID_X;
            g2d.drawLine(x, 0, x, RSIZE_HEIGHT);
        }

        // 横線を描画
        for (int i = 1; i < 4; i++) {
            int y = i * RSIZE_HEIGHT / GameFrame.GRID_Y;
            g2d.drawLine(0, y, RSIZE_WIDTH, y);
        }
        
        // 右下のマスを白く塗る
        int startX = gridWidth * 3; // 右下のマスの左上X座標
        int startY = gridHeight * 3; // 右下のマスの左上Y座標
        g2d.setColor(Color.WHITE);
        g2d.fillRect(startX, startY, gridWidth, gridHeight);
        g2d.dispose();// Graphics2Dのリソースを解放
        return resizedImage;
	}
	
	//画像を16分割して保存するクラス
	public void SplitImageAndSave() {
		
		try {			
            // コピー先のフォルダ
            int size = GameFrame.imgMap.size(); //現在の登録数を取得
            File saveFolder = new File("img/img" + size );
            // フォルダが存在しない場合は作成
            if (!saveFolder.exists()) {
                boolean created = saveFolder.mkdir();
                if (created) {
                    System.out.println("コピー先フォルダを作成しました: " + saveFolder.getAbsolutePath());
                } else {
                    System.err.println("コピー先フォルダの作成に失敗しました。");
                    return;
                }
            }

          // 分割する行数と列数
          int rows = GameFrame.GRID_X;
          int cols = GameFrame.GRID_Y;

          // 各部分の幅と高さを計算
          int partWidth = RSIZE_WIDTH / GameFrame.GRID_X;
          int partHeight = RSIZE_HEIGHT / GameFrame.GRID_Y;

          // 各部分を切り出して保存
          int count = 0;
          for (int y = 0; y < GameFrame.GRID_Y; y++) {
              for (int x = 0; x < GameFrame.GRID_X; x++) {
                  BufferedImage subImage = resizedImage.getSubimage(
                      x * partWidth,
                      y * partHeight,
                      partWidth,
                      partHeight
                  );
                  
                  // 黒い線を引く
                  Graphics2D g2dSub = subImage.createGraphics();
                  g2dSub.setColor(Color.BLACK);
                  g2dSub.setStroke(new java.awt.BasicStroke(5)); // 線の太さを設定
                  g2dSub.drawRect(0, 0, partWidth - 1, partHeight - 1); // 四隅に線を引く
                  g2dSub.dispose();

                  // 各部分画像を保存
                  String fileName = String.format("%02d.png", count+1);
                  File outputFile = new File(saveFolder, fileName);
                  ImageIO.write(subImage, "png", outputFile);
                  count++;
              }
          }
          
          
          
          
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
		
		saveImgFlg = true;
        System.out.println("画像をリサイズして16分割に保存しました。");
	}
}