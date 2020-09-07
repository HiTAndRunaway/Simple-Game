public class Map {
	int manX=0;
	int manY=0;
	byte map[][];
	int grade;
	
	// �˹��췽�����ڳ�������
	//��������ֻ��Ҫ�˵�λ�ú͵�ͼ�ĵ�ǰ״̬(�˵�λ��Ҳ���Բ�Ҫ��Ϊ�˷��������)
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
	
	// �˹��췽�����ڱ������
	//�ָ���ͼʱ��Ҫ�˵�λ�ã���ͼ��ǰ״̬���ؿ���(�ؿ��л�ʱ��Ϊ����)
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

