package com.project.flowergarden.userfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import com.project.flowergarden.R


class NearLocation : Fragment(), OnMapReadyCallback {

    //FusedLocationSource 뷰의 객체를 전달하고 권한 요청 코드 지정
    private lateinit var locationSource: FusedLocationSource

    //mapView를 받아오기 위해 변수 설정
    private lateinit var mapView: MapView

    private lateinit var naverMap: NaverMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_near_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        //getMapAsync 메서드를 호출하여 프래그먼트에서 콜백을 설정
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

    //권한 요청
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


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}