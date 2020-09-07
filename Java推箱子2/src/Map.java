public class Map {
	int manX=0;
	int manY=0;
	byte map[][];
	int grade;
	
	// 此构造方法用于撤销操作
	//撤销操作只需要人的位置和地图的当前状态(人的位置也可以不要，为了方便而加入)
	public Map(int manX,int manY,byte [][]map)
	{
		this.manX=manX;
		this.manY=manY;
		int row=map.length;
		int column=map[0].length;
		byte temp[][]=new byte[row][column];
		for(int i=0;i<row;i++)
			for(int j=0;j<column;j++)
				temp[i][j]=map[i][j];
		this.map=temp;
	}
	
	// 此构造方法用于保存操作
	//恢复地图时需要人的位置，地图当前状态，关卡数(关卡切换时此为基数)
	public Map(int manX,int manY,byte [][]map,int grade)
	{
		this(manX,manY,map);
		this.grade=grade;
	}
	
	public int getManX() {
		return manX;
	}
	public int getManY() {
		return manY;
	}
	public byte[][] getMap() {
		return map;
	}
	
	public int getGrade() {
		return grade;
	}
	
}

