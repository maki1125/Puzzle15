package puzzle15;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GifToPngConverter {
	// GIFをPNGに変換するメソッド
    public void convertGifToPng() {
        try {
        	for (int i = 1; i < 16; i++) {
        		//フォルダの作成
        		File folder = new File("img0");
        		folder.mkdir();
        		//ファイル名の取得
    			String inputFileName = String.format("%02d.gif", i);
    			String outputFileName = String.format("%02d.png", i);
    			File inputGif = new File("img/"+inputFileName);// 入力GIFファイルのパス  		
    			File outputPng = new File("img0/"+outputFileName);// 出力PNGファイルのパス  			
    			// GIFを読み込む
                BufferedImage gifImage = ImageIO.read(inputGif);
                //リサイズ
                BufferedImage resizedImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = resizedImage.createGraphics();
                g2d.drawImage(gifImage, 0, 0, 128, 128, null);
                g2d.dispose();

                // PNG形式で保存する
                ImageIO.write(resizedImage, "png", outputPng);
                
    		}
            
            
        } catch (IOException e) {
            e.printStackTrace();
            
        }
    }
}
    
        
    
