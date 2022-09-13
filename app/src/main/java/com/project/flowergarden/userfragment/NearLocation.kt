package com.project.flowergarden.userfragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.project.flowergarden.databinding.FragmentNearLocationBinding
import kotlinx.android.synthetic.main.fragment_near_location.*


class NearLocation : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentNearLocationBinding
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNearLocationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
    }



    override fun onMapReady(naverMap: NaverMap) {

        this.naverMap = naverMap

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.567271, 126.8252269))
            .animate(CameraAnimation.Fly, 1000)
        naverMap.moveCamera(cameraUpdate)

        val marker = Marker().apply {
            position = LatLng(37.567271, 126.8252269)
            setOnClickListener {
                true
            }

            map = naverMap
        }
    }

}