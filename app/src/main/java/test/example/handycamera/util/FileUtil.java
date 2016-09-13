package test.example.handycamera.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.renderscript.Script;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class for working with files
 * Created on 12.09.2016.
 * @author Dimowner
 */
public class FileUtil {

	/** Application directory name. */
	public static final String APPLICATION_DIR = "HandyCamera";

	/** Tag for logging information. */
	private static final String LOG_TAG = "FileUtil";

	private FileUtil() {
	}
	/**
	 * Создать файл.
	 * Если он не существует, то создать его.
	 * @param path Путь к файлу.
	 * @param fileName Имя файла.
	 */
	public static File createFile(File path, String fileName) {
		if (path != null) {
			Log.d(LOG_TAG, "createFile path = " + path.getAbsolutePath() + " fileName = " + fileName);
			File file = new File(path + "/" + fileName);
			//Create file if need.
			if (!file.exists()) {
				try {
					if (file.createNewFile()) {
						Log.i(LOG_TAG, "The file was successfully created! - " + file.getAbsolutePath());
					} else {
						Log.i(LOG_TAG, "The file exist! - " + file.getAbsolutePath());
					}
				} catch (IOException e) {
					Log.e(LOG_TAG, "Failed to create the file.", e);
					return null;
				}
			}
			if (!file.canWrite()) {
				Log.e(LOG_TAG, "The file can not be written.");
			}
			return file;
		} else {
			return null;
		}
	}

	/**
	 * Записать изображение в файл.
	 * @param file Файл в который будет записано изображение.
	 * @param bitmap Изображение, которое будет записано в файл.
	 * @return Признак успешного выполнения.
	 */
	public static boolean writeImage(File file, Bitmap bitmap) {
		if (!file.canWrite()) {
			Log.e(LOG_TAG, "The file can not be written.");
			return false;
		}
		if (bitmap == null) {
			Log.e(LOG_TAG, "Failed to write! bitmap is null.");
			return false;
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fos)) {
				fos.close();
				return true;
			}
			fos.close();
		} catch (FileNotFoundException e) {
			Log.e(LOG_TAG, "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error accessing file: " + e.getMessage());
		}
		return false;
	}

	/**
	 * Получить публичную папку на внешнем хранилище (external storage).
	 * @param dirName Имя папки.
	 */
	public static File getStorageDir(String dirName) {
		if (dirName != null && !dirName.isEmpty()) {
			File file = new File(Environment.getExternalStorageDirectory(), dirName);
			if (isExternalStorageReadable() && isExternalStorageWritable()) {
				if (!file.exists() && !file.mkdirs()) {
					Log.e(LOG_TAG, "Directory " + file.getAbsolutePath() + " was not created");
				}
			}
			return file;
		} else {
			return null;
		}
	}

	/**
	 * Создать папку в публичных изображениях.
	 * @param dirName Имя папки.
	 */
	public static File getPicturesStorageDir(String dirName) {
		// Get the directory for the user's public pictures directory.
		File file = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), dirName);
		if (file.exists()) {
			Log.i(LOG_TAG, "Directory " + file.getAbsolutePath() + " is exists");
		} else if (!file.mkdirs()) {
			Log.e(LOG_TAG, "Directory " + file.getAbsolutePath() + " was not created");
		}
		Log.v(LOG_TAG, "picturesFile = " + file.getPath());
		return file;
	}

	/**
	 * Checks if external storage is available for read and write.
	 */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	/**
	 * Checks if external storage is available to at least read.
	 */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		return (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
	}

	/**
	 * Read {@link Bitmap} from file system.
	 * @param path Path to file.
	 * @return {@link Bitmap}.
	 */
	public static Bitmap readBitmapFromFile(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		return BitmapFactory.decodeFile(path, options);
	}

	/**
	 * Read {@link Bitmap} from file system.
	 * @param path Path to file.
	 * @return {@link Bitmap}.
	 */
	public static Bitmap readBitmapFromFile(String path, int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		try {
			FileInputStream inputStream = new FileInputStream(new File(path));
			return BitmapFactory.decodeStream(inputStream);
		} catch (FileNotFoundException e) {
			Log.e(LOG_TAG, "", e);
		}


		return null;//BitmapFactory.decodeFile(path, options);

	}

	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight
					&& (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	/**
	 * Проверить что файл с указанным адресом существует.
	 * @param path Адрес файла.
	 */
	public static boolean isExistsFile(String path) {
		return (path != null && new File(path).exists());
	}
}
