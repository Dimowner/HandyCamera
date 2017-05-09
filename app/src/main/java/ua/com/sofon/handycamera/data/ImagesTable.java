/*
 * Copyright 2017 Dmitriy Ponomarenko, sofon.com.ua
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.com.sofon.handycamera.data;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Table Images SQLite database.
 * Created on 10.09.2016.
 * @author Dimowner
 */
public class ImagesTable {

	/** Table name */
	public static final String TABLE_NAME = "images";

	/** Table fields names */
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_IMG_LOCATION = "img_location";

	/** Creation script for table Images. */
	private static final String DATABASE_CREATE_IMAGES_TABLE_SCRIPT =
			"CREATE TABLE " + TABLE_NAME + " ("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ COLUMN_TITLE + " TEXT NOT NULL, "
					+ COLUMN_DATE + " INTEGER NOT NULL, "
					+ COLUMN_IMG_LOCATION + " TEXT NOT NULL);";

	/** Tag for logging information. */
	private static final String LOG_TAG = "ImagesTable";


	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_IMAGES_TABLE_SCRIPT);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
										  int newVersion) {
		Log.d(LOG_TAG, "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}
}
