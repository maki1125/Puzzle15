package puzzle15;

import javax.swing.JFrame;

public class Main {
	private static GameFrame frame;
	public static void main(String[] args) {
		frame = new GameFrame();
		//GridInfo gridInfo = new GridInfo(4,4);

		frame.getContentPane().setLayout(null);//レイアウトマネージャが無効となるので、位置やサイズの指定を行う必要あり。
		//frame.setTitle("Puzzle15");
		//frame.setSize(512, 612);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.getContentPane().addMouseListener(frame);//マウスイベントを受け入れる
		frame.setVisible(true);
		
		//ImgPuni imgPuni = new ImgPuni();
        //imgPuni.resizeAndSplitImage(); // メソッドを呼び出してリサイズ＆分割
		
		/*
		//数字の画像をリサイズする
		GifToPngConverter g = new GifToPngConverter();
        g.convertGifToPng();

        BufferedImage image = null;
        File outputGif = new File("img0/01.png");// 返還後画像ファイルのパス
        try {
            // GIF画像を読み込む
            image = ImageIO.read(outputGif);
        } catch (IOException e) {
            e.printStackTrace();
            return;  // 読み込み失敗時に終了
        }
        JFrame frame2 = new JFrame("画像表示");//変換した画像を表示して確認
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setSize(image.getWidth()+50, image.getHeight()+50);
        JLabel label = new JLabel(new ImageIcon(image));
        frame2.getContentPane().add(label, BorderLayout.CENTER);
        frame2.setVisible(true);
        */

	}
}
