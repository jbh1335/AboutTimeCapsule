package com.aboutcapsule.android.views.ar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.aboutcapsule.android.R;
import com.aboutcapsule.android.util.Utils;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;

import uk.co.appoly.arcorelocation.LocationScene;

public class ArActivity extends AppCompatActivity {
    private ArSceneView arSceneView;
    private LocationScene locationScene;
    private boolean installRequested;

    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Utils.checkIsSupportedDeviceOrFinish(this)) {
            // Not a supported device.
            return;
        }
        Utils.checkPermissions(this); //권한 검사

        //AR
        setContentView(R.layout.activity_ar);
        arSceneView = findViewById(R.id.ar_scene_view);

        //뒤로가기
        back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack(); // 이전 Fragment로 이동합니다.
            } else {
                finish(); // 이전 Fragment이 없으면 현재 Activity를 종료합니다.
            }
        });

        //TODO : AR 객체 세팅
        //AR 객체
        ViewRenderable.builder()
                .setView(getApplication(), R.layout.ar_item) // 이미지를 로드할 레이아웃을 설정
                .build()
                .thenAccept(viewRenderable -> {
                    // 이미지를 렌더링할 노드 생성
                    Node imageNode = new Node();
                    imageNode.setRenderable(viewRenderable);

                    // 노드 위치 및 크기 설정
                    imageNode.setLocalPosition(new Vector3(0.0f, 0.0f, -1.0f));
                    imageNode.setLocalScale(new Vector3(0.5f, 0.5f, 0.5f));

                    // ARCore의 기본 좌표계를 기준으로 y축 기준으로 180도 회전하여 이미지를 뒤집음
                    imageNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0, 1f, 0), 180));

                    // 노드를 씬에 추가
                    arSceneView.getScene().addChild(imageNode);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (locationScene != null) {
            locationScene.resume();
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (arSceneView.getSession() == null) {
            // If the session wasn't created yet, don't resume rendering.
            // This can happen if ARCore needs to be updated or permissions are not granted yet.
            try {
                Session session = Utils.createArSession(this, installRequested);
                if (session == null) {
                    installRequested = true;
                    return;
                } else {
                    arSceneView.setupSession(session);
                }
            } catch (UnavailableException e) {
                Utils.handleSessionException(this, e);
            }
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            arSceneView.resume();
        } catch (CameraNotAvailableException ex) {
            Utils.displayError(this, "Unable to get camera", ex);
            finish();
            return;
        }

        if (arSceneView.getSession() != null) {
            //showLoadingMessage();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (locationScene != null) {
            locationScene.pause();
        }

        arSceneView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        arSceneView.destroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(); // 이전 Fragment로 이동합니다.
        } else {
            finish(); // 이전 Fragment이 없으면 현재 Activity를 종료합니다.
        }
    }
}
