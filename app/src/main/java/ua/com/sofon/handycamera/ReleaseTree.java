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

package ua.com.sofon.handycamera;

import android.util.Log;
import timber.log.Timber;

/**
 * Created on 09.05.2017.
 * @author Dimowner
 */
public class ReleaseTree extends Timber.Tree {

	private static final int MAX_LOG_LENGTH = 4000;

	@Override
	protected boolean isLoggable(String tag, int priority) {
		if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
			return false;
		}

		//Only log WARN, ERROR, WTF
		return true;
	}

	@Override
	protected void log(int priority, String tag, String message, Throwable t) {
		if (isLoggable(tag, priority)) {
			//Report caught exceptions to Crashlytics
//			if (priority == Log.ERROR && t != null) {
//				//Crashlytics.log(e)
//			}

			// Message is short enough, does not need to be broken into chunks
			if (message.length() < MAX_LOG_LENGTH) {
				if (priority == Log.ASSERT) {
					Log.wtf(tag, message);
				} else {
					Log.println(priority, tag, message);
				}
				return;
			}

			// Split by line, then ensure eeach line can fit into Log's maximum length.
			for (int i = 0, length = message.length(); i < length; i++) {
				int newLine = message.indexOf('\n', i);
				newLine = newLine != -1 ? newLine : length;
				do {
					int end = Math.min(newLine, i + MAX_LOG_LENGTH);
					String part = message.substring(i, end);
					if (priority == Log.ASSERT) {
						Log.wtf(tag, part);
					} else {
						Log.println(priority, tag, part);
					}
				} while (i < newLine);
			}
		}
	}
}
