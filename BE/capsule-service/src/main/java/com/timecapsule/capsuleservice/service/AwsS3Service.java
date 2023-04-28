package com.timecapsule.capsuleservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AwsS3Service {
    String uploadFile(List<MultipartFile> multipartFileList);
}
