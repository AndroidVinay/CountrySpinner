package com.vinay.countryspinner;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	Spinner spinner1,spinner2,spinner3;
	ArrayAdapter adapter;
	Model model;
	ArrayList<Model> arrayList = new ArrayList<>();
	ArrayList<String> arrayName = new ArrayList<>();
	ArrayList<String> arrayId = new ArrayList<>();
	ArrayList<String> arrayCountry = new ArrayList<>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner3 = (Spinner) findViewById(R.id.spinner3);


		new fetchData().execute("");

	}


	class fetchData extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... strings) {
			HttpURLConnection httpURLConnection = null;
			BufferedReader bufferedReader = null;
			String result = null;
			Uri uri = Uri.parse("http://harshadcreative.comxa.com/countries.php").buildUpon()
					.build();

			try {
				URL url = new URL(uri.toString());

				httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setRequestMethod("GET");
				httpURLConnection.setConnectTimeout(20000);
				httpURLConnection.connect();

				InputStream inputStream = httpURLConnection.getInputStream();
				StringBuffer stringBuffer = new StringBuffer();

				if (inputStream == null) {
					return null;
				}

				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line = "";
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line + "");
				}

				if (stringBuffer.length() == 0) {
					return null;
				}

				if (inputStream != null) {
					inputStream.close();

					result = stringBuffer.toString();
					return result;
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (httpURLConnection != null) {
					httpURLConnection.disconnect();
				}

				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}


			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			try {
//				JSONObject jsonObject = new JSONObject(result);

				JSONArray jsonArray = new JSONArray(result);


				if (jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {

						JSONObject jsonObject1 = jsonArray.getJSONObject(i);

						String id = jsonObject1.getString("Id");
						String name = jsonObject1.getString("name");
						String country = jsonObject1.getString("country");

						arrayList.add(new Model(id, name, country));
						arrayId.add(id);
						arrayName.add(name);
						arrayCountry.add(country);
					}
					ArrayAdapter<Model> dataAdapter = new ArrayAdapter<Model>(getBaseContext(),
							android.R.layout.simple_spinner_item, arrayList);

					ArrayAdapter<String> adapterId = new ArrayAdapter<String>(getBaseContext(),
							android.R.layout.simple_spinner_item, arrayId);
					ArrayAdapter<String> adapterName = new ArrayAdapter<String>(getBaseContext(),
							android.R.layout.simple_spinner_item, arrayName);
					ArrayAdapter<String> adapterCountry = new ArrayAdapter<String>(getBaseContext(),
							android.R.layout.simple_spinner_item, arrayCountry);

					dataAdapter
							.setDropDownViewResource(android.R.layout
									.simple_spinner_dropdown_item);

					spinner1.setAdapter(adapterId);
					spinner2.setAdapter(adapterName);
					spinner3.setAdapter(adapterCountry);
					arrayList.contains(model);
				}


			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}


	class Model implements Parcelable {

		String id, name, country;

		public Model(String id, String name, String country) {
			this.id = id;
			this.name = name;
			this.country = country;
		}

		protected Model(Parcel in) {
			id = in.readString();
			name = in.readString();
			country = in.readString();
		}

		public final Creator<Model> CREATOR = new Creator<Model>() {
			@Override
			public Model createFromParcel(Parcel in) {
				return new Model(in);
			}

			@Override
			public Model[] newArray(int size) {
				return new Model[size];
			}
		};

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel parcel, int i) {
			parcel.writeString(id);
			parcel.writeString(name);
			parcel.writeString(country);
		}
	}
}
