package restructo.helper;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Util {

	public static Object[] concat(Object[] a, Object[] b) {
		Object[] con = new Object[a.length + b.length];
		for (int i = 0; i < con.length; i++) {
			if (i < a.length)
				con[i] = a[i];
			else
				con[i] = b[i - a.length];
		}
		return con;
	}

	public static Object[] addToArray(Object a, Object[] b) {
		Object[] con = new Object[b.length + 1];
		for (int i = 0; i < b.length; i++) {
			con[i] = b[i];
		}
		con[b.length] = a;
		return con;
	}

	@SuppressWarnings("unchecked")
	public static <T> void addArrayToList(Object[] arr, List<T> list) {
		for (int i = 0; i < arr.length; i++) {
			try {
				list.add((T) arr[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> arrayToLinkedList(Object[] arr){
		List<T> list = new LinkedList<>();
		for (int i = 0; i < arr.length; i++) {
			try {
				list.add((T) arr[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static<T> Set<T> arrayToHashSet(Object[] arr) {
		Set<T> list = new HashSet<>();
		for (int i = 0; i < arr.length; i++) {
			try {
				list.add((T) arr[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static String relativePathToAbsolute(String path){
		return path;
	}
	
}
