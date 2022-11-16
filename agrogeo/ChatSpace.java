package com.example.agrogeo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatSpace extends AppCompatActivity {

    private RecyclerView ChatsRV;
    private EditText userMsgEdt;
    private FloatingActionButton sendMsgFAB;
    private ArrayList<ChatsModel>chatsModelArrayList;
    private ChatRVAdapter chatRVAdapter;

    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_space);
        ChatsRV = findViewById(R.id.idRVChats);
        userMsgEdt = findViewById(R.id.idEditMsg);
        sendMsgFAB = findViewById(R.id.idFMsend);
        chatsModelArrayList = new ArrayList<>();
        chatRVAdapter = new ChatRVAdapter(chatsModelArrayList,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        ChatsRV.setLayoutManager(manager);
        ChatsRV.setAdapter(chatRVAdapter);


        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userMsgEdt.getText().toString().isEmpty()) {
                    Toast.makeText(ChatSpace.this,"Please enter your message", Toast.LENGTH_SHORT).show();
                    return;
                }
               getResponse(userMsgEdt.getText().toString());
                userMsgEdt.setText("");
            }
        });
    }
    private void getResponse(String message){
        chatsModelArrayList.add(new ChatsModel(message,USER_KEY));
        chatRVAdapter.notifyDataSetChanged();
        String url = "http://api.brainshop.ai/get?bid=170509&key=EsArWJr8ie2ZsTKx&uid=[uid]&msg="+message;
        String BASE_URL = "http://api.brainshop.ai/g";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MsgModel> call = retrofitAPI.getMessage(url);
        call.equence(new Callback<MsgModel>(){
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response){
                if (response.isSuccessful()){
                    MsgModel model = response.body();
                    chatsModelArrayList.add(new ChatsModel(model.getCnt().BOT_KEY));
                    chatRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t){
                chatsModelArrayList.add(new ChatsModel("PLEASE REVERT YOUR QUESTION",BOT_KEY));
                chatRVAdapter.notifyDataSetChanged();
            }
        });

        
        @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_space);
        ButterKnife.bind(this);
//keep screen always on
getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        checkPermission();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkPermission() {
        //Set up the permission listener
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                setupPreview();
            }

            @Override
            public void onPermissionDenied(ArrayList deniedPermissions) {
                Toast.makeText(ChatSpace.this, "Permission Denied" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        //Check camera permission
        TedPermission.with((TemporalAdjuster) this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service. Please turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA)
                .check();

    }

    //set up the camera preview
    private void setupPreview() {
        mCamera = Camera.open();
        mPreview = new CameraPreview(getBaseContext(), mCamera);

        try {
            //Set camera autofocus
            Camera.Parameters params = mCamera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(params);
        }catch (Exception e){

        }

        cameraPreview.addView(mPreview);
        mCamera.setDisplayOrientation(90);
        mCamera.startPreview();
        mPicture = getPictureCallback();
        mPreview.refreshCamera(mCamera);
    }

    // take photo when the users tap the button
    @OnClick(R.id.btn_take_picture)
    public void takePhoto() {
        mCamera.takePicture(null, null, mPicture);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //when on Pause, release camera in order to be used from other applications
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    //get the photo from the camera and pass it to ml kit processor
    private Camera.PictureCallback getPictureCallback() {
        return new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                mlinit(BitmapFactory.decodeByteArray(data, 0, data.length));
                mPreview.refreshCamera(mCamera);
            }
        };
    }

    //the main method that processes the image from the camera and gives labeling result
    private void mlinit(Bitmap bitmap) {

        //By default, the on-device image labeler returns at most 10 labels for an image.
        //But it is too much for us and we wants to get less
        FirebaseVisionLabelDetectorOptions options =
                new FirebaseVisionLabelDetectorOptions.Builder()
                        .setConfidenceThreshold(0.5f)
                        .build();

        //To label objects in an image, create a FirebaseVisionImage object from a bitmap
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        //Get an instance of FirebaseVisionCloudLabelDetector
        FirebaseVisionLabelDetector detector = FirebaseVision.getInstance()
                .getVisionLabelDetector(options);

        detector.detectInImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener>() {
            @Override
            public void onSuccess(List labels) {
                StringBuilder builder = new StringBuilder();
                // Get information about labeled objects
                for (FirebaseVisionLabel label : labels) {
                    builder.append(label.getLabel())
                            .append(" ")
                            .append(label.getConfidence()).append("n");
                }
                txtResult.setText(builder.toString());
            }
        })
               .addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        txtResult.setText(e.getMessage());
                    }
                });
    }

    }
}
