package com.project.flowergarden.userfragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.project.flowergarden.databinding.FragmentNearLocationBinding

class NearLocation : Fragment(), OnMapReadyCallback {

    private lateinit var locationSource: FusedLocationSource
    private lateinit var binding: FragmentNearLocationBinding
    private lateinit var naverMap: NaverMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNearLocationBinding.inflate(layoutInflater)
        return binding.root
        onMapReady(naverMap)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onMapReady(naverMap: NaverMap) {
        val cameraPosition = CameraPosition(LatLng(37.579705, 127.054839),12.0,45.0,45.0)
        naverMap.cameraPosition = cameraPosition
    }
}