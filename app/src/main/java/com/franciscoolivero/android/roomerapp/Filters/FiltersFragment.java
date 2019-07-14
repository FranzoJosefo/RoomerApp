package com.franciscoolivero.android.roomerapp.Filters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.franciscoolivero.android.roomerapp.MainActivity;
import com.franciscoolivero.android.roomerapp.ParserService;
import com.franciscoolivero.android.roomerapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FiltersFragment extends Fragment {

    public FiltersFragment() {
        //Required empty constructor
    }


    /**
     * Gender of the product. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private static final int PICK_IMAGE_REQUEST = 100;
    private String userToken;
    private final int GENDER_OTRO_INDEX = 1;
    private final int GENDER_F_INDEX = 2;
    private final int GENDER_M_INDEX = 3;
    private final String GENDER_OTRO_STRING = "Otro";
    private final String GENDER_F_STRING = "F";
    private final String GENDER_M_STRING = "M";
    private List<String> categoriesBarrios;

//    @Override
//    public void onLoadFinished(@NonNull Loader<List<String>> loader, List<String> data) {
//
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull Loader<List<String>> loader) {
//
//    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    // Find all relevant views that we will need to read user input from
    @BindView(R.id.spinner)
    Spinner spinner_barrio;
    @BindView(R.id.edit_edad_min)
    EditText text_edad_min;
    @BindView(R.id.edit_edad_max)
    EditText text_edad_max;
    @BindView(R.id.edit_plata_min)
    EditText text_plata_min;
    @BindView(R.id.edit_plata_max)
    EditText text_plata_max;
    @BindView(R.id.spinnerSexo)
    Spinner spinner_sexo;
    @BindView(R.id.edad_min_error)
    View edad_min_error;
    @BindView(R.id.edad_max_error)
    View edad_max_error;
    @BindView(R.id.dinero_min_error)
    View dinero_min_error;
    @BindView(R.id.dinero_max_error)
    View dinero_max_error;
    @BindView(R.id.container_filter_layout)
    View container_filter_layout;
    @BindView(R.id.loading_spinner_container_filter)
    View container_spinner_filter;


    public boolean ismProductHasChanged() {
        return mProductHasChanged;
    }

    public void setmProductHasChanged(boolean mProductHasChanged) {
        this.mProductHasChanged = mProductHasChanged;
    }

    private boolean mProductHasChanged = false;
    private String LOG_TAG = getClass().getSimpleName();
    private Uri mCurrentProductUri;
    private Uri selectedImage;
    private GoogleSignInAccount account;

    public final OkHttpClient client = new OkHttpClient();
    private static final String ROOMER_API_HOST = "roomer-backend.herokuapp.com";
    private static final String ROOMER_API_PATH_APD = "apd";
    private static final String ROOMER_API_PATH_GET_FILTROS_TOKEN = "getFiltrosPorToken";
    private static final String ROOMER_API_POST_FILTERS = "http://roomer-backend.herokuapp.com/apd/insertFiltro";


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            setmProductHasChanged(true);
            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filters, container, false);
        ButterKnife.bind(this, rootView);


        setmProductHasChanged(false);

        //Avoid keyboard from opening focused on first EditText
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        spinner_barrio.setOnTouchListener(mTouchListener);
        text_edad_min.setOnTouchListener(mTouchListener);
        text_edad_max.setOnTouchListener(mTouchListener);
        text_plata_min.setOnTouchListener(mTouchListener);
        text_plata_max.setOnTouchListener(mTouchListener);
        spinner_sexo.setOnTouchListener(mTouchListener);

        mCurrentProductUri = getActivity().getIntent().getData();
        account = getActivity().getIntent().getParcelableExtra("account");

        // Spinner element
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        Spinner spinnerSexo = (Spinner) rootView.findViewById(R.id.spinnerSexo);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
//                    Toast.makeText(getContext(), item.toString(),
//                            Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(getContext(), "Selected",
//                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        spinnerSexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
//                    Toast.makeText(getContext(), item.toString(),
//                            Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(getContext(), "Selected",
//                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Seleccionar");
        categories.add("Otro");
        categories.add("F");
        categories.add("M");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        // attaching data adapter to spinner
        spinnerSexo.setAdapter(dataAdapter);


        // Spinner Drop down elements
        categoriesBarrios = new ArrayList<String>();
        categoriesBarrios.add("Seleccionar");
        categoriesBarrios.add("Agronomía");
        categoriesBarrios.add("Almagro");
        categoriesBarrios.add("Balvanera");
        categoriesBarrios.add("Barracas");
        categoriesBarrios.add("Belgrano");
        categoriesBarrios.add("Boedo");
        categoriesBarrios.add("Caballito");
        categoriesBarrios.add("Chacarita");
        categoriesBarrios.add("Coghlan");
        categoriesBarrios.add("Colegiales");
        categoriesBarrios.add("Constitución");
        categoriesBarrios.add("Flores");
        categoriesBarrios.add("Floresta");
        categoriesBarrios.add("La Boca");
        categoriesBarrios.add("La Paternal");
        categoriesBarrios.add("Liniers");
        categoriesBarrios.add("Mataderos");
        categoriesBarrios.add("Montserrat");
        categoriesBarrios.add("Monte Castro");
        categoriesBarrios.add("Nueva Pompeya");
        categoriesBarrios.add("Núñez");
        categoriesBarrios.add("Palermo");
        categoriesBarrios.add("Parque Avellaneda");
        categoriesBarrios.add("Parque Chacabuco");
        categoriesBarrios.add("Parque Chas");
        categoriesBarrios.add("Parque Patricios");
        categoriesBarrios.add("Puerto Madero");
        categoriesBarrios.add("Recoleta");
        categoriesBarrios.add("Retiro");
        categoriesBarrios.add("Saavedra");
        categoriesBarrios.add("San Cristóbal");
        categoriesBarrios.add("San Nicolás");
        categoriesBarrios.add("San Telmo");
        categoriesBarrios.add("Vélez Sarsfield");
        categoriesBarrios.add("Versalles");
        categoriesBarrios.add("Villa Crespo");
        categoriesBarrios.add("Villa del Parque");
        categoriesBarrios.add("Villa Devoto");
        categoriesBarrios.add("Villa Gral. Mitre");
        categoriesBarrios.add("Villa Lugano");
        categoriesBarrios.add("Villa Luro");
        categoriesBarrios.add("Villa Ortúzar");
        categoriesBarrios.add("Villa Pueyrredón");
        categoriesBarrios.add("Villa Real");
        categoriesBarrios.add("Villa Riachuelo");
        categoriesBarrios.add("Villa Santa Rita");
        categoriesBarrios.add("Villa Soldati");
        categoriesBarrios.add("Villa Urquiza");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterSpinnerBarrio = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoriesBarrios);

        // Drop down layout style - list view with radio button
        dataAdapterSpinnerBarrio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapterSpinnerBarrio);

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        account = GoogleSignIn.getLastSignedInAccount(getContext());
        container_spinner_filter.setVisibility(View.GONE);//ONLY FOR NOW
        if (getActivity() != null) {
            if (getActivity().getClass().getSimpleName().equals(MainActivity.class.getSimpleName())) {
                userToken = account.getEmail();
                //TODO IMPLEMENT CALLING FILTER DATA SO THAT USER VIEWS HIS PREVIOUS FILTERS.
                if(isConnected()){
                    fetchFilterData();
                } else {
                    Toast.makeText(getContext(), "Verifique la conexion a internet", Toast.LENGTH_SHORT).show();
                }


            }
        }


        //Check
//        userToken = ((MainActivity) getActivity()).getUserToken();
//        loadingSpinner.setVisibility(View.VISIBLE);
//        profileAdapter = new ProfileAdapter(getActivity().getBaseContext(), new ArrayList<>());
//        profileListView.setAdapter(profileAdapter);
//        if(isConnected()){
//            fetchProfileData(ROOMER_API_GET_RESULTS, getActivity(), getContext());
//        } else {
//            Toast.makeText(getContext(), "Verifique la conexion a internet", Toast.LENGTH_SHORT).show();
//        }
//
//        loadingSpinner.setVisibility(View.GONE);

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean saveFilters() {

        edad_min_error.setVisibility(View.GONE);
        edad_max_error.setVisibility(View.GONE);
        dinero_min_error.setVisibility(View.GONE);
        dinero_max_error.setVisibility(View.GONE);

        String sFilter_barrio = spinner_barrio.getSelectedItem().toString();
        String sFilter_edad_min = String.valueOf(text_edad_min.getText()).trim();
        String sFilter_edad_max = String.valueOf(text_edad_max.getText()).trim();
        String sFilter_plata_min = String.valueOf(text_plata_min.getText()).trim();
        String sFilter_plata_max = String.valueOf(text_plata_max.getText()).trim();
        String sFilter_sexo = spinner_sexo.getSelectedItem().toString();

        if (sFilter_barrio.equals("Seleccionar")
                || TextUtils.isEmpty(sFilter_edad_min)
                || TextUtils.isEmpty(sFilter_edad_max)
                || TextUtils.isEmpty(sFilter_plata_min)
                || TextUtils.isEmpty(sFilter_plata_max)
                || sFilter_sexo.equals("Seleccionar")) {
            Toast toast = Toast.makeText(getContext(), "Todos los campos son requeridos.", Toast.LENGTH_SHORT);
            toast.show();

            return false;
        }

        if (!TextUtils.isEmpty(sFilter_edad_min)
                && !TextUtils.isEmpty(sFilter_edad_max)
                && !TextUtils.isEmpty(sFilter_edad_min)
                && !TextUtils.isEmpty(sFilter_plata_min)
                && !TextUtils.isEmpty(sFilter_plata_max)) {
            if (!isNumeric(sFilter_edad_max)
                    || !isNumeric(sFilter_edad_min)
                    || !isNumeric(sFilter_plata_max)
                    || !isNumeric(sFilter_plata_min)) {
                Toast toast = Toast.makeText(getContext(), "Edades/Dinero deben ser numericos", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }

        }

        if (Integer.valueOf(text_edad_min.getText().toString()) < 18
                || Integer.valueOf(text_edad_max.getText().toString()) > 50
                || Integer.valueOf(text_plata_min.getText().toString()) < 5000
                || Integer.valueOf(text_plata_max.getText().toString()) > 50000) {
            if (Integer.valueOf(text_edad_min.getText().toString()) < 18) {
                edad_min_error.setVisibility(View.VISIBLE);
            }

            if (Integer.valueOf(text_edad_max.getText().toString()) > 50) {
                edad_max_error.setVisibility(View.VISIBLE);
            }

            if (Integer.valueOf(text_plata_min.getText().toString()) < 5000) {
                dinero_min_error.setVisibility(View.VISIBLE);
            }

            if (Integer.valueOf(text_plata_max.getText().toString()) > 50000) {
                dinero_max_error.setVisibility(View.VISIBLE);
            }
            return false;
        }


        String postBodyInsertarFiltros = "{\n" +
                "    \"token\": \"" + userToken + "\",\n" +
                "    \"barrio\": \"" + sFilter_barrio + "\",\n" +
                "    \"dineroMin\": " + sFilter_plata_min + ",\n" +
                "    \"dineroMax\": " + sFilter_plata_max + ",\n" +
                "    \"edadMin\": " + sFilter_edad_min + ",\n" +
                "    \"edadMax\": " + sFilter_edad_max + ",\n" +
                "    \"sexo\": \"" + sFilter_sexo + "\"\n" +
                "}";

        container_spinner_filter.setVisibility(View.VISIBLE);
        container_filter_layout.setVisibility(View.GONE);
        try {
            postRequest(ROOMER_API_POST_FILTERS, postBodyInsertarFiltros);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error guardando filtros, pruebe de nuevo!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        account = GoogleSignIn.getLastSignedInAccount(getContext());
        container_spinner_filter.setVisibility(View.VISIBLE);
        if (getActivity() != null) {
            if (getActivity().getClass().getSimpleName().equals(MainActivity.class.getSimpleName())) {
                userToken = account.getEmail();
                if(isConnected()){
                    fetchFilterData();
                } else {
                    Toast.makeText(getContext(), "Verifique la conexion a internet", Toast.LENGTH_SHORT).show();
                }


            }
        }
    }

    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public void postRequest(String postUrl, String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG, "Problem posting Profile information to Backend", e);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                Log.d("TAG", myResponse);


                try {
                    JSONObject json = new JSONObject(myResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUIFilterSaved();

//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }


    private void updateUIFilterSaved() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        container_spinner_filter.setVisibility(View.GONE);
        container_filter_layout.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "Filtros guardados!", Toast.LENGTH_LONG).show();
    }

    private void getFiltrosbyTokenHttpRequest(String mToken) throws IOException {
        Log.v(LOG_TAG, "mToken: " + mToken);

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(ROOMER_API_HOST)
                .addPathSegment(ROOMER_API_PATH_APD)
                .addPathSegment(ROOMER_API_PATH_GET_FILTROS_TOKEN)
                .addQueryParameter("token", mToken)
                .build();
        String url = httpUrl.toString();
        Log.v(LOG_TAG, "URL FOR GET FILTROS BY TOKEN: " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the FILTROS JSON results", e);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String resultsResponse = response.body().string();
                Log.v(LOG_TAG, resultsResponse);
                List<Filter> filters = ParserService.extractFilters(resultsResponse);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUIProfileLoaded(filters);
                            Log.v(LOG_TAG, "SALIENDO DEL RUN DE UI THREAD");
                        }
                    });
                }

            }
        });
    }

    public void updateUIProfileLoaded(List<Filter> filterList) {
        if (!filterList.isEmpty()) {
            Log.v(LOG_TAG, "Filter List is NOT Empty");
            Filter userFilters = filterList.get(0);
            setProfileDataViews(userFilters);
            container_spinner_filter.setVisibility(View.GONE);
            container_filter_layout.setVisibility(View.VISIBLE);
        }
    }


    private void setProfileDataViews(Filter userFilters) {
        text_edad_min.setText(String.valueOf(userFilters.getmMinAge()));
        text_edad_max.setText(String.valueOf(userFilters.getmMaxAge()));
        text_plata_min.setText(String.valueOf(userFilters.getmMinMoney()));
        text_plata_max.setText(String.valueOf(userFilters.getmMaxMoney()));


        for(int i = 0; i < categoriesBarrios.size(); i++){
            if(categoriesBarrios.get(i).equals(userFilters.getmHood())){
                spinner_barrio.setSelection(i);
                i = categoriesBarrios.size();
            }
        }
        switch (userFilters.getmGender()) {
            case GENDER_OTRO_STRING:
                spinner_sexo.setSelection(GENDER_OTRO_INDEX);
                break;
            case GENDER_F_STRING:
                spinner_sexo.setSelection(GENDER_F_INDEX);
                break;
            case GENDER_M_STRING:
                spinner_sexo.setSelection(GENDER_M_INDEX);
                break;
        }

    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

    private void fetchFilterData() {
        container_filter_layout.setVisibility(View.GONE);
        container_spinner_filter.setVisibility(View.VISIBLE);
        try {
            getFiltrosbyTokenHttpRequest(userToken);

        } catch (IOException e) {
            e.printStackTrace();
            //Todo Retry
            Toast.makeText(getContext(), "Hubo un error al cargar, pruebe de nuevo!", Toast.LENGTH_LONG).show();
        }
    }


}


