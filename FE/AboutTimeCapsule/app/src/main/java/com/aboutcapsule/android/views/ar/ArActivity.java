package com.aboutcapsule.android.views.ar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aboutcapsule.android.R;
import com.aboutcapsule.android.data.capsule.GetMapRes;
import com.aboutcapsule.android.data.capsule.MapAroundCapsuleReq;
import com.aboutcapsule.android.data.capsule.MapAroundCapsuleRes;
import com.aboutcapsule.android.factory.CapsuleViewModelFactory;
import com.aboutcapsule.android.model.CapsuleViewModel;
import com.aboutcapsule.android.repository.CapsuleRepo;
import com.aboutcapsule.android.util.GlobalAplication;
import com.aboutcapsule.android.util.Utils;
import com.aboutcapsule.android.views.map.CustomDialogCapsuleInfoFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import uk.co.appoly.arcorelocation.LocationMarker;
import uk.co.appoly.arcorelocation.LocationScene;

public class ArActivity extends AppCompatActivity {
    private int memberId = GlobalAplication.preferences.getInt("currentUser", -1);

    private ArSceneView arSceneView;
    private LocationScene locationScene;

    private boolean installRequested;
    private boolean hasFinishedLoadingRenderable = false;
    private boolean hasFinishedSetRenderable = false;
    private Snackbar loadingMessageSnackbar = null;

    ImageView back_btn;

    public List<MapAroundCapsuleRes> capsuleList;
    private ArrayList<CompletableFuture<ViewRenderable>> capsuleLayoutList;
    private ArrayList<ViewRenderable> capsuleRenderableList;

    private CapsuleViewModel capsuleViewModel = null ;

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
                fragmentManager.popBackStack(); // 이전 Fragment로 이동
            } else {
                finish(); // 이전 Fragment이 없으면 현재 Activity를 종료
            }
        });

        capsuleList = new ArrayList<>();
        capsuleLayoutList = new ArrayList<>();
        capsuleRenderableList = new ArrayList<>();

        //내 주변 캡슐 가져오는 API 호출
        CapsuleRepo capsuleRepo = new CapsuleRepo();
        CapsuleViewModelFactory capsuleViewModelFactory = new CapsuleViewModelFactory(capsuleRepo);

        capsuleViewModel = new ViewModelProvider(this, capsuleViewModelFactory).get(CapsuleViewModel.class);

        Double lat = Double.parseDouble(GlobalAplication.preferences.getString("ar_lat","-1"));
        Double lng = Double.parseDouble(GlobalAplication.preferences.getString("ar_lng","-1"));

        capsuleViewModel.getAroundCapsuleInMap(new MapAroundCapsuleReq(memberId, lat, lng));

        capsuleViewModel.getAroundCapsuleInMapList().observe(this, new Observer<GetMapRes>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onChanged(GetMapRes getMapRes) {
                //데이터 받아와서 capsuleList에 넣기
                for (MapAroundCapsuleRes capsule : getMapRes.getMapAroundCapsuleResList()) {
                    capsuleList.add(capsule);
                }

                //AR 랜더링
                if(capsuleList.size() != 0) {
                    //capsuleLayoutList 생성
                    for (int i = 0; i < capsuleList.size(); i++) {
                        capsuleLayoutList.add(ViewRenderable.builder().setView(getApplication(), R.layout.ar_item).build());
                    }

                    //캡슐 오브젝트 생성
                    CompletableFuture<ViewRenderable> objCapsule = new CompletableFuture<>();
                    for (int i = 0; i < capsuleList.size(); i++) {
                        objCapsule = capsuleLayoutList.get(i);
                    }

                    //랜더링할 리스트에 담기
                    CompletableFuture.allOf(objCapsule)
                            .handle(
                                    (notUsed, throwable) -> {
                                        // When you build a Renderable, Sceneform loads its resources in the background while
                                        // returning a CompletableFuture. Call handle(), thenAccept(), or check isDone()
                                        // before calling get().
                                        if (throwable != null) {
                                            Utils.displayError(getApplication(), "Unable to load renderables", throwable);
                                            return null;
                                        }
                                        try {
                                            if (capsuleList.size() != 0) {
                                                for (int i = 0; i < capsuleList.size(); i++) {
                                                    capsuleRenderableList.add(capsuleLayoutList.get(i).get());
                                                }
                                            }
                                            hasFinishedLoadingRenderable = true;

                                        } catch (InterruptedException |
                                                 ExecutionException ex) {
                                            Utils.displayError(getApplication(), "Unable to load renderables", ex);
                                        }
                                        return null;
                                    });

                    //AR 객체 렌더링
                    arSceneView
                            .getScene()
                            .addOnUpdateListener(
                                    (FrameTime frameTime) -> {
                                        // 모형을 로딩한 후에 다시 다음 작업을 진행
                                        if (!hasFinishedLoadingRenderable) {
                                            return;
                                        }

                                        if (locationScene == null) {
                                            try {
                                                locationScene = new LocationScene(getApplication(), getParent(), arSceneView);
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            locationScene.setOffsetOverlapping(false);  // 겹치는 모형에 이동량을 더할 지 설정
                                        }

                                        // ArFrame 가져오기
                                        Frame frame = arSceneView.getArFrame();
                                        if (frame == null) {
                                            return;
                                        }

                                        // Frame이 추적 중일 때 다시 시작
                                        if (frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
                                            return;
                                        }

                                        // locationScene이 비어 있지 않고 모형이 설치되어 있지 않다면
                                        if (locationScene != null && !hasFinishedSetRenderable) {
                                            LocationMarker[] locationMarker = new LocationMarker[capsuleList.size()];

                                            try {
                                                Thread.sleep(13);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                            for (int i = 0; i < capsuleList.size(); i++) {
                                                //이미지를 렌더링할 노드 생성
                                                Node imageNode = new Node();
                                                imageNode.setRenderable(capsuleRenderableList.get(i));

                                                // 노드 위치 및 크기 설정
                                                imageNode.setLocalPosition(new Vector3(0.0f, 0.0f, -1.0f));
                                                imageNode.setLocalScale(new Vector3(0.5f, 0.5f, 0.5f));

                                                //캡슐 클릭시 상세 화면으로 이동
                                                View view = capsuleRenderableList.get(i).getView();
                                                MapAroundCapsuleRes capsule = capsuleList.get(i);

                                                view.setOnTouchListener((v, event) -> {
                                                    //다이얼로그 띄우기
                                                    CustomDialogCapsuleInfoFragment dialog = new CustomDialogCapsuleInfoFragment();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt("capsuleId", capsule.getCapsuleId());
                                                    bundle.putDouble("lat", capsule.getLatitude());
                                                    bundle.putDouble("lng", capsule.getLongitude());
                                                    dialog.setArguments(bundle);
                                                    dialog.show(getSupportFragmentManager(), "customDialogCapsuleInfoFragment");

                                                    return false;
                                                });
                                                
                                                // 노드를 위치 마커에 찍어서 씬에 추가
                                                locationMarker[i] = new LocationMarker(capsuleList.get(i).getLongitude(), capsuleList.get(i).getLatitude(), imageNode);
                                                locationScene.mLocationMarkers.add(locationMarker[i]);

                                                hasFinishedSetRenderable = true; //캡슐 배치 완료
                                            }
                                        }

                                        //locationScene
                                        if (locationScene != null) {
                                            locationScene.processFrame(frame);
                                        }

                                        //트래킹 관련
                                        if (loadingMessageSnackbar != null) {
                                            for (Plane plane : frame.getUpdatedTrackables(Plane.class)) {
                                                if (plane.getTrackingState() == TrackingState.TRACKING) {
                                                    hideLoadingMessage();
                                                }
                                            }
                                        }

                                    });
                }
            }
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

    private void showLoadingMessage() {
        if (loadingMessageSnackbar != null && loadingMessageSnackbar.isShownOrQueued()) {
            return;
        }

        loadingMessageSnackbar =
                Snackbar.make(
                        ArActivity.this.findViewById(android.R.id.content),
                        "seeking plane!!!",
                        Snackbar.LENGTH_INDEFINITE);
        loadingMessageSnackbar.getView().setBackgroundColor(0xbf323232);
        loadingMessageSnackbar.show();
    }

    private void hideLoadingMessage() {
        if (loadingMessageSnackbar == null) {
            return;
        }

        loadingMessageSnackbar.dismiss();
        loadingMessageSnackbar = null;
    }

}
