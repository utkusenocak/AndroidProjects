package com.utkusenocak.facerecognation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.utkusenocak.facerecognation.Helper.GraphicOverlay;
import com.utkusenocak.facerecognation.Helper.RectOverlay;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    CameraView cameraView;
    GraphicOverlay graphicOverlay;
    AlertDialog waitingDialog;

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraView = findViewById(R.id.camera_view);
        graphicOverlay = findViewById(R.id.graphic_overlay);
        waitingDialog = new SpotsDialog.Builder().setContext(this)
                .setMessage("Lütfen Bekleyin")
                .setCancelable(false)
                .build();

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                waitingDialog.show();

                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);
                cameraView.stop();
                
                runFaceDetector(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    private void runFaceDetector(Bitmap bitmap) {
        FirebaseVisionImage visionImage = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder()
                .build();
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
        detector.detectInImage(visionImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                processFaceResult(firebaseVisionFaces);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processFaceResult(List<FirebaseVisionFace> firebaseVisionFaces) {
        int count = 0;
        for (FirebaseVisionFace face : firebaseVisionFaces){
            Rect bounds = face.getBoundingBox();
            RectOverlay rectOverlay = new RectOverlay(graphicOverlay, bounds);
            graphicOverlay.add(rectOverlay);
            count ++;

        }
        waitingDialog.dismiss();
        Toast.makeText(this, String.format("Resimde %d adet yüz tespit edildi.", count), Toast.LENGTH_SHORT).show();
    }

    public void detect(View view){
        cameraView.start();
        cameraView.captureImage();
        graphicOverlay.clear();

    }
}
