package utility;

import org.primefaces.model.UploadedFile;

public class Utils {

	
	public static boolean isValideImageType(String fileType) {

		if (fileType.equals(new String("image/png" ))|| fileType.equals(new String("image/jpg")) 
				|| fileType.equals(new String("image/jpeg"))) {
			System.out.println("valiiiiiiiid");
			return true;
		}

		return false;
	}

	public static boolean isValideFile(UploadedFile f) {
		if (Utils.isValideImageType(f.getContentType()) && f.getSize()< 1572864) {
			System.out.println("valiiiiiiiid size ");
			return true;
		}
		return false;
	}

	public static void main(String args[]) {
		// System.out.println(getFileType("uiafauf.xx"));
	}
}
