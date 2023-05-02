package com.timecapsule.capsuleservice.service;

public interface DistanceService {
    double distance(double lat1, double lon1, double lat2, double lon2, String unit);
}
