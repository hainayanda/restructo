package restructo.helper;

public class Util {
	
	public static Object[] concat(Object[] a, Object[] b){
		Object[] con = new Object[a.length + b.length];
		for(int i = 0; i < con.length; i++){
			if(i < a.length)
				con[i] = a[i];
			else
				con[i] = b[i - a.length];
		}
		return con;
	}
	
}
