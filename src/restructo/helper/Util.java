package restructo.helper;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Util {

	public static <T> T[] concat(T[] a, T[] b, T[] buffer) {
		for (int i = 0; i < buffer.length; i++) {
			if (i < a.length)
				buffer[i] = a[i];
			else
				buffer[i] = b[i - a.length];
		}
		return buffer;
	}

	public static <T> T[] addToArray(T a, T[] b, T[] buffer) {
		for (int i = 0; i < b.length; i++) {
			buffer[i] = b[i];
		}
		buffer[b.length] = a;
		return buffer;
	}

	public static <T> void addArrayToList(T[] arr, List<T> list) {
		for (int i = 0; i < arr.length; i++) {
			try {
				list.add((T) arr[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static <T> List<T> arrayToLinkedList(T[] arr){
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

	public static<T> Set<T> arrayToHashSet(T[] arr) {
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
