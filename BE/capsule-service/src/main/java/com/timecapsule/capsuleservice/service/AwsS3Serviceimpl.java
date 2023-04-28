package com.timecapsule.capsuleservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.common.io.Files.getFileExtension;

@Service("awsS3Service")
@RequiredArgsConstructor
public class AwsS3Serviceimpl implements AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    @Override
    public String uploadFile(List<MultipartFile> multipartFileList) {
        List<String> fileNameList = new ArrayList<>();

        multipartFileList.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename());

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength((file.getSize()));
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }

            fileNameList.add(fileName);
        });

        String totalImage = "";
        for(String img : fileNameList) {
            totalImage += ("#" + img);
        }

        System.out.println("totalImage: " + totalImage);
        return totalImage;
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }
}
