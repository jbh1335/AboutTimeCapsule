package com.aboutcapsule.android.views.login
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.ActivityLoginBinding
import com.aboutcapsule.android.factory.OauthViewModelFactory
import com.aboutcapsule.android.model.OauthViewModel
import com.aboutcapsule.android.repository.OauthRepo
import com.aboutcapsule.android.util.GlobalAplication
import com.aboutcapsule.android.views.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

class LoginAcitivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: OauthViewModel
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isLogin()
        val repository = OauthRepo()
        val oauthViewModelFactory = OauthViewModelFactory(repository)
        viewModel = ViewModelProvider  (this, oauthViewModelFactory).get(OauthViewModel::class.java)
        binding.kakaoLoginBtnLayout.setOnClickListener{
            kakaoLogin()
        }
        binding.naverLoginBtn.setOnClickListener {
            naverLogin()
        }
    }


    private fun naverLogin() {

        val loginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                val accesstoken = NaverIdLoginSDK.getAccessToken()
                Log.i("네이버로그인성공", "네이버로 로그인 성공 ${accesstoken}")

                viewModel.doLogin("naver", "Bearer ${accesstoken}")
                viewModel.loginInstance.observe(this@LoginAcitivity
                ) {
                    val nickname = viewModel.loginInstance.value?.nickname
                    Log.d("nickname", "${nickname}")
                    val jwtToken = viewModel.loginInstance.value?.accessToken
                    val refreshToken = viewModel.loginInstance.value?.refreshToken
                    Log.d("jwtToken", "${jwtToken}")
                    GlobalAplication.preferences.setInt("currentUser", viewModel.loginInstance.value!!.id)
                    if (nickname.equals("null")) {
                        GlobalAplication.preferences.setString("tempAccessToken", jwtToken!!)
                        supportFragmentManager
                            .beginTransaction()
                            .add(R.id.loginFrame, NicknameSettingFragment())
                            .commit()
                    } else {
                        GlobalAplication.preferences.setString("accessToken", jwtToken!!)
                        GlobalAplication.preferences.setString("refreshToken", refreshToken!!)
                        toMainActivity()
                    }
                }


            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@LoginAcitivity,"errorCode:$errorCode, errorDesc:$errorDescription",Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(this, loginCallback)

    }
    private fun kakaoLogin() {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.i("카카오로그인실패1", "카카오계정으로 로그인 실패 : ${error}")
            } else if (token != null) {
                UserApiClient.instance.me { user, error ->
                    Log.i("카카오로그인성공1", "카카오계정으로 로그인 성공 \n\n " +
                            "token: ${token.accessToken} \n\n " +
                            "me: ${user}")
                    viewModel.doLogin("kakao", "Bearer ${token.accessToken}")
                    viewModel.loginInstance.observe(this@LoginAcitivity
                    ) {
                        val nickname = viewModel.loginInstance.value?.nickname
                        Log.d("nickname", "${nickname}")
                        val jwtToken = viewModel.loginInstance.value?.accessToken
                        val refreshToken = viewModel.loginInstance.value?.refreshToken
                        Log.d("jwtToken", "${jwtToken}")
                        GlobalAplication.preferences.setInt("currentUser", viewModel.loginInstance.value!!.id)
                        if (nickname.equals("null")) {
                            GlobalAplication.preferences.setString("tempAccessToken", jwtToken!!)
                            supportFragmentManager
                                .beginTransaction()
                                .add(R.id.loginFrame, NicknameSettingFragment())
                                .commit()
                        } else {
                            GlobalAplication.preferences.setString("accessToken", jwtToken!!)
                            GlobalAplication.preferences.setString("refreshToken", refreshToken!!)
                            toMainActivity()
                        }
                    }

                    Log.i("카카오로그인성공1", "카카오계정으로 로그인 성공 \n\n " +
                            "token: ${token.accessToken} \n\n " +
                            "me: ${user}")
//                    viewModel.doLogin("naver", "Bearer ${token.accessToken}")
//
//                    val nickname = viewModel.loginInstance.value?.nickname
//                    val jwtToken = viewModel.loginInstance.value?.accessToken
//                    if (nickname == null) {
//                        GlobalAplication.preferences.setString("tempAccessToken", jwtToken!!)
//                        supportFragmentManager
//                            .beginTransaction()
//                            .add(R.id.loginFrame, NicknameSettingFragment())
//                            .commit()
//                    }else {
//                        toMainActivity()
//                    }
                }
            }
        }
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.i("카카오로그인실패2", "카카오톡으로 로그인 실패 : ${error}")
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i("카카오로그인성공2", "카카오톡으로 로그인 성공 ${token.accessToken}")

                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun isLogin() {
        val currentUser = GlobalAplication.preferences.getInt("currentUser", -1)
        if(currentUser != -1) {
            toMainActivity()
        }
    }
    fun toMainActivity() {
        val intent = Intent(this@LoginAcitivity, MainActivity::class.java)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        this@LoginAcitivity.finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        //다이얼로그가 띄워져 있는 상태(showing)인 경우 dismiss() 호출
        if (alertDialog != null && alertDialog!!.isShowing) {
            alertDialog!!.dismiss()
        }
    }





}