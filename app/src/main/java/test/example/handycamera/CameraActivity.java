package test.example.handycamera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created on 10.09.2016.
 * @author Dimowner
 */
public class CameraActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		if (null == savedInstanceState) {
			getFragmentManager().beginTransaction()
					.replace(R.id.container, Camera2BasicFragment.newInstance())
					.commit();
		}
	}
}