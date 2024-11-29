package puzzle15;

//マスの状態を保存するクラス
public class GridInfo{
	private int gridFlg[][]; //マス情報
	private int gridXNum; //マスの横数
	private int gridYNum; //マスの縦数
	
	//コンストラクタ
	GridInfo(int xNum, int yNum){
		gridXNum = xNum;
		gridYNum = yNum;
		
		//各マスに置かれているコマを保持する２次元配列を定義
		gridFlg = new int[gridXNum][gridYNum];
		
		//1~15を格納
		for(int y=0; y<gridYNum; y++) {
			for(int x=0; x<gridXNum; x++) {
				gridFlg[y][x] = y*gridYNum + x + 1;
			}
		}
		//右下のマスにはこまがないことを意味する-1を格納
		gridFlg[gridYNum-1][gridXNum-1] = 0;
		//shfleTile();
	}
	//コマの位置を初期値に戻すメソッド
	public void clearTile() {
		//1~15を格納
		for(int y=0; y<gridYNum; y++) {
			for(int x=0; x<gridXNum; x++) {
				gridFlg[y][x] = y*gridYNum + x + 1;
			}
		}
		//右下のマスにはこまがないことを意味する-1を格納
		gridFlg[gridYNum-1][gridXNum-1] = 0;
	}
	
	//コマを開いているマスに移動させるメソッド
	public boolean moveTile(int clickTileX, int clickTileY) {
		//System.out.println(clickTileX+","+clickTileY);
		//右に動く
		if(clickTileX+1<gridXNum && clickTileX>=0 && clickTileY>=0 && clickTileY < gridYNum) {
		if(gridFlg[clickTileY][clickTileX+1]==0) {
			System.out.println("右");
			gridFlg[clickTileY][clickTileX+1]=gridFlg[clickTileY][clickTileX];
			gridFlg[clickTileY][clickTileX]=0;
			return true;
		}
		}
		//左に動く
		if(clickTileX-1>=0 && clickTileX<gridXNum && clickTileY>=0 && clickTileY < gridYNum) {
		if(gridFlg[clickTileY][clickTileX-1]==0) {
			System.out.println("左");
			gridFlg[clickTileY][clickTileX-1]=gridFlg[clickTileY][clickTileX];
			gridFlg[clickTileY][clickTileX]=0;
			return true;
		}
		}

		//上に動く
		if(clickTileY-1>=0 && clickTileX>=0 && clickTileY<gridYNum && clickTileX < gridXNum) {
		if(gridFlg[clickTileY-1][clickTileX]==0) {
			System.out.println("上");
			gridFlg[clickTileY-1][clickTileX]=gridFlg[clickTileY][clickTileX];
			gridFlg[clickTileY][clickTileX]=0;
			return true;
		}
		}
		//下に動く
		if(clickTileY+1<gridYNum && clickTileX>=0 && clickTileY>=0 && clickTileX < gridXNum) {
		if(gridFlg[clickTileY+1][clickTileX]==0) {
			//System.out.println("下");
			gridFlg[clickTileY+1][clickTileX]=gridFlg[clickTileY][clickTileX];
			gridFlg[clickTileY][clickTileX]=0;
			return true;
		}
		}
		return false;
	}
	
	//引数で指定されたマスに置かれたコマを返すメソッド
	public int getTileNum(int x, int y) {
		return gridFlg[y][x];
	}
	
	//コマが置かれていないx座標を返すメソッド
	public int[] getEmpGridNum() {
		int[] emp = new int[2];
		for(int y = 0; y < gridYNum; y++) {
			for(int x = 0; x < gridXNum; x++) {
				if(gridFlg[y][x] == 0) {
					emp[0] = x;
					emp[1] = y;
				}
			}
		}
		return emp;
	}
	
	//コマをシャッフルするメソッド
	public void shfleTile() {
		int[] empTile = new int[2];
		int randNum;
		boolean blnRet;
		
		for(int i=0; i<200; i++) {
			//こまが置かれていない座標を取得
			empTile = getEmpGridNum();
			//System.out.println("コマなし"+empTile[0]+','+empTile[1]);
			//0~3までの数値をランダムに取得
			randNum = (int)(Math.random() * 4);
			//コマを動かす
			switch(randNum) {
			//空白の右側のマスを動かす
			case 0:
				blnRet = moveTile(empTile[0]+1, empTile[1]);
				break;
			//空白の左側のマスを動かす
			case 1:
				blnRet = moveTile(empTile[0]-1, empTile[1]);
				break;
			//空白の上側のマスを動かす
			case 2:
				blnRet = moveTile(empTile[0], empTile[1]+1);
				break;
			//空白の下側のマスを動かす
			case 3:
				blnRet = moveTile(empTile[0], empTile[1]-1);
				break;
			}
		}
	}
}