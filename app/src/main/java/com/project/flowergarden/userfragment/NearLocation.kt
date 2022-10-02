package com.project.flowergarden.userfragment

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import com.project.flowergarden.R
import java.util.*

//OnMapReadyCallback을 등록하면 비동기로 NaverMap 객체를 얻을 수 있으며  객체(NaverMap)가 준비되면 onMapReady() 콜백 메서드가 호출
class NearLocation : Fragment(), OnMapReadyCallback {

    //FusedLocationSource 뷰의 객체를 전달하고 권한 요청 코드 지정
    private lateinit var locationSource: FusedLocationSource

    //mapView를 받아오기 위해 변수 설정
    private lateinit var mapView: MapView

    private lateinit var naverMap: NaverMap

    private var auth: FirebaseAuth? = null //파이어베이스 인증
    //유저 정보 불러오기 (아이디, 닉네임 등)
    private var user: FirebaseUser? = null
    private lateinit var OwnerDB: DatabaseReference //실시간 데이터베이스
    private var userID: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser
        OwnerDB = FirebaseDatabase.getInstance().getReference("Owner")
        userID = user!!.uid




        OwnerDB.child(userID!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val address = snapshot.child("address").value.toString()



            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_near_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //FusedLocationSource : 런타임 권한 처리를 위해 Activity 또는 Fragment 필요로 하며 권한요청 코드 지정
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        //getMapAsync 메서드를 호출하여 프래그먼트에서 콜백을 설정, 비동기로 NaverMap 객체를 얻을 수 있습니다.
        mapView.getMapAsync(this)
    }

    //뷰 시작시 위치 이동
   override fun onMapReady(naverMap: NaverMap) {

        //NaverMap으로부터 UiSettings 인스턴스를 가져오기 (위치, 나침반, 실내지도 층 피커, 줌버튼)
        val uiSettings = naverMap.uiSettings

        //초기 위치 설정, 위경도
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.562238,127.065175)).animate(CameraAnimation.Easing, 1000)
        naverMap.moveCamera(cameraUpdate)

        //현위치 버튼 활성화
        uiSettings.isLocationButtonEnabled = true

        //현위치 추적, 나선형 꼴 모양
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

    }

    //권한 요청 이후 유저가 권한을 허용 했을 시
    // onRequestPermissionResult()의 결과를 FusedLocationSource의 onRequestPermissionsResult()에 전달
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    //아래 생명주기
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    //컴파일 시간에 결정되는 상수(값이 변하지 않음)
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}