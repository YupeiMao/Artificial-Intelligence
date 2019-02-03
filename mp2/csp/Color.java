
public class Color {
	private char color;
	private boolean source;
	public Color(char c,boolean s){
		color=c;
		source=s;
	}
	public Color(char c){
		color=c;
		source=false;
	}
	public Color(Color c){
		this.color=c.color;
		this.source=c.source;
	}
	public boolean isSource(){
		return source;
	}
	public char getColor(){
		return color;
	}
	public boolean isColor(char c){
		return color==c;
	}
	@Override
	//compare if color is the same
	public boolean equals(Object o){
		if(o==this){
			return true;
		}
		if(!(o instanceof Color)){
			return false;
		}
		Color c=(Color) o;
		return this.color==c.color;
	}
	@Override
	public String toString(){
		return Character.toString(color);
	}
}
