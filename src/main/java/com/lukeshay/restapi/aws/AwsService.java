package com.lukeshay.restapi.aws;

import java.io.File;
import org.springframework.web.multipart.MultipartFile;

public interface AwsService {

  File convertFile(MultipartFile file);

  String uploadFileToS3(String fileName, MultipartFile file);
}
