package com.orebi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orebi.repository.InventoryRepository;
import com.orebi.thirdparty.GoShip.GoShipLocationDTO;
import com.orebi.thirdparty.GoShip.GoShipLocationHelper;
import com.orebi.thirdparty.GoShip.GoShipService;
import com.orebi.thirdparty.GoShip.request.GoShipFeeRequest;
import com.orebi.thirdparty.GoShip.request.GoShipOrderRequest;
import com.orebi.thirdparty.GoShip.response.GoShipFeeResponse;
import com.orebi.thirdparty.GoShip.response.GoShipOrderResponse;

@RestController
@RequestMapping("/api/goship")
public class GoShipController {

    private final GoShipService goShipService;
    private final InventoryRepository inventoryRepository;
    private final GoShipLocationHelper goShipLocationHelper;

    public GoShipController(GoShipService goShipService, InventoryRepository inventoryRepository,
            GoShipLocationHelper goShipLocationHelper) {
        this.goShipService = goShipService;
        this.inventoryRepository = inventoryRepository;
        this.goShipLocationHelper = goShipLocationHelper;
    }

    @PostMapping("/fee")
    public List<GoShipFeeResponse> calculateFee(@RequestBody GoShipFeeRequest request) {

        String fromCityId = goShipLocationHelper.getProvinceIdByName(request.getShipment().getAddress_from().getCity());
        String fromDistrictId = goShipLocationHelper.getDistrictIdByName(fromCityId,
                request.getShipment().getAddress_from().getDistrict());
        String fromWardId = goShipLocationHelper.getWardIdByName(fromDistrictId,
                request.getShipment().getAddress_from().getWard());

        String toCityId = goShipLocationHelper.getProvinceIdByName(request.getShipment().getAddress_to().getCity());
        String toDistrictId = goShipLocationHelper.getDistrictIdByName(toCityId,
                request.getShipment().getAddress_to().getDistrict());
        String toWardId = goShipLocationHelper.getWardIdByName(toDistrictId,
                request.getShipment().getAddress_to().getWard());

        request.getShipment().getAddress_from().setCity(fromCityId);
        request.getShipment().getAddress_from().setDistrict(fromDistrictId);
        request.getShipment().getAddress_from().setWard(fromWardId);

        request.getShipment().getAddress_to().setCity(toCityId);
        request.getShipment().getAddress_to().setDistrict(toDistrictId);
        request.getShipment().getAddress_to().setWard(toWardId);

        return goShipService.calculateFee(request);
    }

    @PostMapping("/shipment")
    public GoShipOrderResponse createShipOrder(@RequestBody GoShipOrderRequest request) {

        String fromCityId = goShipLocationHelper.getProvinceIdByName(request.getShipment().getAddress_from().getCity());
        String fromDistrictId = goShipLocationHelper.getDistrictIdByName(fromCityId,
                request.getShipment().getAddress_from().getDistrict());
        String fromWardId = goShipLocationHelper.getWardIdByName(fromDistrictId,
                request.getShipment().getAddress_from().getWard());

        String toCityId = goShipLocationHelper.getProvinceIdByName(request.getShipment().getAddress_to().getCity());
        String toDistrictId = goShipLocationHelper.getDistrictIdByName(toCityId,
                request.getShipment().getAddress_to().getDistrict());
        String toWardId = goShipLocationHelper.getWardIdByName(toDistrictId,
                request.getShipment().getAddress_to().getWard());

        request.getShipment().getAddress_from().setCity(fromCityId);
        request.getShipment().getAddress_from().setDistrict(fromDistrictId);
        request.getShipment().getAddress_from().setWard(fromWardId);

        request.getShipment().getAddress_to().setCity(toCityId);
        request.getShipment().getAddress_to().setDistrict(toDistrictId);
        request.getShipment().getAddress_to().setWard(toWardId);

        return goShipService.createShipOrder(request);
    }

    @GetMapping("/provinces")
    public List<GoShipLocationDTO> getProvinces() {
        return goShipService.getAllProvinces();
    }

    @GetMapping("/districts")
    public List<GoShipLocationDTO> getDistricts(@RequestParam String citycode) {
        return goShipService.getDistrictsByCityCode(citycode);
    }

    @GetMapping("/wards")
    public List<GoShipLocationDTO> getWards(@RequestParam String districtcode) {
        return goShipService.getWardByDistrictCode(districtcode);
    }
}
