package org.myblog.web.file;

import org.myblog.domain.user.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename){
        return fileDir + filename;
    }

    // 내 파일경로에, 파일자체를 저장부터 하기 (원래 AWS 서버의 S3에 파일 자체를 저장하긴 함)
    // 그리고 임베디드타입인 UploadFile로 리턴
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()){
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename(); // -- ex) cat.png
        String storeFilename = createStoreFilename(originalFilename); // -- 내 로컬 서버에 저장할 파일명 (특, 유일함)

        // 파일 저장
        multipartFile.transferTo(new File(getFullPath(storeFilename)));

        return new UploadFile(originalFilename, storeFilename);
    }

    private String createStoreFilename(String originalFilename){ // -- 서버에 저장할 파일명
        String ext = extractExt(originalFilename); // ex) "png"

        // 서버에 저장하는 파일명 -- UUID 사용해야 함
        String uuid = UUID.randomUUID().toString();// -- ex) "qew-qew-123-1234-sf"

        return uuid + "." + ext; // "uuid.확장자" -- ex) "qew-qew-123-1234-sf.png"
    }

    private String extractExt(String originalFilename){
        int pos = originalFilename.lastIndexOf(".");

        return originalFilename.substring(pos + 1); // -- ex) "png"
    }
}
