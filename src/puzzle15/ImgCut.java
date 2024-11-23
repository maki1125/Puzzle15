package puzzle15;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//画像を編集するクラス
public class ImgCut {
	public void resizeAndSplitImage() {
		try {
          // 画像を読み込む
          BufferedImage originalImage = ImageIO.read(new File("puni_img/pni.png"));

          // リサイズされた画像を作成（256x256）
          int newWidth = 512;
          int newHeight = 512;
          BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
          Graphics2D g2d = resizedImage.createGraphics();
          g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
          g2d.dispose();
          
       // リサイズ後の画像を保存
          File resizedFile = new File("puni_img/resized_image.png");
          ImageIO.write(resizedImage, "png", resizedFile);
          System.out.println("リサイズされた画像を保存しました: " + resizedFile.getAbsolutePath());

          // 分割する行数と列数
          int rows = 4;
          int cols = 4;

          // 各部分の幅と高さを計算
          int partWidth = newWidth / cols;
          int partHeight = newHeight / rows;

          // 各部分を切り出して保存
          int count = 0;
          for (int y = 0; y < rows; y++) {
              for (int x = 0; x < cols; x++) {
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
                  File outputFile = new File("puni_img/" + fileName);
                  ImageIO.write(subImage, "png", outputFile);
                  //File outputFile = new File("puni_img/" + count + ".png");
                  //ImageIO.write(subImage, "png", outputFile);
                  count++;
              }
          }

          System.out.println("画像をリサイズして16分割に保存しました。");

      } catch (IOException e) {
          e.printStackTrace();
      }
	}
}