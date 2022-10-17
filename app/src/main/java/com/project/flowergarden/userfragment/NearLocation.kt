package com.project.flowergarden.userfragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat.animate
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.project.flowergarden.R
import com.project.flowergarden.StoreDetailActivity
import kotlinx.android.synthetic.main.fragment_near_location.*
import kotlinx.android.synthetic.main.fragment_near_location.view.*


//OnMapReadyCallback을 등록하면 비동기로 NaverMap 객체를 얻을 수 있으며  객체(NaverMap)가 준비되면 onMapReady() 콜백 메서드가 호출
class NearLocation : Fragment(), OnMapReadyCallback {

    //FusedLocationSource 뷰의 객체를 전달하고 권한 요청 코드 지정
    private lateinit var locationSource: FusedLocationSource

    //mapView를 받아오기 위해 변수 설정
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap

    //파이어베이스 접근하기 위한 객체 생성
    //유저 정보 불러오기 (아이디, 닉네임 등)
    private lateinit var OwnerDB: DatabaseReference //실시간 데이터베이스


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

        //OnCreate에서 database 객체를 초기화 해줬다.
        OwnerDB = FirebaseDatabase.getInstance().getReference("Owner")

    }

    //뷰 시작시 위치 이동
    @SuppressLint("SuspiciousIndentation")
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

            OwnerDB.addListenerForSingleValueEvent(object : ValueEventListener{
            @SuppressLint("LogNotTimber")
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children) {
                    val x = i.child("x").value //경도 값 뽑아오기
                    val y = i.child("y").value //위도 값 뽑아오기
                    //val x, y 예시) 37.12312321 127.123815 -> split으로 나눠서 값 위도 따로 경도 따로 나누기
                    val latlong = "${y} ${x}".split(" ").toTypedArray()
                    val y_Result = latlong[0].toDouble()
                    val x_Reuslt = latlong[1].toDouble()
                    val storeName = i.child("storename").value
                    val opentime = i.child("opentime").value
                    val closetime = i.child("closetime").value
                    val storeNumber = i.child("number").value
                    val openday = i.child("openday").value
                    val address = i.child("address").value

                    Log.d("y_Result 값은", "${y_Result}")
                    Log.d("x_Reuslt 값은", "${x_Reuslt}")

                    val marker = Marker()
                    marker.icon = OverlayImage.fromResource(R.drawable.ic_marker)
                    //마커의 초기 아이콘 -> 조그만한 마커
                    marker.position = LatLng(y_Result, x_Reuslt)

                    card_view.visibility = View.GONE

                    naverMap.setOnMapClickListener { _, _ ->
                        marker.icon = OverlayImage.fromResource(R.drawable.ic_marker)
                        hideMenu()
                    }

                    marker.setOnClickListener { overlay ->
                        marker.icon = OverlayImage.fromResource(R.drawable.ic_clickedmarker)
                        showMenu()
                        card_view.storeName.text = storeName.toString()
                        card_view.openTime.text = opentime.toString()
                        card_view.closeTime.text = closetime.toString()
                        card_view.storeNumber.text = storeNumber.toString()
                        card_view.openDay1.text = openday.toString()
                        card_view.setOnClickListener {
                            val intent = Intent(context, StoreDetailActivity::class.java)
                            intent.apply {
                                intent.putExtra("storeName", "${storeName}")
                                intent.putExtra("opentime", "${opentime}")
                                intent.putExtra("closetime", "${closetime}")
                                intent.putExtra("storeNumber", "${storeNumber}")
                                intent.putExtra("openday", "${openday}")
                                intent.putExtra("address", "${address}")
                            }
                            startActivity(intent)
                        }
                        true
                    }
                    marker.map = naverMap
                }

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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

    private fun showMenu(){
        card_view!!.visibility = View.VISIBLE
        card_view!!.animate()
            .alpha(1f)
            .setDuration(ANIMATION_DURATION.toLong())
            .setListener(null)
    }

    private fun hideMenu(){
        card_view!!.animate()
            .alpha(0f)
            .setDuration(ANIMATION_DURATION.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    card_view!!.visibility = View.GONE
                }
            })
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
        private const val ANIMATION_DURATION = 500
    }
}

