package unikom.skripsi.angga.masyarakat.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import unikom.skripsi.angga.masyarakat.BuildConfig;
import unikom.skripsi.angga.masyarakat.R;
import unikom.skripsi.angga.masyarakat.adapter.ShowNotificationAdapter;
import unikom.skripsi.angga.masyarakat.config.Config;
import unikom.skripsi.angga.masyarakat.model.RekomendasiTempatModel;
import unikom.skripsi.angga.masyarakat.service.APIClient;
import unikom.skripsi.angga.masyarakat.service.APIServices;

public class ShowNotificationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, ShowNotificationAdapter.Listener {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl(Config.FIREBASE_URL);
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private RecyclerView recyclerView;
    private ShowNotificationAdapter adapter;

    private Location lastLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double latitude;
    double longitude;
    private ImageView imageViewMap;

    private GoogleApiClient googleApiClient;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int GOOGLE_API_CLIENT_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notification);
        ShowNotificationActivity.context = getApplicationContext();

        final ProgressBar progress = findViewById(R.id.loadingProgress);
        final TextView txtProgress = findViewById(R.id.loadingText);
        progress.setVisibility(View.VISIBLE);
        txtProgress.setVisibility(View.VISIBLE);

        final TextView title = findViewById(R.id.notificationTitle);
        final TextView time = findViewById(R.id.notificationTime);
        final TextView message = findViewById(R.id.notificationMessage);
        final ImageView img = findViewById(R.id.notificationImage);
        imageViewMap = findViewById(R.id.google_maps);
        recyclerView = findViewById(R.id.shownotif_recyclerview);

        title.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
        img.setVisibility(View.GONE);

        String notificationTitle = null;
        String notificationTime = null;
        String notificationMessage = null;
        String notificationImage = null;
        String notificationLat = null;
        String notificationLng = null;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .addApi(Places.GEO_DATA_API)
                .build();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                notificationTitle = extras.getString("title");
                notificationTime = extras.getString("timestamp");
                notificationMessage = extras.getString("message");
                notificationImage = extras.getString("image");
                notificationLat = extras.getString("lat");
                notificationLng = extras.getString("lng");
            }
        } else {
            notificationTitle = (String) savedInstanceState.getSerializable("title");
            notificationTime = (String) savedInstanceState.getSerializable("timestamp");
            notificationMessage = (String) savedInstanceState.getSerializable("message");
            notificationImage = (String) savedInstanceState.getSerializable("image");
            notificationLat = (String) savedInstanceState.getSerializable("lat");
            notificationLng = (String) savedInstanceState.getSerializable("lng");
        }

        title.setText(notificationTitle);
        time.setText(notificationTime);
        message.setText(notificationMessage);
        Picasso.get().load("https://maps.googleapis.com/maps/api/staticmap?" + notificationLat + "," + notificationLng + "&markers=color:0x43a047%7C" + notificationLat + "," + notificationLng + "&zoom=17&size=640x640&key=AIzaSyD8rU8Upu8hPpPhE1n-SicGBrSDGWEJNIM").into(imageViewMap);

        FirebaseUser user = mAuth.getCurrentUser();
        final StorageReference imageRef = storageRef.child("foto-notifikasi/" + notificationImage);
        if (user != null) {
            final String finalNotificationLat = notificationLat;
            final String finalNotificationLng = notificationLng;
            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    progress.setVisibility(View.GONE);
                    txtProgress.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    time.setVisibility(View.VISIBLE);
                    message.setVisibility(View.VISIBLE);
                    Picasso.get().load(uri).into(img);
                    img.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, "Gagal mendapatkan foto.", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            final String finalNotificationLat1 = notificationLat;
            final String finalNotificationLng1 = notificationLng;
            mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progress.setVisibility(View.GONE);
                            txtProgress.setVisibility(View.GONE);
                            title.setVisibility(View.VISIBLE);
                            time.setVisibility(View.VISIBLE);
                            message.setVisibility(View.VISIBLE);
                            Picasso.get().load(uri).into(img);
//                            Picasso.get().load("https://maps.googleapis.com/maps/api/staticmap?" + finalNotificationLat1 + "," + finalNotificationLng1 + "&markers=color:0x43a047%7C" + finalNotificationLat1 + "," + finalNotificationLng1 + "&zoom=17&size=640x640&key=AIzaSyCQLG0c1b_L1XZrBhJ8-QMFus7KSBuB0jE").into(imageViewMap);
                            img.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(context, "Gagal mendapatkan foto.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            })
            .addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, "Gagal melakukan autentikasi firebase.", Toast.LENGTH_LONG).show();
                }
            });
        }

        setupRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermission();
        } else {
            getLastLocation();
        }
        loadDataRekomendasi();
    }

    private void loadDataRekomendasi() {
        Call<RekomendasiTempatModel.RekomendasiTempatDataModel> call = APIClient.getClient().create(APIServices.class).getRekomentasiTempat(latitude, longitude);
        call.enqueue(new Callback<RekomendasiTempatModel.RekomendasiTempatDataModel>() {
            @Override
            public void onResponse(Call<RekomendasiTempatModel.RekomendasiTempatDataModel> call, Response<RekomendasiTempatModel.RekomendasiTempatDataModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getMessage().equals("Berhasil")){
                        adapter.replaceData(response.body().getResults());
                    }else {
                        Toast.makeText(ShowNotificationActivity.this, "Tidak ada tempat yang direkomendasikan", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RekomendasiTempatModel.RekomendasiTempatDataModel> call, Throwable t) {
                Toast.makeText(ShowNotificationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        adapter = new ShowNotificationAdapter(this, new ArrayList<RekomendasiTempatModel>(), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            lastLocation = task.getResult();
                            latitude = lastLocation.getLatitude();
                            longitude = lastLocation.getLongitude();
                        } else {
                            showSnackbar("Cannot Get Your Location");
                        }
                    }
                });
    }

    private void showSnackbar(String message) {
        View container = findViewById(R.id.main_container);
        if (container != null) {
            Snackbar.make(container, message, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(final String mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                mainTextStringId,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(ShowNotificationActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermission() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        if (shouldProvideRationale) {
            showSnackbar("Location permission is needed for core functionality", R.string.oke,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startLocationPermissionRequest();
                        }
                    });
        } else {
            startLocationPermissionRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Toast.makeText(this, "User interaction was cancelled", Toast.LENGTH_SHORT).show();
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                showSnackbar("Location permission is needed for core functionality", R.string.oke,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void clickDirection(String latitude, String longitude) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude+","+longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
