package br.com.finduhouse.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class CalculadoraDistanciaImpl implements CalculadoraDistancia {

    private static final double RAIO_TERRA_KM = 6371.0;
    private static final double CAMPUS_LATITUDE = -22.83365;
    private static final double CAMPUS_LONGITUDE = -47.05197;

    @Override
    public BigDecimal calcular(BigDecimal latitude, BigDecimal longitude) {
        double latitudeRad = Math.toRadians(latitude.doubleValue());
        double campusLatitudeRad = Math.toRadians(CAMPUS_LATITUDE);
        double deltaLatitude = Math.toRadians(latitude.doubleValue() - CAMPUS_LATITUDE);
        double deltaLongitude = Math.toRadians(longitude.doubleValue() - CAMPUS_LONGITUDE);
        double haversine = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2)
                + Math.cos(campusLatitudeRad) * Math.cos(latitudeRad)
                * Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);
        double distancia = 2 * RAIO_TERRA_KM
                * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));
        return BigDecimal.valueOf(distancia).setScale(2, RoundingMode.HALF_UP);
    }
}
