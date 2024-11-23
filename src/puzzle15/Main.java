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
	}
}
