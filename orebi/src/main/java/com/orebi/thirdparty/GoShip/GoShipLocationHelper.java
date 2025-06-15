package com.orebi.thirdparty.GoShip;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GoShipLocationHelper {

    private final GoShipService goShipService;

    public GoShipLocationHelper(GoShipService goShipService) {
        this.goShipService = goShipService;
    }

    public String getProvinceIdByName(String provinceName) {
        return getIdByName(goShipService.getAllProvinces(), provinceName);
    }

    public String getDistrictIdByName(String cityId, String districtName) {
        return getIdByName(goShipService.getDistrictsByCityCode(cityId), districtName);
    }

    public String getWardIdByName(String districtid, String wardName) {
        return getIdByName(goShipService.getWardByDistrictCode(districtid), wardName);
    }

    private String getIdByName(List<GoShipLocationDTO> locations, String name) {
        String normalizedInput = normalize(name);
        for (GoShipLocationDTO location : locations) {
            if (normalize(location.getName()).equals(normalizedInput)) {
                return location.getId();
            }
        }
        return null;
    }

    private String normalize(String input) {
        return input == null ? "" : input.trim().toLowerCase();
    }
}