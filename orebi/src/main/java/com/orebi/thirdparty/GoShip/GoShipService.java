package com.orebi.thirdparty.GoShip;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.orebi.thirdparty.GoShip.config.GoShipProperties;
import com.orebi.thirdparty.GoShip.request.GoShipFeeRequest;
import com.orebi.thirdparty.GoShip.response.GoShipFeeResponse;

@Service
public class GoShipService {

    private final RestTemplate restTemplate;
    private final GoShipProperties properties;

    public GoShipService(RestTemplate restTemplate, GoShipProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public List<GoShipFeeResponse> calculateFee(GoShipFeeRequest request) {

        HttpHeaders headers = createHeaders();
        HttpEntity<GoShipFeeRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                properties.getBaseUrl() + "/rates",
                HttpMethod.POST,
                entity,
                JsonNode.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Không thể lấy phí giao hàng từ GoShip");
        }

        JsonNode data = response.getBody().get("data");
        List<GoShipFeeResponse> result = new ArrayList<>();

        if (data != null && data.isArray()) {
            for (JsonNode item : data) {
                GoShipFeeResponse fee = new GoShipFeeResponse();
                fee.setId(item.get("id").asText());
                fee.setCarrierName(item.get("carrier_name").asText());
                fee.setServiceName(item.get("service").asText());
                fee.setExpectedTime(item.get("expected").asText());
                fee.setTotalFee(item.get("total_fee").asDouble());
                result.add(fee);
            }
        }

        return result;
    }

    public List<GoShipLocationDTO> getAllProvinces() {
        return fetchDataList(properties.getBaseUrl() + "/cities");
    }

    public List<GoShipLocationDTO> getDistrictsByCityCode(String cityCode) {
        String url = properties.getBaseUrl() + "/cities/" +
                UriUtils.encode(cityCode, StandardCharsets.UTF_8) + "/districts";
        return fetchDataList(url);
    }

    // helper
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + properties.getToken());
        return headers;
    }

    private List<GoShipLocationDTO> fetchDataList(String url) {
        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        JsonNode data = response.getBody() != null ? response.getBody().get("data") : null;

        if (data == null || !data.isArray())
            return List.of();

        List<GoShipLocationDTO> result = new ArrayList<>();
        for (JsonNode item : data) {
            String id = item.has("id") ? item.get("id").asText() : null;
            String name = item.has("name") ? item.get("name").asText() : null;
            if (id != null && name != null) {
                result.add(new GoShipLocationDTO(id, name));
            }
        }

        return result;
    }

}