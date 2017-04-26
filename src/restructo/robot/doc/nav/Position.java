package restructo.robot.doc.nav;

public class Position {
	private int line;
	private int sentenceNumber;
	
	public Position(int line, int sentenceNumber){
		this.line = line;
		this.sentenceNumber = sentenceNumber;
	}

	public int getLine() {
		return line;
	}

	public int getSentenceNumber() {
		return sentenceNumber;
	}
	
}
